package com.lingzhuo.timetable;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Wang on 2016/3/29.
 */
public class MyFragment extends Fragment {
    private ListView listView;

    public ListView getListView() {
        return listView;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_pager,container,false);
        listView= (ListView) view.findViewById(R.id.listView_pager);
        return view;
    }
}
