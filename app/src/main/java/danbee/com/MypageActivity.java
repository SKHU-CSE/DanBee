package danbee.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        TextView changepassword = (TextView) findViewById(R.id.mypageitem_tv_changepassword);
        TextView deleteaccount = (TextView) findViewById(R.id.mypageitem_tv_deleteaccount);


        name.setText(UserInfo.info.getName());
        id.setText(UserInfo.info.getUserid());
        birth.setText(UserInfo.info.getBirth());
        phone.setText(UserInfo.info.getPhone());


        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 비밀번호 변경하기 화면으로 넘김
                Intent intent = new Intent(MypageActivity.this, ChangePwActivity.class);
                intent.putExtra("id", UserInfo.info.getUserid());
                startActivity(intent);
            }
        });

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 탈퇴하기 다이알로그 뜨고 확인하면 탈퇴완료 or 취소하면 뒤돌아가기
                AlertDialog.Builder adbuilder = new AlertDialog.Builder(MypageActivity.this);
                adbuilder.setTitle("알림")
                        .setMessage("정말 탈퇴하시겠습니까?")
                        .setCancelable(false)
                        .setIcon(R.drawable.danbeelogoj)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userid = UserInfo.info.getUserid();

                                //내부디비수정
                                AutoLoginDbHelper.openDatabase(MypageActivity.this, "auto");
                                AutoLoginDbHelper.deleteLog();
                                AutoLoginDbHelper.createAutoTable();
                                AutoLoginDbHelper.insertData(0, "", "", "", 10, "");

                                deleteRequest();
                                // 회원탈퇴 처리
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onSuccess(Long result) { }
                                });

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 다이알로그만 없어지게하기(그냥냅두면 될듯), finish는 액티비티가 종료라 메인화면으로 나가짐
                            }
                        })
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



