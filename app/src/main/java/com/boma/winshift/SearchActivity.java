package com.boma.winshift;


import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity{
    private ViewPager vp;
    private boolean shouldClose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        vp = (ViewPager)findViewById(R.id.search_viewpager);

        FragmentManager fm = getSupportFragmentManager();
        vp.setAdapter(new SearchPagerAdapter(fm));
    }

    @Override
    public void onBackPressed() {
        if(shouldClose) {
            super.onBackPressed();
        }
        else {
            Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
            shouldClose = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    shouldClose = false;
                }
            },2000);

        }
    }

    class SearchPagerAdapter extends FragmentPagerAdapter {
        public SearchPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        Fragment[] frags = {new MyShiftFragment(),new StaffListFragment()};
        String[] fragTitle = {"Me","Colleagues"};

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            for (int i =0;i<frags.length;i++){
                if(position==i){
                    fragment = frags[i];
                    break;
                }
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "Tab";

            for (int i =0;i<frags.length;i++){
                if(position==i){
                    title = fragTitle[i];
                    break;
                }
            }
            return title;
        }



    }
}
