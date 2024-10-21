package de.enflexit.awb.assist.baseCase;

import java.util.ArrayList;

/**
 * The Class defines the general structure of a project blueprint.
 * @author Omar Ben Chobba - EnFlex.IT GmbH
 */
public class ProjectBlueprint {
	
	private String name;
	private String description;
	private String baseFolder;
	private ArrayList<String> replacementStrings;
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the base folder.
	 *
	 * @return the base folder
	 */
	public String getBaseFolder() {
		return baseFolder;
	}
	
	/**
	 * Sets the base folder.
	 *
	 * @param baseFolder the new base folder
	 */
	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}
	
	/**
	 * Gets the replacement strings.
	 *
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
	 *
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

}
