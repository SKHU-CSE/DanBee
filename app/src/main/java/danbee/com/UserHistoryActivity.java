package danbee.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AbsListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;

import danbee.com.historydata.HistoryResult;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class UserHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryRecyclerViewAdapter adapter;
    ArrayList<HistoryItem> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);
        items=new ArrayList<HistoryItem>();
        recyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);

        //시작하면 reqestQueue가 만들어짐 Main에 넣기
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        sendRequest(); //통신

        recyclerView = findViewById(R.id.history_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //컨텍스트, 방향, 아이템보이는방향(위->아래, 아래->위)
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryRecyclerViewAdapter(this,items);
        recyclerView.setAdapter(adapter);

    }

    void sendRequest(){
        items.clear();
        String url = "http://3.17.25.223/api/history/user/"+UserInfo.info.getUserid();
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {//응답을 문자열로받아서 넣어달라는뜻
                    @Override
                    public void onResponse(String response) {
                        historyProcessResponse(response);  // gson변환함수
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
    public void historyProcessResponse(String response){
        Gson gson = new Gson();
        HistoryResult historyResult = gson.fromJson(response, HistoryResult.class);

        if(historyResult.result == 777){
            int datasize = historyResult.data.size();
            Log.d("test", "History count: "+datasize);

           for(int i=datasize-1; i>=0; i--){
                String sDate = historyResult.data.get(i).start;
                String eDate = historyResult.data.get(i).end;
                int kickid = historyResult.data.get(i).kickid;
                items.add(new HistoryItem(sDate,eDate,kickid));
            }

            adapter.notifyDataSetChanged();
        }

        if(historyResult.result == 404) {
            final PrettyDialog prettyDialog = new PrettyDialog(this);
            prettyDialog
                    .setTitle("알림")
                    .setMessage("이용내역이 없습니다.")
                    .setIcon(R.drawable.danbeelogoj)
                    .addButton(
                            "확인",					// button text
                            R.color.pdlg_color_black,		// button text color
                            R.color.pdlg_color_yellow,		// button background color
                            new PrettyDialogCallback() {		// button OnClick listener
                                @Override
                                public void onClick() {
                                    Intent intent = new Intent(UserHistoryActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    prettyDialog.dismiss();
                                }
                            }
                    )
                    .show();

            /*AlertDialog.Builder adbuilder = new AlertDialog.Builder(UserHistoryActivity.this);
            adbuilder.setTitle("알림")
                    .setMessage("이용내역이 없습니다.")
                    .setCancelable(false)
                    .setIcon(R.drawable.danbeelogoj)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(UserHistoryActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();*/
        }

    }

}
