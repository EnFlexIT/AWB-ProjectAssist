package de.enflexit.awbAssist.core;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

class AwbAssistTest {

	@Test
	void testing_renameCheck_SingleReplacement() throws Exception {
	    String currentLocalTargetDirectory = "src/featureBlueprint/targetDir";
	    HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("featureBlueprint", "testBlueprint");
	    AwbAssist awb =new AwbAssist();
	    Method renameCheckMethod;
				renameCheckMethod = AwbAssist.class.getDeclaredMethod("renameCheck", String.class, HashMap.class);
				renameCheckMethod.setAccessible(true);
				String result = (String) renameCheckMethod.invoke(awb, currentLocalTargetDirectory, replacements);
				assertEquals("src/testBlueprint/targetDir", result);
	}
	
	@Test
	void testing_renameCheck_Replacements() throws Exception {
		String currentLocalTargetDirectory = "src/featureBlueprint/targetDir/bundleName";
		HashMap<String, String> replacements = new HashMap<>();
		replacements.put("featureBlueprint", "testingBlueprint");
		replacements.put("bundleName", "testingBundle");

		AwbAssist awb = new AwbAssist();
		Method renameCheckMethod;
		renameCheckMethod = AwbAssist.class.getDeclaredMethod("renameCheck", String.class, HashMap.class);
		renameCheckMethod.setAccessible(true);

		String result = (String) renameCheckMethod.invoke(awb, currentLocalTargetDirectory, replacements);
		assertEquals("src/testingBlueprint/targetDir/testingBundle", result);
	}
	
	@Test
	void testing_renameCheck_NoReplacements() throws Exception {
	    String currentLocalTargetDirectory = "src/featureBlueprint/targetDir/bundleName";
	    HashMap<String, String> replacements = new HashMap<>();

	    AwbAssist awb = new AwbAssist();
	    Method renameCheckMethod;
	    renameCheckMethod = AwbAssist.class.getDeclaredMethod("renameCheck", String.class, HashMap.class);
	    renameCheckMethod.setAccessible(true);

	    String result = (String) renameCheckMethod.invoke(awb, currentLocalTargetDirectory, replacements);
	    assertEquals("src/featureBlueprint/targetDir/bundleName", result);
	}

	@Test
	void testing_renameCheck_NotFound() throws Exception {
	    String currentLocalTargetDirectory = "src/featureBlueprint/targetDir/bundleName";
	    HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("nonExisting", "replaced");

	    AwbAssist awb = new AwbAssist();
	    Method renameCheckMethod;
	    renameCheckMethod = AwbAssist.class.getDeclaredMethod("renameCheck", String.class, HashMap.class);
	    renameCheckMethod.setAccessible(true);

	    String result = (String) renameCheckMethod.invoke(awb, currentLocalTargetDirectory, replacements);
	    assertEquals("src/featureBlueprint/targetDir/bundleName", result);
	}
	
	@Test
	void testing_renameCheck_EmptyString() throws Exception {
	    String currentLocalTargetDirectory = "";
	    HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("featureBlueprint", "testingBlueprint");

	    AwbAssist awb = new AwbAssist();
	    Method renameCheckMethod;
	    renameCheckMethod = AwbAssist.class.getDeclaredMethod("renameCheck", String.class, HashMap.class);
	    renameCheckMethod.setAccessible(true);

	    String result = (String) renameCheckMethod.invoke(awb, currentLocalTargetDirectory, replacements);
	    assertEquals("", result);
	}
	
	@Test
	void testing_renameCheck_ReplacementWithEmptyValue() throws Exception {
	    String currentLocalTargetDirectory = "src/featureBlueprint/targetDir/bundleName";
	    HashMap<String, String> replacements = new HashMap<>();
	    replacements.put("featureBlueprint", "");

	    AwbAssist awb = new AwbAssist();
	    Method renameCheckMethod;
	    renameCheckMethod = AwbAssist.class.getDeclaredMethod("renameCheck", String.class, HashMap.class);
	    renameCheckMethod.setAccessible(true);

	    String result = (String) renameCheckMethod.invoke(awb, currentLocalTargetDirectory, replacements);
	    assertEquals("src//targetDir/bundleName", result);
	}
	
	
	@Test
	void testing_getCurrentRelativeResource_WithValidIndex() throws Exception {
	    List<String> blueprintRelativeResources = new ArrayList<>();
	    blueprintRelativeResources.add("src/featureBlueprint/targetDir");

	    int i = 0;  

	    AwbAssist awb = new AwbAssist();
	    Method getCurrentRelativeResourceMethod;
	    getCurrentRelativeResourceMethod = AwbAssist.class.getDeclaredMethod("getCurrentRelativeResource", int.class, List.class);
	    getCurrentRelativeResourceMethod.setAccessible(true);

	    String result = (String) getCurrentRelativeResourceMethod.invoke(awb, i, blueprintRelativeResources);
	    assertEquals("/src/featureBlueprint/targetDir", result);  // Should return the correct path
	}

