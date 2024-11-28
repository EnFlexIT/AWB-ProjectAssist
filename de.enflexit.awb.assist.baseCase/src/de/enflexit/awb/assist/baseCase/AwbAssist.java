package de.enflexit.awb.assist.baseCase;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
		
		if (args.length<3) {
			System.out.println("Not enough arguments are given");
			return;
		}
		
		// this instance is a key for having access to the methods and fields of the class AwbAssist.
		AwbAssist assist = new AwbAssist() ;
	
		// To do
		// create a list that contains the files to be modified based on the blueprint templates.
		
		// A new instance of the class ProjectBlueprint is created
				
		ProjectBlueprint blueprintTest = ProjectBlueprint.load(Path.of("D:\\learning\\Testing java product.json"));
		
		
		// Here the getProjectBlueprint method is called to create the ArrayList projectBlueprints if it is not existing. 
		// Afterwards the object blueprintExample is added to the ArrayList projectBlueprints.
		assist.getProjectBlueprints().add(blueprintTest);
		
		
		// @Omar: Just playing around
		//String bundlNameFromBlueprint = blueprintTest.getName();
		//System.out.println("the bundle name is :  "+bundlNameFromBlueprint);
		// Playing ends here. The lines in between could be removed without affecting the functionalities of the script
		
		
		// Associating arguments with constants. The order with which arguments are given need to be kept unchanged.
		String bundleName = args[0];
		String symBunName = args[1];
		String targetDirectory = args[2];
		//symBunName.matches(targetDirectory)
		
		// Here an object called replacements of type HashMap is created. 
		//Pairs are manually stored in the HashMap.
		HashMap<String, String> replacements = new HashMap<String, String>();
		replacements.put("[BundleName]", bundleName);
		replacements.put("[SymBundleName]", symBunName);
		
		System.out.println(replacements);
		
		//assist.createProjectFromBlueprint(blueprintExample, targetDirectory, symBunName, replacements);
		assist.createProjectFromBlueprint(blueprintTest, targetDirectory, symBunName, replacements);
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
		
		this.createTargetFolder(projectPath);
		this.copyBlueprintStructure(projectDirectory, sourceFolderPath);
		//For
		
		try {
			this.doTextReplacementToDirectory(projectPath, replacements);
		} catch (IOException e) {
		System.err.println("Text replacement failed");
			e.printStackTrace();
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
	
	private void doTextReplacementToDirectory(Path rootDirectory, HashMap<String, String> replacements) throws IOException {
		Files.walkFileTree(rootDirectory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				for (String replacementString : replacements.keySet()) {
					AwbAssist.this.doTextReplacement(replacementString, replacements.get(replacementString), file);
					//The get method of the HashMap retrieves the value that corresponds to the replacementString key.
				}
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
