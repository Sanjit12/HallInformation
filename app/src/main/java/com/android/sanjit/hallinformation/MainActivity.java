package com.android.sanjit.hallinformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    Button login_button,register_button;
    EditText email,username,password,confirm_password;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        if(user!=null){
            startActivity(new Intent(MainActivity.this,HomeWindow.class));
            finish();
        }

        login_button = (Button) findViewById(R.id.login_button);
        register_button = (Button)findViewById(R.id.register_button);

        email = (EditText)findViewById(R.id.email);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        confirm_password = (EditText)findViewById(R.id.confirm_password);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().
                            setDisplayName(username.getText().toString()).build();
                    user.updateProfile(profileUpdate);
                }
            }
        };
        auth.addAuthStateListener(authListener);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = email.getText().toString().trim();
                String inputUsername = username.getText().toString().trim();
                String pwd = password.getText().toString();
                String con_pwd = confirm_password.getText().toString();

                if(!pwd.equals(con_pwd)){
                    clear();
                    Toast.makeText(getApplicationContext(),"Password doesn't match.",Toast.LENGTH_LONG).show();
                    confirm_password.setError("Doesn't Match");
                    return;
                }
                else{
                    confirm_password.setError(null);
                }

                auth.createUserWithEmailAndPassword(inputEmail,pwd).
                        addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(MainActivity.this,HomeWindow.class));
                            finish();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Failed To Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void clear(){
        //password.setText("");
        confirm_password.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authListener!=null){
            auth.removeAuthStateListener(authListener);
        }
    }
}
