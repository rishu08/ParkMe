package garg.sarthik.kpit.ui.history;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import garg.sarthik.kpit.Constants.Database;
import garg.sarthik.kpit.POJO.Order;
import garg.sarthik.kpit.Statics.Variables;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<Order>> history;
    private String TAG = "historyVM";

    public HistoryViewModel() {
        history = new MutableLiveData<>();

    }

    public LiveData<List<Order>> getHistory(String userId) {

        Variables.colUsers.document(userId).collection(Database.COL_HISTORY).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                history.setValue(queryDocumentSnapshots.toObjects(Order.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });

        return history;
    }
}