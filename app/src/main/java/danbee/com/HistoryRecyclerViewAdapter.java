package danbee.com;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>  {

    Activity context;
    HistoryItem item;
    static ArrayList<HistoryItem> items; //데이터


    public HistoryRecyclerViewAdapter (Activity context,ArrayList<HistoryItem> items){
        this.context = context;
        this.items=items;
    }

    @NonNull
    @Override
    public HistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = context.getLayoutInflater().inflate(R.layout.history_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        item = items.get(i);  //position
        viewHolder.setItem(item); //데이터 세팅
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_kickboard;
        TextView tv_date;
        TextView tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.history_tv_date);
            tv_time = itemView.findViewById(R.id.history_tv_time);
            tv_kickboard = itemView.findViewById(R.id.history_tv_kickboard);

        }

        public void setItem(HistoryItem item) {
            tv_kickboard.setText(""+item.getKickId());
            tv_date.setText(item.getDate());
            tv_time.setText(item.getTime());
        }

        public HistoryItem getItem(int pos){
            return items.get(pos);
        }
    }
}
