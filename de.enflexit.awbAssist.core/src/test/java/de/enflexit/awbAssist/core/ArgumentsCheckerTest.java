package de.enflexit.awbAssist.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

class ArgumentsCheckerTest {
	
	//TODO format: after the method declaration an empty line should be inserted before starting with the content 

	
	// Here is the name of the blueprint to be used as reference
	String nameOfBlueprintUsedFortesting = "featureBlueprint";

	private ProjectBlueprint getBlueprintForTesting() {
		List<ProjectBlueprint> listOfBlueprints = InternalResourceHandler.getProjectBlueprintsAvailable();
		for (int i = 0; i < listOfBlueprints.size(); i++) {
			if (listOfBlueprints.get(i).getBaseFolder().equals(nameOfBlueprintUsedFortesting)) {
				System.out.println("The used blueprint for testing is " + listOfBlueprints.get(i).getBaseFolder()
						+ "\n The texts that should be replaced are \n" + listOfBlueprints.get(i).getTextReplacements()
						+ "\n The required argments for this bluerint are \n"
						+ listOfBlueprints.get(i).getRequiredArguments());
				return listOfBlueprints.get(i);
			}
		}
		System.err.println(nameOfBlueprintUsedFortesting
				+ "couldn't be found in the availble blueprints. Below are the found blueprints \n" + listOfBlueprints);
		return null;
	}
	

	@Test
	void testing_Check_WithValidArguments() {
		String[] args = {"-blueprint", "featureBlueprint", "-bundleName", "testBundle", "-symBunName", "test.sym", "-targetDir", "/test/dir"};
		ProjectBlueprint referenceBlueprint = getBlueprintForTesting();
		
		HashMap<String, String> result = ArgumentsChecker.check(args, referenceBlueprint);
		assertNotNull(result);
		assertEquals("testBundle", result.get("bundleName"));
        assertEquals("test.sym", result.get("symBunName"));
        assertEquals("/test/dir", result.get("targetDir"));
	}
	
	
	@Test
	// in case a mandatory argument is missing the check method should return null
	void testing_Check_WithAtLeastOneMandatoryArgumentEmpty() {
		String[] args = {"-blueprint", "featureBlueprint", "-bundleName", "-symBunName", "test.sym", "-targetDir", "/test/dir"};
		ProjectBlueprint referenceBlueprint = getBlueprintForTesting();
		
		HashMap<String, String> result = ArgumentsChecker.check(args, referenceBlueprint);
		assertNull(result);
	}
	
	@Test
	//---------------------------For which purpose is this method created -----------------------------------
	// this problem was faced on the 25.11.2024 a feature blueprint was used and the feature to be created was
	// supposed to be located under D:\Eclipse workspace, it was created under D:\EclipseWorkspace though
	//------------------------------------------- end of comment --------------------------------------------
	void testing_Check_TargetDirContainsSpace() {
		String[] args = {"-blueprint", "featureBlueprint", "-bundleName", "testBundle", "-symBunName", "test.sym", "-targetDir", "D:\\Eclipse workspace"};
		ProjectBlueprint referenceBlueprint = getBlueprintForTesting();
		
		HashMap<String, String> result = ArgumentsChecker.check(args, referenceBlueprint);
		assertNotNull(result);
        assertEquals("D:\\Eclipse workspace", result.get("targetDir"));
	}
	
	@Test
	void testing_Check_UsingFeatureBlueprint_And_bundleNameStartsWithCapitalLetter() {
		String[] args = {"-blueprint", "featureBlueprint", "-bundleName", "TestBundle", "-symBunName", "test.sym", "-targetDir", "D:\\Eclipse workspace"};
		ProjectBlueprint referenceBlueprint = getBlueprintForTesting();
		
		HashMap<String, String> result = ArgumentsChecker.check(args, referenceBlueprint);
		assertNotNull(result);
        assertEquals("testBundle", result.get("bundleName"));
        assertEquals("test.sym", result.get("symBunName"));
	}
	
