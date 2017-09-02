package com.android.sanjit.hallinformation;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchForStudents.this, "Search Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
