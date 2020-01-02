package garg.sarthik.kpit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;

import garg.sarthik.kpit.Constants.Database;
import garg.sarthik.kpit.Constants.POJODetails;
import garg.sarthik.kpit.Constants.Status;
import garg.sarthik.kpit.POJO.Admin;
import garg.sarthik.kpit.POJO.Order;
import garg.sarthik.kpit.Statics.Functions;
import garg.sarthik.kpit.Statics.Variables;

public class OrderDetailActivity extends AppCompatActivity {

    private String TAG = "OrderDetails";

    private TextView tvOrderName;
    private TextView tvOrderStatus;
    private TextView tvOrderOwner;
    private TextView tvOrderSlots;
    private TextView tvOrderTime;
    private TextView tvOrderPenalty;
    private TextView tvOrderLatLng;

    private Button btnOrderGoogle;
    private Button btnOrderCancel;

    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvOrderName = findViewById(R.id.tvOrderName);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderOwner = findViewById(R.id.tvOrderOwner);
        tvOrderSlots = findViewById(R.id.tvOrderSlots);
        tvOrderTime = findViewById(R.id.tvOrderTime);
        tvOrderPenalty = findViewById(R.id.tvOrderPenalty);
        tvOrderLatLng = findViewById(R.id.tvOrderLatLng);

        btnOrderGoogle = findViewById(R.id.btnOrderGoogle);
        btnOrderCancel = findViewById(R.id.btnOrderCancel);

        final Order order = getIntent().getParcelableExtra(POJODetails.PARCEL_ORDER);


        tvOrderName.setText(order.getLocation().getName().toUpperCase());
        tvOrderStatus.setText(order.getBookingStatus());
        tvOrderSlots.setText("" + order.getSlots());
        tvOrderTime.setText(Functions.convertToTime(order.getArrivalTime()));
        tvOrderPenalty.setText("₹" + order.getPenalty());
        tvOrderLatLng.setText(order.getLocation().getLatLng());

        Variables.colAdmins.whereEqualTo(POJODetails.UserEmailId,order.getLocation().getAdminId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(queryDocumentSnapshots.isEmpty())
                    return;

                String adminName = queryDocumentSnapshots.toObjects(Admin.class).get(0).getName();
                tvOrderOwner.setText(Functions.toCapitalise(adminName));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });

        String[] args = order.getLocation().getLatLng().split(" , ");
        lat = Double.parseDouble(args[0]);
        lng = Double.parseDouble(args[1]);

        btnOrderGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (Parking Location)";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });

        switch (order.getBookingStatus()) {
            case Status.BOOKING_CONFIRM:
                tvOrderStatus.setTextColor(getResources().getColor(R.color.colorActive));
                break;
            case Status.BOOKING_CANCEL:
                tvOrderStatus.setTextColor(getResources().getColor(R.color.colorClosed));

            case Status.BOOKING_OVER:
                btnOrderCancel.setVisibility(View.GONE);
        }

        btnOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = Variables.firebaseUser.getUid();

                Variables.colUsers.document(userId).collection(Database.COL_HISTORY).document(order.getOrderId()).update("bookingStatus", Status.BOOKING_CANCEL).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Variables.colLocations.document(order.getLocation().getAdminId() + order.getLocation().getLocationId()).collection(Database.COL_BOOKINGS).document(order.getUserId() + order.getOrderId()).update("bookingStatus", Status.BOOKING_CANCEL);
                        Variables.colLocations.document(order.getLocation().getAdminId() + order.getLocation().getLocationId()).update(POJODetails.AvailSlots, FieldValue.increment(order.getSlots()));
                    }
                });

                tvOrderStatus.setText(Status.BOOKING_CANCEL);
                tvOrderStatus.setTextColor(getResources().getColor(R.color.colorClosed));
                btnOrderCancel.setVisibility(View.GONE);
            }
        });

    }
}
