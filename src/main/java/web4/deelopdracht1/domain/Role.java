package web4.deelopdracht1.domain;

public enum Role {
	BIB("bibliothekaris"), LID("lid");

	private String description;

	Role(String description) {
		this.description = description;
	}
	
	Role() {
		
	}

	public String getDescription() {
		return description;
	}
}
