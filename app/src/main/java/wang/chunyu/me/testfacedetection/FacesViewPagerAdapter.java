package wang.chunyu.me.testfacedetection;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 脸部适配器
 * <p/>
 * Created by wangchenlong on 15/12/15.
 */
public class FacesViewPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM = 9;

    public FacesViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        return ShowFaceFragment.newInstance(position);
    }

    @Override public int getCount() {
        return NUM;
    }
}
