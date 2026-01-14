package de.enflexit.awbAssist.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The Class InternalResourceHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class InternalResourceHandler {
	
	/**
	 * Just a test method .
	 */
	public static void test() {
		
		try {

			List<InternalResource> ressourcesFound = InternalResourceHandler.findProjectBlueprintResources();
			ressourcesFound.forEach(ref -> System.out.println(ref));
			
			@SuppressWarnings("unused")
			List<ProjectBlueprint> previous = InternalResourceHandler.getProjectBlueprintsAvailable();
			System.err.println("just to check previous");
			
		} catch (Exception ioEx) {
			ioEx.printStackTrace();
		}
	}
	
	/**
	 * Return all project blueprints available.
	 * @return the project blueprints available
	 */
	public static List<ProjectBlueprint> getProjectBlueprintsAvailable() {
		
		List<ProjectBlueprint> pbList = new ArrayList<>();
		
		List<InternalResource> pbResourcesList = findProjectBlueprintResources();
		for (InternalResource pbResource : pbResourcesList) {
			ProjectBlueprint pbp = ProjectBlueprint.load(pbResource.getPath());
			if (pbp!=null) {
				pbList.add(pbp);
			}
		}
		return pbList;
	}
	
	/**
	 * Finds project blueprint resources.
	 * @return the list
	 */
	public static List<InternalResource> findProjectBlueprintResources() {
		
		List<InternalResource> pbResourcesList = new ArrayList<>();
		
		List<InternalResource> ressourcesFound = findResources("blueprints");
		for (InternalResource intResource : ressourcesFound) {
			if (intResource.getPath().toLowerCase().endsWith("BlueprintStructure.json".toLowerCase())) {
				pbResourcesList.add(intResource);
			}
		}
		return pbResourcesList;
	}
	
	/**
	 * Find resources as specified by the prefix search path.
	 *
	 * @param searchPath the search path
	 * @return the list
	 */
	public static List<InternalResource> findResources(String searchPath) {

		// --- Define result variable ---------------------
		List<InternalResource> resoucesFound = new ArrayList<>();
		
		try {
			// --- Check where we are ---------------------
			File jarFileOrIDEPath = new File(InternalResourceHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			if (jarFileOrIDEPath.isFile()==true) {  
				// ----------------------------------------
				// --- Run within JAR file ----------------
				// ----------------------------------------
				JarFile jar = new JarFile(jarFileOrIDEPath);
				Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
				while (entries.hasMoreElements()) {
					
					JarEntry jarEntry = entries.nextElement();
					String path = jarEntry.getName();
					// --- Filter according to searchPath -
					if (path.equals(searchPath)==false && path.equals(searchPath + "/")==false  && path.startsWith(searchPath + "/")) { 
						resoucesFound.add(new InternalResource(path, jarEntry.isDirectory()));
					}
				}
				jar.close();
				
			} else { 
				// ----------------------------------------
				// --- Run within IDE and file system -----
				// ----------------------------------------
				URL url = InternalResourceHandler.class.getResource("/" + searchPath);
				if (url != null) {
					try {
						File basePath = new File(url.toURI());
						resoucesFound.addAll(InternalResourceHandler.getDirectoryResources(basePath, jarFileOrIDEPath));
						
					} catch (URISyntaxException ex) {
						// never happens
					}
				}
			}
			
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		
		// --- Sort the search result -----------
		if (resoucesFound.size()>0) {
			Collections.sort(resoucesFound);
		}
		return resoucesFound;
	}
	/**
	 * Returns resources that are to be found in the specified search directory and relativizes the resources path in the results.
	 *
	 * @param searchDirectory the search directory
	 * @param basePathForRelativization the base path for relativization
	 * @return the directory resources
	 */
	private static List<InternalResource> getDirectoryResources(File searchDirectory, File basePathForRelativization) {
	
		// --- Define result variable ---------------------
		List<InternalResource> resoucesFound = new ArrayList<>();

		if (searchDirectory.exists()==false || searchDirectory.isDirectory()==false) return resoucesFound;
		
		for (File file : searchDirectory.listFiles()) {
			
			String relativePath = basePathForRelativization.toPath().relativize(file.toPath()).toString();
			relativePath = relativePath.replace(File.separator, "/");
			resoucesFound.add(new InternalResource(relativePath, file.isDirectory()));
			if (file.isDirectory()==true) {
				resoucesFound.addAll(InternalResourceHandler.getDirectoryResources(file, basePathForRelativization));
			}
		}
		return resoucesFound;
	}
	
	
	
	/**
	 * Extract file from bundle.
	 *
	 * @param internalPath the internal path
	 * @param destinationPath the destination path
	 * @return true, if successful
	 */
	public static boolean extractFileFromBundle(String internalPath, File destinationPath) {


	    if (internalPath == null || destinationPath == null) {
	        System.err.println("Internal path or destination path is null");
	        return false;
	    }

	    InputStream is = null;
	    FileOutputStream fos = null;

	    try {
	    	URL resource = InternalResourceHandler.class.getResource(internalPath);

	        if (resource == null) {
	            System.err.println("Could not find resource: " + internalPath);
	            return false;
	        }

	        File parentDir = destinationPath.getParentFile();
	        if (parentDir != null && parentDir.exists() == false  && parentDir.mkdirs() == false) {
	            System.err.println("Could not create destination directory: " + parentDir);
	            return false;
	        }

	        is = resource.openStream();

	        	
	        fos = new FileOutputStream(destinationPath);
	        byte[] buffer = new byte[1024];
	        int len;
	        while ((len = is.read(buffer)) != -1) {
	        	fos.write(buffer, 0, len);
	        }

	        return true;

	    } catch (IOException e) {
	        System.err.println("Error extracting file: " + internalPath);
	        System.err.println("Destination: " + destinationPath.getAbsolutePath());
	        e.printStackTrace();
	        return false;

	    } finally {
	    	try {
	            if (fos != null) fos.close();
	        } catch (IOException ioEx) {
	            ioEx.printStackTrace();
	        }
	        try {
	            if (is != null) is.close();
	        } catch (IOException ioEx) {
	            ioEx.printStackTrace();
	        }
	    }
	}
	
	
}
