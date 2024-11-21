package de.enflexit.awbAssist.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class AwbAssist.
 */
public class AwbAssist {

	// ------------------
	// --- old arguments : -blueprint SampleAgent -bundleName "Sample agent project"
	// -symBunName de.enflexit.awb.agentSample -targetDir D:
	// --- new arguments : -bp --- in case the user wants to get the available blueprints
	// ------------------

	private ArrayList<ProjectBlueprint> projectBlueprints;

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		// Print the help instructions if requested
		if (ArgumentsChecker.helper(args) == true) {
			return;
		}
		// Print the available blueprints if requested
		if (ArgumentsChecker.callForBlueprints(args) == true) {
			List<ProjectBlueprint> availableBlueprints = new ArrayList<>();
			availableBlueprints = InternalResourceHandler.getProjectBlueprintsAvailable();
			for (int i = 0; i < availableBlueprints.size(); i++) {
				System.out.println(availableBlueprints.get(i).getBaseFolder());
			}
			return;
		}
		AwbAssist assist = new AwbAssist();
		// a check is performed to verify that a blueprint name was mentioned in the arguments
		String bluePrint = ArgumentsChecker.checkBlueprintArgument(args);
		if (bluePrint.length() == 0) {
			System.err
					.println("[" + AwbAssist.class.getSimpleName() + "] No blue print name is given in the arguments");
			return;
		}
		// call the createProjectFromBlueprint method
		assist.createProjectFromBlueprint(bluePrint, args);
	}

	private void createProjectFromBlueprint(String blueprintName, String[] args) {

		// Check whether a blueprint template corresponds to the given blueprintName
		// and load the blueprint template corresponding to the given blueprint name.
		ProjectBlueprint blueprintToBeUsed = this.getFoundProjectBlueprint(blueprintName);
		if (blueprintToBeUsed == null) {
			System.err.println("no blue print was found with the name " + blueprintName);
			return;
		}

		// Required arguments and given arguments are transfered to the check method
		// in order to see whether it is possible to associate a value for each required
		// mandatory argument.
		HashMap<String, String> arguments = ArgumentsChecker.check(args, blueprintToBeUsed.getStartArguments());
		if (arguments == null) {
			System.out.println(
					"[" + AwbAssist.class.getSimpleName() + "] Arguments are not correct / Arguments are missing");
			return;
		}

		String symBunName = arguments.get("symBunName");
		String targetDirectory = arguments.get("targetDir");

		// A HashMap of replacement strings is generated
		// keys are extracted from the blueprint template 
		// values are extracted indirectly from the given arguments using the HashMap "arguments"
		HashMap<String, String> replacements = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : blueprintToBeUsed.getTextReplacements().entrySet()) {
			replacements.put(entry.getKey(), arguments.get(entry.getValue()));
		}

		// The folder called symBundleName is to be replaced with a folder structure
		// based on the given argument (symbolic bundle name)
		String folderToBeChanged = "symBundleName";
		String folderAfterChangements = symBunName.replace(".", File.separator);

		// Get a list of relative paths that represent the substructure of the blueprint template
		// Generate the relative search path to be used as reference
		String relativeSearchPath = "blueprints/" + blueprintToBeUsed.getBaseFolder();
		List<String> blueprintRelativeResources = InternalResourceHandler.findResources(relativeSearchPath);

		// create the main folder that should contain the blueprint's substructure
		Boolean resultCreateMainFolder = this.createTargetFolder(Path.of(targetDirectory + File.separator + symBunName));
		if (resultCreateMainFolder == false) {
			System.err.println(" the main folder couldn't be created");
		}

		// go through the list of paths, which stand for all files and folders that are present in the substructure
		// and create them one by one in the the target directory, while renaming files and folders if required as well as performing text replacements
		// in the files if needed.
		int i = 0;
		String currentLocalTargetDirectory = new String();
		String currentLocalTargetDirectoryAfterRenameCheck = new String();
		String currentRelativeResource = new String();
		while (i < blueprintRelativeResources.size()) {
			List<String> howManyResourcesAreHere = InternalResourceHandler.findResources(blueprintRelativeResources.get(i));
			// ---------- empty folders are recognized as files! -------------
			if (howManyResourcesAreHere.size() == 0) {
				// we have a file
				// extract to a specific directory and perform text replacements.
				// The json file is not extracted
				currentLocalTargetDirectory = this.getCurrentLocaltargetDirectory(i, blueprintRelativeResources,	folderToBeChanged, relativeSearchPath, targetDirectory, symBunName, folderAfterChangements);
				currentLocalTargetDirectoryAfterRenameCheck = fileRenameCheck(currentLocalTargetDirectory, replacements);
				currentRelativeResource = getCurrentRelativeResource(i, blueprintRelativeResources, folderToBeChanged);
				if (currentLocalTargetDirectoryAfterRenameCheck.contains("BlueprintStructure.json") == false) {
					File currentFilePath = new File(currentLocalTargetDirectoryAfterRenameCheck);
					InternalResourceHandler.extractFromBundle(currentRelativeResource, currentFilePath);
					doTextReplacement(currentFilePath, replacements);
				}

			} else {
				// we have a folder
				// try creating the folder
				currentLocalTargetDirectory = this.getCurrentLocaltargetDirectory(i, blueprintRelativeResources,folderToBeChanged, relativeSearchPath, targetDirectory, symBunName, folderAfterChangements);
				Boolean resultCreateFolder = this.createTargetFolder(Path.of(currentLocalTargetDirectory));
				if (resultCreateFolder == false) {
					System.out.println("the folder " + currentLocalTargetDirectory + " already exists");
				}
			}
			i++;
		}
	}

	/**
	 * File rename check to rename the currentLocalTargetDirectory if it contains a string from the replacement list.
	 *
	 * @param currentLocalTargetDirectory the current local target directory
	 * @param replacements the replacements
	 * @return the string
	 */
	private String fileRenameCheck(String currentLocalTargetDirectory, HashMap<String, String> replacements) {
		for (Map.Entry<String, String> entry : replacements.entrySet()) {
			if (currentLocalTargetDirectory.contains(entry.getKey())) {
				currentLocalTargetDirectory = currentLocalTargetDirectory.replace(entry.getKey(), entry.getValue());
			}
		}
		return currentLocalTargetDirectory;
	}

	/**
	 * Gets the relative resource of the element i of the given list.
	 *
	 * @param i                          the i
	 * @param blueprintRelativeResources the blueprint relative resources
	 * @param folderToBeChanged          the folder to be changed
	 * @return the current relative resource
	 */
	private String getCurrentRelativeResource(int i, List<String> blueprintRelativeResources,
			String folderToBeChanged) {
		String currentRelativeResource = null;
		if (blueprintRelativeResources.get(i).contains(folderToBeChanged)) {
			currentRelativeResource = "/" + blueprintRelativeResources.get(i);
		} else {
			currentRelativeResource = "/" + blueprintRelativeResources.get(i);
		}
		return currentRelativeResource;
	}

	/**
	 * Gives back the target directory to copy the element i of the given list at.
	 * The directory creation includes modifying the folder structure
	 * 
	 * @param i
	 * @param blueprintRelativeResources
	 * @param folderToBeChanged
	 * @param relativeSearchPath
	 * @param targetDirectory
	 * @param symBunName
	 * @param folderAfterChangements
	 * @return
	 */
	private String getCurrentLocaltargetDirectory(int i, List<String> blueprintRelativeResources,
			String folderToBeChanged, String relativeSearchPath, String targetDirectory, String symBunName,
			String folderAfterChangements) {
		String currentLocalTargetDirectory = null;
		if (blueprintRelativeResources.get(i).contains(folderToBeChanged)) {
			currentLocalTargetDirectory = blueprintRelativeResources.get(i)
					.replace(relativeSearchPath, targetDirectory + "/" + symBunName).replace("/", File.separator)
					.replace(folderToBeChanged, folderAfterChangements);
		} else {
			currentLocalTargetDirectory = blueprintRelativeResources.get(i)
					.replace(relativeSearchPath, targetDirectory + "/" + symBunName).replace("/", File.separator);
		}
		return currentLocalTargetDirectory;
	}

	/**
	 * Checks if a blueprint template is found, that has the name given as argument
	 * to the method and return it as ProjectBlueprint
	 *
	 * @param blueprintName the blueprint name
	 * @return true, if is blueprint found
	 */
	private ProjectBlueprint getFoundProjectBlueprint(String blueprintName) {
		// call the getProjectBlueprintsAvailable method to get the list of available
		// local blueprints first
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
	 * 
	 * @param projectPath the project path and returns whether the folder already
	 *                    exists and whether a failure occurred while creating the
	 *                    new folder
	 */
	private boolean createTargetFolder(Path projectPath) {
		File folder = projectPath.toFile();
		boolean success = true;
		if (folder.exists() == false) {
			success = folder.mkdirs();
		}
		return success;
	}

	/**
	 * Reads a file and do text replacement in it
	 * 
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
	 * 
	 * @return the project blueprints
	 */
	public ArrayList<ProjectBlueprint> getProjectBlueprints() {
		if (projectBlueprints == null) {
			projectBlueprints = new ArrayList<ProjectBlueprint>();
		}
		return projectBlueprints;
	}
}
