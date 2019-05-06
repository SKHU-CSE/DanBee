package danbee.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NoticeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RelativeLayout detailView;
    Animation translateLeft;
    Animation translateRight;

    boolean isPageShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        set();

        recyclerView = findViewById(R.id.notice_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //컨텍스트, 방향, 아이템보이는방향(위->아래, 아래->위)
        recyclerView.setLayoutManager(layoutManager);

        final NoticeRecyclerViewAdapter adapter = new NoticeRecyclerViewAdapter(this);

        adapter.addItem(new NoticeItem("제목","내용","2019-5-5"));
        adapter.addItem(new NoticeItem("제목1","내용1","2019-5-5"));
        adapter.addItem(new NoticeItem("제목2","내용2","2019-5-5"));
        recyclerView.setAdapter(adapter);

        //카드뷰클릭시 이벤트
        adapter.setOnItemClickListener(new NoticeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoticeRecyclerViewAdapter.ViewHolder holder, View view, int pos) {
                TextView tv_detail_title = detailView.findViewById(R.id.notice_detail_title);
                TextView tv_detail_content = detailView.findViewById(R.id.notice_detail_content);
                TextView tv_detail_date = detailView.findViewById(R.id.notice_detail_date);

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
            }else if(!isPageShow){
                isPageShow = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
