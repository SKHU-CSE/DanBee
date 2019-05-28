package danbee.com;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import danbee.com.fragment.GuideFragment1;
import danbee.com.fragment.GuideFragment2;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        ViewPager pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3); //뒤에안보이는화면 캐싱
        pager.setClipToPadding(false);  //자식들의 패딩무시 (양쪽미리보기하기위해서)
        pager.setPadding(150,0,150,0);

        GuidePagerAdapter adapter = new GuidePagerAdapter(getSupportFragmentManager());
        GuideFragment1 guideFragment1 = new GuideFragment1();
        GuideFragment2 guideFragment2 = new GuideFragment2();
        adapter.addItem(guideFragment1);
        adapter.addItem(guideFragment2);

        pager.setAdapter(adapter);

    }

    class GuidePagerAdapter extends FragmentStatePagerAdapter{

        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public GuidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item){
            this.items.add(item);
        }

        @Override
        public Fragment getItem(int i) {
            return items.get(i);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}
