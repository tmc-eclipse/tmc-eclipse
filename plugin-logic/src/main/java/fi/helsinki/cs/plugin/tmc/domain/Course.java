package fi.helsinki.cs.plugin.tmc.domain;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing an instance of a Course in the TMC system.
 */
public class Course {
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
    
    public Course() {
    	//In case of a missing name-field in the JSON, GSON would replace any default value of name with null.
    	//Therefore using f.ex. empty string here is useless.
        this(null);
    }

    public Course(String name) {
        this.name = name;
        this.spywareUrls = new ArrayList<String>();
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
    
    @Override
    public String toString() {
        return name;
    }
}