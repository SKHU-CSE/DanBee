package danbee.com;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.ViewHolder> {

    Activity context;
    NoticeItem item;
    static ArrayList<NoticeItem> items = new ArrayList<NoticeItem>(); //데이터
    OnItemClickListener listener;

    public NoticeRecyclerViewAdapter (Activity context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= context.getLayoutInflater().inflate(R.layout.notice_item,viewGroup,false);
        //layout 디자인 적용
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        item = items.get(i);  //position
        viewHolder.setItem(item); //데이터 세팅

        viewHolder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //데이터추가
    public void addItem(NoticeItem item){
        items.add(item);
    }

    //데이터수정
    public void setItems(ArrayList<NoticeItem> items){
        this.items = items;
    }

    //해당데이터반환
    public NoticeItem getItem(int position){
        return items.get(position);
    }

    //listener
    public interface OnItemClickListener{
        void onItemClick(ViewHolder holder, View view,  int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    ///////////////////////////////////////////////////////
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
      //  TextView tv_content;
        TextView tv_date;
        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.notice_title);
           // tv_content = itemView.findViewById(R.id.notice_content);
            tv_date = itemView.findViewById(R.id.notice_date);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, pos);
                    }
                }
            });
        }

        public void setItem(NoticeItem item){
            //text수정
            tv_title.setText(item.getTitle());
           // tv_content.setText(item.getContent());
            tv_date.setText(item.getDate());
        }

        public NoticeItem getItem(int pos){
            return items.get(pos);
        }

        public void setOnItemClickListener(OnItemClickListener listener){  //사용자정의 이벤트처리
            this.listener = listener;
        }
    }
}
