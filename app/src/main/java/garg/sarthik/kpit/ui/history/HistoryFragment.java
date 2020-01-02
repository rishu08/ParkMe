package garg.sarthik.kpit.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import garg.sarthik.kpit.Adapters.HistoryAdapter;
import garg.sarthik.kpit.POJO.Order;
import garg.sarthik.kpit.R;
import garg.sarthik.kpit.Statics.Variables;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;

    private RecyclerView rvHistory;
    private HistoryAdapter historyAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_history, container, false);

        rvHistory = root.findViewById(R.id.rvHistory);

        historyViewModel.getHistory(Variables.firebaseUser.getUid()).observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> history) {
                rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
                historyAdapter = new HistoryAdapter(history, getContext());
                rvHistory.setAdapter(historyAdapter);
            }
        });

        return root;
    }
}