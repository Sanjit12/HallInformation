package com.android.sanjit.hallinformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class HomeWindow extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    Button logout,newStudentButton,allStudent,searchForStudents;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_window);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Database.getDatabase();

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if(user==null){
                    startActivity(new Intent(HomeWindow.this,LoginActivity.class));
                    finish();
                }
            }
        };

        username = (TextView)findViewById(R.id.username1);
        username.setText("Logged In as, "+auth.getCurrentUser().getEmail().split("@")[0]);

        logout = (Button) findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        newStudentButton = (Button)findViewById(R.id.add_new);
        newStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeWindow.this,StudentInfoInput.class));
                //finish();
            }
        });

        allStudent = (Button)findViewById(R.id.all_student);
        allStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeWindow.this,ViewAllStudents.class));
            }
        });

        searchForStudents = (Button)findViewById(R.id
                .search_button);
        searchForStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeWindow.this,SearchForStudents.class));
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
