package com.example.abhishek.notezero.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abhishek.notezero.Model.ItemData;
import com.example.abhishek.notezero.Model.ListViewData;
import com.example.abhishek.notezero.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<ListViewData> folderDataList;

    public RecyclerViewAdapter(ArrayList<ListViewData> folderDataList){
        this.folderDataList = folderDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ListViewData listViewData = folderDataList.get(position);
        holder.itemName.setText(listViewData.getTitle());
    }

    @Override
    public int getItemCount() {
        return folderDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.list_item_name_textview_id);

        }
    }
}
