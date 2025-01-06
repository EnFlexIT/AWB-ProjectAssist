package de.enflexit.awbAssist.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class TestingBlueprintTemplates.
 *
 * @author Omar Ben Chobba - Enflex.IT GmbH
 */
class CheckingBlueprintTemplates {

	public static void main(String[] args) {
		
		/**
		 *  ---------------------------------------------------------- Hi ----------------------------------------------------------
		 *  In this class we go through the blueprint templates one by one and look for texts between brackets.
		 *  We compare the found texts with the replacement texts related to each blueprint, so that we count how many 
		 *  times each of the replacement-texts appeared in the found texts. Found texts that doesn't match any replacement
		 *  text are placed in a separate list which could be later investigated.
		 *  
		 *  The procedure mentioned above is repeated for a folder where projects are created via a script (AwbAssist)
		 *  that uses the blueprint templates for making new projects. 
		 *  
		 *  The script is supposed to replace all replacement-texts with other parameters given as argument. 
		 *  If so, replacement-texts should not be found in the created folder. Moreover, the list of texts found between brackets,
		 *  that are not associated to a replacement-text for the blueprint and for the created folder should match each other.
		 *  This is verified at the end of the current class.
		 *  ------------------------------------------------------------------------------------------------------------------------
		 */

		// First get the available blueprints
		List<ProjectBlueprint> allAvailableBlueprints = InternalResourceHandler.getProjectBlueprintsAvailable();
		// Initialization of the List that should contain the texts found between brackets in the blueprint templates
		List<List<String>> foundSquareBracketsInBlueprintTemplates = new ArrayList<>();
		// Initialize a HashMap that contains each replacement text for each blueprint and its occurrence number
		HashMap<List<String>, Integer> replacementTextsAndOccurrencesInBlueprintTemplates = new HashMap<>();
		// Initialize a list that contains texts between brackets, but not associated to a replacement text
		List<List<String>> listOfFoundTextsNonAssociatedToAnArgumentInBlueprintTemplates = new ArrayList<>();
		
		findTextsInSquareBracketsAndGetReplacementTextsForBlueprintTemplates(allAvailableBlueprints,foundSquareBracketsInBlueprintTemplates, replacementTextsAndOccurrencesInBlueprintTemplates);
		countOccurrencesAndFillListOfFoundTextsNonAssociatedToATextReplacement(foundSquareBracketsInBlueprintTemplates, replacementTextsAndOccurrencesInBlueprintTemplates, listOfFoundTextsNonAssociatedToAnArgumentInBlueprintTemplates);
		
		
		// AwbAssist was manually used to generated projects using all blueprint templates and they were stored locally under D:\\testingAwbAssistOutputs
		
		// --------------- This is optional ---------------
		Scanner scanner = new Scanner(System.in);
		System.out.println("please enter the path under which your folder was generated");
		String pathToGeneratedFolder = scanner.next();
		// ------------------------------------------------
		
		
		File generatedFolder = new File ("D:\\testingAwbAssistOutputs");
		// Make a list of files found in the substructure of the generated folder
		List<File> foundResourcesInGeneratedFolder = getSubstructure(generatedFolder);
		// Initialization of the List that should contain the texts found between brackets in the files generated using AwbAssist.
		List<List<String>> foundSquareBracketsInGeneratedFiles = new ArrayList<>();
		// Initialize a HashMap that contains each replacement text for each blueprint and its occurrence number
		HashMap<List<String>, Integer> replacementTextsAndOccurrencesInGeneratedFiles = new HashMap<>();
		// Fill the keys of the HashMap while initializing the occurrences through putting 0 as values
		for (List<String> key : replacementTextsAndOccurrencesInBlueprintTemplates.keySet()) {
			replacementTextsAndOccurrencesInGeneratedFiles.put(key, 0);
		}
		// Initialize a list that contains texts between brackets, but not associated to a replacement text
		List<List<String>> listOfFoundTextsNonAssociatedToAnArgumentInGeneratedFiles = new ArrayList<>();
		
		findTextsInSquareBracketsForGeneratedfiles(foundResourcesInGeneratedFolder, allAvailableBlueprints, foundSquareBracketsInGeneratedFiles);
		countOccurrencesAndFillListOfFoundTextsNonAssociatedToATextReplacement(foundSquareBracketsInGeneratedFiles, replacementTextsAndOccurrencesInGeneratedFiles, listOfFoundTextsNonAssociatedToAnArgumentInGeneratedFiles);
		
		
//		int commonElements = 0;
//		int differentElements = 0;
//		List<List<String>> listOfCommonElements = new ArrayList<>();
//		List<List<String>> uniqueToBlueprint = new ArrayList<>();
//		List<List<String>> uniqueToCreatedFolder = new ArrayList<>();
//		
//		for (List<String> sublist:listOfFoundTextsNonAssociatedToAnArgumentInBlueprintTemplates) {
//			for (List<String> sublistTwo : listOfFoundTextsNonAssociatedToAnArgumentInGeneratedFiles) {
//				boolean sameBlueprint = sublist.get(0).equals(sublistTwo.get(0));
//				boolean sameTextBetweenBrackets = sublist.get(1).equals(sublistTwo.get(1));
//				String sublistFileName = (new File (sublist.get(3))).getName();
//				String sublistTwoFileName = (new File(sublistTwo.get(3)).getName());
//				boolean sameFileName = sublistFileName.equals(sublistTwoFileName);
//				if (sameBlueprint && sameTextBetweenBrackets && sameFileName) {
//					listOfCommonElements.add(List.of(sublist.get(0), sublist.get(1), sublist.get(2), sublistTwo.get(2)));
//				}
//			}
//		}
		
//		Set<List<String>> setOfFoundTextInBlueprintTemplates = new HashSet<>(); 
//		Set<List<String>> setOfFoundTextInGeneratedFolder = new HashSet<>(); 
//		for (List<String> list : listOfFoundTextsNonAssociatedToAnArgumentInBlueprintTemplates ) {
//			String fileName = (new File(list.get(2))).getName();
//			setOfFoundTextInBlueprintTemplates.add(List.of(list.get(0), list.get(1), fileName));
//		}
//		for (List<String> list : listOfFoundTextsNonAssociatedToAnArgumentInGeneratedFiles ) {
//			String fileName = (new File(list.get(2))).getName();
//			setOfFoundTextInGeneratedFolder.add(List.of(list.get(0), list.get(1), fileName));
//		}
//		
//		Set<List<String>> uniqueToBlueprintTemplates = new HashSet<>(setOfFoundTextInBlueprintTemplates); 
//		Set<List<String>> uniqueToGeneratedFolder = new HashSet<>(setOfFoundTextInGeneratedFolder);
//		Set<List<String>> commonElements = new HashSet<>(setOfFoundTextInBlueprintTemplates);
//		
//		uniqueToBlueprintTemplates.removeAll(setOfFoundTextInGeneratedFolder);
//		uniqueToGeneratedFolder.removeAll(setOfFoundTextInBlueprintTemplates);
//		commonElements.retainAll(setOfFoundTextInGeneratedFolder);
		
		boolean replacementTextInGeneratedFolder =isreplacementTextPresentInGeneratedFolder(replacementTextsAndOccurrencesInGeneratedFiles);
		
	}

