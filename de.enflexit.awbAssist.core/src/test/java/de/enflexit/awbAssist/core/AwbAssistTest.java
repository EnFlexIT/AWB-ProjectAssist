package de.enflexit.awbAssist.core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

// TODO add a method that checks whether the  there are [ or ] in which no recognized text are mentioned (recognized texts are which that are mentioned in the json file) -----Test the local blueprint-------

class AwbAssistTest {

	@Test
	void testing_renameCheck_SingleReplacement() throws Exception {

		String currentLocalTargetDirectory = "src/featureBlueprint/targetDir";
		HashMap<String, String> replacements = new HashMap<>();
		replacements.put("featureBlueprint", "testBlueprint");
		AwbAssist awb = new AwbAssist();
		String result = awb.renameCheck(currentLocalTargetDirectory, replacements);
		assertEquals("src/testBlueprint/targetDir", result);
	}
	
	@Test
	void testing_renameCheck_MultipleReplacements() throws Exception {

		String currentLocalTargetDirectory = "src/featureBlueprint/targetDir/bundleName";
		HashMap<String, String> replacements = new HashMap<>();
		replacements.put("featureBlueprint", "testingBlueprint");
		replacements.put("bundleName", "testingBundle");
		AwbAssist awb = new AwbAssist();
		String result = awb.renameCheck(currentLocalTargetDirectory, replacements);
		assertEquals("src/testingBlueprint/targetDir/testingBundle", result);
	}
	
	@Test
	void testing_renameCheck_NoReplacements() throws Exception {

		String currentLocalTargetDirectory = "src/featureBlueprint/targetDir/bundleName";
		HashMap<String, String> replacements = new HashMap<>();
		AwbAssist awb = new AwbAssist();
		String result = awb.renameCheck(currentLocalTargetDirectory, replacements);
		assertEquals("src/featureBlueprint/targetDir/bundleName", result);
	}

	@Test
	void testing_renameCheck_ReplacementNotFound() throws Exception {
		
	    String currentLocalTargetDirectory = "src/featureBlueprint/targetDir/bundleName";
	    HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("nonExisting", "replaced");
	    AwbAssist awb = new AwbAssist();
	    String result = awb.renameCheck(currentLocalTargetDirectory, replacements);
	    assertEquals("src/featureBlueprint/targetDir/bundleName", result);
	}
	
	@Test
	void testing_renameCheck_EmptyString() throws Exception {
		
	    String currentLocalTargetDirectory = "";
	    HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("featureBlueprint", "testingBlueprint");
	    AwbAssist awb = new AwbAssist();
	    String result= awb.renameCheck(currentLocalTargetDirectory, replacements);
	    assertEquals("", result);
	}
	
	@Test
	void testing_renameCheck_ReplacementWithEmptyValue() throws Exception {
		
	    String currentLocalTargetDirectory = "src/featureBlueprint/targetDir/bundleName";
	    HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("featureBlueprint", "");
	    AwbAssist awb = new AwbAssist();
	    String result = awb.renameCheck(currentLocalTargetDirectory, replacements);
	    assertEquals("src//targetDir/bundleName", result);
	}
	
	
	@Test
	void testing_getCurrentRelativeResource_WithValidIndex() throws Exception {
		
	    List<String> blueprintRelativeResources = new ArrayList<>();
	    blueprintRelativeResources.add("src/featureBlueprint/targetDir");
	    int i = 0;  
	    AwbAssist awb = new AwbAssist();
	    String result = awb.getCurrentRelativeResource(i, blueprintRelativeResources);
	    assertEquals("/src/featureBlueprint/targetDir", result);  
	}

	// -------- the following two methods are expected to return an error ---------
	@Test
	void testing_getCurrentRelativeResource_WithInvalidIndex() throws Exception {

		List<String> blueprintRelativeResources = new ArrayList<>();
		blueprintRelativeResources.add("src/featureBlueprint/targetDir");
		int i = 1;
		AwbAssist awb = new AwbAssist();
		String result = awb.getCurrentRelativeResource(i, blueprintRelativeResources);
		assertEquals(null, result);
	}
	@Test
	void testing_getCurrentRelativeResource_WithEmptyList() throws Exception {
		
		List<String> blueprintRelativeResources = new ArrayList<>();
		int i = 0;
		AwbAssist awb = new AwbAssist();
		String result = awb.getCurrentRelativeResource(i, blueprintRelativeResources);
		assertEquals(null, result);
	}
	// ----------- Methods with expected errors end here ----------------
	
	
	@Test
	void testing_getCurrentRelativeResource_WithMultipleResources() throws Exception {
		
	    List<String> blueprintRelativeResources = new ArrayList<>();
	    blueprintRelativeResources.add("src/featureBlueprint/targetDir");
	    blueprintRelativeResources.add("src/testBlueprint/targetDir");
	    int i = 1;  
	    AwbAssist awb = new AwbAssist();
	    String result = awb.getCurrentRelativeResource(i, blueprintRelativeResources);
	    assertEquals("/src/testBlueprint/targetDir", result); 
	}
	
	
	@Test void testing_getCurrentLocaltargetDirectory_WithFolderChange() throws Exception { 
		
	    List<String> blueprintRelativeResources = new ArrayList<>(); 
	    blueprintRelativeResources.add("src/featureBlueprint/targetDir/bundleName"); 
	    String folderToBeChanged = "bundleName";
	    String relativeSearchPath = "src/featureBlueprint"; 
	    String targetDirectory = "D:/projects"; 
	    String symBunName = "testSymBun";
	    String folderAfterChangements = "newBundleName"; 
	    int i = 0;
	    AwbAssist awb = new AwbAssist();
	    String result = awb.getCurrentLocaltargetDirectory(i, blueprintRelativeResources, folderToBeChanged, relativeSearchPath, targetDirectory, symBunName, folderAfterChangements);
	    String expected = ("D:/projects/testSymBun/targetDir/newBundleName").replace("/", File.separator); 
	    assertEquals(expected, result); 
	}
	
	
	
	@Test void testing_createProjectFromBlueprint_normalCase() throws Exception { 
		
		
	    List<String> blueprintRelativeResources = new ArrayList<>(); 
	    blueprintRelativeResources.add("src/featureBlueprint/targetDir/noFolderChange"); 
	    String folderToBeChanged = "bundleName"; 
	    String relativeSearchPath = "src/featureBlueprint"; 
	    String targetDirectory = "D:/projects"; 
	    String symBunName = "testSymBun"; 
	    String folderAfterChangements = "newBundleName"; 
	    int i = 0; 
	    AwbAssist awb = new AwbAssist(); 
	    String result = awb.getCurrentLocaltargetDirectory(i, blueprintRelativeResources, folderToBeChanged, relativeSearchPath, targetDirectory, symBunName, folderAfterChangements);
	    String expected = ("D:/projects/testSymBun/targetDir/noFolderChange").replace("/", File.separator); 
	    assertEquals(expected, result); 
	}
	
	




	
}
