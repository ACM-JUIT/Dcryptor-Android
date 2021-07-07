package com.anshagrawal.dcmlkit.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anshagrawal.dcmlkit.Activities.DashboardActivity;
import com.anshagrawal.dcmlkit.Models.Dcryptor;
import com.anshagrawal.dcmlkit.R;

import java.util.List;

public class CypherAdapter extends RecyclerView.Adapter<CypherAdapter.cypherViewHolder> {
    DashboardActivity dashboardActivity;
    List<Dcryptor> dcryptors;
    public CypherAdapter(DashboardActivity dashboardActivity, List<Dcryptor> dcryptors) {
        this.dashboardActivity = dashboardActivity;
        this.dcryptors = dcryptors;
    }

    @NonNull
    @Override
    public cypherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new cypherViewHolder(LayoutInflater.from(dashboardActivity).inflate(R.layout.item_cyphers,parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull cypherViewHolder holder, int position) {

        holder.title.setText(dcryptors.get(position).cypherTitle);
//        holder.decodedcipher.setText(dcryptors.get(dcryptors.size()));
        holder.date.setText(dcryptors.get(position).cypherDate);
    }

    @Override
    public int getItemCount() {
        return dcryptors.size();
    }

    class cypherViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, decodedcipher;
        public cypherViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.cTitle);
            date = itemView.findViewById(R.id.cDate);
            decodedcipher=itemView.findViewById(R.id.decodedCipher);
        }
    }
}
