package de.enflexit.awbAssist.core;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class defines the general structure of a project blueprint.
 * @author Omar Ben Chobba - EnFlex.IT GmbH
 */
public class ProjectBlueprint {
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The base folder. */
	private String baseFolder;
	
	/** The replacement strings. */
	private ArrayList<String> replacementStrings;
	
	/** The required arguments. */
	private ArrayList<String> requiredArguments;

	// add a list that contains the required arguments for the corresponding blueprint.
	//
	
	
	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name.
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
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
	 * Gets the replacement strings.
	 * @return the replacement strings
	 */
	public ArrayList<String> getReplacementStrings() {
		if (replacementStrings==null) {
			replacementStrings=new ArrayList<String>();
		}
		return replacementStrings;
	}
	/**
	 * Sets the replacement strings.
	 * @param replacementStrings the new replacement strings
	 */
	public void setReplacementStrings(ArrayList<String> replacementStrings) {
		this.replacementStrings = replacementStrings;
	}
	/**
	 * Adds a new replacement string to the list.
	 * @param replacementString the replacement string
	 */
	public void addReplacementString(String replacementString) {
		this.getReplacementStrings().add(replacementString);
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
	 *
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
	 * Loads {@link ProjectBlueprint} from the specified path.
	 *
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
	 * Gets the required arguments.
	 *
	 * @return the required arguments
	 */
	public ArrayList<String> getRequiredArguments() {
		if (requiredArguments==null) {
			requiredArguments=new ArrayList<String>();
		}
		return requiredArguments;
	}
	
	/**
	 * Sets the replacement strings.
	 *
	 * @param requiredArguments the new required arguments
	 */
	public void setRequiredArguments(ArrayList<String> requiredArguments) {
		this.requiredArguments = requiredArguments;
	}
	
	
	/**
	 * Adds the required arguments.
	 *
	 * @param requiredArguments the required arguments
	 */
	public void addRequiredArguments(String requiredArgument) {
		this.getRequiredArguments().add(requiredArgument);
	}
	
}