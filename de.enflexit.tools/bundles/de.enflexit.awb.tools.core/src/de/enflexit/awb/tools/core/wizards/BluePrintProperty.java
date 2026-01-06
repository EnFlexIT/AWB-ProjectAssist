package de.enflexit.awb.tools.core.wizards;

/**
 * The Class BluePrintProperty.
 */
public class BluePrintProperty {

	private String key;
	private String value;
	
	/**
	 * Instantiates a new project property.
	 */
	public BluePrintProperty() {
		this(null, null);
	}
	/**
	 * Instantiates a new project property.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public BluePrintProperty(String key, String value) {
		this.setKey(key);
		this.setValue(value);
	}

	/**
	 * Gets the key.
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * Sets the key.
	 * @param key the new key
	 */
	public void setKey(String key) {
		this.key = key;
	}


	/**
	 * Gets the value.
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * Sets the value.
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
