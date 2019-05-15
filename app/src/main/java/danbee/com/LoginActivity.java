package danbee.com;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import danbee.com.DbHelper.AutoLoginDbHelper;
import danbee.com.logindata.LoginResult;
import danbee.com.signupdata.UserIdCheckResult;

public class LoginActivity extends AppCompatActivity {
    Activity activity;
    boolean autoCheck = false;
    private SessionCallback sessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //시작하면 reqestQueue가 만들어짐 Main에 넣기
        if (AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        activity = this;
        //회원가입버튼
        TextView tv_singUp = findViewById(R.id.newMem);
        tv_singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Membership_Activity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼
        Button bt_login = findViewById(R.id.loginBT);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRequest();
            }
        });

        //자동로그인 체크박스
        CheckBox cb_autoLogin = findViewById(R.id.auto_login);
       cb_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   autoCheck = true;
               }else{
                   autoCheck = false;
               }
           }
       });


        //아이디비밀번호찾기버튼
        TextView tv_findUser = findViewById(R.id.findidpw);
        tv_findUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindUserActivity.class);
                startActivity(intent);
            }
        });

        cb_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autoCheck = true;
                } else {
                    autoCheck = false;
                }
            }
        });

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

    }

    void loginRequest() {

        EditText et_id = findViewById(R.id.InputId);
        EditText et_pw = findViewById(R.id.InputPW);
        String id = et_id.getText().toString();
        String pw = et_pw.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (id.equals("") || pw.equals("")) {
            builder.setTitle("빈칸이 존재합니다.")
                    .setPositiveButton("확인", null)
                    .setCancelable(false)
                    .show();
            return;
        }

        String url = "http://3.17.25.223/api/user/login/" + id + "/" + pw;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() { //응답 받음
                    @Override
                    public void onResponse(String response) {
                        loginProcess(response);
                        Log.d("test", "login: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "login: " + error);
                    }
                }
        );
        //자동캐싱잇는경우 이전결과 그대로보여짐
        request.setShouldCache(false);  //새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);
    }

    //로그인버튼클릭시 통신
    void loginProcess(String response) {
        Gson gson = new Gson();
        LoginResult loginResult = gson.fromJson(response, LoginResult.class);

        //로그인 실패
        if (loginResult.result == 404) {
            AlertDialog.Builder adbuilder = new AlertDialog.Builder(this);
            adbuilder.setTitle("아이디 혹은 비밀번호가 다릅니다.")
                    .setPositiveButton("확인", null)
                    .setCancelable(false)
                    .show();
        } else {
            Log.d("test", "LoginDataCount: " + loginResult.data.size());
            //user정보 저장
            String userid = loginResult.data.get(0).userid;
            String phone = loginResult.data.get(0).phone;
            String name = loginResult.data.get(0).name;
            int gender = loginResult.data.get(0).gender;
            String birth = loginResult.data.get(0).birth;
            UserInfo.info.setUserid(userid);
            UserInfo.info.setPhone(phone);
            UserInfo.info.setName(name);
            UserInfo.info.setGender(gender);
            UserInfo.info.setBirth(birth);
            UserInfo.info.setLoginState(true);

            Toast toast = Toast.makeText(this, "반갑습니다. " + userid + " 님", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();

            AutoLoginDbHelper.openDatabase(activity, "auto");
            AutoLoginDbHelper.deleteLog();
            AutoLoginDbHelper.createAutoTable();
            //자동로그인 체크시
            if (autoCheck) {
                AutoLoginDbHelper.insertData(1, userid, phone, name, gender, birth);
            } else {
                AutoLoginDbHelper.insertData(0, "", "", "", 10, "");
            }

            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    // 소셜로그인 처리
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(), "세션이 닫혔습니다. 다시 시도해 주세요: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    String needsScopeAutority = ""; // 정보 제공이 허용되지 않은 항목의 이름을 저장하는 변수

                    // 선택사항 제공동의했는지 체크
                    if (result.getKakaoAccount().needsScopeAccountEmail()) {
                        needsScopeAutority = needsScopeAutority + "이메일";
                    }
                    if (result.getKakaoAccount().needsScopeGender()) {
                        needsScopeAutority = needsScopeAutority + ", 성별";
                    }
                    if (result.getKakaoAccount().needsScopeAgeRange()) {
                        needsScopeAutority = needsScopeAutority + ", 연령대";
                    }
                    if (result.getKakaoAccount().needsScopeBirthday()) {
                        needsScopeAutority = needsScopeAutority + ", 생일";
                    }

                    // 정보 제공이 허용되지 않은 항목이 있다면 -> 허용되지 않은 항목을 안내하고 회원탈퇴 처리
                    if (needsScopeAutority.length() != 0) {
                        if (needsScopeAutority.charAt(0) == ',') {
                            needsScopeAutority = needsScopeAutority.substring(2);
                        }
                        Toast.makeText(getApplicationContext(), needsScopeAutority + "에 대한 권한이 허용되지 않았습니다. 개인정보 제공에 동의해주세요.", Toast.LENGTH_SHORT).show();

                        // 선택항목 제공안하고 가입시 무조건가입됨
                        // 동의안할시 회원탈퇴 -> 가입못하도록막기
                        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                int result = errorResult.getErrorCode();

                                if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
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
                            public void onSuccess(Long result) {
                            }
                        });
                    } else {

                        String name = result.getNickname();
                        String email = result.getKakaoAccount().getEmail();
                        String gender = result.getKakaoAccount().getGender().toString();
                        String birth = result.getKakaoAccount().getBirthday();
                        //String phone = result.getKakaoAccount().getPhoneNumber();

                        UserInfo.info.setName(name);
                        UserInfo.info.setUserid(email);
                        if (gender.equals("male")) {
                            UserInfo.info.setGender(1);
                        } else {
                            UserInfo.info.setGender(0);
                        }
                        UserInfo.info.setBirth(birth);
                        // UserInfo.info.setPhone(phone);
                        UserInfo.info.setLoginState(true);

                        Log.d("test", "name: " + name + " email: " + email + " gender: " + gender + " birth: " + birth);
                        finish();
                    }
                }

            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + e.toString(), Toast.LENGTH_SHORT).show();

        }
    }
}
