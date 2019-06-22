package danbee.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kakao.auth.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import danbee.com.DbHelper.AutoLoginDbHelper;
import danbee.com.deletedata.DeleteResult;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class MypageActivity extends AppCompatActivity {

    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        //시작하면 reqestQueue가 만들어짐 Main에 넣기
        if (AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());


        TextView name = (TextView) findViewById(R.id.mypageitem_tv_name);
        TextView id = (TextView) findViewById(R.id.mypageitem_tv_id);
        TextView birth = (TextView) findViewById(R.id.mypageitem_tv_birth);
        TextView phone = (TextView) findViewById(R.id.mypageitem_tv_phone);
        ImageView im_changepassword = (ImageView) findViewById(R.id.mypageitem_iv_changepassword);
        TextView deleteaccount = (TextView) findViewById(R.id.mypageitem_tv_deleteaccount);
        CardView card_changepw = findViewById(R.id.mypageitem_cardView_pwchange);

        name.setText(UserInfo.info.getName());
        id.setText(UserInfo.info.getUserid());
        birth.setText(UserInfo.info.getBirth());
        phone.setText(UserInfo.info.getPhone());


        im_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 비밀번호 변경하기 화면으로 넘김
                Intent intent = new Intent(MypageActivity.this, ChangePwActivity.class);
                intent.putExtra("id", UserInfo.info.getUserid());
                startActivity(intent);
            }
        });

        card_changepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ChangePwActivity.class);
                intent.putExtra("id", UserInfo.info.getUserid());
                startActivity(intent);
            }
        });

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 탈퇴하기 다이알로그 뜨고 확인하면 탈퇴완료 or 취소하면 뒤돌아가기
                final PrettyDialog prettyDialog = new PrettyDialog(MypageActivity.this);
                prettyDialog
                        .setTitle("알림")
                        .setMessage("정말 탈퇴하시겠습니까?")
                        .setIcon(R.drawable.danbeelogoj)
                        .addButton(
                                "확인",					// button text
                                R.color.pdlg_color_black,		// button text color
                                R.color.danbeeBomButton1,		// button background color
                                new PrettyDialogCallback() {		// button OnClick listener
                                    @Override
                                    public void onClick() {
                                        userid = UserInfo.info.getUserid();
                                        UserInfo.info.setLoginState(false);
                                        UserInfo.info.setUserid("");
                                        UserInfo.info.setPhone("");
                                        UserInfo.info.setName("");
                                        UserInfo.info.setGender(-1);
                                        UserInfo.info.setBirth("");

                                        //내부디비수정
                                        AutoLoginDbHelper.openDatabase(MypageActivity.this, "auto");
                                        AutoLoginDbHelper.deleteLog();
                                        AutoLoginDbHelper.createAutoTable();
                                        AutoLoginDbHelper.insertData(0, "", "", "", 10, "");

                                        deleteRequest();

                                        prettyDialog.dismiss();
                                    }
                                }
                        )
                        .addButton(
                                "취소",
                                R.color.pdlg_color_black,		// button text color
                                R.color.danbeeBomButton1,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        prettyDialog.dismiss();
                                    }
                                }
                        )
                        .show();

            }
        });
    }

    //서버 통신
    void deleteRequest(){
        String url = "http://3.17.25.223/api/user/delete/"+ userid;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {//응답을 문자열로받아서 넣어달라는뜻
                    @Override
                    public void onResponse(String response) {
                        deleteProcessResponse(response);  // gson변환함수
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
    public void deleteProcessResponse(String response){
        Gson gson = new Gson();
        DeleteResult deleteResult = gson.fromJson(response, DeleteResult.class);

        if(deleteResult.result == 777) {
            UserInfo.info.setLoginState(false);
            UserInfo.info.setUserid("");
            UserInfo.info.setPhone("");
            UserInfo.info.setName("");
            UserInfo.info.setGender(-1);
            UserInfo.info.setBirth("");

            Toast.makeText(getApplicationContext(), "회원탈퇴가 완료 되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "회원탈퇴 도중 오류가 발생하였습니다..", Toast.LENGTH_SHORT).show();
        }
    }

}



