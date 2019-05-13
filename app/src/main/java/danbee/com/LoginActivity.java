package danbee.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //회원가입버튼
        TextView tv_singUp = findViewById(R.id.newMem);
        tv_singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Membership_Activity.class);
                startActivity(intent);
            }
        });

        //아이디비밀번호찾기버튼
        TextView tv_find = findViewById(R.id.findidpw);
        tv_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindUserActivity.class);
                startActivity(intent);
            }
        });
    }
}
