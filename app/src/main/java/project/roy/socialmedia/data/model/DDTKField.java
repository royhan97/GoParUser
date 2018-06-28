package project.roy.socialmedia.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DDTKField {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ddtk_id")
    @Expose
    private Integer ddtkId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_field")
    @Expose
    private String userField;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("ddtk")
    @Expose
    private DDTK ddtk;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDdtkId() {
        return ddtkId;
    }

    public void setDdtkId(Integer ddtkId) {
        this.ddtkId = ddtkId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserField() {
        return userField;
    }

    public void setUserField(String userField) {
        this.userField = userField;
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

    public DDTK getDdtk() {
        return ddtk;
    }

    public void setDdtk(DDTK ddtk) {
        this.ddtk = ddtk;
    }

}