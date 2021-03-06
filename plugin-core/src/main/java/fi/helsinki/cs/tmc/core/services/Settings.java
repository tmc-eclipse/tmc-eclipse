package fi.helsinki.cs.tmc.core.services;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import fi.helsinki.cs.tmc.core.io.FileUtil;

/**
 * Settings class for storing user preferences, such as TMC username and
 * password, in a platform independent way. Wraps java.util.prefs.Preferences.
 * Changes may be propagated to the persistent store at any time.
 */
public class Settings {

    public static final String PREF_KEY_TMC_SERVER_URL = "baseUrl";
    public static final String PREF_KEY_USERNAME = "username";
    public static final String PREF_KEY_PASSWORD = "password";
    public static final String PREF_KEY_CURRENT_COURSE = "currentCourse";
    public static final String PREF_KEY_EXERCISE_FILEPATH = "filePathDownloadedExercises";
    public static final String PREF_KEY_CHECK_FOR_UPDATES_IN_BACKGROUND = "checkForUpdatesInBackground";
    public static final String PREF_KEY_CHECK_FOR_UNOPENED_AT_STARTUP = "checkForUnopenedAtStartup";
    public static final String PREF_KEY_SPYWARE_ENABLED = "spywareEnabled";
    public static final String PREF_KEY_DETAILED_SPYWARE_ENABLED = "detailedSpywareEnabled";
    public static final String PREF_ERROR_MSG_LOCALE = "errorMsgLocale";
    public static final String[] AVAILABLE_LOCALES = new String[] {"English", "Finnish"};
    public static final int DEFAULT_LOCALE_NUM = 1;
    public static final String PREF_KEY_SAVE_PASSWORD = "savePassword";
    public static final String PREF_KEY_IS_LOGGED_IN = "isLoggedIn";

    private boolean loggedIn = false;

    private Preferences prefs;

    private String temporaryPassword;

    /**
     * Returns the user preferences from the default storage for the current
     * user.
     * 
     * @return the user preferences from the default storage for the current
     *         user.
     */
    public static Settings getDefaultSettings() {
        try {
            Settings settings = new Settings(Preferences.userRoot().node(Settings.class.getName()));
            return settings;
        } catch (SecurityException se) {
            return null;
        }
    }

    public Settings(Preferences prefs) {
        this.prefs = prefs;
        this.temporaryPassword = "";
    }

    public String getExerciseFilePath() {
        return prefs.get(PREF_KEY_EXERCISE_FILEPATH, "");
    }

    public void setExerciseFilePath(String exerciseFilePath) {
        if (exerciseFilePath != null) {
            // Resolve symbolic links
            String realPath;
            try {
                realPath = new File(exerciseFilePath).getCanonicalPath();
                prefs.put(PREF_KEY_EXERCISE_FILEPATH, FileUtil.getUnixPath(realPath));
            } catch (IOException e) {
            }
        }
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

    public void setLoggedIn(boolean isloggedIn) {
        prefs.putBoolean(PREF_KEY_IS_LOGGED_IN, isloggedIn);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(PREF_KEY_IS_LOGGED_IN, false);
    }

    public String getPassword() {
        if (!isSavePassword()) {
            return temporaryPassword;
        }
        return prefs.get(PREF_KEY_PASSWORD, "");
    }

    public void setPassword(String password) {
        if (!isSavePassword()) {
            this.temporaryPassword = password;
            return;
        }
        prefs.put(PREF_KEY_PASSWORD, password);
    }

    public String getCurrentCourseName() {
        return prefs.get(PREF_KEY_CURRENT_COURSE, "");
    }

    public void setCurrentCourseName(String currentCourseName) {
        prefs.put(PREF_KEY_CURRENT_COURSE, currentCourseName);
    }

    public boolean isCheckingForUpdatesInTheBackground() {
        return prefs.getBoolean(PREF_KEY_CHECK_FOR_UPDATES_IN_BACKGROUND, true);
    }

    public void setCheckingForUpdatesInTheBackground(boolean value) {
        prefs.putBoolean(PREF_KEY_CHECK_FOR_UPDATES_IN_BACKGROUND, value);
    }

    public boolean isCheckingForUnopenedAtStartup() {
        return prefs.getBoolean(PREF_KEY_CHECK_FOR_UNOPENED_AT_STARTUP, true);
    }

    public void setCheckingForUnopenedAtStartup(boolean value) {
        prefs.putBoolean(PREF_KEY_CHECK_FOR_UNOPENED_AT_STARTUP, value);
    }

    public boolean isSpywareEnabled() {
        return prefs.getBoolean(PREF_KEY_SPYWARE_ENABLED, true);
    }

    public void setIsSpywareEnabled(boolean value) {
        prefs.putBoolean(PREF_KEY_SPYWARE_ENABLED, value);
    }

    public boolean isDetailedSpywareEnabled() {
        return prefs.getBoolean(PREF_KEY_DETAILED_SPYWARE_ENABLED, true);
    }

    public void setIsDetailedSpywareEnabled(boolean value) {
        prefs.putBoolean(PREF_KEY_DETAILED_SPYWARE_ENABLED, value);
    }

    public boolean isSavePassword() {
        return prefs.getBoolean(PREF_KEY_SAVE_PASSWORD, true);
    }

    public void setSavePassword(boolean value) {
        prefs.putBoolean(PREF_KEY_SAVE_PASSWORD, value);
    }

    public int getDefaultLocaleNum() {
        return DEFAULT_LOCALE_NUM;
    }

    public String[] getAvailableLocales() {
        return AVAILABLE_LOCALES;
    }

    /**
     * Forces the changes to be saved on a persistent store.
     * 
     * @return true if the changes are saved successfully, false if an exception
     *         occurred.
     */
    public boolean save() {
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            return false;
        }
        return true;
    }

    public Locale getErrorMsgLocale() {
        return parseLocale(AVAILABLE_LOCALES[getErrorMsgLocaleNum()]);
    }

    public int getErrorMsgLocaleNum() {
        return prefs.getInt(PREF_ERROR_MSG_LOCALE, DEFAULT_LOCALE_NUM);
    }

    public void setErrorMsgLocale(int localeNum) {
        prefs.putInt(PREF_ERROR_MSG_LOCALE, localeNum);
    }

    private Locale parseLocale(String s) {
        if (s.isEmpty()) {
            return new Locale(AVAILABLE_LOCALES[DEFAULT_LOCALE_NUM]);
        }
        return new Locale(s);
    }
}
