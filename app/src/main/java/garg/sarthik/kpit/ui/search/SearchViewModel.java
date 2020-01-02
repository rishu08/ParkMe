package garg.sarthik.kpit.ui.search;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import garg.sarthik.kpit.POJO.Location;
import garg.sarthik.kpit.Statics.Variables;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<Location>> locations;
    private String TAG = "SearchFrag";

    public SearchViewModel() {
        locations = new MutableLiveData<>();

    }

    public LiveData<List<Location>> getLocations() {
        return locations;
    }

    public void searchLocations(String loc){

        String locNext = "" + (char)(loc.charAt(0) + 1);

        Log.e(TAG, "onClick: " + locNext );

        if(Variables.colLocations == null) {
            Log.e(TAG, "searchLocations: \nWHAT THW HECK IS THE FIREBASE!!!" );
            return;
        }
        Variables.colLocations.whereGreaterThanOrEqualTo("name", loc).whereLessThan("name", locNext).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    List<Location> locationList = queryDocumentSnapshots.toObjects(Location.class);
                    locations.setValue(locationList);

                    Log.e("SEARCH", "onSuccess: " + locationList.size());

                } else {
                    Log.e("SEARCH", "locations: EMPTY ");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ",e);
            }
        });

    }
}