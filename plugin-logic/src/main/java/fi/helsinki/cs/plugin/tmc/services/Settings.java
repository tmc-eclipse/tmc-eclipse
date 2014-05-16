package fi.helsinki.cs.plugin.tmc.services;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Settings class for storing user preferences, such as TMC username and password, in a platform independent way.
 * Wraps java.util.prefs.Preferences. Changes may be propagated to the persistent store at any time.
 */
public class Settings {

	public static final String PREF_KEY_TMC_SERVER_URL = "baseUrl";	
	public static final String PREF_KEY_USERNAME = "username";
	public static final String PREF_KEY_PASSWORD = "password";
	public static final String PREF_KEY_CURRENT_COURSE = "currentCourse";

	private Preferences prefs;

	/**
	 * Returns the user preferences from the default storage for the current user.
	 * @return the user preferences from the default storage for the current user.
	 */
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
	
	public String getCurrentCourseName() {
		return prefs.get(PREF_KEY_CURRENT_COURSE, "");
	}
	
	public void setCurrentCourseName(String currentCourseName) {
		prefs.put(PREF_KEY_CURRENT_COURSE, currentCourseName);
	}
	
	/**
	 * Forces the changes to be saved on a persistent store.
	 * @return true if the changes are saved successfully, false if an exception occurred.
	 */
	public boolean save() {
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			return false;
		}
		return true;
	}

}
