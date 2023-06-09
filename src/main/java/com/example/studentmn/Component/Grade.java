package com.example.studentmn.Component;

public class Grade {
    private int grade_id;
    private int student_id;
    private String student_name;
    private float component_point;

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    private float mid_point;
    private float end_point;
    private float final_point;

    public Grade(int grade_id, int student_id, String student_name, float component_point, float mid_point, float end_point, float final_point) {
        this.grade_id = grade_id;
        this.student_id = student_id;
        this.student_name = student_name;
        this.component_point = component_point;
        this.mid_point = mid_point;
        this.end_point = end_point;
        this.final_point = final_point;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }


    public float getComponent_point() {
        return component_point;
    }

    public void setComponent_point(Float component_point) {
        this.component_point = component_point;
    }

    public float getMid_point() {
        return mid_point;
    }

    public void setMid_point(Float mid_point) {
        this.mid_point = mid_point;
    }

    public float getEnd_point() {
        return end_point;
    }

    public void setEnd_point(float end_point) {
        this.end_point = end_point;
    }

    public float getFinal_point() {
        return final_point;
    }

    public void setFinal_point(float final_point) {
        this.final_point = final_point;
    }
}
