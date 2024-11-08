package de.enflexit.awbAssist.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
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
	
	// TODO create a class that defines from a list of strings "arguments" for each arguments whether it is mandatory or optional
	// TODO check whether the blueprint name is given. (if not return error)
	// TODO put this in a separate method
	// TODO  a search method that looks for a blueprint based on the given argument, regardless of the case sensitivity
	// TODO create the path out of the blueprint name : convention -->
	// TODO read the json file 
	// TODO get the list of the required arguments
	// TODO give the list of required arguments to argumentsChekcer (use reference list)
	// TODO do the check as already planned with the class arguments checker.
	
	//TODO replace hard-coded absolute path with a relative path to the project root
	private static final String BLUEPRINT_DIRECTORY ="D:\\Git\\AWB-ProjectAssist\\de.enflexit.awbAssist.core\\src\\main\\resources\\blueprints";
	
	// In our case the ArrayList is storing objects of type <ProjectBlueprint>
	private ArrayList<ProjectBlueprint> projectBlueprints;
	/**
	 * The main method.
	 *
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
		ArrayList<String> requiredArgs = blueprintToBeUsed.getRequiredArguments();

		// The array list of required arguments is given together with the arguments to the check method
		// in order to see whether it is possible to associate a value for each required argument using the given arguments.
		HashMap<String, String> arguments = ArgumentsChecker.check(args, requiredArgs);
		if (arguments == null) {
			System.out.println("Arguments are not correct / Arguments are missing");
			return;
		}
		
		// -------- the values of required arguments are extracted from the HashMap and manually put in separate objects ----  
		// TODO check whether it is possible to replace manual input with automated input
		String bundleName = arguments.get("bundleName");
		String symBunName = arguments.get("symBunName");
		String targetDirectory = arguments.get("targetDir");
		//Pairs are manually stored in the HashMap.
		HashMap<String, String> replacements = new HashMap<String, String>();
		replacements.put("[BundleName]", bundleName);
		replacements.put("[SymBundleName]", symBunName);
		
		System.out.println("the replacements are " + replacements);
		
		// the creation of the project out of the blueprintToBeUsed is handed to 
		boolean resultCreateProjectFromBluePRint = assist.createProjectFromBlueprint(blueprintToBeUsed, targetDirectory, symBunName, replacements);
		if(resultCreateProjectFromBluePRint == false) {
			System.err.println("project creation was not successfull");
		}
	}
	
	//TODO create a method to check whether the target folder already exists
	// --> end the process and write that the folder already exists
	
	/**
	 * Creates the project from blueprint.
	 * 
	 * First is the projectpath set as a combination of the project name and the project directory
	 * Using a combination of three methods the project folder is created using its name and 
	 *
	 * @param projectName the project name
	 * @param symBunName the sym bun name
	 * @param targetDirectory the target directory
	 */
	//public void createProjectFromBlueprint(String projectName, String symBunName, String targetDirectory, String bluePrintDirectory) {
	public boolean createProjectFromBlueprint(ProjectBlueprint bluePrint, String targetDirectory, String symbBunName, HashMap<String, String> replacements) {	
		String projectDirectory= targetDirectory +File.separatorChar+ symbBunName;
		Path projectPath = Paths.get(projectDirectory);
		
		String sourceFolderPath = BLUEPRINT_DIRECTORY + File.separator + bluePrint.getBaseFolder();
		//TODO verifying each of the three methods whether it worked correctly or not using a boolean variable. 
		boolean resultCreateTargetFolder = this.createTargetFolder(projectPath);
		if (resultCreateTargetFolder == false) {
			System.out.println("Failed to create the target folder");
			return false;
		}
		
		boolean resultCopyBlueprintStructure = this.copyBlueprintStructure(projectDirectory, sourceFolderPath, symbBunName);
		if (resultCopyBlueprintStructure == false) {
			return false;
		}
		//if the copying didn't work --> end the method
		
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
	 *
	 * @param projectPath the project path and returns whether the folder already exists and whether a failure occurred while creating the new folder
	 * 
	 */
	private boolean createTargetFolder(Path projectPath) {
		// Define the folder path
		File folder = projectPath.toFile();
		// Check whether the folder is already existing before proceeding with the creation
		boolean success = true;
		if (folder.exists() == false) {
			// Create the folder
			success = folder.mkdirs();
//			if (success == false) {
//				System.out.println("Failed to create the folder");
//			}
		}	
		return success;
	}

	/**
	 * Copy blueprint structure.
	 *
	 * @param projectDirectory the project directory
	 */
	
	//TODO : return a boolean value to  tell whether the method succeeded copying the structure or not statement is temporary
	private boolean copyBlueprintStructure(String projectDirectory, String bluePrintDirectory, String symBundleName) {
		// Copy the content of the folder baseCase under the new folder created above
		// Step 1: Defining the source and target directories
		Path sourceDir = Paths.get(bluePrintDirectory);
		Path targetDir = Paths.get(projectDirectory);
		System.out.println("The Target Directory is: " + targetDir);
		System.out.println("The source directory is: " + sourceDir);
		try {
			copyDirectory(sourceDir, targetDir, symBundleName);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Copy directory.
	 *The substructure of a folder is copied from the given source directory to the target directory
	 *
	 * @param sourceDir the source dir
	 * @param targetDir the target dir
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void copyDirectory(Path sourceDir, Path targetDir, String symBunName) throws IOException {
		// Walk through the file tree starting from the source directory
		CopyDirectoryVisitor copyVisitor = new CopyDirectoryVisitor(symBunName, sourceDir, targetDir);
		Files.walkFileTree(sourceDir, copyVisitor);
	}
	
	// This method looks for the last folder in the given path, replaces its name with the given symbolic bundle name 
	// while also replacing points in the symbolic bundle name by file separators and returns the result as a path object
//	private static Path correctFilePath(Path dir, String symBunName) {
//		String dirAsString = dir.toString();
//		int indexLastSeparator = dirAsString.lastIndexOf(File.separator);
//		String dirAsStringFirstPart = dirAsString.substring(0, indexLastSeparator);
//		String dirAsStringSecondPart = symBunName.replace(".", File.separator);
//		String dirAsStringModified = dirAsStringFirstPart + dirAsStringSecondPart;
//		Path dirModified = Paths.get(dirAsStringModified);
	
//		return dirModified;
//	}
	
	private boolean doTextReplacementToDirectory(Path rootDirectory, HashMap<String, String> replacements) throws IOException {
		TextReplacementFileVisitor replacementVisitor = new TextReplacementFileVisitor(replacements);
		Files.walkFileTree(rootDirectory, replacementVisitor);
		return replacementVisitor.isReplacementSuccess();
	}
	
	
	public ArrayList<ProjectBlueprint> getProjectBlueprints() {
		if (projectBlueprints==null) {
			projectBlueprints= new ArrayList<ProjectBlueprint>();
		}
		return projectBlueprints;
	}
	
	/**
	 * this method looks for a blueprint name given as an argument while ignoring small and capital letter differences
	 * if found the name is returned as string otherwise an empty string is returned
	 * 
	 *
	 * @param args the args
	 * @return the string
	 */
	private static String checkBlueprintArgument(String[] args) {
	int i=0;
	String bluePrint="";
	while (i < args.length) {
		if(args[i].equalsIgnoreCase("-blueprint")) {
			if (i+1 < args.length) {
				bluePrint = args[i+1];
				return bluePrint;
			}
		}
		i++;	
	}
	return bluePrint;
	}

}
