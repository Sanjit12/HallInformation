package com.android.sanjit.hallinformation;

import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchForStudents extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton id,name,room,year;
    EditText searchBox;
    Button searchButton;

    ListView searchResult;
    List<Student> studentList;

    String searchText="";

    DatabaseReference databaseRef;
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_students);

        databaseRef = Database.getDatabase().getReference("BSFMH");
        studentList = new ArrayList<>();


        searchBox = (EditText)findViewById(R.id.search_box);
        searchButton = (Button)findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = searchBox.getText().toString().trim();
                searchStudent(searchText);
            }
        });

        searchResult = (ListView)findViewById(R.id.search_result);

        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            }
        });

        radioGroup.check(R.id.name_radio);

        searchResult.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        DatabaseReference dbref = Database.getDatabase().getReference("BSFMH").child(student.getRoom()+"-"+student.getBed());

        dbref.setValue(student);

        Toast.makeText(SearchForStudents.this,"Successfully updated Information.",Toast.LENGTH_LONG).show();

        return true;

    }

    public void searchStudent(String searchText){
//        Query query = databaseRef.orderByChild("name").equalTo(searchText);
//        query.addChildEventListener(childEventListener);
        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentList.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Student student = data.getValue(Student.class);
                    if(radioGroup.getCheckedRadioButtonId()==R.id.name_radio){
                        if(student.getName().toLowerCase().contains(searchText)){
                            studentList.add(student);
                        }
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==R.id.id_radio){
                        if(student.getId().contains(searchText)){
                            studentList.add(student);
                        }
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==R.id.room_radio){
                        if(student.getRoom().equals(searchText)){
                            studentList.add(student);
                        }
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==R.id.year_radio){
                        if(student.getYear().contains(searchText)){
                            studentList.add(student);
                        }
                    }
                    else if(radioGroup.getCheckedRadioButtonId()==R.id.dept_radio){
                        if(student.getDepartment().equalsIgnoreCase(searchText)){
                            studentList.add(student);
                        }
                    }

                }
                StudentList list =  new StudentList(SearchForStudents.this,studentList);
                searchResult.setAdapter(list);
                Toast.makeText(SearchForStudents.this, studentList.size()+" Students Found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchForStudents.this, "Search Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
