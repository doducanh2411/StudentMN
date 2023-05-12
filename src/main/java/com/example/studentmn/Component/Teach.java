package com.example.studentmn.Component;

import javafx.scene.layout.HBox;

public class Teach {
    private int teach_id;

    private String teacher_name;

    private String subject_name;

    private String class_name;

    private HBox hbox;

    public HBox getHbox() {
        return hbox;
    }

    public void setHbox(HBox hbox) {
        this.hbox = hbox;
    }

    public Teach(int teach_id, String teacher_name, String subject_name, String class_name) {
        this.teach_id = teach_id;
        this.teacher_name = teacher_name;
        this.subject_name = subject_name;
        this.class_name = class_name;

        hbox = new HBox();


    }

    public int getTeach_id() {
        return teach_id;
    }

    public void setTeach_id(int teach_id) {
        this.teach_id = teach_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