	/**
	 * Look for texts written between brackets in the blueprints substructure and put them in a list
	 * Put all replacement texts associated with each blueprint in a HashMap
	 * 
	 * @param ListOfProjectBlueprints
	 * @param foundTextsInSquareBrackets
	 * @param replacementTextsAndOccurrences
	 */
	private static void findTextsInSquareBracketsAndGetReplacementTextsForBlueprintTemplates(List<ProjectBlueprint> ListOfProjectBlueprints, List<List<String>> foundTextsInSquareBrackets, HashMap<List<String>, Integer> replacementTextsAndOccurrences ) {
		
		// In a loop, handle each blueprint on its own
		for (int i = 0; i < ListOfProjectBlueprints.size(); i++) {
			String nameOfCurrentBlueprint = ListOfProjectBlueprints.get(i).getBaseFolder();
			String searchPathOfTheCurrentBlueprint = "blueprints/" + nameOfCurrentBlueprint;
			// The list that contains the files and folders under the current blueprint
			List<String> listOfFilesAndFoldersUnderTheCurrentBlueprint = InternalResourceHandler.findResources(searchPathOfTheCurrentBlueprint);
			// Each element of the list of files/folders is checked: the searched strings are looked for in the names and in the contents of files
			for (int j = 0; j < listOfFilesAndFoldersUnderTheCurrentBlueprint.size(); j++) {
				URL fileURL = InternalResourceHandler.class.getResource("/" + listOfFilesAndFoldersUnderTheCurrentBlueprint.get(j));
				if (fileURL.toString().contains("BlueprintStructure.json") == false) {
					// use a method to look for texts between brackets in file/folder name
					lookForBracketsInName(fileURL, foundTextsInSquareBrackets, nameOfCurrentBlueprint);
					try {
						File file = new File(fileURL.toURI());
						if (file.isFile()) {
							// use a method to look for texts between brackets in file content
							lookForBracketsInFileContent(fileURL, foundTextsInSquareBrackets, nameOfCurrentBlueprint);
						}
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
			// Fill the list of replacement texts corresponding to each blueprint while looping in the blueprints
			HashMap<String,String> replacementTextsOfTheCurrentBlueprint = ListOfProjectBlueprints.get(i).getTextReplacements();
			for (String key : replacementTextsOfTheCurrentBlueprint.keySet()) {
				// the sublists of replacementTextsAndOccurrences are established here
				// each sublist contains two elements : first the name of the blueprint then the text to be replaced
				List<String> temporarySublist = new ArrayList<>();
				temporarySublist.add(nameOfCurrentBlueprint);
				temporarySublist.add(key.substring(1, key.length()-1)); // the replacement text is placed between brackets, which need to be removed
				replacementTextsAndOccurrences.put(temporarySublist, 0);
			}
		}
	}
	

	// Look in resources for texts between brackets and fill them along with their corresponding blueprint name and file URL in a list.
	/**
	 * @param resources
	 * @param ListOfProjectBlueprints
	 * @param foundTextsInSquareBrackets
	 */
	private static void findTextsInSquareBracketsForGeneratedfiles(List<File> resources, List<ProjectBlueprint> ListOfProjectBlueprints, List<List<String>> foundTextsInSquareBrackets) {
		
		// Loop through the passed resources 
		for (int i = 0; i < resources.size(); i++) {
			for (int j=0; j < ListOfProjectBlueprints.size(); j++) {
				String nameOfCurrentBlueprint = ListOfProjectBlueprints.get(j).getBaseFolder();
				if (resources.get(i).toString().contains(nameOfCurrentBlueprint)){
					try {
						URI	uri = (resources.get(i)).toURI();
						URL fileURL = uri.toURL();
					
						lookForBracketsInName(fileURL, foundTextsInSquareBrackets, nameOfCurrentBlueprint);
					
						File file = new File(fileURL.toURI());
						if (file.isFile()) {
							// use a method to look for texts between brackets in file content
							lookForBracketsInFileContent(fileURL, foundTextsInSquareBrackets, nameOfCurrentBlueprint);
						}
					} catch (URISyntaxException | MalformedURLException e) {
						e.printStackTrace();
					}
				break;	
				}
			}
		}
	}

	
	/**
	 * Use the list of found texts between to count the occurrences of replacements texts and put the found texts, which do not correspond to a replacement text in a separate list
	 * 
	 * @param ListOfFoundTexts
	 * @param replacementTextsAndOccurrences
	 * @param listOfFoundTextsNonAssociatedToAReplacementText
	 */
	private static void countOccurrencesAndFillListOfFoundTextsNonAssociatedToATextReplacement(List<List<String>> ListOfFoundTexts, HashMap<List<String>, Integer> replacementTextsAndOccurrences, List<List<String>> listOfFoundTextsNonAssociatedToAReplacementText) {
		
		for (List<String> element : ListOfFoundTexts) {
			List<String> rectifiedElement = new ArrayList<>();
			rectifiedElement.add(element.get(0)); // This refers to the blueprint name
			rectifiedElement.add(element.get(1)); // This refers to the texts between brackets 
			// The keys of the HashMap are lists with two elements each [BlueprintName, TextTobeReplaced] 
			if (replacementTextsAndOccurrences.containsKey(rectifiedElement)) {
				replacementTextsAndOccurrences.computeIfPresent(rectifiedElement, (key,value) -> value+1);
			} else {
				listOfFoundTextsNonAssociatedToAReplacementText.add(element);
			}
		}
		
	}


	/**
	 * Look for texts between brackets in a file content.
	 *
	 * @param urlOfTheFileOrFolder the url of the file or folder
	 * @param resultList the result list
	 * @param blueprintName the blueprint name
	 */
	public static void lookForBracketsInFileContent(URL urlOfTheFileOrFolder, List<List<String>> resultList, String blueprintName) {

		// File path
		String filePath = urlOfTheFileOrFolder.toString();
	    try {
	        // Convert the URL to a File object
	        File file = new File(urlOfTheFileOrFolder.toURI());
	        if (!file.isFile()) {
	            return; // Skip directories or invalid files
	        }
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			// Regular expression to match text between square brackets
			Pattern pattern = Pattern.compile("\\[(.*?)]");
			while ((line = reader.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				while (matcher.find() == true) {
					// Add the matched group (text inside the brackets) to the list
					List<String> pair = new ArrayList<>();
					pair.add(blueprintName);
					pair.add(matcher.group(1)); 
					pair.add(filePath); 
					resultList.add(pair); // Add the sub-list to the main list
				}
			}
		}
	    } catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	
    /**
     * Look for texts between brackets in a string.
     *
     * @param urlOfTheFileOrFolder the url of the file or folder
     * @param resultList the result list
     * @param BlueprintName the blueprint name
     */
    private static void lookForBracketsInName(URL urlOfTheFileOrFolder, List<List<String>> resultList, String BlueprintName) {
		// Extract the file name from the path
		String fileName;
		try {
			fileName = Paths.get(urlOfTheFileOrFolder.toURI()).getFileName().toString();
			// Regular expression to match text between square brackets
			Pattern pattern = Pattern.compile("\\[(.*?)]");

			Matcher matcher = pattern.matcher(fileName);
			while (matcher.find() == true) {
				// Create a sub-list for the file name match
				List<String> pair = new ArrayList<>();
				pair.add(BlueprintName);
				pair.add(matcher.group(1)); 
				pair.add(fileName); 
				resultList.add(pair); // Add to main list
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
    
    
    /**
     * Get the substructure of a given folder
     * 
     * @param folder
     * @return
     */
    private static List<File> getSubstructure(File folder) {
    	
    	List<File> foundResources = new ArrayList<>();
    	if (folder.exists()==false || folder.isDirectory()==false) return foundResources;
    	
    	for (File file : folder.listFiles()) {
    		foundResources.add(file);
    		if (file.isDirectory()==true) {
    			foundResources.addAll(getSubstructure(file));
    		}
    	}
    	return foundResources;
    }
    
    
    /**
     * Checks if is replacement text present in generated folder.
     *
     * @param replacementTextsAndOccurrences the replacement texts and occurrences
     * @return true, if is replacement text present in generated folder
     */
    private static boolean isreplacementTextPresentInGeneratedFolder(HashMap<List<String>, Integer> replacementTextsAndOccurrences) {
    	
    	for (Entry<List<String>, Integer> i : replacementTextsAndOccurrences.entrySet()) {
    		if (i.getValue() != 0 ) {
    			return true;
    		}
    	}
    	return false;
    }
}