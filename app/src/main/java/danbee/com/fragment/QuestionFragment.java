package danbee.com.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import danbee.com.AppHelper;
import danbee.com.FaqItem;
import danbee.com.FaqRecyclerViewAdapter;
import danbee.com.R;
import danbee.com.questiondata.QuestionResult;


public class QuestionFragment extends Fragment {
    ExpandableLayout expandview;
    ImageView arrow;
    RecyclerView recyclerView;
    Context context;
    FaqRecyclerViewAdapter adapter;
    ArrayList<FaqItem> items = new ArrayList<FaqItem>();
    ProgressBar progressBar;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        //시작하면 reqestQueue가 만들어짐 Main에 넣기
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_question, container, false);
        expandview = rootView.findViewById(R.id.question_item_expandable_layout);
        arrow = rootView.findViewById(R.id.question_item_img_updown);
/*
        rootView.findViewById(R.id.question_item_expand_tv_title).setOnClickListener(new View.OnClickListener() {
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
*/

        //리사이클러뷰
        recyclerView = rootView.findViewById(R.id.question_fragment_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new FaqRecyclerViewAdapter((Activity)context, items);
        recyclerView.setAdapter(adapter);

        progressBar = rootView.findViewById(R.id.question_fragment_progressBar);

        sendRequest();

        return rootView;
    }


    //서버 통신
    void sendRequest(){
        items.clear();
        progressBar.setVisibility(View.VISIBLE);
        String url = "http://3.17.25.223/api/question/list";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {//응답을 문자열로받아서 넣어달라는뜻
                    @Override
                    public void onResponse(String response) {
                        questionProcessResponse(response);  // gson변환함수
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "volley err: "+error);
                    }
                }
        );
        //자동캐싱기능이있음 이전결과가 그대로보여질수도있다.
        request.setShouldCache(false); //이전결과가잇더라도 새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);

    }

    //json 파싱
    public void questionProcessResponse(String response){
        Gson gson = new Gson();
        QuestionResult questionResult = gson.fromJson(response, QuestionResult.class);

        if(questionResult.result == 777){
            int datasize = questionResult.data.size();
            Log.d("test", "Question count: "+datasize);

            for(int i=datasize-1; i>=0; i--){
                String title = questionResult.data.get(i).title;
                String userid = questionResult.data.get(i).userid;
                String content = questionResult.data.get(i).content;
                items.add(new FaqItem(userid,title,content));

            }
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}
