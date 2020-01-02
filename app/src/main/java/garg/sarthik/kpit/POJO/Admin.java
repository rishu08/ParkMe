package garg.sarthik.kpit.POJO;

public class Admin extends User{

    public Admin(String name, String emailId, String mobileNo) {
        super(name, emailId, mobileNo);
    }

    int totalOwnedLocations = 0;

    public int getTotalOwnedLocations() {
        return totalOwnedLocations;
    }

    Admin(){
        super();
    }

    //Collections in this document will be :
    // 1. His owned locations


}
