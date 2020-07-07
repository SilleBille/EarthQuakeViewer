package com.sillebille.earthquakeviewer.data;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sillebille.earthquakeviewer.MainActivity;
import com.sillebille.earthquakeviewer.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class EarthQuakeListAdapter extends RecyclerView.Adapter<EarthQuakeListAdapter.CustomRecyclerView> {
    private final String TAG_NAME = EarthQuakeListAdapter.class.getName();
    private List<EarthquakesModel.EarthQuake> itemList;
    private Context mContext;
    private OnEarthQuakeClickListener mListener;

    public EarthQuakeListAdapter(Context context, List<EarthquakesModel.EarthQuake> itemList, OnEarthQuakeClickListener listener) {
        this.itemList = itemList;
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CustomRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new CustomRecyclerView(layoutView, mListener);
    }

    @Override
    public void onBindViewHolder(CustomRecyclerView holder, int position) {
        final EarthquakesModel.EarthQuake myData = itemList.get(position);
        holder.txtEqid.setText(myData.equivalentId);
        holder.txtMagnitue.setText(String.valueOf(myData.magnitude));
        holder.txtDateTime.setText(myData.dateTime);

        if (myData.magnitude >= 8) {
            holder.layoutItem.setBackgroundColor(mContext.getColor(R.color.warning));
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public interface OnEarthQuakeClickListener {
        void onItemClick(int position);
    }

    public static class CustomRecyclerView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtEqid;
        TextView txtMagnitue;
        TextView txtDateTime;
        CardView layoutItem;
        OnEarthQuakeClickListener onEarthQuakeClickListener;

        CustomRecyclerView(View itemView, OnEarthQuakeClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onEarthQuakeClickListener = listener;
            txtEqid = itemView.findViewById(R.id.txt_eqid);
            txtMagnitue = itemView.findViewById(R.id.txt_magnitude);
            txtDateTime = itemView.findViewById(R.id.txt_dateTime);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }

        @Override
        public void onClick(View view) {
            onEarthQuakeClickListener.onItemClick(getAdapterPosition());
        }
    }
}
