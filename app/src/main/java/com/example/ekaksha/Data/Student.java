package com.example.ekaksha.Data;

public class Student {
   private String studentsEmail;
    private  int studentRollNo;

    public Student(String substring, int i) {
        this.studentsEmail=substring;
        this.studentRollNo=i;
    }

    public int getStudentRollNo() {
        return studentRollNo;
    }

    public String getStudentsEmail() {
        return studentsEmail;
    }

    public void setStudentsEmail(String studentsEmail) {
        this.studentsEmail = studentsEmail;
    }

    public void setStudentRollNo(int studentRollNo) {
        this.studentRollNo = studentRollNo;
    }
}
