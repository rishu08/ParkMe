package garg.sarthik.kpit.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import garg.sarthik.kpit.Constants.POJODetails;
import garg.sarthik.kpit.LocationActivity;
import garg.sarthik.kpit.POJO.Location;
import garg.sarthik.kpit.R;
import garg.sarthik.kpit.Statics.Functions;
import garg.sarthik.kpit.Statics.Variables;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    List<Location> locations;
    Context ctx;

    private String TAG = "ADAPTER";

    public SearchAdapter(Context ctx, List<Location> locations) {

        this.ctx = ctx;
        this.locations = locations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.location_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Location location = locations.get(position);

        Log.e(TAG, "onBindViewHolder: " + location.getName());
        String locName = Functions.toCapitalise(location.getName());
        holder.tvLocation.setText(locName);
        Log.e(TAG, "onBindViewHolder: " + locName);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(ctx, LocationActivity.class);
               intent.putExtra(POJODetails.PARCEL_LOCATION,location);
               ctx.startActivity(intent);
           }
       });


    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }

}
