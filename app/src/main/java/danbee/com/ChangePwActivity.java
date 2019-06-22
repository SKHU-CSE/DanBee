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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import danbee.com.finddata.FinduserData;
import danbee.com.signupdata.UserIdCheckResult;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class ChangePwActivity extends AppCompatActivity {

    EditText pw1;
    EditText pw2;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        activity = this;
        pw1 = findViewById(R.id.change_pw_et_pw);
        pw2 = findViewById(R.id.change_pw_et_checkpw);

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        Button bt_changepw = (Button)findViewById(R.id.change_pw_bt);
        bt_changepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = pw1.getText().toString();
                String s2 = pw2.getText().toString();
                if(s1.equals(s2)) {
                    changePwRequest(s1);
                }
            }
        });
    }

    //비밀번호 변경하기 버튼 눌렀을때 함수
    public void changePwRequest(String pw) {
        String id = getIntent().getStringExtra("id");
        Log.d("test", "change id : "+id);
        Log.d("test", "change pw : "+pw);

        //통신
        String url = "http://3.17.25.223/api/user/change/" + id + "/" +pw;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() { //응답 받음
                    @Override
                    public void onResponse(String response) {
                        changePwProcess(response);
                        Log.d("test", "changepw: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "changepw: " + error);
                    }
                }
        );
        //자동캐싱잇는경우 이전결과 그대로보여짐
        request.setShouldCache(false);  //새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);


    }

    //json처리 결과파싱
    public void changePwProcess(String response) {
        Gson gson = new Gson();
        UserIdCheckResult changeResult = gson.fromJson(response, UserIdCheckResult.class); //비번변경 결과 = useridcheck와 json같아서 사용

        //비밀번호 변경 실패
        if (changeResult.result == 404) {
            final PrettyDialog prettyDialog = new PrettyDialog(activity);
            prettyDialog
                    .setTitle("알림")
                    .setMessage("변경도중 오류가 발생하였습니다.")
                    .setIcon(R.drawable.danbeelogoj)
                    .addButton(
                            "확인",                    // button text
                            R.color.pdlg_color_black,        // button text color
                            R.color.danbeeBomButton1,        // button background color
                            new PrettyDialogCallback() {        // button OnClick listener
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            }
                    )
                    .show();

            /*AlertDialog.Builder adbuilder = new AlertDialog.Builder(this);
            adbuilder.setTitle("변경도중 오류가 발생하였습니다.")
                    .setPositiveButton("확인", null)
                    .setCancelable(false)
                    .show();*/
        } else {
            final PrettyDialog prettyDialog2 = new PrettyDialog(activity);
            prettyDialog2
                    .setTitle("알림")
                    .setMessage("비밀번호 변경이 완료되었습니다.")
                    .setIcon(R.drawable.danbeelogoj)
                    .addButton(
                            "확인",                    // button text
                            R.color.pdlg_color_black,        // button text color
                            R.color.danbeeBomButton1,        // button background color
                            new PrettyDialogCallback() {        // button OnClick listener
                                @Override
                                public void onClick() {
                                    finish();
                                }
                            }
                    )
                    .show();


            /*AlertDialog.Builder adbuilder = new AlertDialog.Builder(activity);
            adbuilder.setTitle("비밀번호 변경이 완료되었습니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();*/

        }

    }
}
