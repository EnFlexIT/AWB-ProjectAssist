package de.enflexit.awbAssist.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
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
			
			InternalResourceHandler.getProjectBlueprintsAvailable();
			
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
	 * Extracts a file from a bundle project.
	 * @param internalPath the internal path
	 * @param destinationPath the destination path
	 */
	public static void extractFromBundle(String internalPath, File destinationPath) {
		
		boolean debug = false;

		if (debug) {
			System.out.println("Extracting '" + internalPath + "' to " + destinationPath.toString());
		}
		
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			URL fileURL = InternalResourceHandler.class.getResource(internalPath);
			if (fileURL!=null) {
				// --- Write file to directory ------------
				is = fileURL.openStream();
				fos = new FileOutputStream(destinationPath);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				
			} else {
				// --- Could not find fileURL -------------
				System.err.println(InternalResourceHandler.class.getSimpleName() + " could not find resource for '" + internalPath + "'");
			}
			
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			try {
				if (fos!=null) fos.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			try {
				if (is!=null) is.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
		
	}
	
}
