package danbee.com;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import danbee.com.fragment.FindIdFragment;
import danbee.com.fragment.FindPwFragment;
import danbee.com.fragment.NoticeFragment;
import danbee.com.fragment.QuestionFragment;
import segmented_control.widget.custom.android.com.segmentedcontrol.SegmentedControl;
import segmented_control.widget.custom.android.com.segmentedcontrol.item_row_column.SegmentViewHolder;
import segmented_control.widget.custom.android.com.segmentedcontrol.listeners.OnSegmentClickListener;


public class NoticeQuestionActivity extends AppCompatActivity {

    final int WirteSuccessCode = 777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_question);

        final NoticeFragment noticeFragment = new NoticeFragment();
        final QuestionFragment questionFragment = new QuestionFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.noticeQuestion_frameLayout, noticeFragment).commit();


        //segment 클릭리스터
        final SegmentedControl segment = findViewById(R.id.segmented_control);
        segment.setSelectedSegment(0);
        segment.addOnSegmentClickListener(new OnSegmentClickListener() {
            @Override
            public void onSegmentClick(SegmentViewHolder segmentViewHolder) {
                int pos = segmentViewHolder.getAbsolutePosition();

                if(pos==0){
                    getSupportFragmentManager().beginTransaction().replace(R.id.noticeQuestion_frameLayout,noticeFragment).commit();
                }
                else
                {
                    noticeFragment.isPageShow = false;
                    getSupportFragmentManager().beginTransaction().replace(R.id.noticeQuestion_frameLayout,questionFragment).commit();
                }

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == WirteSuccessCode) {
            Log.d("test", String.valueOf(requestCode));
            Log.d("test", String.valueOf(resultCode));
            if (resultCode == RESULT_OK) {  //글작성한경우만 리스트최신화
                //sendRequest();
            }
        }
    }
}
