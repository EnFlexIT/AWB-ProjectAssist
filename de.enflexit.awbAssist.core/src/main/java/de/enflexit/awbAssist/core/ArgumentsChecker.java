package de.enflexit.awbAssist.core;

import java.util.ArrayList;
import java.util.HashMap;

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
	 *
	 * @param args the args
	 * @param referenceList the reference list
	 * @return the hash map
	 */
	public static HashMap<String, String> check(String[] args, ProjectBlueprint referenceBlueprint){
		HashMap<String, String> arguments = new HashMap<String, String>();
		
		// This loop associates each of the given arguments to the correspondent argument name.
		for (StartArgument currentReference : referenceBlueprint.getRequiredArguments()) {
			int i=0;
			while (i < args.length) {
				String currentArg = args[i].substring(1);
				if (currentArg.equalsIgnoreCase(currentReference.getArgumentName())) {
					if ((i+1) < args.length && args[i+1].startsWith("-")==false) {
						arguments.put(currentReference.getArgumentName(), args[i+1]);
						break;
					} else { 
						System.err.println("no value is found for the argument " + currentReference.getArgumentName());
					}
				}
				i++;
//				if ( i == args.length) {
//					System.err.println("(-" + currentReference.getArgumentName() + ") couldn't be found in the given arguments");
//					return null;
//				}
			}
		}
		
		// This loop checks whether all mandatory arguments are given. 
		//TODO ---- question ---- should the default value used as value for non given mandatory arguments 
		for (StartArgument currentReference : referenceBlueprint.getRequiredArguments()) {
			if (currentReference.isMandatory() == true && arguments.containsKey(currentReference.getArgumentName()) == false) {
				System.err.println("Missing required argument: " + currentReference.getArgumentName());
				return null;
//				if (arguments.get(currentReference.getArgumentName()) == null) {
//					// Default values are checked if no value is given as argument. 
//					if ( currentReference.getDefaultValue() != null) {
//						arguments.put(currentReference.getArgumentName(), currentReference.getDefaultValue());
//					} else {
//						System.err.println("Missing required argument " + currentReference.getArgumentName());
//						return null;
//					}
//					
//				}
			}
		}
		
		//----------- This part is designed for the blueprint "feature"-------------
		// For this blueprint the required arguments are bundleName, symBundleName and targetDir
		// The replacement strings include bundleName, symBundleName as well as projectName
		// projectName isn't requested from the user but it's interpreted out of the bundleName in this part
		// and added to the HashMap arguments in this part
		// ----------------------------------------------------------------------------
		if (referenceBlueprint.getBaseFolder().equals("featureBlueprint")) {
			String currentBundleName = arguments.get("bundleName");
			String bundleNameStartWithSmallLetter = currentBundleName.substring(0,1).toLowerCase() + currentBundleName.substring(1);
			String bundleNameStartWithCapitalLEtter = currentBundleName.substring(0,1).toUpperCase() + currentBundleName.substring(1);
			arguments.replace("bundleName", currentBundleName, bundleNameStartWithSmallLetter);
			arguments.put("projectName", bundleNameStartWithCapitalLEtter);
		}
		return arguments;
	}
	
	/**
	 * This method checks whether a blueprint name is present among the arguments.
	 *
	 * @param args the args
	 * @return the string
	 */
	public static String getBlueprintArgument(String[] args) {
		int i = 0;
		String bluePrint = "";
		while (i < args.length) {
			if (args[i].equalsIgnoreCase("-blueprint")) {
				if (i + 1 < args.length && args[i+1].startsWith("-") == false ) {
					bluePrint = args[i + 1];
					return bluePrint;
				}
			}
			i++;
		}
		System.err.println("no blueprint name was given as argument");
		return null;
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
				System.out.println("The current file enables you to create a new project based on an existing blueprint (a template) through giving the name of the blueprint to be used and its required arguments \n "
						+ "These have to be given in key/value sets in order to be recognized. Keys have to be preceeded by a - \n"
						+ " An example on how the arguments should look like is given in the following line"
						+ "\n -blueprint TestingAgent -bundleName \"FlexAqua Assistant Agent\" -symBunName de.enflexit.flexAqua.assistantAgent -targetDir D:"
						+ "\n In this example bundlename, symBundleName and targetDir were needed to create a project with a similar structure to which of the blueprint TestingAgent"
						+ "\n To get the list of available blueprints and the required arguments for each of them, please give -bp as argument");
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
