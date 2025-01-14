package de.enflexit.awbAssist.core;

/**
 * The Class InternalResource describes the relative paths of an internal resources
 * and indicates, if this resource is a directory or not.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class InternalResource implements Comparable<InternalResource> {

	public String path;
	public boolean isDirectory;
	
	/**
	 * Instantiates a new internal resource.
	 */
	public InternalResource() { }
	/**
	 * Instantiates a new internal resource.
	 *
	 * @param path the path
	 * @param isDirectory the is directory
	 */
	public InternalResource(String path, boolean isDirectory) {
		this.setPath(path);
		this.setDirectory(isDirectory);
	}

	/**
	 * Returns the path.
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * Sets the path.
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Checks if is directory.
	 * @return true, if is directory
	 */
	public boolean isDirectory() {
		return isDirectory;
	}
	/**
	 * Sets the directory.
	 * @param isDirectory the new directory
	 */
	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (this.isDirectory==true ? " D: " : "F: ") + this.getPath();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(InternalResource extRes) {
		return this.getPath().compareTo(extRes.getPath());
	}
	
}
