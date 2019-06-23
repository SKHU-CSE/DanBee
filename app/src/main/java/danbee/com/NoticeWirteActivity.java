package danbee.com;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NoticeWirteActivity extends AppCompatActivity {

    Activity activity = this;
    LinearLayout view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_wirte);

        view = findViewById(R.id.noticewrite_progress_view);
        Button bt_ok = findViewById(R.id.noticewrite_bt_ok);
        Button bt_cancel = findViewById(R.id.noticewrite_bt_cancel);

        //시작하면 reqestQueue가 만들어짐 Main에 넣기
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());



        //작성버튼클릭시
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //진행중 표시 레이아웃

                view.setVisibility(View.VISIBLE);
                view.setClickable(false);

                //통신
                writeNoticeSendRequest();
            }
        });

        //취소클릭시
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });


    }

    //작성 서버통신
    void writeNoticeSendRequest(){

        EditText et_title = findViewById(R.id.noticewrite_et_title);
        EditText et_content = findViewById(R.id.noticewrite_et_content);
        final String title = et_title.getText().toString();
        final String content = et_content.getText().toString();

        if(title.equals("") || content.equals("")){
            final PrettyDialog prettyDialog = new PrettyDialog(activity);
            prettyDialog
                    .setTitle("알림")
                    .setMessage("제목과 내용을 작성해주세요.")
                    .setIcon(R.drawable.danbeelogoj)
                    .addButton(
                            "확인",					// button text
                            R.color.pdlg_color_black,		// button text color
                            R.color.danbeeBomButton1,		// button background color
                            new PrettyDialogCallback() {		// button OnClick listener
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            }
                    )
                    .show();

            view.setVisibility(View.GONE);
            return;
        }

       // String url = "http://3.17.25.223/api/notice/insert/"+title+"&"+content;
        String url = "http://3.17.25.223/api/notice/insert";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new PrettyDialog(activity)
                                .setTitle("알림")
                                .setMessage("작성이 완료되었습니다.")
                                .setIcon(R.drawable.danbeelogoj)
                                .addButton(
                                        "확인",					// button text
                                        R.color.pdlg_color_black,		// button text color
                                        R.color.danbeeBomButton1,		// button background color
                                        new PrettyDialogCallback() {		// button OnClick listener
                                            @Override
                                            public void onClick() {
                                                view.setVisibility(View.GONE);
                                                Intent intent = new Intent();
                                                setResult(RESULT_OK, intent);
                                                finish();
                                            }
                                        }
                                )
                                .show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "write notice err: "+error);
                        new PrettyDialog(activity)
                                .setTitle("알림")
                                .setMessage("전송이 실패되었습니다.")
                                .setIcon(R.drawable.danbeelogoj)
                                .addButton(
                                        "확인",					// button text
                                        R.color.pdlg_color_black,		// button text color
                                        R.color.danbeeBomButton1,		// button background color
                                        new PrettyDialogCallback() {		// button OnClick listener
                                            @Override
                                            public void onClick() {
                                                view.setVisibility(View.GONE);
                                                setResult(RESULT_CANCELED);
                                                finish();
                                            }
                                        }
                                )
                                .show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("content", content);

                return params;
            }
        };

        //자동캐싱기능이있음 이전결과가 그대로보여질수도있다.
        request.setShouldCache(false); //이전결과가잇더라도 새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);

    }


}
