package com.chobo.controlfirebase;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        //연습1: DB작동 확인을 위한 쓰기
        /*
       myRef.setValue("Helflo, World!");
        */

        //연습2: child를 이용한 Node 생성

        myRef.child("message").setValue("Hello, World! changed");
        myRef.child("message2").setValue("Hello, World!2");
        myRef.child("message3").child("message3-1").child("message3-2").setValue("Hello. World3");


        //연습3: 하나의 Node 밑에 여러 가지 정보를 저장 (HashMap)
        /*
        HashMap<String, Object> data = new HashMap<>();
        data.put("id","user1");
        data.put("age",25);
        data.put("name","John");
        myRef.child("userinfo1").setValue(data);
        */
        //연습4: 모델 Class를 생성하여 data를 일정한 형태로 저장하기 (다른 형태를 사용하게되어 발생하는 오류를 방지할 수 있다)
        /*
        UserInfoModel userdata = new UserInfoModel("Park","user2", 26);
        myRef.child("userinfo2").setValue(userdata);
        */
        //연습5: 노드를 자동으로 생성하는 방법 (push()를 이용한다.)
        /*
        for(int i=0 ;i<10; i++) {
            myRef.child("room1").child("messages").push().setValue("msg"+1);
        }
        */
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // ..
                Log.e("osslog",dataSnapshot.toString());
                Log.e("osslog",dataSnapshot.getValue().toString());
                Log.e("osslog",dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        myRef.child("message").addValueEventListener(postListener);

    }
}