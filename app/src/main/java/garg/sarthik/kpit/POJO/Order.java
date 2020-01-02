package garg.sarthik.kpit.POJO;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {

    //Order will be in the format of -> YYYYMMDDHHMMSS-SR

    String orderId;
    String bookedTime;
    String arrivalTime;
    String bookingStatus;
    String userId;

    int slots;
    int penalty;

    Location location;

    public Order() {
    }

    public Order(String orderId, String bookedTime, String arrivalTime, String bookingStatus, int slots, int penalty, Location location, String userId) {
        this.orderId = orderId;
        this.bookedTime = bookedTime;
        this.arrivalTime = arrivalTime;
        this.bookingStatus = bookingStatus;
        this.slots = slots;
        this.penalty = penalty;
        this.location = location;
        this.userId = userId;
    }

    protected Order(Parcel in) {
        orderId = in.readString();
        bookedTime = in.readString();
        arrivalTime = in.readString();
        bookingStatus = in.readString();
        userId = in.readString();
        slots = in.readInt();
        penalty = in.readInt();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public String getBookedTime() {
        return bookedTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public int getSlots() {
        return slots;
    }

    public int getPenalty() {
        return penalty;
    }

    public Location getLocation() {
        return location;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(bookedTime);
        dest.writeString(arrivalTime);
        dest.writeString(bookingStatus);
        dest.writeString(userId);
        dest.writeInt(slots);
        dest.writeInt(penalty);
        dest.writeParcelable(location, flags);
    }
}
