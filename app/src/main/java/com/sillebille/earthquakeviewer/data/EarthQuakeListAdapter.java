package com.sillebille.earthquakeviewer.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sillebille.earthquakeviewer.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EarthQuakeListAdapter extends RecyclerView.Adapter<EarthQuakeListAdapter.CustomRecyclerView> {

    private List<EarthquakesModel.EarthQuake> itemList;
    private Context mContext;

    public EarthQuakeListAdapter(Context context, List<EarthquakesModel.EarthQuake> itemList) {
        this.itemList = itemList;
        this.mContext = context;
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

        if (myData.magnitude >= 8) {
            holder.layoutItem.setBackgroundColor(mContext.getColor(R.color.warning));
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public static class CustomRecyclerView extends RecyclerView.ViewHolder {
        TextView txtEqid;
        TextView txtMagnitue;
        TextView txtDateTime;
        CardView layoutItem;

        CustomRecyclerView(View itemView) {
            super(itemView);
            txtEqid = itemView.findViewById(R.id.txt_eqid);
            txtMagnitue = itemView.findViewById(R.id.txt_magnitude);
            txtDateTime = itemView.findViewById(R.id.txt_dateTime);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }
    }


}
