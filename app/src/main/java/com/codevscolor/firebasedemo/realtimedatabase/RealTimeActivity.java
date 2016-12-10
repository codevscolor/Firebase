package com.codevscolor.firebasedemo.realtimedatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codevscolor.firebasedemo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RealTimeActivity extends AppCompatActivity {

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time);
        database = FirebaseDatabase.getInstance();
    }

    public void addData(View view){
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RealTimeActivity.this, "Oops " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addMore(View v) {
        DatabaseReference myRef = database.getReference("series");
        Person phoebe = new Person("Phoebe", 26);
        Person rachel = new Person("Rachel", 26);
        Person ross = new Person("Ross", 31);
        Person monica = new Person("Monica", 26);
        Person joey = new Person("Joey", 29);

        myRef.child("friends").child("1").setValue(phoebe);
        myRef.child("friends").child("2").setValue(rachel);
        myRef.child("friends").child("3").setValue(ross);
        myRef.child("friends").child("4").setValue(monica);
        myRef.child("friends").child("5").setValue(joey);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RealTimeActivity.this, "Oops " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void appendData(View v){
        DatabaseReference myRef = database.getReference("series");
        Person chandler = new Person("Chandler",29);
        String key = myRef.child("friends").push().getKey();
        myRef.child("friends").child(key).setValue(chandler);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RealTimeActivity.this,"Oops "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteData(View v) {
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue(null);
    }

    public void findData(View v) {
        //sorting and searching
        final DatabaseReference myRef = database.getReference("series/friends");
        Query query = myRef.orderByChild("name").equalTo("Chandler");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String name = (String) messageSnapshot.child("name").getValue();
                    Long age = (Long) messageSnapshot.child("age").getValue();
                    Toast.makeText(RealTimeActivity.this, "found " + name +" age: "+age, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateData(View v){
        final DatabaseReference myRef = database.getReference("series/friends");
        myRef.child("1").child("age").setValue(27);
    }


}
