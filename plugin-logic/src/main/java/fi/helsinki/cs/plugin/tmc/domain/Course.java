package fi.helsinki.cs.plugin.tmc.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Course {

    private int id;
    private String name;

    @SerializedName("details_url")
    private String detailsUrl;
    @SerializedName("unlock_url")
    private String unlockUrl;
    @SerializedName("reviews_url")
    private String reviewsUrl;
    @SerializedName("comet_url")
    private String cometUrl;
    @SerializedName("spyware_urls")
    private List<String> spywareUrls;

    private boolean exercisesLoaded;

    private List<Exercise> exercises;
    private List<String> unlockables; // Exercise names

    public Course() {

        /*
         * In case of a missing name-field in the JSON, GSON would replace any
         * default value of name with null. Therefore using f.ex. empty string
         * here is useless.
         */
        this(null);
    }

    public Course(String name) {
        this.name = name;
        this.exercises = new ArrayList<Exercise>();
        this.unlockables = new ArrayList<String>();
        this.spywareUrls = new ArrayList<String>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public String getUnlockUrl() {
        return unlockUrl;
    }

    public void setUnlockUrl(String unlockUrl) {
        this.unlockUrl = unlockUrl;
    }

    public String getReviewsUrl() {
        return reviewsUrl;
    }

    public void setReviewsUrl(String reviewsUrl) {
        this.reviewsUrl = reviewsUrl;
    }

    public String getCometUrl() {
        return cometUrl;
    }

    public void setCometUrl(String cometUrl) {
        this.cometUrl = cometUrl;
    }

    public List<String> getSpywareUrls() {
        return spywareUrls;
    }

    public void setSpywareUrls(List<String> spywareUrls) {
        this.spywareUrls = spywareUrls;
    }

    public boolean isExercisesLoaded() {
        return exercisesLoaded;
    }

    public void setExercisesLoaded(boolean exercisesLoaded) {
        this.exercisesLoaded = exercisesLoaded;
    }

    public List<String> getUnlockables() {
        return unlockables;
    }

    public void setUnlockables(List<String> unlockables) {
        this.unlockables = unlockables;
    }

    public List<Exercise> getDownloadableExercises() {
        List<Exercise> downloadableExercises = new ArrayList<Exercise>();
        for (Exercise e : getExercises()) {
            if (!e.isDownloaded() && !e.hasDeadlinePassed()) {
                downloadableExercises.add(e);
            }
        }
        return downloadableExercises;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Course)) {
            return false;
        }

        Course c = (Course) o;

        return this.name.equals(c.name);
    }

}