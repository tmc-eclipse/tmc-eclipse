package fi.helsinki.cs.plugin.tmc.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Exercise implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";

    private int id;

    private String name;

    private transient Course course;

    private String courseName;

    private Date deadlineDate;

    @SerializedName("deadline")
    private String deadlineString;

    /**
     * The URL this exercise can be downloaded from.
     */
    @SerializedName("zip_url")
    private String downloadUrl;

    /**
     * The URL the solution can be downloaded from (admins only).
     */
    @SerializedName("solution_zip_url")
    private String solutionDownloadUrl;

    /**
     * The URL where this exercise should be posted for review.
     */
    @SerializedName("return_url")
    private String returnUrl;

    private boolean locked;

    @SerializedName("deadline_description")
    private String deadlineDescription;

    private boolean returnable;
    @SerializedName("requires_review")
    private boolean requiresReview;
    private boolean attempted;
    private boolean completed;
    private boolean reviewed;
    @SerializedName("all_review_points_given")
    private boolean allReviewPointsGiven;

    private String oldChecksum;
    private boolean updated;

    private String checksum;

    private transient Project project;

    @SerializedName("memory_limit")
    private Integer memoryLimit;

    public Exercise() {
    }

    public Exercise(String name) {
        this(name, "unknown-course");
    }

    public Exercise(String name, String courseName) {
        this.name = name;
        this.courseName = courseName;
    }

    /**
     * This method exists because api used by the original TMC-plugin was
     * deprecated. Originally Date's constructor accepted strings that contained
     * date and it parsed them. This functionality has now been split to
     * SimpleDateFormat class. As such we deserialize the date to separate
     * string and then call this method to create Date-object from the string
     */
    public void finalizeDeserialization() {
        if (deadlineString == null || deadlineString.equals("")) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            deadlineDate = sdf.parse(deadlineString);
        } catch (ParseException e) {
            // Set to null on failure
            // TODO: Log here?
            deadlineDate = null;
        }
    }

    public void setDeadlineString(String deadline) {
        deadlineString = deadline;
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
        if (name == null) {
            throw new NullPointerException("name was null at Exercise.setName");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty at Exercise.setName");
        }
        this.name = name;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getDeadlineDescription() {
        return deadlineDescription;
    }

    public void setDeadlineDescription(String deadlineDescription) {
        this.deadlineDescription = deadlineDescription;
    }

    public boolean hasDeadlinePassed() {
        return hasDeadlinePassedAt(new Date());
    }

    public boolean hasDeadlinePassedAt(Date time) {
        if (time == null) {
            throw new NullPointerException("Given time was null at Exercise.isDeadlineEnded");
        }
        if (getDeadline() != null) {
            return getDeadline().getTime() < time.getTime();
        } else {
            return false;
        }
    }

    public ExerciseKey getKey() {
        return new ExerciseKey(courseName, name);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadAddress) {
        if (downloadAddress == null) {
            throw new NullPointerException("downloadAddress was null at Exercise.setDownloadAddress");
        }
        if (downloadAddress.isEmpty()) {
            throw new IllegalArgumentException("downloadAddress cannot be empty at Exercise.setDownloadAddress");
        }

        this.downloadUrl = downloadAddress;
    }

    public String getReturnUrl() {
        return this.returnUrl;
    }

    public void setSolutionDownloadUrl(String solutionDownloadUrl) {
        this.solutionDownloadUrl = solutionDownloadUrl;
    }

    public String getSolutionDownloadUrl() {
        return solutionDownloadUrl;
    }

    public void setReturnUrl(String returnAddress) {
        if (returnAddress == null) {
            throw new NullPointerException("returnAddress was null at Exercise.setReturnAddress");
        }
        if (returnAddress.isEmpty()) {
            throw new IllegalArgumentException("downloadAddress cannot be empty at Exercise.setReturnAddress");
        }
        this.returnUrl = returnAddress;
    }

    public Date getDeadline() {
        return deadlineDate;
    }

    public void setDeadline(Date deadline) {
        this.deadlineDate = deadline;
    }

    public boolean isReturnable() {
        return returnable && !hasDeadlinePassed();
    }

    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }

    public boolean requiresReview() {
        return requiresReview;
    }

    public void setRequiresReview(boolean requiresReview) {
        this.requiresReview = requiresReview;
    }

    public boolean isAttempted() {
        return attempted;
    }

    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public boolean isAllReviewPointsGiven() {
        return allReviewPointsGiven;
    }

    public void setAllReviewPointsGiven(boolean allReviewPointsGiven) {
        this.allReviewPointsGiven = allReviewPointsGiven;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public void setOldChecksum(String oldChecksum) {
        this.oldChecksum = oldChecksum;
    }

    public String getOldChecksum() {
        return oldChecksum;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Exercise)) {
            return false;
        }

        Exercise e = (Exercise) o;
        if (this.courseName == null || e.courseName == null) {
            return false;
        }
        if (this.name == null || e.name == null) {
            return false;
        }

        return this.courseName.equals(e.courseName) && this.name.equals(e.name);
    }

    @Override
    public int hashCode() {
        return courseName.hashCode() + 7 * name.hashCode();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setHasBeenUpdated() {
        updated = false;
    }

    public boolean isUpdated() {
        System.out.println("old: " + oldChecksum + " new: " + checksum + "  " + updated);
        if (!updated) {
            if (oldChecksum == null || oldChecksum.equals(checksum)) {
                return updated;
            } else {
                updated = true;
                System.out.println(updated);
            }
        }
        return updated;
    }

    public boolean isDownloadable() {
        return (hasDeadlinePassed() == false) && (project == null || project.getStatus() != ProjectStatus.DOWNLOADED);
    }

}