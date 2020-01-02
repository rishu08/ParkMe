package garg.sarthik.kpit.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import garg.sarthik.kpit.Adapters.SearchAdapter;
import garg.sarthik.kpit.POJO.Location;
import garg.sarthik.kpit.R;

public class SearchFragment extends Fragment {

    RecyclerView searchRV;
    SearchAdapter searchAdapter;
    android.widget.SearchView svSearchLocation;

    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        svSearchLocation = root.findViewById(R.id.svSearchLocation);
        searchRV = root.findViewById(R.id.searchRV);

        svSearchLocation.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String loc = query.trim().toLowerCase();
                if (!loc.isEmpty()) {
                    searchViewModel.searchLocations(loc);
                } else {
                    Toast.makeText(getContext(), "You have to search for something man!!!", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchViewModel.getLocations().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {

                searchRV.setLayoutManager(new LinearLayoutManager(getContext()));
                searchAdapter = new SearchAdapter(getContext(), locations);
                searchRV.setAdapter(searchAdapter);
            }
        });

        return root;
    }
}