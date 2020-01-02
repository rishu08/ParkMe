package garg.sarthik.kpit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import garg.sarthik.kpit.Constants.Database;
import garg.sarthik.kpit.POJO.User;
import garg.sarthik.kpit.Statics.Variables;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etRegEmail;
    private EditText etRegName;
    private EditText etRegMobile;

    private Button btnRegUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Variables.db = FirebaseFirestore.getInstance();

        Variables.colLocations = Variables.db.collection(Database.COL_LOCATION);
        Variables.colAdmins = Variables.db.collection(Database.COL_ADMIN);
        Variables.colUsers = Variables.db.collection(Database.COL_USER);

        Variables.user = null;

        etRegMobile = findViewById(R.id.etRegMobile);
        etRegName = findViewById(R.id.etRegName);
        etRegEmail = findViewById(R.id.etRegEmail);
        btnRegUser = findViewById(R.id.btnRegUser);

        etRegEmail.setEnabled(false);
        etRegName.setEnabled(false);

        etRegName.setText(Variables.firebaseUser.getDisplayName());
        etRegEmail.setText(Variables.firebaseUser.getEmail());

        if (Variables.firebaseUser.getPhoneNumber() != null)
            etRegMobile.setText(Variables.firebaseUser.getPhoneNumber());


        btnRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = etRegMobile.getText().toString().trim().toLowerCase();

                if (mobile.length() != 10) {
                    Toast.makeText(RegistrationActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                    return;
                }

                final User user = new User(Variables.firebaseUser.getDisplayName(), Variables.firebaseUser.getEmail(), mobile);


                Variables.colUsers.document(Variables.firebaseUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Variables.user = user;
                        startActivity(new Intent(RegistrationActivity.this, UserActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, "Error occurred, Please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
