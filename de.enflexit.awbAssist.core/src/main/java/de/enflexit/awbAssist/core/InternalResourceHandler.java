package de.enflexit.awbAssist.core;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class InternalResourceHandler {

	
	public static List<ProjectBlueprint> getProjectBlueprintsAvailable() {
		
		List<ProjectBlueprint> pbList = new ArrayList<>();
		
		
		return pbList;
	}

	public static void test() {
		
		try {

			File[] fileArray = (new File(InternalResourceHandler.class.getResource("/blueprints").toURI())).listFiles();
			for (File file : fileArray) {
				System.out.println(file);
			}
			
		} catch (URISyntaxException ioEx) {
			ioEx.printStackTrace();
		}
		
	}
	
	
}
