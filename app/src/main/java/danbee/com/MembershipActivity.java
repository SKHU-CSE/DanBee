package danbee.com;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import danbee.com.signupdata.UserIdCheckResult;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class MembershipActivity extends AppCompatActivity {

    Activity activity = this;
    int gender = -1;
    boolean checkidBtClick = false;
    EditText et;
    Button signUpBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_);

        et = findViewById(R.id.membership_et_ID);

        //시작하면 reqestQueue가 만들어짐 Main에 넣기
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        //아이디 중복체크버튼
        Button checkIdBt = findViewById(R.id.membership_bt_checkID);
        checkIdBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et.getText().toString().equals(""))
                    checkIdRequest();
                else{
                    final PrettyDialog prettyDialog = new PrettyDialog(activity);
                    prettyDialog
                            .setTitle("알림")
                            .setMessage("아이디를 입력해주세요.")
                            .setIcon(R.drawable.danbeelogoj)
                            .addButton(
                                    "확인",					// button text
                                    R.color.pdlg_color_black,		// button text color
                                    R.color.pdlg_color_yellow,		// button background color
                                    new PrettyDialogCallback() {		// button OnClick listener
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                        }
                                    }
                            )
                            .show();

                    /*AlertDialog.Builder adbuilder = new AlertDialog.Builder(activity);
                    adbuilder.setTitle("아이디를 입력해주세요")
                            .setPositiveButton("확인", null)
                            .setCancelable(false)
                            .show();*/
                }
            }
        });

        //가입하기버튼
        signUpBt = findViewById(R.id.membership_bt_SignUp);
        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpRequest();
            }
        });

        //성별 라디오버튼리스너
        RadioGroup rg = findViewById(R.id.membership_rg_gender);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.membership_rb_female:
                        gender = 0;
                        break;
                    case R.id.membership_rb_male:
                        gender = 1;
                        break;
                    default:
                        gender = -1;
                        break;
                }
            }
        });


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkidBtClick = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void checkIdRequest(){

        String userId = et.getText().toString();

        String url = "http://3.17.25.223/api/user/find/"+userId;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() { //응답 받음
                    @Override
                    public void onResponse(String response) {
                        checkIdProcess(response);
                        Log.d("test", "checkid: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "checkid: "+error);
                    }
                }
        );
        //자동캐싱잇는경우 이전결과 그대로보여짐
        request.setShouldCache(false);  //새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);
    }

    //아이디 중복검사 응답시실행
    void checkIdProcess(String response){
        Gson gson = new Gson();
        UserIdCheckResult userIdCheckResult = gson.fromJson(response, UserIdCheckResult.class);

        if(userIdCheckResult.result == 404){ //id가 존재안함
            checkidBtClick = true;
            final PrettyDialog prettyDialog2 = new PrettyDialog(this);
            prettyDialog2
                    .setTitle("알림")
                    .setMessage("사용 가능한 아이디입니다.")
                    .setIcon(R.drawable.danbeelogoj)
                    .addButton(
                            "확인",					// button text
                            R.color.pdlg_color_black,		// button text color
                            R.color.pdlg_color_yellow,		// button background color
                            new PrettyDialogCallback() {		// button OnClick listener
                                @Override
                                public void onClick() {
                                    prettyDialog2.dismiss();
                                }
                            }
                    )
                    .show();

            /*AlertDialog.Builder adbuilder = new AlertDialog.Builder(this);
            adbuilder.setTitle("사용 가능한 아이디입니다.")
                    .setPositiveButton("확인", null)
                    .setCancelable(false)
                    .show();*/
        }else{
            final PrettyDialog prettyDialog3 = new PrettyDialog(this);
            prettyDialog3
                    .setTitle("알림")
                    .setMessage("아이디가 이미 존재합니다.")
                    .setIcon(R.drawable.danbeelogoj)
                    .addButton(
                            "확인",					// button text
                            R.color.pdlg_color_black,		// button text color
                            R.color.pdlg_color_yellow,		// button background color
                            new PrettyDialogCallback() {		// button OnClick listener
                                @Override
                                public void onClick() {
                                    prettyDialog3.dismiss();
                                }
                            }
                    )
                    .show();

            /*AlertDialog.Builder adbuilder = new AlertDialog.Builder(this);
            adbuilder.setTitle("아이디가 이미 존재합니다.")
                    .setPositiveButton("확인", null)
                    .setCancelable(false)
                    .show();*/
        }
    }

    //회원가입(단비시작하기 버튼) 서버통신
    void signUpRequest(){
        //pw
        final EditText pw1 = findViewById(R.id.membership_et_pw);
        EditText pw2 = findViewById(R.id.membership_et_checkpw);
        final String spw1 = pw1.getText().toString();
        String spw2 = pw2.getText().toString();

        //pw다를시 통신x
        if(!spw1.equals(spw2)){
            final PrettyDialog prettyDialog4 = new PrettyDialog(this);
            prettyDialog4
                    .setTitle("알림")
                    .setMessage("비밀번호가 일치하지 않습니다.")
                    .setIcon(R.drawable.danbeelogoj)
                    .addButton(
                            "확인",					// button text
                            R.color.pdlg_color_black,		// button text color
                            R.color.pdlg_color_yellow,		// button background color
                            new PrettyDialogCallback() {		// button OnClick listener
                                @Override
                                public void onClick() {
                                    prettyDialog4.dismiss();
                                }
                            }
                    )
                    .show();


            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder
                            .setTitle("비밀번호가 일치하지 않습니다.")
                            .setPositiveButton("확인",null)
                            .setCancelable(false)
                            .show();*/
            return;
        }

        //아이디 재확인
        if(!checkidBtClick){
            final PrettyDialog prettyDialog5 = new PrettyDialog(this);
            prettyDialog5
                    .setTitle("알림")
                    .setMessage("아이디 중복확인을 해주세요.")
                    .setIcon(R.drawable.danbeelogoj)
                    .addButton(
                            "확인",					// button text
                            R.color.pdlg_color_black,		// button text color
                            R.color.pdlg_color_yellow,		// button background color
                            new PrettyDialogCallback() {		// button OnClick listener
                                @Override
                                public void onClick() {
                                    prettyDialog5.dismiss();
                                }
                            }
                    )
                    .show();

            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("아이디 중복확인을 해주세요")
                    .setPositiveButton("확인",null)
                    .setCancelable(false)
                    .show();*/
            return;
        }
        //id
        final EditText et_userid = findViewById(R.id.membership_et_ID);
        final String userid = et_userid.getText().toString();

        //phone
        EditText et_ph = findViewById(R.id.membership_et_phone);
        final String phone = et_ph.getText().toString();
        //name
        EditText et_name = findViewById(R.id.membership_et_name);
        final String name = et_name.getText().toString();

        EditText et_birth = findViewById(R.id.membership_et_birth);
        final String birth = et_birth.getText().toString();

        if(userid.equals("") || phone.equals("") || name.equals("") || pw2.equals("") || gender == -1 ||  birth.equals("")){
            final PrettyDialog prettyDialog6 = new PrettyDialog(this);
            prettyDialog6
                    .setTitle("알림")
                    .setMessage("빈칸이 존재합니다.")
                    .setIcon(R.drawable.danbeelogoj)
                    .addButton(
                            "확인",					// button text
                            R.color.pdlg_color_black,		// button text color
                            R.color.pdlg_color_yellow,		// button background color
                            new PrettyDialogCallback() {		// button OnClick listener
                                @Override
                                public void onClick() {
                                    prettyDialog6.dismiss();
                                }
                            }
                    )
                    .show();


            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("빈칸이 존재합니다.")
                    .setPositiveButton("확인",null)
                    .setCancelable(false)
                    .show();*/
            return;
        }

        //String url = "http://3.17.25.223/api/user/signup/"+userid+"/"+spw1+"/"+phone+"/"+name+"/"+gender+"/"+birth;
        String url = "http://3.17.25.223/api/user/signup";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() { //응답 받음
                    @Override
                    public void onResponse(String response) {
                        Log.d("test", "sign: "+response);

                        new PrettyDialog(activity)
                                .setTitle("알림")
                                .setMessage("회원가입이 완료되었습니다.")
                                .setIcon(R.drawable.danbeelogoj)
                                .addButton(
                                        "확인",					// button text
                                        R.color.pdlg_color_black,		// button text color
                                        R.color.pdlg_color_yellow,		// button background color
                                        new PrettyDialogCallback() {		// button OnClick listener
                                            @Override
                                            public void onClick() {
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
                        Log.d("test", "SignUpErr: "+error);
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("pw", spw1);
                params.put("name", name);
                params.put("birth", birth);
                params.put("phone", phone);
                params.put("gender", String.valueOf(gender));

                return params;
            }
        };
        //자동캐싱잇는경우 이전결과 그대로보여짐
        request.setShouldCache(false);  //새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);
    }
}
