package fi.helsinki.cs.plugin.tmc.getJson;

public enum UrlExtension {
	COURSES("courses.json");
	
	private String extension;
	private UrlExtension(String extension) {
		this.extension = extension;
	}
	
	public String getExtension() {
		return extension;
	}

	
}
