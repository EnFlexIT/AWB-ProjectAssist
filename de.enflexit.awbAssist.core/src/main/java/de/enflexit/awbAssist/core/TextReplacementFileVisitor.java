package de.enflexit.awbAssist.core;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class TextReplacementFileVisitor does replaces specified strings in all files in a folder and 
 * its sub-folders. It is a sub-class of {@link SimpleFileVisitor}.
 *
 * @author Omar Ben Chobba - Enflex.IT GmbH
 */
public class TextReplacementFileVisitor extends SimpleFileVisitor<Path> {
	
	/** The replacements. */
	private HashMap<String, String> replacements;
	
	/** The replacement success. */
	private boolean replacementSuccess = true;
	
	/**
	 * Instantiates a new text replacement file visitor.
	 * @param replacements the strings to be replaced, and their corresponding replacements.
	 */
	public TextReplacementFileVisitor(HashMap<String, String> replacements) {
		this.replacements = replacements;
	}

	/**
	 * Visit file.
	 *
	 * @param file the file
	 * @param attrs the attrs
	 * @return the file visit result
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		// TODO Auto-generated method stub
		for (String replacementString : replacements.keySet()) {
			boolean resultDoTextReplacement = this.doTextReplacement(replacementString, replacements.get(replacementString), file);
			if (resultDoTextReplacement == false) {
				this.replacementSuccess = false;
			}
			//The get method of the HashMap retrieves the value that corresponds to the replacementString key.
		}
		return FileVisitResult.CONTINUE;
	}
	
	/**
	 * Do text replacement replaces a given old text with a new one in a specific file.
	 *
	 * @param oldText the old text
	 * @param newText the new text
	 * @param filePath the file path
	 * @return true, if successful
	 */
	private boolean doTextReplacement(String oldText, String newText, Path filePath) {
		
		try {
			// Step 1: Read all lines from the file into a List
			List<String> fileContent = Files.readAllLines(filePath);
	
			// Step 2: Replace the text blocks
			List<String> modifiedContent = fileContent.stream().map(line -> line.replace(oldText, newText)).collect(Collectors.toList());
	
			// Step 3: Write the modified content back to the same file (or a different file)
			Files.write(filePath, modifiedContent);
			return true;
	
		} catch (IOException e1) {
			System.out.println("An error occurred while processing the file: " + e1.getMessage());
			return false;
		}
		
	}
	
	/**
	 * Checks if the replacement was successful.
	 * @return true, if is replacement success
	 */
	public boolean isReplacementSuccess() {
		return replacementSuccess;
	}
}
