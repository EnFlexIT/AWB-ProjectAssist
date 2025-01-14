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

			List<String> ressourcesFound = InternalResourceHandler.findProjectBlueprintResources();
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
		
		List<String> pbResourcesList = findProjectBlueprintResources();
		for (String pbResource : pbResourcesList) {
			ProjectBlueprint pbp = ProjectBlueprint.load(pbResource);
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
	public static List<String> findProjectBlueprintResources() {
		
		List<String> pbResourcesList = new ArrayList<>();
		
		List<String> ressourcesFound = findResources("blueprints");
		for (String resource : ressourcesFound) {
			if (resource.toLowerCase().endsWith("BlueprintStructure.json".toLowerCase())) {
				pbResourcesList.add(resource);
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
	public static List<String> findResources(String searchPath) {

		// --- Define result variable ---------------------
		List<String> resoucesFound = new ArrayList<>();
		
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
					String name = entries.nextElement().getName();
					// --- Filter according to searchPath -
					if (name.equals(searchPath)==false && name.equals(searchPath + "/")==false  && name.startsWith(searchPath + "/")) { 
						resoucesFound.add(name);
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
	private static List<String> getDirectoryResources(File searchDirectory, File basePathForRelativization) {
	
		// --- Define result variable ---------------------
		List<String> resoucesFound = new ArrayList<>();

		if (searchDirectory.exists()==false || searchDirectory.isDirectory()==false) return resoucesFound;
		
		for (File file : searchDirectory.listFiles()) {
			
			String relativePath = basePathForRelativization.toPath().relativize(file.toPath()).toString();
			relativePath = relativePath.replace(File.separator, "/");
			resoucesFound.add(relativePath);
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
	public static boolean extractFromBundle(String internalPath, File destinationPath) {
		//TODO Omar if folder false if file true : use it along with isBundleFile to check whether the internal path a file or a folder 
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
				return true;
				
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
		return false;
		
	}

	/**
	 * Extracts a file from a bundle project.
	 * @param internalPath the internal path
	 * @param destinationPath the destination path
	 */
	public static boolean isBundleFile(String internalPath) {

		InputStream is = null;
//		FileOutputStream fos = null;
		// TODO Omar : this should be discussed to decide what would be the best way to check whether a relative path is which of a folder or file
		/*
		 *  -------------------------------- Remarks --------------------------------
		 * openStream works for folders inside the jar file so that the method returns true despite that the checked url is for a folder
		 * The getProtocol returns file when operating in a fileSystem and gives a reliable result. However, it gets skipped when executed as maven plugin
		 * since the getProtocol returns jar in that case
		 *  -------------------------------------------------------------------------
		 */
		
		try {
			URL fileURL = InternalResourceHandler.class.getResource(internalPath);
//			// Protection layer 
//            if ("file".equalsIgnoreCase(fileURL.getProtocol())) {
//                File file = new File(fileURL.toURI());
//                boolean filenn = file.isFile();
//                boolean foldernn = file.isDirectory();
//                return file.isFile(); // Check explicitly if it's a file
//            }
//            String enaWin = fileURL.getProtocol();
//            //end of protection layer
			if (fileURL!=null) {
				// --- Write file to directory ------------
				is = fileURL.openStream();
				byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) != -1) {
				}
				return true;
				
			} else {
				// --- Could not find fileURL -------------
				System.err.println(InternalResourceHandler.class.getSimpleName() + " could not find resource for '" + internalPath + "'");
			}
			
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
//			try {
//				if (fos!=null) fos.close();
//			} catch (IOException ioEx) {
//				ioEx.printStackTrace();
//			}
			try {
				if (is!=null) is.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
		return false;
		
	}
	
}
