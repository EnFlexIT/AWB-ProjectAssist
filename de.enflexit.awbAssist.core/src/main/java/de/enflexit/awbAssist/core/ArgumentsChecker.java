package de.enflexit.awbAssist.core;

import java.util.ArrayList;
import java.util.HashMap;

public class ArgumentsChecker {

	/**
	 * This class gets an ArrayList of "expected arguments", puts them in a HashMap as keys
	 * Using the given arguments it looks for the corresponding value for each key and puts it in the HashMap
	 * It returns then HashMap back either completely filled with keys and values or null in case a value is missing.
	 *
	 * @param args the args
	 * @param referenceList the reference list
	 * @return the hash map
	 */
	public static HashMap<String, String> check(String[] args, ArrayList<String> referenceList){
		HashMap<String, String> arguments = new HashMap<String, String>();
		
		int i=0;
		while (i < referenceList.size()) {
			int j=0;
			while (j < args.length) {
				String currentReference = referenceList.get(i);
				String currentArg = args[j].substring(1);
				if (currentArg.equals(currentReference) && (j+1) < args.length) {
					arguments.put(referenceList.get(i), args[j+1]);
					j=args.length;
				}
				j++;
				if (j == (args.length-1)) {
					return null;
				}
			}
			i++;
		}
		return arguments;
	}
}
