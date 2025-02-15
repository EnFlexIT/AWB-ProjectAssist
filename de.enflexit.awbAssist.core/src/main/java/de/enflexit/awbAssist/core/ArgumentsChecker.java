package de.enflexit.awbAssist.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
				if (currentArg.equalsIgnoreCase(currentReference.getArgumentName()) && (i+1) < args.length && args[i+1].startsWith("-") == false) {
					if (args[i+1] != null && args[i+1].isBlank() == false) {
						arguments.put(currentReference.getArgumentName(), args[i+1]);
						break;
					} else { 
						System.err.println("no value is found for the argument " + currentReference.getArgumentName());
					}
				}
				i++;
			}
		}
		
		// This loop checks whether all mandatory arguments are given. 
		for (StartArgument currentReference : referenceBlueprint.getRequiredArguments()) {
			if (currentReference.isMandatory() == true && arguments.containsKey(currentReference.getArgumentName()) == false) {
				System.err.println("Missing required argument: " + currentReference.getArgumentName());
				return null;
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
			if (args[i].equalsIgnoreCase("-blueprint") && i+1 < args.length && args[i+1].startsWith("-") == false) {
				boolean isNullOrBlank = (args[i+1]==null || args[i+1].isBlank());
				if(isNullOrBlank == false) {
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
	public static boolean isHelpRequested(String[] args) {
		
		int i = 0;
		boolean helpNeeded = false;
		if (args==null || args.length==0) args = new String[] {"-?"};
		while (i < args.length) {
			if (args[i].equalsIgnoreCase("-help") || args[i].equals("-?")) {
				System.out.println("Awb-Assist enables you to create a new project using an existing blueprint as a reference structure." 
//						+ "\n In order to do that certain parameters have to be given as arguments in key/value sets while preceeding each key with a - "
//						+ "\n An example on how the arguments should look like is given  in the following line"
//						+ "\n -blueprint \"TestingAgent\" -bundleName \"FlexAqua Assistant Agent\" -symBunName \"de.enflexit.flexAqua.assistantAgent\" -targetDir \"D:\""
//						+ "\n In this example bundlename, symBundleName and targetDir were needed to create a project with a similar structure to which of the blueprint TestingAgent"
						+ "\nTo get the list of available blueprints and the required arguments for each one of them type -bp as argument");
//						isBlueprintRequested(new String[] {"-bp"});
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
	public static boolean isBlueprintRequested(String[] args) {
		
		int i = 0;
		while (i < args.length) {
			if (args[i].equalsIgnoreCase("-bp")) {
				List<ProjectBlueprint> availableBlueprints = new ArrayList<>();
				availableBlueprints = InternalResourceHandler.getProjectBlueprintsAvailable();
				for (ProjectBlueprint currentBlueprint: availableBlueprints) {
					System.out.println(currentBlueprint.getBaseFolder() + ": " + currentBlueprint.getDescription());
					System.out.println("This blueprint requires the following arguments:\t" + currentBlueprint.getRequiredArguments());
					System.out.println("Arguments should be provided as follows:\t");
					System.out.print("-blueprint " + "\"" + currentBlueprint.getBaseFolder() + "\" ");
					ArrayList<StartArgument> currentStartArguments = currentBlueprint.getRequiredArguments();
					for (int j=0; j < currentStartArguments.size(); j++) {
						System.out.print("-" + currentStartArguments.get(j).getArgumentName() + " \"my"+ currentStartArguments.get(j).getArgumentName() + "\" ");
					}
					System.out.println("\n");
				}
				return true;
			}
			i++;
		}
		return false;
	}
	
}
