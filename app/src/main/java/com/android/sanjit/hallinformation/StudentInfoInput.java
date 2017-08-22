package com.android.sanjit.hallinformation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A login screen that offers login via email/password.
 */
public class StudentInfoInput extends AppCompatActivity  {
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    Button logout_button,register_button;
    TextView tv;
    EditText name,id, department, year, session, room, bed, phone_number;

    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info_input);

        auth = FirebaseAuth.getInstance();
        database=Database.getDatabase();
        //database.setPersistenceEnabled(true);

        ref = database.getReference("BSFMH");

        final FirebaseUser user = auth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if(user==null){
                    startActivity(new Intent(StudentInfoInput.this,LoginActivity.class));
                    finish();
                }
            }
        };
        register_button = (Button)findViewById(R.id.insert_button);

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
                final String student_name,student_id,student_department,student_year,student_session,student_room,student_bed,student_phone_number;
                student_name = name.getText().toString();
                student_id = id.getText().toString();
                student_department = department.getText().toString();
                student_year = year.getText().toString();
                student_session = session.getText().toString();
                student_room = room.getText().toString();
                student_bed = bed.getText().toString();
                student_phone_number = phone_number.getText().toString();

                if(TextUtils.isEmpty(student_name)){
                    name.setError("Name Required");
                    return;
                }
                else{
                    name.setError(null);
                }

                if(TextUtils.isEmpty(student_id)){
                    id.setError("ID Required");
                    return;
                }
                else{
                    id.setError(null);
                }

                if(TextUtils.isEmpty(student_department)){
                    department.setError("Department Required");
                    return;
                }
                else{
                    department.setError(null);
                }

                if(TextUtils.isEmpty(student_year)){
                    year.setError("Year Required");
                    return;
                }
                else{
                    year.setError(null);
                }

                if(TextUtils.isEmpty(student_session)){
                    session.setError("Session Required");
                    return;
                }
                else{
                    session.setError(null);
                }

                if(TextUtils.isEmpty(student_room)){
                    room.setError("Room Required");
                    return;
                }
                else{
                    room.setError(null);
                }

                if(TextUtils.isEmpty(student_bed)){
                    bed.setError("Bed Required");
                    return;
                }
                else{
                    bed.setError(null);
                }

                if(TextUtils.isEmpty(student_phone_number)){
                    phone_number.setError("Phone Number Required");
                    return;
                }
                else{
                    phone_number.setError(null);
                }
                final Student student = new Student(student_name,student_id,student_department,student_year,student_session,student_room,student_bed,student_phone_number);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChild(student_room+"-"+student_bed)){
                            ref.child(student_room+"-"+student_bed).setValue(student);
                            clearAll();
                        }
                        else{
                            Toast.makeText(StudentInfoInput.this,"Child already Exists",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    public void clearAll(){
        name.setText("");
        id.setText("");
        department.setText("");
        year.setText("");
        session.setText("");
        room.setText("");
        bed.setText("");
        phone_number.setText("");
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

