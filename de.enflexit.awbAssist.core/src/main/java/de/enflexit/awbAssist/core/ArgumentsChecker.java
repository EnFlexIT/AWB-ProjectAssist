package de.enflexit.awbAssist.core;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class ArgumentsChecker.
 *
 * @author Omar Ben Chobba - Enflex.IT GmbH
 */
public class ArgumentsChecker {

	/**
	 * This method gets an ArrayList of "expected arguments", puts them in a HashMap as keys
	 * Using the given arguments it looks for the corresponding value for each key and puts it in the HashMap
	 * It returns then HashMap back either completely filled with keys and values or null in case a value is missing.
	 * TODO if the given values for the required arguments contain spaces than remove the spaces and start each word with capital letter  
	 *
	 * @param args the args
	 * @param referenceList the reference list
	 * @return the hash map
	 */
	public static HashMap<String, String> check(String[] args, ArrayList<StartArgument> referenceList){
		HashMap<String, String> arguments = new HashMap<String, String>();
		
		// This loop associates each of the given arguments to the correspondent argument name.
		for (StartArgument currentReference : referenceList) {
			int i=0;
			while (i < args.length) {
				String currentArg = args[i].substring(1);
				if (currentArg.equals(currentReference.getArgumentName())) {
					if ((i+1) < args.length) {
						arguments.put(currentReference.getArgumentName(), removeSpacesAndStartWithCapitalLetters(args[i+1]));
						break;
					} else { 
						System.err.println("no value is found for the argument " + currentReference.getArgumentName());
					}
				}
				i++;
				if ( i == args.length) {
					System.err.println("(-" + currentReference.getArgumentName() + ") couldn't be found in the given arguments");
					return null;
				}
			}
		}
		
		// This loop checks whether all mandatory arguments are given. 
		for (StartArgument currentReference : referenceList) {
			if (currentReference.isMandatory() == true ) {
				if (arguments.get(currentReference.getArgumentName()) == null) {
					// Default values are checked if no value is given as argument. 
					if ( currentReference.getDefaultValue() != null) {
						arguments.put(currentReference.getArgumentName(), currentReference.getDefaultValue());
					} else {
						System.err.println("Missing required argument " + currentReference.getArgumentName());
						return null;
					}
					
				}
			}
		}
		return arguments;
	}
	
	
	private static String removeSpacesAndStartWithCapitalLetters(String setOfWords) {
		for (int i=0 ; i < setOfWords.length(); i++) {
			if(setOfWords.charAt(i) == ' ') {
				if ((i+1) < setOfWords.length()) {
					setOfWords = setOfWords.substring(0, i+1) + Character.toUpperCase(setOfWords.charAt(i+1)) + setOfWords.substring(i + 2);
				}
			}
		}
		setOfWords = setOfWords.replace(" ", "");
		return setOfWords;
	}


	/**
	 * This method checks whether a blueprint name is present among the arguments.
	 *
	 * @param args the args
	 * @return the string
	 */
	public static String checkBlueprintArgument(String[] args) {
		int i = 0;
		String bluePrint = "";
		while (i < args.length) {
			if (args[i].equalsIgnoreCase("-blueprint")) {
				if (i + 1 < args.length) {
					bluePrint = args[i + 1];
					return bluePrint;
				}
			}
			i++;
		}
		return bluePrint;
	}
	
	/** Checks whether the user needs instructions on how to use the code
	 * @param args
	 * @return
	 */
	public static boolean helper(String[] args) {
		int i = 0;
		boolean helpNeeded = false;
		while (i < args.length) {
			if (args[i].equalsIgnoreCase("-help") || args[i].equals("-?")) {
				System.out.println("The current file enables you to create a new project based on an existing blueprint \n to get the list of available blueprints, please give  -bp  as argument"
						+ "\n for lunching the new project creation, arguments have to be given as a sequence of keywords followed by the respective values as in the following example: "
						+ "\n -blueprint TestingAgent -bundleName \"FlexAqua Assistant Agent\" -symBunName de.enflexit.flexAqua.assistantAgent -targetDir D:");
				helpNeeded = true;
				return helpNeeded;
			}
			i++;
		}
		return helpNeeded;
	}
	
	
	/** Returns whether the user wants to get the available blueprints
	 * @param args
	 * @return
	 */
	public static boolean callForBlueprints(String[] args) {
		int i = 0;
		boolean blueprintsNeeded = false;
		while (i < args.length) {
			if (args[i].equalsIgnoreCase("-bp")) {
				blueprintsNeeded = true;
				return blueprintsNeeded;
			}
			i++;
		}
		return blueprintsNeeded;
	}
	
}
