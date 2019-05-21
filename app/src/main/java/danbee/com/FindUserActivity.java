package danbee.com;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.toolbox.Volley;

import danbee.com.fragment.FindIdFragment;
import danbee.com.fragment.FindPwFragment;
import segmented_control.widget.custom.android.com.segmentedcontrol.SegmentedControl;
import segmented_control.widget.custom.android.com.segmentedcontrol.item_row_column.SegmentViewHolder;
import segmented_control.widget.custom.android.com.segmentedcontrol.listeners.OnSegmentClickListener;
import segmented_control.widget.custom.android.com.segmentedcontrol.listeners.OnSegmentSelectRequestListener;

public class FindUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        FindIdFragment idFragment = new FindIdFragment();
        FindPwFragment pwFragment = new FindPwFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fl_finduser, idFragment).commit();

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());



        final SegmentedControl segment = findViewById(R.id.segmented_control);
        segment.setSelectedSegment(0);
        segment.addOnSegmentClickListener(new OnSegmentClickListener() {
            @Override
            public void onSegmentClick(SegmentViewHolder segmentViewHolder) {
                int pos = segmentViewHolder.getAbsolutePosition();
                Log.d("test", " pos: "+pos);

                if(pos==0){
                    FindIdFragment idFragment = new FindIdFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_finduser,idFragment).commit();
                }
                else
                {
                    FindPwFragment pwFragment = new FindPwFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_finduser,pwFragment).commit();
                }

            }
        });

    }
}
