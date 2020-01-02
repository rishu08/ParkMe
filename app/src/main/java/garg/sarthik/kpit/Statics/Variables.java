package garg.sarthik.kpit.Statics;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import garg.sarthik.kpit.POJO.Admin;
import garg.sarthik.kpit.POJO.User;

public class Variables {


    public static FirebaseFirestore db;

    public static CollectionReference colUsers;
    public static CollectionReference colLocations;
    public static CollectionReference colAdmins;
    public static FirebaseUser firebaseUser;

    public static User user;

}
