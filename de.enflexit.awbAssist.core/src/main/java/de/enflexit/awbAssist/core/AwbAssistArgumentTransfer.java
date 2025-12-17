package de.enflexit.awbAssist.core;

import java.util.HashMap;

public class AwbAssistArgumentTransfer {
	
	
	/**
	 * passes a Hashmap of arguments in a correct form to awb assist main method.
	 *
	 * @param arguments the arguments
	 */
	public static void passArguments (HashMap<String, String> arguments) {
		
		if (arguments == null || arguments.isEmpty()) {
			throw new IllegalArgumentException("Arguments map is null or empty");
	    }

	    String[] args = new String[arguments.size() * 2];

	    int index = 0;
	    for (HashMap.Entry<String, String> entry : arguments.entrySet()) {

	        String key = entry.getKey();
	        String value = entry.getValue();
	        
	        if (key == null || value == null || value.isBlank()) {
	        	throw new IllegalArgumentException( "Invalid argument detected. Key: " + key + ", Value: " + value );
	        }

	        args[index] = "-" + key.toLowerCase();
	        index ++;
	        args[index] = value;
	        index++;
	    }

	    AwbAssist.main(args);
		
	}

}
