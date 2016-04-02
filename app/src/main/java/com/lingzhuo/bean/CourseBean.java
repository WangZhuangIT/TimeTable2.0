package com.lingzhuo.bean;

import android.widget.ListView;

/**
 * Created by Wang on 2016/3/30.
 */
public class CourseBean {
    private String Course_name; // 课程名
    private String Course_address; // 上课地点
    private String Course_teacher; // 课程老师
    private String Course_type; // 课程种类 专业/专选/选修/实验
    private String Course_week; // 课程周数
    public CourseBean() {
    }

    public CourseBean(String course_name,  String course_type,String course_week, String course_teacher, String course_address) {
        Course_name = course_name;
        Course_week = course_week;
        Course_type = course_type;
        Course_teacher = course_teacher;
        Course_address = course_address;
    }

    public String getCourse_name() {
        return Course_name;
    }

    public void setCourse_name(String course_name) {
        Course_name = course_name;
    }

    public String getCourse_address() {
        return Course_address;
    }

    public void setCourse_address(String course_address) {
        Course_address = course_address;
    }

    public String getCourse_teacher() {
        return Course_teacher;
    }

    public void setCourse_teacher(String course_teacher) {
        Course_teacher = course_teacher;
    }

    public String getCourse_type() {
        return Course_type;
    }

    public void setCourse_type(String course_type) {
        Course_type = course_type;
    }

    public String getCourse_week() {
        return Course_week;
    }

    public void setCourse_week(String course_week) {
        Course_week = course_week;
    }

    @Override
    public String toString() {
        return Course_name + Course_address
                + Course_teacher + Course_type;
    }
}
