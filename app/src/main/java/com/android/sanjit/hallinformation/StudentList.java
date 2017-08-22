package com.android.sanjit.hallinformation;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sanjit on 11-Aug-17.
 */

public class StudentList extends ArrayAdapter<Student>{
    private Activity context;
    private List<Student> studentList;

    public StudentList(Activity context, List<Student> studentList) {
        super(context, R.layout.list_layout, studentList);
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView name = (TextView) listViewItem.findViewById(R.id.name);
        TextView id = (TextView) listViewItem.findViewById(R.id.id);
        TextView department = (TextView) listViewItem.findViewById(R.id.department);

        Student student = studentList.get(position);

        name.setText(student.getName()+"  ( "+student.getRoom()+"-"+student.getBed()+" )");
        id.setText("ID: "+student.getId());
        department.setText("Dept: "+student.getDepartment());

        return listViewItem;
    }
}
