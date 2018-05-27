package project.roy.socialmedia.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Commentar {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("timeline_commentar")
    @Expose
    private String timelineCommentar;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("timeline_id")
    @Expose
    private Integer timelineId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user")
    @Expose
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimelineCommentar() {
        return timelineCommentar;
    }

    public void setTimelineCommentar(String timelineCommentar) {
        this.timelineCommentar = timelineCommentar;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTimelineId() {
        return timelineId;
    }

    public void setTimelineId(Integer timelineId) {
        this.timelineId = timelineId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}