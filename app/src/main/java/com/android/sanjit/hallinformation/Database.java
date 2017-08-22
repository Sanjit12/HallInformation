package com.android.sanjit.hallinformation;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Sanjit on 16-Aug-17.
 */

public class Database {
    private static FirebaseDatabase myDatabase;

    public static FirebaseDatabase getDatabase(){
        if(myDatabase==null){
            myDatabase = FirebaseDatabase.getInstance();
            myDatabase.setPersistenceEnabled(true);
        }
        return myDatabase;
    }
}
