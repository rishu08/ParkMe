package garg.sarthik.kpit.POJO;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {


    String locationId;
    String name;
    String adminId;
    String latLng;
    int availSlots;

    //Collection inside this document:
    // 1. Users booked sorted in the order of date as the order
    int totalSlots;

    public Location() {
    }

    public Location(String locationId, String name, String latLng, int totalSlots, String adminId, int availSlots) {
        this.locationId = locationId;
        this.name = name;
        this.latLng = latLng;
        this.availSlots = totalSlots;
        this.totalSlots = totalSlots;
        this.adminId = adminId;
        this.availSlots = availSlots;
    }


    protected Location(Parcel in) {
        locationId = in.readString();
        name = in.readString();
        adminId = in.readString();
        latLng = in.readString();
        availSlots = in.readInt();
        totalSlots = in.readInt();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public String getLatLng() {
        return latLng;
    }

    public int getAvailSlots() {
        return availSlots;
    }

    public void setAvailSlots(int availSlots) {

        this.availSlots = availSlots;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public String getAdminId() {
        return adminId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationId);
        dest.writeString(name);
        dest.writeString(adminId);
        dest.writeString(latLng);
        dest.writeInt(availSlots);
        dest.writeInt(totalSlots);
    }
}
