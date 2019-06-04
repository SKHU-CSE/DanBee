package danbee.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class LoadingActivity extends Activity {

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView danbeelogos = (ImageView) findViewById(R.id.loading_iv_danbeelogos);
        GlideDrawableImageViewTarget gif_danbeelogo = new GlideDrawableImageViewTarget(danbeelogos);
        Glide.with(this).load(R.drawable.danbeelogos).into(gif_danbeelogo);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 3000);
    }
}
