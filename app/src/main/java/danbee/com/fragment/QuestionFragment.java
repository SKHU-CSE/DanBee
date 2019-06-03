package danbee.com.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.cachapa.expandablelayout.ExpandableLayout;

import danbee.com.R;


public class QuestionFragment extends Fragment {
    ExpandableLayout expandview;
    ImageView arrow;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_question, container, false);
        expandview = rootView.findViewById(R.id.question_fragment_expandable_layout);
        arrow = rootView.findViewById(R.id.question_fragment_down);

        rootView.findViewById(R.id.question_fragment_expand_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandview.isExpanded()){
                    expandview.collapse();
                    arrow.setImageResource(R.drawable.downarrow);
                }else {
                    expandview.expand();
                    arrow.setImageResource(R.drawable.uparrow);
                }
            }
        });





        return rootView;
    }
}
