package de.enflexit.awb.assist.baseCase;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;

public class AwbAssist {

	// --- Exercise ---
	/*
	 * Das Projekt 'baseCase' kopieren und in ein neues Projekt / einen neuen Ordner
	 * erstellen. - aus dem Ordner 'baseCase' soll der [SymBundleName] werden (z.B.
	 * net.peak.agent.prosumerAgent) - die Parameter ('SymBundleName' & 'BundleName'
	 * & Zielverzeichnis) sollen als Startargumente übergeben werden können
	 * Eine Classe hinzufügen, die die Argumente in Variabeln packt?
	 */

	public static void main(String[] args) {

		// define the folder path
		File folder = new File(args[2] + args[0]);

		// Check whether the folder is already existing before proceeding with the creation
		if (folder.exists()) {
			System.out.println("The folder is already exisiting");
		} else {
			// Create the folder
			if (folder.mkdirs()) {
				// mkdirs creates the directory and returns the result of the operation as a
				// boolean value
				// Is the confirmation of the creation needed?
				// System.out.println("Folder created successfully");
			} else {
				System.out.println("Failed to create the folder");
			}
		}

		// Copy the content of the folder baseCase under the new folder created above
		// Step 1: Defining the source and target directories
		Path sourceDir = Paths.get(args[3]);
		Path targetDir = Paths.get(args[2] + args[0]);
		System.out.println("The Target Directory is: " + targetDir);
		System.out.println("The source directory is: " + sourceDir);

		try {
			copyDirectory(sourceDir, targetDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Text replacement
		// Step 1: Define the file path
		Path filePath = Paths.get(args[2] + args[0] + args[4]);

		// optional
		String filepath = (args[2] + args[0] + args[4]);
		System.out.println("the file path is : " + filepath);

		// Step 2: Define the text to be replaced and the new text
		String oldText1 = "[BundleName]";
		String newText1 = (args[0]);
		String oldText2 = "[SymBundleName]";
		String newText2 = (args[1]);

		try {
			// Step 3: Read all lines from the file into a List
			List<String> fileContent = Files.readAllLines(filePath);

			// Step 4: Replace the text blocks
			List<String> modifiedContent = fileContent.stream().map(line -> line.replace(oldText1, newText1))
					.map(line -> line.replace(oldText2, newText2)).collect(Collectors.toList());

			// Step 5: Write the modified content back to the same file (or a different
			// file)
			Files.write(filePath, modifiedContent);

			System.out.println("Text replacement completed successfully.");
		} catch (IOException e1) {
			System.out.println("An error occurred while processing the file: " + e1.getMessage());
		}
	}

	/**
	 * Copy directory.
	 *+
	 * @param sourceDir the source dir
	 * @param targetDir the target dir
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copyDirectory(Path sourceDir, Path targetDir) throws IOException {
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
}
