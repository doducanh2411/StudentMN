package com.example.studentmn.ViewController;

public interface ViewStudent extends ViewInterface{
    void showStudentPoint(boolean updateTable);
    void getStudentInfo();
    void insertStudentImg();
    void updateStudentInfo();
    void clearStudentInfo();
    void changeStudentPassword();
    void exportStudentSubjectGrades();
    void addGenderList();
}
