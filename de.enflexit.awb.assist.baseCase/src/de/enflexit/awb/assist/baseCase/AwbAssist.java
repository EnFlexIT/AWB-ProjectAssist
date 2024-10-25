package de.enflexit.awb.assist.baseCase;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

	/** The Constant MANIFEST_FILE_PATH. */
	private static final String MANIFEST_FILE_PATH = "\\META-INF\\MANIFEST.MF" ;
	
	/** The Constant BLUEPRINT_DIRECTORY. */
	private static final String BLUEPRINT_DIRECTORY ="D:\\Git\\AWB-ProjectAssist\\de.enflexit.awb.assist.baseCase\\projectBlueprints";
	
	// ArrayList could automatically extend and shrink when changing its content. It is more dynamic compared to List
	// In our case the ArrayList is storing objects of type <ProjectBlueprint>
	private ArrayList<ProjectBlueprint> projectBlueprints;
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
	
		// this instance is a key for having access to the methods and fields of the class AwbAssist.
		AwbAssist assist = new AwbAssist() ;
	
		// To do
		// create a list that contains the files to be modified based on the blueprint templates.
		
		// A new instance of the class ProjectBlueprint is created
		ProjectBlueprint blueprintExample = new ProjectBlueprint();
		
		
		blueprintExample.setName("sample agent project");
		blueprintExample.setDescription("This blueprint provides a basic structure for an agent project.");
		blueprintExample.setBaseFolder("de.enflexit.awb.agentSample");
		blueprintExample.addReplacementString("[SymBundleName]");
		blueprintExample.addReplacementString("[BundleName]");
		blueprintExample.addReplacementFilesDirectory(".project");
		blueprintExample.addReplacementFilesDirectory(MANIFEST_FILE_PATH);
		
		// Here the getProjectBlueprint method is called to create the ArrayList projectBlueprints if it is not existing. 
		// Afterwards the object blueprintExample is added to the ArrayList projectBlueprints.
		assist.getProjectBlueprints().add(blueprintExample);
		
		// Associating arguments with constants. The order with which arguments are given need to be kept unchanged.
		String symBunName = args[1];
		String targetDirectory = args[2];
		
		// Here an object called replacements of type HashMap is created. 
		//Pairs are manually stored in the HashMap.
		HashMap<String, String> replacements = new HashMap<String, String>();
		replacements.put("[BundleName]", "Sample agent project");
		replacements.put("[SymBundleName]", "de.enflexit.awb.agentSample");
		
		System.out.println(replacements);
		
		//assist.createProjectFromBlueprint(blueprintExample, targetDirectory, symBunName, replacements);
		assist.createProjectFromBlueprint(blueprintExample, targetDirectory, symBunName, replacements);
	}
	
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
	public void createProjectFromBlueprint(ProjectBlueprint bluePrint, String targetDirectory, String symbBunName, HashMap<String, String> replacements) {	
		
		String projectDirectory= targetDirectory +File.separatorChar+ symbBunName;
		Path projectPath = Paths.get(projectDirectory);
		
		String sourceFolderPath = BLUEPRINT_DIRECTORY + File.separator + bluePrint.getBaseFolder();
		
		Path filePath1 = Paths.get(projectDirectory + MANIFEST_FILE_PATH);
		
		
		this.createTargetFolder(projectPath);
		this.copyBlueprintStructure(projectDirectory, sourceFolderPath);
		//For 
		for (String replacementString : bluePrint.getReplacementStrings()) {
			this.doTextReplacement(replacementString, replacements.get(replacementString), filePath1);
			//The get method of the HashMap retrieves the value that corresponds to the replacementString key.
		}
			
	}
	
	/**
	 * Creates the target folder.
	 *
	 * @param projectPath the project path
	 */
	private void createTargetFolder(Path projectPath) {
		
		// define the folder path
		File folder = projectPath.toFile();
		
		// Check whether the folder is already existing before proceeding with the creation
		if (folder.exists()) {
			System.out.println("The folder is already exisiting");
		} else {
			// Create the folder
			boolean success = folder.mkdirs();
			if (success == false) {
				System.out.println("Failed to create the folder");
			}
		}
	}

	/**
	 * Copy blueprint structure.
	 *
	 * @param projectDirectory the project directory
	 */
	private void copyBlueprintStructure(String projectDirectory, String bluePrintDirectory) {
		// Copy the content of the folder baseCase under the new folder created above
		// Step 1: Defining the source and target directories
		Path sourceDir = Paths.get(bluePrintDirectory);
		Path targetDir = Paths.get(projectDirectory);
		System.out.println("The Target Directory is: " + targetDir);
		System.out.println("The source directory is: " + sourceDir);
	
		try {
			copyDirectory(sourceDir, targetDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	private static void copyDirectory(Path sourceDir, Path targetDir) throws IOException {
		// Walk through the file tree starting from the source directory
		Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
			
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				// Create target directory by resolving its path relative to the source
				// directory
				Path targetPath = targetDir.resolve(sourceDir.relativize(dir));
				Files.createDirectories(targetPath); // Create directories as needed
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				// Copy each file from the source to the target directory
				Path targetPath = targetDir.resolve(sourceDir.relativize(file));
				Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				System.err.println("Failed to copy file: " + file + " - " + exc.getMessage());
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	/**
	 * Do text replacement.
	 * 
	 * The method looks for a defined text in a text with a given path and replaces it with another given text
	 *
	 * @param oldText the old text
	 * @param newText the new text
	 * @param filePath the file path
	 */
	private void doTextReplacement(String oldText, String newText, Path filePath) {
	
		try {
			// Step 1: Read all lines from the file into a List
			List<String> fileContent = Files.readAllLines(filePath);
	
			// Step 2: Replace the text blocks
			List<String> modifiedContent = fileContent.stream().map(line -> line.replace(oldText, newText)).collect(Collectors.toList());
	
			// Step 3: Write the modified content back to the same file (or a different file)
			Files.write(filePath, modifiedContent);
	
			System.out.println("Text replacement completed successfully.");
		} catch (IOException e1) {
			System.out.println("An error occurred while processing the file: " + e1.getMessage());
		}
		
	}
	
	public ArrayList<ProjectBlueprint> getProjectBlueprints() {
		if (projectBlueprints==null) {
			projectBlueprints= new ArrayList<ProjectBlueprint>();
		}
		return projectBlueprints;
	}

}