	@Test
	// A non mandatory argument must not be given
	void testing_Check_NonMandatoryArgumentIsNotGiven() {
		String[] args = {"-blueprint", "featureBlueprint", "-bundleName", "TestBundle", "-symBunName", "test.sym", "-targetDir"};
		ProjectBlueprint referenceBlueprint = getBlueprintForTesting();
		ArrayList<StartArgument> requiredArguments = referenceBlueprint.getRequiredArguments();
		for (StartArgument requiredArgument : requiredArguments) {
			if (requiredArgument.getArgumentName().equals("targetDir")) {
				requiredArgument.setMandatory(false);
			}
		}
		
		HashMap<String, String> result = ArgumentsChecker.check(args, referenceBlueprint);
		assertNotNull(result);
        assertEquals(null, result.get("targetDir"));
        assertEquals("test.sym", result.get("symBunName"));
        System.out.println(result);
	}
	
	@Test
	void testing_CheckBlueprintArgument_WithValidArguments() {
		String[] args = {"-blueprint", "featureBlueprint", "-bundleName", "TestBundle", "-symBunName", "test.sym", "-targetDir", "D:\\Eclipse workspace"};
		String result = ArgumentsChecker.getBlueprintArgument(args);
		assertNotNull(result);
        assertEquals("featureBlueprint", result);
	}
	
	@Test
	void testing_CheckBlueprintArgument_WithEmptyArgument() {
		String[] args = {"-blueprint", "-bundleName", "TestBundle", "-symBunName", "test.sym", "-targetDir", "D:\\Eclipse workspace"};
		String result = ArgumentsChecker.getBlueprintArgument(args);
        assertEquals(null, result);
	}
	
	@Test
	void testing_CheckBlueprintArgument_WithEmptyArray() {
		String[] args = {};
		String result = ArgumentsChecker.getBlueprintArgument(args);
        assertEquals(null, result);
	}
	
	@Test
	void testing_CheckBlueprintArgument_BlueprintIsMissing() {
		String[] args = { "featureBlueprint", "-bundleName", "TestBundle", "-symBunName", "test.sym", "-targetDir", "D:\\Eclipse workspace"};
		String result = ArgumentsChecker.getBlueprintArgument(args);
        assertEquals(null, result);
	}
	
	@Test
	void testing_CheckBlueprintArgument_MultipleBlueprints() {
		String[] args = { "-blueprint", "firstBlueprint", "-blueprint", "secondBlueprint", "-bundleName", "TestBundle", "-symBunName", "test.sym", "-targetDir", "D:\\Eclipse workspace"};
		String result = ArgumentsChecker.getBlueprintArgument(args);
		assertNotNull(result);
        assertEquals("firstBlueprint", result);
	}
	
	@Test
	void testing_CheckBlueprintArgument_WithSpace() {
		String[] args = { "-blueprint", "   firstBlueprint   ", "-blueprint", "secondBlueprint", "-bundleName", "TestBundle", "-symBunName", "test.sym", "-targetDir", "D:\\Eclipse workspace"};
		String result = ArgumentsChecker.getBlueprintArgument(args);
		assertNotNull(result);
        assertEquals("   firstBlueprint   ", result);
	}
	
	// TODO rename the two methods 
	@Test
	void testing_Check_WithMandatoryArgumentsEmpty() {
		String[] args = {"-blueprint", "featureBlueprint", "-bundleName", "-symBunName", "-targetDir", "/test/dir"};
		ProjectBlueprint referenceBlueprint = getBlueprintForTesting();
		
		HashMap<String, String> result = ArgumentsChecker.check(args, referenceBlueprint);
		assertNull(result);
	}
	
	@Test
	void testing_Check_WithMultipleMandatoryArgumentsEmpty() {
		String[] args = {"-blueprint", "featureBlueprint", "-bundleName", "-targetDir", "/test/dir"};
		ProjectBlueprint referenceBlueprint = getBlueprintForTesting();
		
		HashMap<String, String> result = ArgumentsChecker.check(args, referenceBlueprint);
		assertNull(result);
	}
	
}
