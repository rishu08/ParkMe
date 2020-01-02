package garg.sarthik.kpit.ui.profile;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import garg.sarthik.kpit.POJO.User;
import garg.sarthik.kpit.R;

public class ProfileFragment extends Fragment {

    private String TAG = "ProfileFrag";
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView tvProfileName = root.findViewById(R.id.tvProfileName);
        final TextView tvProfileEmail = root.findViewById(R.id.tvProfileEmail);
        final EditText etProfileMobile = root.findViewById(R.id.etProfileMobile);
        Button btnlogout = root.findViewById(R.id.btnlogout);


        final FloatingActionButton fabEditProfile = root.findViewById(R.id.fabEditProfile);

        profileViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                tvProfileEmail.setText(user.getEmailId());
                etProfileMobile.setText(user.getMobileNo());
                tvProfileName.setText(user.getName().toUpperCase());
            }
        });

        etProfileMobile.setEnabled(false);
        final Drawable originalDrawable = etProfileMobile.getBackground();
        etProfileMobile.setBackgroundColor(Color.TRANSPARENT);

        fabEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etProfileMobile.isEnabled()) {
                    Log.e(TAG, "onClick: " + etProfileMobile.isEnabled());
                    String mobile = etProfileMobile.getText().toString().trim();

                    if (mobile.length() != 10) {
                        Toast.makeText(getContext(), "Invalid Data", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    profileViewModel.EditUserData(mobile);

                    etProfileMobile.setEnabled(false);
                    etProfileMobile.setBackgroundColor(Color.TRANSPARENT);
                    fabEditProfile.setImageResource(R.drawable.ic_edit_white);
                    Log.e(TAG, "onClick: \nHere I'm");

                } else {

                    Log.e(TAG, "onClick: " + etProfileMobile.isEnabled());
                    etProfileMobile.setEnabled(true);
                    etProfileMobile.setBackground(originalDrawable);
                    fabEditProfile.setImageResource(R.drawable.ic_done_white);
                }
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(getContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                Toast.makeText(getContext(), "Sucessfully Logout", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return root;
    }


}