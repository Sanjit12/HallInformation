package com.android.sanjit.hallinformation;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAllStudents extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseRef;

    ListView allStudent;

    List<Student> studentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_students);

        auth = FirebaseAuth.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        databaseRef  =Database.getDatabase().getReference("BSFMH");
        studentList = new ArrayList<>();

        allStudent = (ListView)findViewById(R.id.allstudent);

        allStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = studentList.get(position);

                showUpdateDialog(student);
                return false;
            }
        });
    }

    private void showUpdateDialog(final Student student){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialogue,null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Updating Student Information");

        final AlertDialog dialog = dialogBuilder.create();

        dialog.show();

        final EditText name,id,department,year,phone_number;

        name = (EditText)dialogView.findViewById(R.id.name);
        id = (EditText)dialogView.findViewById(R.id.id);
        department = (EditText)dialogView.findViewById(R.id.department);
        year = (EditText)dialogView.findViewById(R.id.year);
        phone_number = (EditText)dialogView.findViewById(R.id.phone_number);

        name.setText(student.getName());
        id.setText(student.getId());
        department.setText(student.getDepartment());
        year.setText(student.getYear());
        phone_number.setText(student.getPhone_number());

        Button update_button = (Button)dialogView.findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st_name,st_id,st_dept,st_year,st_phn;
                st_name = name.getText().toString();
                st_id = id.getText().toString();
                st_dept = department.getText().toString();
                st_year = year.getText().toString();
                st_phn = phone_number.getText().toString();

                if(TextUtils.isEmpty(st_name)){
                    name.setError("Name Required");
                    return;
                }
                else{
                    name.setError(null);
                }

                if(TextUtils.isEmpty(st_id)){
                    id.setError("ID Required");
                    return;
                }
                else{
                    id.setError(null);
                }

                if(TextUtils.isEmpty(st_dept)){
                    department.setError("Department Required");
                    return;
                }
                else{
                    department.setError(null);
                }

                if(TextUtils.isEmpty(st_year)){
                    year.setError("Year Required");
                    return;
                }
                else{
                    year.setError(null);
                }

                if(TextUtils.isEmpty(st_phn)){
                    phone_number.setError("Phone Number Required");
                    return;
                }
                else{
                    phone_number.setError(null);
                }

                updateStudent(new Student(st_name,st_id,st_dept,st_year,student.getSession(),student.getRoom(),
                        student.getBed(),st_phn));

                dialog.dismiss();
            }
        });

    }
    public boolean updateStudent(Student student){
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("BSFMH").child(student.getRoom()+"-"+student.getBed());

        dbref.setValue(student);

        Toast.makeText(ViewAllStudents.this,"Successfully updated Information.",Toast.LENGTH_LONG).show();

        return true;

    }
    @Override
    protected void onStart() {
        super.onStart();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentList.clear();

                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Student student = data.getValue(Student.class);

                    studentList.add(student);
                }
                StudentList list = new StudentList(ViewAllStudents.this,studentList);
                allStudent.setAdapter(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
