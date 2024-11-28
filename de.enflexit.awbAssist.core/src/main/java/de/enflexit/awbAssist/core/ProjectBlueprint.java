package de.enflexit.awbAssist.core;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The Class defines the general structure of a project blueprint.
 * @author Omar Ben Chobba - EnFlex.IT GmbH
 */
public class ProjectBlueprint {
	
	private String description;
	private String baseFolder;
	private HashMap<String, String> textReplacements;
	private ArrayList<StartArgument> requiredArguments;

	/**
	 * Gets the description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Sets the description.
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the base folder.
	 * @return the base folder
	 */
	public String getBaseFolder() {
		return baseFolder;
	}
	/**
	 * Sets the base folder.
	 * @param baseFolder the new base folder
	 */
	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}
	
	/**
	 * Saves the current blueprint to a file.
	 * @param file the file
	 */
	public void save(Path file) {
		ProjectBlueprint.save(this, file);
	}
	/**
	 * Saves the specified {@link ProjectBlueprint} to a specified path.
	 * @param bluePrint the blue print
	 * @param file the destination file
	 */
	public static void save(ProjectBlueprint bluePrint, Path file) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter writer = new FileWriter(file.toFile());
			gson.toJson(bluePrint, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads {@link ProjectBlueprint} from the specified path in a json file.
	 * @param file the file
	 * @return the project blueprint
	 */
	public static ProjectBlueprint load(Path file) {
		
		// Initialize Gson
		Gson gson = new Gson();

		ProjectBlueprint blueprintEntry = null;
		try {
			FileReader reader = new FileReader(file.toFile());
			// Parse JSON to BlueprintEntry object
			blueprintEntry = gson.fromJson(reader, ProjectBlueprint.class);

		} catch (IOException e) {
			e.printStackTrace();
        }
		return blueprintEntry;
	}
	
	/**
	 * Loads {@link ProjectBlueprint} from the specified path.
	 *
	 * @param file the file
	 * @return the project blueprint
	 */
	public static ProjectBlueprint load(String relativePackagePath) {
		
		// Initialize Gson
		Gson gson = new Gson();
		ProjectBlueprint blueprintEntry = null;
		try {
			// Parse JSON to BlueprintEntry object
			InputStream stream = ProjectBlueprint.class.getClassLoader().getResourceAsStream(relativePackagePath);
			InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
			blueprintEntry = gson.fromJson(streamReader, ProjectBlueprint.class);

		} catch (Exception ex) {
			ex.printStackTrace();
        }
		return blueprintEntry;
	}
	
	/**
	 * Gets the required arguments.
	 * @return the required arguments
	 */
	public ArrayList<StartArgument> getRequiredArguments() {
		if (requiredArguments==null) {
			requiredArguments=new ArrayList<StartArgument>();
		}
		return requiredArguments;
	}
	
	/**
	 * Sets the replacement strings.
	 * @param requiredArguments the new required arguments
	 */
	public void setRequiredArguments(ArrayList<StartArgument> requiredArguments) {
		this.requiredArguments = requiredArguments;
	}
	
	
	/**
	 * Adds the required arguments.
	 * @param requiredArguments the required arguments
	 */
	public void addRequiredArguments(StartArgument requiredArgument) {
		this.getRequiredArguments().add(requiredArgument);
	}
	
	public HashMap<String, String> getTextReplacements() {
		if (textReplacements==null) {
			textReplacements = new HashMap<String, String>();
		}
		return textReplacements;
	}
	
}