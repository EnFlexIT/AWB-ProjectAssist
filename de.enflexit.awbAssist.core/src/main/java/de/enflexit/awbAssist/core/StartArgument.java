package de.enflexit.awbAssist.core;

/**
 * The Class StartArgument.
 * @author Omar Ben Chobba - Enflex.IT GmbH
 */
public class StartArgument {
	
	private String argumentName;
	private boolean mandatory;
	private String defaultValue;
	
	/**
	 * Gets the argument name.
	 * @return the argument name
	 */
	public String getArgumentName() {
		return argumentName;
	}
	/**
	 * Sets the argument name.
	 * @param argumentName the new argument name
	 */
	public void setArgumentName(String argumentName) {
		this.argumentName = argumentName;
	}
	
	/**
	 * Checks if is mandatory.
	 * @return true, if is mandatory
	 */
	public boolean isMandatory() {
		return mandatory;
	}
	/**
	 * Sets the mandatory.
	 * @param mandatory the new mandatory
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	/**
	 * Gets the default value.
	 * @return the default value
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * Sets the default value.
	 * @param defaultValue the new default value
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return this.getArgumentName();
	}
}
