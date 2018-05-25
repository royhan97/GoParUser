package project.roy.socialmedia.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Timeline implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("timeline_moment")
    @Expose
    private String timelineMoment;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("media")
    @Expose
    private List<Media> media = null;
    @SerializedName("commentar")
    @Expose
    private List<Object> commentar = null;

    protected Timeline(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        timelineMoment = in.readString();
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<Timeline> CREATOR = new Creator<Timeline>() {
        @Override
        public Timeline createFromParcel(Parcel in) {
            return new Timeline(in);
        }

        @Override
        public Timeline[] newArray(int size) {
            return new Timeline[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimelineMoment() {
        return timelineMoment;
    }

    public void setTimelineMoment(String timelineMoment) {
        this.timelineMoment = timelineMoment;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public List<Object> getCommentar() {
        return commentar;
    }

    public void setCommentar(List<Object> commentar) {
        this.commentar = commentar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(timelineMoment);
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }
}
