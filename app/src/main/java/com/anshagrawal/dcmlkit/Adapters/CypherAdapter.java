package com.anshagrawal.dcmlkit.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anshagrawal.dcmlkit.Activities.DecodeActivity;
import com.anshagrawal.dcmlkit.Activities.MainActivity;
import com.anshagrawal.dcmlkit.Models.CypherModel;
import com.anshagrawal.dcmlkit.R;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CypherAdapter extends RecyclerView.Adapter<CypherAdapter.ViewHolder> {

    ArrayList<CypherModel> arrayList;
    Context context;

    public CypherAdapter(Context context, ArrayList<CypherModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;

    }


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

        String s = date;

        s = s.substring(s.indexOf("T") + 1);
        s = s.substring(0, s.indexOf("."));

        holder.title.setText(title);
        holder.Date.setText(date);
        holder.time.setText(s);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DecodeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("textToDecode", title);
                bundle.putBoolean("toStore", false);
                bundle.putBoolean("method", false);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, Date, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cTitle);
            Date = itemView.findViewById(R.id.cDate);
            time = itemView.findViewById(R.id.cTime);

        }
    }
}