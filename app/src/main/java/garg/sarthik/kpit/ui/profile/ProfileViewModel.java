package garg.sarthik.kpit.ui.profile;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import garg.sarthik.kpit.POJO.User;
import garg.sarthik.kpit.Statics.Variables;

public class ProfileViewModel extends ViewModel {

    private String TAG = "ProfileVM";
    private MutableLiveData<User> mUser;

    public ProfileViewModel() {
        mUser = new MutableLiveData<>();
        mUser.setValue(Variables.user);
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    public void EditUserData(String mobileNo){

        final User user = new User(Variables.firebaseUser.getDisplayName(), Variables.firebaseUser.getEmail(), mobileNo);

        Variables.colUsers.document(Variables.firebaseUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Variables.user = user;
                mUser.setValue(user);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ",e );
            }
        });
    }
}