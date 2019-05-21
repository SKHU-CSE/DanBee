package danbee.com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import danbee.com.AppHelper;
import danbee.com.ChangePwActivity;
import danbee.com.R;
import danbee.com.finddata.FinduserData;

public class FindPwFragment extends Fragment {

    EditText findpw_id;
    EditText findpw_name;
    EditText findpw_birth;
    EditText findpw_phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView2 = (ViewGroup) inflater.inflate(R.layout.fragment_findpw,container,false);
        findpw_id = rootView2.findViewById(R.id.findpw_user_et_id);
        findpw_name = rootView2.findViewById(R.id.findpw_user_et_name);
        findpw_birth = rootView2.findViewById(R.id.findpw_user_et_birth);
        findpw_phone = rootView2.findViewById(R.id.findpw_user_et_phone);

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getActivity());

        Button findpwButton =  rootView2.findViewById(R.id.findpw_user_bt_pw);
        findpwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFindPwRequest();
            }
        });

        return rootView2;
    }

    //비밀번호찾기 버튼 눌렀을때 함수
    public void onClickFindPwRequest() {
        final String id = findpw_id.getText().toString(); //값을 받아옴
        String name = findpw_name.getText().toString();
        String birth = findpw_birth.getText().toString();
        String phone = findpw_phone.getText().toString();

        //통신
        String url = "http://3.17.25.223/api/user/show&pw/" + id + "/" +name + "/" + phone + "/" + birth;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() { //응답 받음
                    @Override
                    public void onResponse(String response) {
                        findUserProcess(response, id);
                        Log.d("test", "findpw: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "findpw: " + error);
                    }
                }
        );
        //자동캐싱잇는경우 이전결과 그대로보여짐
        request.setShouldCache(false);  //새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);


    }

    //json처리 결과파싱
    public void findUserProcess(String response, String userid) {
        Gson gson = new Gson();
        FinduserData finduserResult = gson.fromJson(response, FinduserData.class);

        //비밀번호찾기 실패
        if (finduserResult.result == 404) {
            AlertDialog.Builder adbuilder = new AlertDialog.Builder(getActivity());
            adbuilder.setTitle("일치하는 정보가 없습니다.\n다시 입력해주세요.")
                    .setPositiveButton("확인", null)
                    .setCancelable(false)
                    .show();
        } else {

            Intent intent = new Intent(getActivity(), ChangePwActivity.class);
            intent.putExtra("id", userid);
            startActivity(intent);
            getActivity().finish();

        }

    }
}
