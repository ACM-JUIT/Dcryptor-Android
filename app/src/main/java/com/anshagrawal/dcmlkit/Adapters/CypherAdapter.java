package com.anshagrawal.dcmlkit.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anshagrawal.dcmlkit.Activities.DashboardActivity;
import com.anshagrawal.dcmlkit.Models.CypherModel;
import com.anshagrawal.dcmlkit.Models.DecodedTextModel;
import com.anshagrawal.dcmlkit.R;

import java.util.ArrayList;

public class CypherAdapter extends RecyclerView.Adapter<CypherAdapter.ViewHolder> {

    ArrayList<CypherModel> arrayList;



//    ArrayList<DecodedTextModel> arrayList1;
    Context context;
    public CypherAdapter(Context context, ArrayList<CypherModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

//    public CypherAdapter(ArrayList<DecodedTextModel> arrayList1, Context context) {
//
//        this.arrayList1 = arrayList1;
//        this.context = context;
//    }

    public void searchDcryptor(ArrayList<CypherModel> filteredName) {
        this.arrayList = filteredName;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cyphers, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String title = arrayList.get(position).getStringtoDecode();
        final String date = arrayList.get(position).getDecodedAt();
        final String id = arrayList.get(position).get_id();
//        final  String des = arrayList1.get(position).

        holder.title.setText(title);
        holder.Date.setText(date);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,Date,Des;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cTitle);
            Date = itemView.findViewById(R.id.cDate);
            Des = itemView.findViewById(R.id.decodedCipher);
        }
    }
}