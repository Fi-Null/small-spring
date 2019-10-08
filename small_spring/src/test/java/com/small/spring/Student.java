package com.small.spring;

import com.small.spring.beans.annotation.AutoWired;
import com.small.spring.beans.annotation.Component;
import com.small.spring.beans.annotation.Value;

/**
 * @ClassName Student
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/24 23:20
 * @Version 1.0
 **/
@Component
public class Student {

    @Value("huangtianyu")
    private String stuName;

    @AutoWired
    private School school;

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void learning() {
        System.out.println("i am learning");
    }
}
