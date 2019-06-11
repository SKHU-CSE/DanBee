package danbee.com.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import danbee.com.AppHelper;
import danbee.com.DbHelper.AutoLoginDbHelper;
import danbee.com.MainActivity;
import danbee.com.R;
import danbee.com.UserInfo;
import danbee.com.finddata.FinduserData;
import danbee.com.logindata.LoginResult;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class FindIdFragment extends Fragment {

    EditText findid_name;
    EditText findid_phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView1 = (ViewGroup) inflater.inflate(R.layout.fragment_findid, container, false);
        findid_name = rootView1.findViewById(R.id.findid_user_et_name);
        findid_phone = rootView1.findViewById(R.id.findid_user_et_phone);

        Button findidButton =  rootView1.findViewById(R.id.findid_user_bt_id);
        findidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFindIdRequest();
            }
        });
        return rootView1;
    }

    //아이디찾기 버튼 눌렀을때 함수
    public void onClickFindIdRequest() {
        String name = findid_name.getText().toString();
        String phone = findid_phone.getText().toString(); //값을 받아옴

        //통신
        String url = "http://3.17.25.223/api/user/show&id/" + name + "/" + phone;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() { //응답 받음
                    @Override
                    public void onResponse(String response) {
                        findUserProcess(response);
                        Log.d("test", "findid: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "findid: " + error);
                    }
                }
        );
        //자동캐싱잇는경우 이전결과 그대로보여짐
        request.setShouldCache(false);  //새로요청해서 결과보여줌
        AppHelper.requestQueue.add(request);


    }

    //json처리 결과파싱
    public void findUserProcess(String response) {
        Gson gson = new Gson();
        FinduserData finduserResult = gson.fromJson(response, FinduserData.class);

        //아이디찾기 실패
        if (finduserResult.result == 404) {
            final PrettyDialog prettyDialog = new PrettyDialog(getActivity());
            prettyDialog
                    .setTitle("알림")
                    .setMessage("아이디가 존재하지 않습니다.")
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

            /*AlertDialog.Builder adbuilder = new AlertDialog.Builder(getActivity());
            adbuilder.setTitle("아이디가 존재하지 않습니다.")
                    .setPositiveButton("확인", null)
                    .setCancelable(false)
                    .show();*/
        } else {
            String id = finduserResult.data;
            final PrettyDialog prettyDialog2 = new PrettyDialog(getActivity());
            prettyDialog2
                    .setTitle("알림")
                    .setMessage("아이디 : \" + id ")
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

            /*AlertDialog.Builder adbuilder = new AlertDialog.Builder(getActivity());
            adbuilder.setTitle("아이디 : " + id )
                    .setPositiveButton("확인", null)
                    .setCancelable(false)
                    .show();*/
        }

    }
}
