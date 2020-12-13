package com.example.ekaksha.Data;

import android.util.Log;

import java.util.ArrayList;

public class Classroom {
    private  String name="s";
    private String id="119110";
    private String description="swbdjkewb";
    private String teacherName="wjdbewfeg";
    private String studentsList ="e3kfrgt4t";

    public Classroom(String name,String id,String description,String teacherName,String studentsList){
          this.name=name;
          this.id=id;
          this.description=description;
          this.teacherName=teacherName;
          this.studentsList=studentsList;
    }
    public Classroom (){
        Log.v("public ","constructor called");
    }

    public String getId() {
        return id;
    }

    public String getStudentsList() {
        return studentsList;
    }

    public void setId(String id) {
        Log.v("public ","constructor called Id"+id);
        this.id = id;
    }



    public void setName(String name) {
        this.name = name;
        Log.v("public ","constructor called Name"+name);
    }


    public String getDescription() {
        return description;

    }

    public String getName() {
        return name;
    }

    public String getTeacherName() {
        return teacherName;
    }



    public void setDescription(String description) {
        Log.v("public ","constructor called description"+description);
        this.description = description;
    }



    public void setStudentsList(String studentsList) {

        Log.v("public ","constructor called list" +studentsList)
        ;this.studentsList = studentsList;
    }

    public void setTeacherName(String teacherName) {
        Log.v("public ","constructor called teacher name"+teacherName);
        this.teacherName = teacherName;
    }
}
