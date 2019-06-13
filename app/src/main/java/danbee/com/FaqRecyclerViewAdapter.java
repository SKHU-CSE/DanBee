package danbee.com;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class FaqRecyclerViewAdapter extends RecyclerView.Adapter<FaqRecyclerViewAdapter.ViewHolder>  {

    ArrayList<FaqItem> items;
    Activity context;
    FaqItem item;
    public FaqRecyclerViewAdapter(Activity context, ArrayList<FaqItem> items){
        this.context=context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View itemView = context.getLayoutInflater().inflate(R.layout.faq_item,viewGroup,false);
       return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        item = items.get(i);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /////////////////////////////////////////////////////////
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_userid;
        TextView tv_content;
        ImageView img_updown;
        ExpandableLayout expandableLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.question_item_expand_tv_title);
            tv_userid=itemView.findViewById(R.id.question_item_expand_tv_userid);
            tv_content= itemView.findViewById(R.id.question_item_expand_tv_content);
            img_updown = itemView.findViewById(R.id.question_item_img_updown);

            expandableLayout = itemView.findViewById(R.id.question_item_expandable_layout);


            tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableLayout.isExpanded()){
                        expandableLayout.collapse();
                        img_updown.setImageResource(R.drawable.downarrow);
                    }else{
                        expandableLayout.expand();
                        img_updown.setImageResource(R.drawable.uparrow);
                    }
                }
            });

            img_updown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableLayout.isExpanded()){
                        expandableLayout.collapse();
                        img_updown.setImageResource(R.drawable.downarrow);
                    }else{
                        expandableLayout.expand();
                        img_updown.setImageResource(R.drawable.uparrow);
                    }
                }
            });

        }

        public void setItem(FaqItem item){
            tv_title.setText(item.getTitle());
            tv_userid.setText("작성자: "+item.getUserid());
            tv_content.setText(item.getContent());
        }


    }
}
