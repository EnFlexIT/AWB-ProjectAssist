package de.enflexit.awbAssist.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Class AwbAssist.
 */
public class AwbAssist {

	// --- Exercise ---
	/*
	 * Das Projekt 'baseCase' kopieren und in ein neues Projekt / einen neuen Ordner
	 * erstellen. - aus dem Ordner 'baseCase' soll der [SymBundleName] werden (z.B.
	 * net.peak.agent.prosumerAgent) - die Parameter ('SymBundleName' & 'BundleName'
	 * & Zielverzeichnis) sollen als Startargumente übergeben werden können
	 * Eine Classe hinzufügen, die die Argumente in Variabeln packt?
	 */
	
	// TODO replace hard-coded absolute path with a relative path to the project root
	private static final String BLUEPRINT_DIRECTORY ="D:\\Git\\AWB-ProjectAssist\\de.enflexit.awbAssist.core\\src\\main\\resources\\blueprints";
	
	// In our case the ArrayList is storing objects of type <ProjectBlueprint>
	private ArrayList<ProjectBlueprint> projectBlueprints;
	/**
	 * The main method.
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		// I
		InternalResourceHandler.test();
		
		// this instance is a key for having access to the methods and fields of the class AwbAssist.
		AwbAssist assist = new AwbAssist() ;
		// a check is performed to verify that a blueprint was mentioned in the arguments
		String bluePrint = checkBlueprintArgument(args);
		if (bluePrint.length() == 0 ) {
				System.err.println("no blue print name is given in the arguments");
			return;
		}
		
		// Here a path is created under which the required arguments are to be found
		Path pathOfJsonFile = Paths.get(BLUEPRINT_DIRECTORY + File.separator + bluePrint + File.separator + "BlueprintStructure.json");

		// The json file is read to get the attributes of blueprintToBeUsed as defined in the ProjectBlueprint class.
		ProjectBlueprint blueprintToBeUsed = ProjectBlueprint.load(pathOfJsonFile);
		
		// out of the attributes of blueprintToBeUsed we get the required arguments using the getRequiredArguments method-
		ArrayList<StartArgument> requiredArguments = blueprintToBeUsed.getRequiredArguments();
		
		// ---- Arguments that need to be given (mandatory arguments) are extracted and handed to the arguments checker. ----
		// TODO The presence of optional arguments is not checked - does this need to change?? 
		int i=0;
		ArrayList<String> requiredArgumentEsxtract = new ArrayList<>();
		while (i < requiredArguments.size()) {
			String argName = requiredArguments.get(i).getArgumentName();
			if (requiredArguments.get(i).isMandatory()) {
				requiredArgumentEsxtract.add(argName);
			}
			i++;
		}

		// The array list of required arguments is given together with the arguments to the check method
		// in order to see whether it is possible to associate a value for each required argument using the given arguments.
		// TODO not only (requiredArgumentEsxtract.add(argName) is required but also default values --> input of ArgumentsChecker.check should be of type StartArgument
		
		HashMap<String, String> arguments = ArgumentsChecker.check(args, requiredArgumentEsxtract);
		if (arguments == null) {
			System.out.println("Arguments are not correct / Arguments are missing");
			return;
		}
		
		// -------- the values of required arguments are extracted from the HashMap and manually put in separate objects ----  
		String bundleName = arguments.get("bundleName");
		String symBunName = arguments.get("symBunName");
		String targetDirectory = arguments.get("targetDir");
		//Pairs are manually stored in the HashMap.
		HashMap<String, String> replacements = new HashMap<String, String>();
		replacements.put("[BundleName]", bundleName);
		replacements.put("[SymBundleName]", symBunName);
		
		System.out.println("the replacements are " + replacements);
		
		// the creation of the project out of the blueprintToBeUsed is then handed to a separate method.
		boolean resultCreateProjectFromBluePRint = assist.createProjectFromBlueprint(blueprintToBeUsed, targetDirectory, symBunName, replacements);
		if(resultCreateProjectFromBluePRint == false) {
			System.err.println("project creation was not successfull");
		}
	}
	
	/**
	 * Creates the project from blueprint and returns a boolean value regarding the operation success.
	 * under the given target directory a folder named with the given symbolic bundle name is created by calling the createTargetFolder method. 
	 * under the project directory a modified copy of the used blueprint's structure is pasted according to the copyBlueprintStructure method.
	 * Text replacement is performed under the project path and for the replacements HashMap by calling the doTextReplacementToDirectory method.
	 * each of the methods called return a boolean value that stops the process in case a false is returned.
	 * @param projectName the project name
	 * @param symBunName the sym bun name
	 * @param targetDirectory the target directory
	 */
	public boolean createProjectFromBlueprint(ProjectBlueprint bluePrint, String targetDirectory, String symbBunName, HashMap<String, String> replacements) {	
		String projectDirectory= targetDirectory +File.separatorChar+ symbBunName;
		Path projectPath = Paths.get(projectDirectory);
		String sourceFolderPath = BLUEPRINT_DIRECTORY + File.separator + bluePrint.getBaseFolder();
		
		boolean resultCreateTargetFolder = this.createTargetFolder(projectPath);
		if (resultCreateTargetFolder == false) {
			System.out.println("Failed to create the target folder");
			return false;
		}
		boolean resultCopyBlueprintStructure = this.copyBlueprintStructure(projectDirectory, sourceFolderPath, symbBunName);
		if (resultCopyBlueprintStructure == false) {
			return false;
		}
		try {
			return this.doTextReplacementToDirectory(projectPath, replacements);
		} catch (IOException e) {
			System.err.println("Text replacement failed");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Creates the target folder.
	 * If the file doesn't already exist, a try to create the folder is performed. The result of the try is returned as boolean.
	 * @param projectPath the project path and returns whether the folder already exists and whether a failure occurred while creating the new folder
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
	 * Copy blueprint structure and paste it under project directory while changing it according to the 
	 * copyDirectory method. A boolean is returned as a result of the operation
	 * @param projectDirectory the project directory
	 */
	
	private boolean copyBlueprintStructure(String projectDirectory, String bluePrintDirectory, String symBundleName) {
		// The input strings are converted to paths before handing them to the copyDirectory method
		Path sourceDir = Paths.get(bluePrintDirectory);
		Path targetDir = Paths.get(projectDirectory);
		System.out.println("The Target Directory is: " + targetDir);
		System.out.println("The source directory is: " + sourceDir);
		try {
			copyDirectory(sourceDir, targetDir, symBundleName);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Copy directory.
	 * The substructure of a folder is copied from the given source directory to the target directory
	 * @param sourceDir the source dir
	 * @param targetDir the target dir
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void copyDirectory(Path sourceDir, Path targetDir, String symBunName) throws IOException {
		// Walk through the file tree starting from the source directory
		CopyDirectoryVisitor copyVisitor = new CopyDirectoryVisitor(symBunName, sourceDir, targetDir);
		Files.walkFileTree(sourceDir, copyVisitor);
	}
	
	/**
	 * Do text replacement to directory.
	 * Each file is visited and texts, which are matching the keys of the replacements HashMap
	 * are replaced with the corresponding values.
	 * @param rootDirectory the root directory
	 * @param replacements the replacements
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private boolean doTextReplacementToDirectory(Path rootDirectory, HashMap<String, String> replacements) throws IOException {
		TextReplacementFileVisitor replacementVisitor = new TextReplacementFileVisitor(replacements);
		Files.walkFileTree(rootDirectory, replacementVisitor);
		return replacementVisitor.isReplacementSuccess();
	}
	
	/**
	 * Gets the project blueprints.
	 * @return the project blueprints
	 */
	public ArrayList<ProjectBlueprint> getProjectBlueprints() {
		if (projectBlueprints==null) {
			projectBlueprints= new ArrayList<ProjectBlueprint>();
		}
		return projectBlueprints;
	}
	

	/**
	 * Check whether a blueprint name is present among the arguments.
	 *
	 * @param args the args
	 * @return the string
	 */
	private static String checkBlueprintArgument(String[] args) {
		int i = 0;
		String bluePrint = "";
		while (i < args.length) {
			if (args[i].equalsIgnoreCase("-blueprint")) {
				if (i + 1 < args.length) {
					bluePrint = args[i + 1];
					return bluePrint;
				}
			}
			i++;
		}
		return bluePrint;
	}

}
