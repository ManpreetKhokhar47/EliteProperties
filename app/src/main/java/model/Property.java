package model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Property implements Serializable {
    private String propertyId;
    private String userId;
    private String lotSize;
    private String coveredArea;
    private String bedroomsCount;
    private String bathroomCount;
    private String parkingSpaces;
    private float price;
    private String address;
    private String desc;
    private String propType;
    private String srcImg;
    private String postingFor;
    private String managedBy;
    private String managerId;

    public Property() {
    }

    public Property(String propertyId, String userId, String lotSize, String coveredArea, String bedroomsCount, String bathroomCount, String parkingSpaces, float price, String address, String desc, String propType, String srcImg, String postingFor) {
        this.propertyId = propertyId;
        this.userId = userId;
        this.lotSize = lotSize;
        this.coveredArea = coveredArea;
        this.bedroomsCount = bedroomsCount;
        this.bathroomCount = bathroomCount;
        this.parkingSpaces = parkingSpaces;
        this.price = price;
        this.address = address;
        this.desc = desc;
        this.propType = propType;
        this.srcImg = srcImg;
        this.postingFor = postingFor;
        this.managedBy = "Owner";
        this.managerId = userId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLotSize() {
        return lotSize;
    }

    public void setLotSize(String lotSize) {
        this.lotSize = lotSize;
    }

    public String getCoveredArea() {
        return coveredArea;
    }

    public void setCoveredArea(String coveredArea) {
        this.coveredArea = coveredArea;
    }

    public String getBedroomsCount() {
        return bedroomsCount;
    }

    public void setBedroomsCount(String bedroomsCount) {
        this.bedroomsCount = bedroomsCount;
    }

    public String getBathroomCount() {
        return bathroomCount;
    }

    public void setBathroomCount(String bathroomCount) {
        this.bathroomCount = bathroomCount;
    }

    public String getParkingSpaces() {
        return parkingSpaces;
    }

    public void setParkingSpaces(String parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPropType() {
        return propType;
    }

    public void setPropType(String propType) {
        this.propType = propType;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public String getPostingFor() {
        return postingFor;
    }

    public void setPostingFor(String postingFor) {
        this.postingFor = postingFor;
    }

    public String getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(String managedBy) {
        this.managedBy = managedBy;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    @NonNull
    @Override
    public String toString() {
        //property = new Property(propertyId, userId, lotSize, coveredArea, noOfBed, noOfBath, parkingSpaces, propPrice, address, desc, propertyType, imageUrl, postingFor);
        return "Property Id"+ propertyId + "User Id" + userId + "Lot Size" + lotSize + "covered Area" + coveredArea + "No of beds"+ bedroomsCount + "No of bath"+ bathroomCount + "Parking Spaces" + parkingSpaces + "Price "+ price + "Address "+ address + "Desc"+ desc +"Proptype" +propType + "Image Url"+ srcImg+"Posting for" +postingFor ;
    }
}
