package de.enflexit.awbAssist.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class AwbAssist.
 */

public class AwbAssist {

	private ArrayList<ProjectBlueprint> projectBlueprints;
	

	/**
	 * The main method.
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		// Print the help instructions if requested
		if (ArgumentsChecker.isHelpRequested(args) == true) {
			return;
		}
		// Print the available blueprints if requested
		if (ArgumentsChecker.isBlueprintRequested(args) == true) {
			return;
		}
		// a check is performed to verify that a blueprint name was mentioned in the arguments
		String bluePrint = ArgumentsChecker.getBlueprintArgument(args);
		// call the createProjectFromBlueprint method
		if (bluePrint == null) {
			return;
		} else {
			new AwbAssist().createProjectFromBlueprint(bluePrint, args);
		}
	}
	private void createProjectFromBlueprint(String blueprintName, String[] args) {

		// Check whether a blueprint template corresponds to the given blueprintName and load it
		ProjectBlueprint blueprintToBeUsed = this.getFoundProjectBlueprint(blueprintName);
		if (blueprintToBeUsed == null) {
			System.err.println("no blue print was found with the name " + blueprintName);
			return;
		}

		// Required arguments and given arguments are transfered to the check method in order 
		// to see whether it is possible to associate a value for each required mandatory argument.
		HashMap<String, String> arguments = ArgumentsChecker.check(args, blueprintToBeUsed);
		if (arguments == null) {
			return;
		}

		String symbolicBundleName = arguments.get(Arguments.SYMBOLIC_BUNDLE_NAME);
		String targetDirectory = arguments.get(Arguments.TARGET_DIRECTORY);

		
		// A HashMap of replacement strings is generated. Keys are extracted from the blueprint template 
		// and values are extracted indirectly from the given arguments using the HashMap "arguments"
		HashMap<String, String> replacements = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : blueprintToBeUsed.getTextReplacements().entrySet()) {
			replacements.put(entry.getKey(), arguments.get(entry.getValue()));
		}

		// The folder called [SymBunNameWithStructureChange] is to be replaced with a folder structure based on the given input for the argument symbolic bundle name
		String folderAfterChangements = symbolicBundleName.replace(".", File.separator);

		// Get a list of relative paths that represent the substructure of the blueprint template
		// Generate the relative search path to be used as reference
		String relativeSearchPath = "blueprints/" + blueprintToBeUsed.getBaseFolder();
		List<InternalResource> blueprintRelativeResources = InternalResourceHandler.findResources(relativeSearchPath);
		if (blueprintRelativeResources.isEmpty() || blueprintRelativeResources == null) {
			System.err.println("No substructure was found under the " + blueprintToBeUsed.getBaseFolder() + " blueprint");
			return;
		}

		// create the main folder that should contain the blueprint's substructure
		String DirectoryOfMainFolder = (targetDirectory + File.separator + symbolicBundleName);
		Boolean resultCreateMainFolder = this.createTargetFolder(Path.of(DirectoryOfMainFolder));
		if (resultCreateMainFolder == false) {
			System.err.println(" the main folder couldn't be created");
			return;
		}

		// go through the list of paths, which stands for all files and folders that are present in the substructure and create them one by one in 
		// the target directory, while renaming files and folders if required as well as performing text replacements in the files if needed.
		int i = 0;
		String currentLocalTargetDirectory = new String();
		String currentLocalTargetDirectoryAfterRenameCheck = new String();
		while (i < blueprintRelativeResources.size()) {
			
			String currentElement = blueprintRelativeResources.get(i).getPath();
			boolean isDirectory = blueprintRelativeResources.get(i).isDirectory();
			if (isDirectory == false) {
				// we have a file
				// extract to a specific directory and perform text replacements except for the json file, which is ignored
				currentLocalTargetDirectory = this.getCurrentLocaltargetDirectory(currentElement, Arguments.FOLDERWITHSTRUCTUREMODIFICATION, relativeSearchPath, targetDirectory, symbolicBundleName, folderAfterChangements);
				currentLocalTargetDirectoryAfterRenameCheck = renameCheck(currentLocalTargetDirectory, replacements);
				if (currentLocalTargetDirectoryAfterRenameCheck.contains("BlueprintStructure.json") == false) {
					File currentFilePath = new File(currentLocalTargetDirectoryAfterRenameCheck);
					InternalResourceHandler.extractFileFromBundle("/" + currentElement, currentFilePath);
					doTextReplacement(currentFilePath, replacements);
				}

			} else {
				// we have a folder
				// try creating the folder
				currentLocalTargetDirectory = this.getCurrentLocaltargetDirectory(currentElement, Arguments.FOLDERWITHSTRUCTUREMODIFICATION, relativeSearchPath, targetDirectory, symbolicBundleName, folderAfterChangements);
				currentLocalTargetDirectoryAfterRenameCheck = this.renameCheck(currentLocalTargetDirectory, replacements);
				Boolean resultCreateFolder = this.createTargetFolder(Path.of(currentLocalTargetDirectoryAfterRenameCheck));
				if (resultCreateFolder == false) {
					System.out.println("the folder " + currentLocalTargetDirectoryAfterRenameCheck + " already exists");
				}
			}
			i++;
		}
		if (blueprintName.equals("restServerBlueprint") || blueprintName.equals("restClientBlueprint")) {
			Path pathToBePassed = Path.of(DirectoryOfMainFolder + File.separator + "xCodgen");
			try {
				runMavenGenerateSources(pathToBePassed);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Project creation successful! You can find your project at:\t" + DirectoryOfMainFolder);
	}

	
	
	/**
	 * File rename check to rename the currentLocalTargetDirectory if it contains a string from the replacement list.
	 * @param currentLocalTargetDirectory the current local target directory
	 * @param replacements the replacements
	 * @return the string
	 */
	public String renameCheck(String currentLocalTargetDirectory, HashMap<String, String> replacements) {
		for (Map.Entry<String, String> entry : replacements.entrySet()) {
			if (currentLocalTargetDirectory.contains(entry.getKey())) {
				currentLocalTargetDirectory = currentLocalTargetDirectory.replace(entry.getKey(), entry.getValue());
			}
		}
		return currentLocalTargetDirectory;
	}


