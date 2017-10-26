package com.app.RULookout; /**
 * Created by TRoc9 on 8/8/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrimeData {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("CrimeDateTime")
    @Expose
    private String crimeDateTime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public CrimeData(int iD, String title, double latitude, double longitude, String description, String crimeDateTime, String createdAt){
        this.iD = iD;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.crimeDateTime = crimeDateTime;
        this.createdAt = createdAt;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCrimeDateTime() {
        return crimeDateTime;
    }

    public void setCrimeDateTime(String crimeDateTime) {
        this.crimeDateTime = crimeDateTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
/*
public class com.example.myfirstapp.CrimeData {

    private String title;
    private double latitude;
    private double longitude;
    private String description;

    public String getTitle(){
        return title;
    }


    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }


    public String getDescription() {
        return description;
    }
}
*/
