package com.anshagrawal.dcmlkit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anshagrawal.dcmlkit.Models.CypherModel;
import com.anshagrawal.dcmlkit.Models.ResultModel;
import com.anshagrawal.dcmlkit.R;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    ArrayList<ResultModel> arrayList;
    Context context;

    public ResultAdapter(Context context, ArrayList<ResultModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cyphers_decoded, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String[] Title = arrayList.get(position).getDecoded_Text();

        holder.cTitle.setText(Title.toString());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cTitle = itemView.findViewById(R.id.cTitle);
        }
    }


}
