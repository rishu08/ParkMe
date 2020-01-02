package garg.sarthik.kpit.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import garg.sarthik.kpit.Constants.POJODetails;
import garg.sarthik.kpit.OrderDetailActivity;
import garg.sarthik.kpit.POJO.Order;
import garg.sarthik.kpit.R;
import garg.sarthik.kpit.Statics.Functions;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private String TAG = "HistoryAdapter";
    private List<Order> history;
    private Context ctx;

    public HistoryAdapter(List<Order> history, Context ctx) {
        this.history = history;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Order order = history.get(position);

        Log.e(TAG, "onBindViewHolder: " + order.getLocation().getName());

        holder.tvOrderName.setText(Functions.toCapitalise(order.getLocation().getName()));
        holder.tvOrderId.setText(order.getOrderId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, OrderDetailActivity.class);
                intent.putExtra(POJODetails.PARCEL_ORDER, order);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOrderName;
        public TextView tvOrderId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderName = itemView.findViewById(R.id.tvOrderName);
        }
    }
}