	@Test
	void testing_getCurrentRelativeResource_WithInvalidIndex() throws Exception {
		List<String> blueprintRelativeResources = new ArrayList<>();
		blueprintRelativeResources.add("src/featureBlueprint/targetDir");
		int i = 1;

		AwbAssist awb = new AwbAssist();
		Method getCurrentRelativeResourceMethod;
		getCurrentRelativeResourceMethod = AwbAssist.class.getDeclaredMethod("getCurrentRelativeResource", int.class,
				List.class);
		getCurrentRelativeResourceMethod.setAccessible(true);

			String result = (String) getCurrentRelativeResourceMethod.invoke(awb, i, blueprintRelativeResources);
			assertEquals(null, result);
	}
	
	@Test
	void testing_getCurrentRelativeResource_WithEmptyList() throws Exception {
		List<String> blueprintRelativeResources = new ArrayList<>();
		int i = 0;

		AwbAssist awb = new AwbAssist();
		Method getCurrentRelativeResourceMethod;
		getCurrentRelativeResourceMethod = AwbAssist.class.getDeclaredMethod("getCurrentRelativeResource", int.class,
				List.class);
		getCurrentRelativeResourceMethod.setAccessible(true);

			String result = (String) getCurrentRelativeResourceMethod.invoke(awb, i, blueprintRelativeResources);
			assertEquals(null, result);
	}
	
	@Test
	void testing_getCurrentRelativeResource_WithMultipleResources() throws Exception {
	    List<String> blueprintRelativeResources = new ArrayList<>();
	    blueprintRelativeResources.add("src/featureBlueprint/targetDir");
	    blueprintRelativeResources.add("src/testBlueprint/targetDir");

	    int i = 1;  

	    AwbAssist awb = new AwbAssist();
	    Method getCurrentRelativeResourceMethod;
	    getCurrentRelativeResourceMethod = AwbAssist.class.getDeclaredMethod("getCurrentRelativeResource", int.class, List.class);
	    getCurrentRelativeResourceMethod.setAccessible(true);

	    String result = (String) getCurrentRelativeResourceMethod.invoke(awb, i, blueprintRelativeResources);
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
	    Method method = AwbAssist.class.getDeclaredMethod("getCurrentLocaltargetDirectory", int.class, List.class, String.class, String.class, String.class, String.class, String.class); 
	    method.setAccessible(true);
	    String result = (String) method.invoke(awb, i, blueprintRelativeResources, folderToBeChanged, relativeSearchPath, targetDirectory, symBunName, folderAfterChangements); 
	    String expected = "D:/projects/testSymBun/targetDir/newBundleName"; 
	    assertEquals(expected, result); 
	}
	
	@Test void testing_getCurrentLocaltargetDirectory_WithoutFolderChange() throws Exception { 
	    List<String> blueprintRelativeResources = new ArrayList<>(); 
	    blueprintRelativeResources.add("src/featureBlueprint/targetDir/noFolderChange"); 
	    String folderToBeChanged = "bundleName"; 
	    String relativeSearchPath = "src/featureBlueprint"; 
	    String targetDirectory = "D:/projects"; 
	    String symBunName = "testSymBun"; 
	    String folderAfterChangements = "newBundleName"; 
	    int i = 0; 
	    
	    AwbAssist awb = new AwbAssist(); 
	    Method method = AwbAssist.class.getDeclaredMethod("getCurrentLocaltargetDirectory", int.class, List.class, String.class, String.class, String.class, String.class, String.class); 
	    method.setAccessible(true); 
	    String result = (String) method.invoke(awb, i, blueprintRelativeResources, folderToBeChanged, relativeSearchPath, targetDirectory, symBunName, folderAfterChangements); 
	    String expected = "D:/projects/testSymBun/targetDir/noFolderChange"; 
	    assertEquals(expected, result); 
	}




	
}
