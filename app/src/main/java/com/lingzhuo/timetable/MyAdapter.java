package com.lingzhuo.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lingzhuo.bean.CourseBean;

import java.util.List;

/**
 * Created by Wang on 2016/3/30.
 */
public class MyAdapter extends ArrayAdapter<CourseBean> {
    private int layout_item;
    public MyAdapter(Context context,int textViewResourceId, List<CourseBean> objects) {
        super(context, textViewResourceId, objects);
        this.layout_item=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseBean courseBean=getItem(position);
        View view= View.inflate(getContext(),layout_item,null);
        TextView textView_course_name= (TextView) view.findViewById(R.id.textView_course_name);
        TextView textView_course_detial= (TextView) view.findViewById(R.id.textView_course_detial);
        textView_course_name.setText(courseBean.getCourse_name());
        textView_course_detial.setText(courseBean.getCourse_address()+" "+" "+
                courseBean.getCourse_teacher()+" "+courseBean.getCourse_type()+" "+courseBean.getCourse_week());

        return view;
    }
}
