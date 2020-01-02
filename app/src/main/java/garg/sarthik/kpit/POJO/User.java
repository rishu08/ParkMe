package garg.sarthik.kpit.POJO;

import java.util.ArrayList;

public class User {

    String name;
    String emailId;
    String mobileNo;

    int totalBookings;


    //Collection inside this Document
    //1. His Booking history


    public User() {
    }

    public User(String name, String emailId, String mobileNo) {
        this.name = name;
        this.emailId = emailId;
        this.mobileNo = mobileNo;
        this.totalBookings = 0;
    }


    public String getName() {
        return name;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }
}
