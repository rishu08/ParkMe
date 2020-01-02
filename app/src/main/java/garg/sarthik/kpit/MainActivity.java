package garg.sarthik.kpit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import garg.sarthik.kpit.Constants.Database;
import garg.sarthik.kpit.Constants.POJODetails;
import garg.sarthik.kpit.POJO.User;
import garg.sarthik.kpit.Statics.Variables;

public class MainActivity extends AppCompatActivity {

    private String TAG = "FireAuth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.e(TAG, "onCreate: " + isConnectedToNetwork());

        if (isConnectedToNetwork() == false) {
            Toast.makeText(this, "Please connect to Internet and Restart", Toast.LENGTH_SHORT).show();
        } else {


            Variables.db = FirebaseFirestore.getInstance();

            Variables.colLocations = Variables.db.collection(Database.COL_LOCATION);
            Variables.colAdmins = Variables.db.collection(Database.COL_ADMIN);
            Variables.colUsers = Variables.db.collection(Database.COL_USER);


            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build());


            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    POJODetails.REQUEST_CODE);
        }

    }

    public boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));

        } else {
            return  connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == POJODetails.REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Variables.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                Variables.colUsers.document(Variables.firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            Variables.user = documentSnapshot.toObject(User.class);

                            Log.e(TAG, "onSuccess: " + Variables.user.getEmailId() );

                            startActivity(new Intent(MainActivity.this, UserActivity.class));
                        } else {
                            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                        }

                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error, Please try again!!", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e(TAG, "onActivityResult: \n Successfully Logged in");

                // ...
            } else {

                if (response != null) {
                    Log.e(TAG, "onActivityResult: " + response.getError().getErrorCode());
                    Toast.makeText(this, "Error Occurred, Please try again", Toast.LENGTH_SHORT).show();
                }
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

}


