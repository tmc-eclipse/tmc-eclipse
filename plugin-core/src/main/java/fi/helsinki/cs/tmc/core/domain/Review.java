package fi.helsinki.cs.tmc.core.domain;

import java.util.ArrayList;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * A domain class storing the code review result that will be shown to user.
 */
public class Review {
    @SerializedName("id")
    private int id;
    @SerializedName("submission_id")
    private int submissionId;
    @SerializedName("exercise_name")
    private String exerciseName;
    @SerializedName("marked_as_read")
    private boolean markedAsRead;
    @SerializedName("reviewer_name")
    private String reviewerName;
    @SerializedName("review_body")
    private String reviewBody;
    @SerializedName("points")
    private ArrayList<String> points;
    @SerializedName("points_not_awarded")
    private ArrayList<String> pointsNotAwarded;
    @SerializedName("url")
    private String url;
    @SerializedName("update_url")
    private String updateUrl;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public boolean isMarkedAsRead() {
        return markedAsRead;
    }

    public void setMarkedAsRead(boolean markedAsRead) {
        this.markedAsRead = markedAsRead;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public ArrayList<String> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<String> points) {
        this.points = points;
    }

    public ArrayList<String> getPointsNotAwarded() {
        return pointsNotAwarded;
    }

    public void setPointsNotAwarded(ArrayList<String> pointsNotAwarded) {
        this.pointsNotAwarded = pointsNotAwarded;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Review other = (Review) obj;
        if (id != other.id)
            return false;
        return true;
    }

}