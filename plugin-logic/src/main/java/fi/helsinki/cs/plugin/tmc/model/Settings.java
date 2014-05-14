package fi.helsinki.cs.plugin.tmc.model;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings {

	public static final String PREF_KEY_TMC_SERVER_URL = "baseUrl";	
	public static final String PREF_KEY_USERNAME = "username";
	public static final String PREF_KEY_PASSWORD = "password";
	
	private Preferences prefs;
	
	public static Settings getDefaultSettings() {
		try {
			Settings settings = new Settings(Preferences.userRoot().node(Settings.class.getName()));
			return settings;
		} catch(SecurityException se) {
			return null;
		}
	}
	
	public Settings(Preferences prefs) {
		this.prefs = prefs;
	}
	
	public String getServerBaseUrl() {
		return prefs.get(PREF_KEY_TMC_SERVER_URL, "");
	}
	
	public void setServerBaseUrl(String serverBaseUrl) {
		prefs.put(PREF_KEY_TMC_SERVER_URL, serverBaseUrl);
	}
	
	public String getUsername() {
		return prefs.get(PREF_KEY_USERNAME, "");
	}
	
	public void setUsername(String username) {
		prefs.put(PREF_KEY_USERNAME, username);
	}
	
	public String getPassword() {
		return prefs.get(PREF_KEY_PASSWORD, "");
	}
	
	public void setPassword(String password) {
		prefs.put(PREF_KEY_PASSWORD, password);
	}
	
	public boolean save() {
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			return false;
		}
		return true;
	}
	
}
