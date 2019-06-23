package danbee.com;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rd.PageIndicatorView;

import java.util.ArrayList;

import danbee.com.fragment.GuideFragment1;
import danbee.com.fragment.GuideFragment2;
import danbee.com.fragment.GuideFragment3;
import danbee.com.fragment.GuideFragment4;
import danbee.com.fragment.GuideFragment5;

public class GuideActivity extends AppCompatActivity {

    PageIndicatorView pageIndicatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        ViewPager pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3); //뒤에안보이는화면 캐싱
        //pager.setClipToPadding(false);  //자식들의 패딩무시 (양쪽미리보기하기위해서)
        pager.setPadding(100,30,100,30);

        GuidePagerAdapter adapter = new GuidePagerAdapter(getSupportFragmentManager());
        GuideFragment1 guideFragment1 = new GuideFragment1();
        GuideFragment2 guideFragment2 = new GuideFragment2();
        GuideFragment3 guideFragment3 = new GuideFragment3();
        GuideFragment4 guideFragment4 = new GuideFragment4();
        GuideFragment5 guideFragment5 = new GuideFragment5();

        adapter.addItem(guideFragment1);
        adapter.addItem(guideFragment2);
        adapter.addItem(guideFragment3);
        adapter.addItem(guideFragment4);
        adapter.addItem(guideFragment5);

        pageIndicatorView = findViewById(R.id.guide_pageIndicatorView);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });

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
