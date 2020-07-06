package com.sillebille.earthquakeviewer.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.sillebille.earthquakeviewer.R;
import com.sillebille.earthquakeviewer.network.FetchResultQueue;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EarthQuakeListAdapter extends RecyclerView.Adapter<EarthQuakeListAdapter.CustomRecyclerView> {

    private List<EarthquakesModel.EarthQuake> itemList;

    private RequestQueue mRequestQueue;

    public EarthQuakeListAdapter(Context context, List<EarthquakesModel.EarthQuake> itemList) {
        this.itemList = itemList;
        mRequestQueue = FetchResultQueue.getInstance(context).getRequestQueue();
    }

    @NonNull
    @Override
    public CustomRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new CustomRecyclerView(layoutView);
    }

    @Override
    public void onBindViewHolder(CustomRecyclerView holder, int position) {

        EarthquakesModel.EarthQuake myData = itemList.get(position);
        holder.txtEqid.setText(myData.equivalentId);
        holder.txtMagnitue.setText(String.valueOf(myData.magnitude));
        holder.txtDateTime.setText(myData.dateTime);


    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public static class CustomRecyclerView extends RecyclerView.ViewHolder {
        TextView txtEqid;
        TextView txtMagnitue;
        TextView txtDateTime;

        CustomRecyclerView(View itemView) {
            super(itemView);
            txtEqid = itemView.findViewById(R.id.txt_eqid);
            txtMagnitue = itemView.findViewById(R.id.txt_magnitude);
            txtDateTime = itemView.findViewById(R.id.txt_dateTime);
        }
    }


}
