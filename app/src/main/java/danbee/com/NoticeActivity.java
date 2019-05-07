package danbee.com;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;

import danbee.com.noticedata.NoticeData;
import danbee.com.noticedata.NoticeResult;

public class NoticeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RelativeLayout detailView;
    Animation translateLeft;
    Animation translateRight;
    final int WirteSuccessCode = 777;
    ArrayList<NoticeItem> items = new ArrayList<NoticeItem>();
    boolean isPageShow = false;
    NoticeRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        set();

        //시작하면 reqestQueue가 만들어짐 Main에 넣기
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        sendRequest(); //통신

        recyclerView = findViewById(R.id.notice_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //컨텍스트, 방향, 아이템보이는방향(위->아래, 아래->위)
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NoticeRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        //카드뷰클릭시 이벤트
        adapter.setOnItemClickListener(new NoticeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoticeRecyclerViewAdapter.ViewHolder holder, View view, int pos) {
                TextView tv_detail_title = detailView.findViewById(R.id.notice_detail_title);
                TextView tv_detail_content = detailView.findViewById(R.id.notice_detail_content);
                TextView tv_detail_date = detailView.findViewById(R.id.notice_detail_date);

                //클릭된 아이템가져옴
                NoticeItem item = holder.getItem(pos);
                tv_detail_title.setText(item.getTitle());
                tv_detail_content.setText(item.getContent());
                tv_detail_date.setText(item.getDate());

                if(!isPageShow) { //반복호출 금지 트리거
                    detailView.setClickable(true);
                    detailView.setVisibility(View.VISIBLE);
                    detailView.startAnimation(translateLeft);
                }
            }
        });

        //뷰클릭시 닫힘
        detailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageShow) {
                    detailView.setClickable(false);
                    v.startAnimation(translateRight);
                }
            }
        });

        Button bt_write = findViewById(R.id.noticewrite_bt_write);
        bt_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), NoticeWirte.class);
                startActivityForResult(intent, WirteSuccessCode);
            }
        });


    }

    //초기 설정
    void set(){
        detailView = findViewById(R.id.notice_detail_view);  //슬라이드될 뷰
        //애니메이션 설정
        translateLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.notice_translate_left);
        translateRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.notice_translate_right);

        SlideNoticeDetailView listener = new SlideNoticeDetailView();
        translateRight.setAnimationListener(listener);
        translateLeft.setAnimationListener(listener);

    }

    class SlideNoticeDetailView implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isPageShow) {  //slide닫는 에니메이션후
                detailView.setVisibility(View.GONE);
                isPageShow = false;
            }else {
                isPageShow = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    //서버 통신
    void sendRequest(){
        String url = "http://3.17.25.223/api/notice/list";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {//응답을 문자열로받아서 넣어달라는뜻
                    @Override
                    public void onResponse(String response) {
                        noticeProcessResponse(response);  // gson변환함수
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
    public void noticeProcessResponse(String response){
        Gson gson = new Gson();
        NoticeResult noticeResult = gson.fromJson(response, NoticeResult.class);

        if(noticeResult.result == 777){
            int datasize = noticeResult.data.size();
            Log.d("test", "Notice count: "+datasize);

            for(int i=0; i<datasize; i++){
                String title = noticeResult.data.get(i).title;
                String content = noticeResult.data.get(i).content;
                String date = noticeResult.data.get(i).time;
                items.add(new NoticeItem(title,content,date));
            }

            adapter.setItems(items);  //변경된 리스트로반영
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == WirteSuccessCode) {
            if (resultCode == RESULT_OK) {  //글작성한경우만 리스트최신화
                sendRequest();
            }
        }
    }
}