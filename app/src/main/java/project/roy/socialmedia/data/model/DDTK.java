package project.roy.socialmedia.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DDTK {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ddtk_description")
    @Expose
    private String ddtkDescription;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("usia")
    @Expose
    private String usia;
    @SerializedName("aspek_perkembangan")
    @Expose
    private String aspekPerkembangan;

    private int status;

    public DDTK(Integer id, String ddtkDescription, Object createdAt, Object updatedAt, String usia, String aspekPerkembangan, int status) {
        this.id = id;
        this.ddtkDescription = ddtkDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.usia = usia;
        this.aspekPerkembangan = aspekPerkembangan;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDdtkDescription() {
        return ddtkDescription;
    }

    public void setDdtkDescription(String ddtkDescription) {
        this.ddtkDescription = ddtkDescription;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsia() {
        return usia;
    }

    public void setUsia(String usia) {
        this.usia = usia;
    }

    public String getAspekPerkembangan() {
        return aspekPerkembangan;
    }

    public void setAspekPerkembangan(String aspekPerkembangan) {
        this.aspekPerkembangan = aspekPerkembangan;
    }
}