package com.lingzhuo.timetable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wang on 2016/3/29.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> pager_list;
    private List<String> title_list;

    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> pager_list) {
        super(fm);
        this.pager_list=pager_list;
        this.title_list= new ArrayList<String>();
        title_list.add("周一");
        title_list.add("周二");
        title_list.add("周三");
        title_list.add("周四");
        title_list.add("周五");
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title_list.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return pager_list.get(position);
    }

    @Override
    public int getCount() {
        return pager_list.size();
    }
}
