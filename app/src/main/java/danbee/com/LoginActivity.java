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

import danbee.com.DbHelper.AutoLoginDbHelper;
import danbee.com.logindata.LoginResult;
import danbee.com.signupdata.UserIdCheckResult;

public class LoginActivity extends AppCompatActivity {
    Activity activity;
    boolean autoCheck = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //시작하면 reqestQueue가 만들어짐 Main에 넣기
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        activity=this;
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
    }

    void loginRequest(){

        EditText et_id = findViewById(R.id.InputId);
        EditText et_pw = findViewById(R.id.InputPW);
        String id = et_id.getText().toString();
        String pw = et_pw.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(id.equals("") || pw.equals("")) {
            builder.setTitle("빈칸이 존재합니다.")
                    .setPositiveButton("확인", null)
                    .setCancelable(false)
                    .show();
            return;
        }

        String url = "http://3.17.25.223/api/user/login/"+id+"/"+pw;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() { //응답 받음
                    @Override
                    public void onResponse(String response) {
                        loginProcess(response);
                        Log.d("test", "login: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "login: "+error);
                    }
                }
        );
        //자동캐싱잇는경우 이전결과 그대로보여짐
        request.setShouldCache(false);  //새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);
    }

    //로그인버튼클릭시 통신
    void loginProcess(String response){
        Gson gson = new Gson();
        LoginResult loginResult = gson.fromJson(response, LoginResult.class);

        //로그인 실패
        if(loginResult.result == 404) {
            AlertDialog.Builder adbuilder = new AlertDialog.Builder(this);
            adbuilder.setTitle("아이디 혹은 비밀번호가 다릅니다.")
                    .setPositiveButton("확인", null)
                    .setCancelable(false)
                    .show();
        }else {
            Log.d("test", "LoginDataCount: "+loginResult.data.size());
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

            Toast toast = Toast.makeText(this, "반갑습니다. "+userid+" 님", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();

            AutoLoginDbHelper.openDatabase(activity, "auto");
            AutoLoginDbHelper.deleteLog();
            AutoLoginDbHelper.createAutoTable();
            //자동로그인 체크시
            if(autoCheck) {
                AutoLoginDbHelper.insertData(1, userid, phone, name, gender, birth);
            }else{
                AutoLoginDbHelper.insertData(0, "", "", "", 10, "");
            }

            finish();
        }

    }
}