	/**
	 * Gives back the target directory to copy the element i of the given list at.
	 * The directory creation includes modifying the folder structure
	 * @param i
	 * @param blueprintRelativeResources
	 * @param folderToBeChanged
	 * @param relativeSearchPath
	 * @param targetDirectory
	 * @param symBunName
	 * @param folderAfterChangements
	 * @return
	 */
	public String getCurrentLocaltargetDirectory(String resource, String folderToBeChanged, String relativeSearchPath, String targetDirectory, String symBunName,String folderAfterChangements) {
		String currentLocalTargetDirectory = null;
		if (resource.contains(folderToBeChanged)) {
			currentLocalTargetDirectory = resource.replace(relativeSearchPath, targetDirectory + "/" + symBunName).replace(folderToBeChanged, folderAfterChangements).replace("/", File.separator);
		} else {
			currentLocalTargetDirectory = resource.replace(relativeSearchPath, targetDirectory + "/" + symBunName).replace("/", File.separator);
		}
		return currentLocalTargetDirectory;
	}

	/**
	 * Checks if a blueprint template is found, that has the name given as argument
	 * to the method and return it as ProjectBlueprint
	 * @param blueprintName the blueprint name
	 * @return true, if is blueprint found
	 */
	private ProjectBlueprint getFoundProjectBlueprint(String blueprintName) {
		// call the getProjectBlueprintsAvailable method to get the list of available local blueprints first
		List<ProjectBlueprint> availableBlueprints;
		availableBlueprints = InternalResourceHandler.getProjectBlueprintsAvailable();

		// use the blueprint name given as argument to look for the required blueprint
		for (ProjectBlueprint pbp : availableBlueprints) {
			if (pbp.getBaseFolder().equalsIgnoreCase(blueprintName) == true) {
  				return pbp;
			}
		}
		return null;
	}

	/**
	 * Creates the target folder. If the file doesn't already exist, a try to create
	 * the folder is performed. The result of the try is returned as boolean.
	 * @param projectPath the project path and returns whether the folder already
	 * exists and whether a failure occurred while creating the new folder
	 */
	public boolean createTargetFolder(Path projectPath) {
		File folder = projectPath.toFile();
		boolean success = true;
		if (folder.exists() == false) {
			success = folder.mkdirs();
		}
		return success;
	}

	/**
	 * Reads a file and do text replacement in it
	 * @param file
	 * @param targetText
	 * @param replacementText
	 */
	private static void doTextReplacement(File file, HashMap<String, String> replacements) {
		// Read the content of the file and replace target text
		try {
			// Read file content
			StringBuilder content = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				// Replace target text with replacement text
				for (Map.Entry<String, String> entry : replacements.entrySet()) {
					String oldText = entry.getKey();
					String newText = entry.getValue();
					line = line.replace(oldText, newText);
				}
				content.append(line).append(System.lineSeparator());
			}
			reader.close();

			// Write the modified content back to the file
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(content.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the project blueprints.
	 * @return the project blueprints
	 */
	public ArrayList<ProjectBlueprint> getProjectBlueprints() {
		if (projectBlueprints == null) {
			projectBlueprints = new ArrayList<ProjectBlueprint>();
		}
		return projectBlueprints;
	}
	
	
	
	/**
	 * @param projectDir
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void runMavenGenerateSources(Path projectDir) throws IOException, InterruptedException {
	    ProcessBuilder builder = new ProcessBuilder( "cmd.exe", "/c", ".\\mvnw.cmd", "clean", "generate-sources");
	    builder.directory(projectDir.toFile());   
	    builder.inheritIO();
	    Process process = builder.start();
	    int exitCode = process.waitFor();
	    if (exitCode != 0) {
	        throw new RuntimeException("Maven command failed with exit code: " + exitCode);
	    } else {
	        try {
				deleteFileOrDirectory(projectDir.resolve(".mvn"));
			} catch (Exception e) {
				e.printStackTrace();
			}
	        Files.deleteIfExists(projectDir.resolve("mvnw"));
	        Files.deleteIfExists(projectDir.resolve("mvnw.cmd"));
	    }
	}
	
	/**
	 * @param path
	 * @throws Exception
	 */
	public static void deleteFileOrDirectory(Path path) throws Exception {
        File file = path.toFile();

        if (file.exists() == false) {
            return;
        }

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteFileOrDirectory(child.toPath());
                }
            }
        }
        boolean deleted = file.delete();
        if (!deleted) {
            System.out.println("Failed to delete: " + file.getAbsolutePath());
        }
	}

}
