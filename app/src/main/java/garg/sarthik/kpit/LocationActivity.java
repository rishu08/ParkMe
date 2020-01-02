package garg.sarthik.kpit;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

import garg.sarthik.kpit.Constants.Database;
import garg.sarthik.kpit.Constants.POJODetails;
import garg.sarthik.kpit.Constants.Status;
import garg.sarthik.kpit.POJO.Admin;
import garg.sarthik.kpit.POJO.Location;
import garg.sarthik.kpit.POJO.Order;
import garg.sarthik.kpit.Statics.Functions;
import garg.sarthik.kpit.Statics.Variables;
import garg.sarthik.kpit.ui.TimeFragment;

public class LocationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private String TAG = "LocationActivity";

    private LinearLayout llLocation;

    private TextView tvLocationName;
    private TextView tvLocationOwner;
    private TextView tvLocationAvail;
    private TextView tvLocationTotal;
    private TextView tvLocationTime;
    private TextView tvLocationLatLng;

    private EditText etLocationSlot;

    private ImageButton ibLocationTimePick;
    private Button btnLocationBookSubmit;
    private Button btnLocationGoogle;

    private ProgressBar pbBookProgress;

    private double lat, lng;

    private int bookingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        final Location location = getIntent().getParcelableExtra(POJODetails.PARCEL_LOCATION);

        llLocation = findViewById(R.id.llLocation);

        tvLocationName = findViewById(R.id.tvLocationName);
        tvLocationOwner = findViewById(R.id.tvLocationOwner);
        tvLocationAvail = findViewById(R.id.tvLocationAvail);
        tvLocationTotal = findViewById(R.id.tvLocationTotal);
        tvLocationLatLng = findViewById(R.id.tvLocationLatLng);
        tvLocationTime = findViewById(R.id.tvLocationTime);

        etLocationSlot = findViewById(R.id.etLocationSlot);

        btnLocationGoogle = findViewById(R.id.btnLocationGoogle);
        ibLocationTimePick = findViewById(R.id.ibLocationTimePick);
        btnLocationBookSubmit = findViewById(R.id.btnLocationBookSubmit);

        pbBookProgress = findViewById(R.id.pbBookProgress);
        pbBookProgress.setVisibility(View.GONE);

        tvLocationLatLng.setText(location.getLatLng());
        tvLocationName.setText(location.getName().toUpperCase());
        tvLocationAvail.setText("" + location.getAvailSlots());
        tvLocationTotal.setText("/ " + location.getTotalSlots());

        String[] args = location.getLatLng().split(" , ");
        lat = Double.parseDouble(args[0]);
        lng = Double.parseDouble(args[1]);



        Variables.colAdmins.whereEqualTo(POJODetails.UserEmailId,location.getAdminId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(queryDocumentSnapshots.isEmpty())
                    return;

                String adminName = queryDocumentSnapshots.toObjects(Admin.class).get(0).getName();
                tvLocationOwner.setText(Functions.toCapitalise(adminName));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });

        btnLocationGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (Parking Location)";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });


        ibLocationTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimeFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        btnLocationBookSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enableButton(false);

                final String userId = Variables.firebaseUser.getUid();

                Variables.colUsers.document(userId).update(POJODetails.UserTotalBookings, FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Variables.colUsers.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                String orderId = Functions.getId((long) documentSnapshot.get(POJODetails.UserTotalBookings));
                                final int slots = Integer.parseInt(etLocationSlot.getText().toString());

                                Order order = new Order(orderId, "" + Functions.getCurrentTime(), "" + bookingTime, Status.BOOKING_CONFIRM, slots, 0, location, Variables.user.getEmailId());

                                Variables.colUsers.document(userId).collection(Database.COL_HISTORY).document(orderId).set(order).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        enableButton(true);
                                        Toast.makeText(LocationActivity.this, "Error in Submitting to User", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Variables.colLocations.document(location.getAdminId() + location.getLocationId()).collection(Database.COL_BOOKINGS).document(Variables.user.getEmailId() + orderId).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Variables.colLocations.document(location.getAdminId() + location.getLocationId()).update(POJODetails.AvailSlots, FieldValue.increment(-1 * slots));

                                        location.setAvailSlots(location.getAvailSlots() - slots);
                                        enableButton(true);
                                        tvLocationAvail.setText("" + location.getAvailSlots());

                                        Snackbar.make(llLocation, "Slots Booked", Snackbar.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        enableButton(true);
                                        Toast.makeText(LocationActivity.this, "Error in Submitting to Location", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                enableButton(true);
                                Toast.makeText(LocationActivity.this, "Error in fetching the data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        enableButton(true);
                        Toast.makeText(LocationActivity.this, "Error Encountered!! Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void enableButton(boolean enable) {
        if (enable) {
            pbBookProgress.setVisibility(View.GONE);
            btnLocationBookSubmit.setVisibility(View.VISIBLE);
        } else {
            btnLocationBookSubmit.setVisibility(View.GONE);
            pbBookProgress.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        bookingTime = hourOfDay * 100 + minute;

        String time = Functions.convertToTime("" + bookingTime);

        tvLocationTime.setText(time);
    }

}
