package de.enflexit.awbAssist.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyDirectoryVisitor extends SimpleFileVisitor<Path> {
	private String symbolicBundleName;
	private Path sourceDir;
	private Path targetDir; 
	
	public CopyDirectoryVisitor(String symbolicBundleName, Path sourceDir, Path targetDir) {
		this.symbolicBundleName = symbolicBundleName;
		this.sourceDir = sourceDir;	
		this.targetDir = targetDir;	
	}
	
	/**
	 * Pre visit directory.
	 * @param dir the dir
	 * @param attrs the attrs
	 * @return the file visit result
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

		// Create target directory by resolving its path relative to the source directory
		Path targetPath = null;
		targetPath = targetDir.resolve(sourceDir.relativize(dir));
		
		// if a folder contains the name "symBundleName", the correctFilePath method is called to modify it.
		if (targetPath.toString().contains("symBundleName")) {
			targetPath = correctFilePath(targetPath, symbolicBundleName);
		}
		
		// Create directories as needed
		Files.createDirectories(targetPath);
		return FileVisitResult.CONTINUE;
	}
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		
		// Copy each file from the source to the target directory
		Path targetPath = targetDir.resolve(sourceDir.relativize(file));

		// if a folder contains the name "symBundleName", the correctFilePath method is called to modify it.
		if (targetPath.toString().contains("symBundleName")) {
			targetPath = correctFilePath(targetPath, symbolicBundleName);
		}
		try {
			Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (NoSuchFileException nsfe) {
			System.out.println("Failed to copy: " + file + "\n to " + targetPath);
		}
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		System.err.println("Failed to copy file: " + file + " - " + exc.getMessage());
		return FileVisitResult.CONTINUE;
	}
	
	/**
	 * Correct file path.
	 * The given symBunName is treated : the dots of this input are replaced by file separators resulting in a folder structure.
	 * The method looks then for the folder with the name "symBundleName" and replaces it with the set of subsequent folders mentioned in the previous line.
	 * The modified path is then returned.
	 * @param dir the dir
	 * @param symBunName the sym bun name
	 * @return the path
	 */
	private static Path correctFilePath(Path dir, String symBunName) {
		String dirAsString = dir.toString();
		String dirAsStringModified = "";
		int indexOfSymBunName = dirAsString.indexOf("symBundleName");
		String dirAsStringFirstPart = dirAsString.substring(0, indexOfSymBunName);
		String dirAsStringSecondPart = symBunName.replace(".", File.separator);
		
			
		if (dirAsString.length() > (dirAsStringFirstPart.length() + "symBundleName".length())) {
			String dirAsStringThirdPart = dirAsString.substring(dirAsStringFirstPart.length() + "symBundleName".length() + 1);
			dirAsStringModified = dirAsStringFirstPart + dirAsStringSecondPart + File.separator + dirAsStringThirdPart;
		}
		if (dirAsString.length() == (dirAsStringFirstPart.length() + "symBundleName".length())) {
			dirAsStringModified = dirAsStringFirstPart + dirAsStringSecondPart;
		}
		
		Path dirModified = Paths.get(dirAsStringModified);
		return dirModified;
	}
}
