package com.android.sanjit.hallinformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    Button logout_button,register_button;
    TextView tv;
    EditText name,id, department, year, session, room, bed, phone_number;

    FirebaseDatabase database;
    DatabaseReference ref;

    Map<String,Student> BSFMH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        auth = FirebaseAuth.getInstance();
        database=Database.getDatabase();
        ref = database.getReference("BSFMH");

        BSFMH = new HashMap<>();

        final FirebaseUser user = auth.getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if(user==null){
                    startActivity(new Intent(UserActivity.this,LoginActivity.class));
                    finish();
                }
            }
        };
        register_button = (Button)findViewById(R.id.register_button);

        name = (EditText)findViewById(R.id.name);
        id = (EditText)findViewById(R.id.id);
        department = (EditText)findViewById(R.id.department);
        year = (EditText)findViewById(R.id.year);
        session = (EditText)findViewById(R.id.session);
        room = (EditText)findViewById(R.id.room);
        bed = (EditText)findViewById(R.id.bed);
        phone_number = (EditText)findViewById(R.id.phone_number);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String student_name,student_id,student_department,student_year,student_session,student_room,student_bed,student_phone_number;
                student_name = name.getText().toString();
                student_id = id.getText().toString();
                student_department = department.getText().toString();
                student_year = year.getText().toString();
                student_session = session.getText().toString();
                student_room = room.getText().toString();
                student_bed = bed.getText().toString();
                student_phone_number = phone_number.getText().toString();

                Student student = new Student(student_name,student_id,student_department,student_year,student_session,student_room,student_bed,student_phone_number);

                ref.child(ref.push().getKey()).setValue(student);
            }
        });
    }

    public void Logout(){
        auth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
