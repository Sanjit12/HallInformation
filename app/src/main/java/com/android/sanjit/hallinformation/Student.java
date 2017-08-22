package com.android.sanjit.hallinformation;

/**
 * Created by Sanjit on 04-Aug-17.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Student {
    private String name;
    private String id;
    private String department;
    private String year;
    private String session;
    private String room;
    private String bed;
    private String phone_number;

    public Student(){

    }
    public Student(String name,String id, String department, String year, String session, String room, String bed, String phone_number) {
        this.name = name;
        this.id = id;
        this.department = department;
        this.year = year;
        this.session = session;
        this.room = room;
        this.bed = bed;
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public String getId(){
        return id;
    }

    public String getDepartment() {
        return department;
    }

    public String getYear() {
        return year;
    }

    public String getSession() {
        return session;
    }

    public String getRoom() {
        return room;
    }

    public String getBed() {
        return bed;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
