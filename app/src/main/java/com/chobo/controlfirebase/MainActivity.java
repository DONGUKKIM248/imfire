package com.chobo.controlfirebase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //데이터 추가를 위한 것
    private EditText editText1,editText2;
    private TextView textView1;
    private ImageButton imageButton1;
//데이터 추가를 위해 받는 값 저장소
    private String UID;
    private String text;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    //recyclerView
    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Post> arrayList;
    private ArrayList<Post> arrayListSearched;


    //검색을 위한 변수
    private Button searchButton;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        textView1 = findViewById(R.id.textView1); //HelloWorld(추가 값)
        imageButton1 = findViewById(R.id.imageButton1);
        arrayList = new ArrayList<>();

        //검색을 위한 연결
        searchButton = findViewById(R.id.searchButton);
        editTextSearch = findViewById(R.id.editTextSearch);

        //recyclerview를 위한 추가
        recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        //검색을 위한 추가
        arrayListSearched = new ArrayList<>();


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://controlfirebase-ad0a7-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Post");
        //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("message");


        //recyclerView 구현부
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                arrayListSearched.clear();
                for( DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    //arrayList.add(post);
                    arrayListSearched.add(post);
                }
                //arrayListSearched.addAll(arrayList);
                arrayList.addAll(arrayListSearched);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity",String.valueOf(databaseError.toException()));
            }

        };
        //arrayListSearched.addAll(arrayList);
        myRef.child("NormalPost").addValueEventListener(postListener);
        //adapter = new PostAdapter(arrayList);
        adapter = new PostAdapter(arrayListSearched);
        recyclerview.setAdapter(adapter);
        /*
        myRef.child("NormalPost").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                for( DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    arrayList.add(post);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity",String.valueOf(databaseError.toException()));
            }
        });
        adapter = new PostAdapter(arrayList);
        recyclerview.setAdapter(adapter);
        */
        //여기까지 recyclerView

        //검색구현
        // 데이터베이스에서 데이터 읽기

        // 검색 기능 구현
        //여러번 되는 것
        /*
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            String searchText = editTextSearch.getText().toString().toLowerCase(); // 사용자가 입력한 검색어를 받음
                            ArrayList<Post> filteredList = new ArrayList<>();

                            for (Post post : arrayList) {
                                if (post.getText().toLowerCase().contains(searchText)) {
                                    filteredList.add(post); // 검색어가 제목 또는 내용에 포함된 데이터를 새로운 리스트에 추가
                                }
                            }
                            //adapter.setFilter(filteredList); // RecyclerView 어댑터에 새로운 리스트 설정
                            arrayList.clear(); // 기존 arrayList 비우기
                            arrayList.addAll(filteredList); // 필터링된 데이터로 arrayList 업데이트
                            adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    };


                };
            });
            */
        //한번만 되는 것
        myRef.child("NormalPost").addValueEventListener(postListener);

        //adapter = new PostAdapter(arrayList);
        adapter = new PostAdapter(arrayListSearched);
        recyclerview.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = editTextSearch.getText().toString().toLowerCase();
                //arrayList.clear(); // 검색 결과 리스트 초기화
                arrayListSearched.clear();

                //for (Post post : arrayListSearched) {
                for(Post post : arrayList) {
                    if (post.getText().toLowerCase().contains(searchText)) {
                        // arrayList.add(post); // 검색된 데이터를 arrayListSearched에 추가
                        arrayListSearched.add(post);
                    }
                }

                //arrayList.clear(); // 기존 arrayList 비우기
                //arrayList.addAll(arrayListSearched); // 필터링된 데이터로 arrayList 업데이트
                adapter.notifyDataSetChanged(); // 변경된 데이터셋을 RecyclerView에 알림
            }
        });
        //

        //만능 chatgpt
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UID = editText1.getText().toString();
                text = editText2.getText().toString();

                // 시간을 얻는 방식 변경
                long unixTime = System.currentTimeMillis();
                Date date = new Date(unixTime);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                String time = simpleDateFormat.format(date);


                DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("Post").child("NormalPost");
//중복은 실패시켜보아요
                    // 이미 존재하는지 확인하는 쿼리
                postReference.orderByChild("UID").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // 이미 존재하는 ID인 경우 처리
                            // 사용자에게 알리거나 다른 ID를 입력하도록 안내
                            // 예를 들어, Toast 메시지를 통해 사용자에게 알림
                            Toast.makeText(getApplicationContext(), "이미 존재하는 ID입니다. 다른 ID를 사용해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // 존재하지 않는 ID인 경우 회원가입 처리
                            // usersRef.push().setValue(new User(userID, password)); // 회원가입 처리 예시
                            // 예시에서는 User 클래스를 새로운 사용자로 추가하는 것으로 가정하고 있습니다.
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("UID", UID);
                            data.put("text", text);
                            data.put("time", time);

                            textView1.setText("UID = "+ UID + "\n" +"text = " + text + "\n" +"time = " + time);
                            editText1.setText(null);
                            editText2.setText(null);

                            postReference.push().setValue(data)

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // 성공적으로 데이터가 추가된 경우 처리할 작업
                                            Toast.makeText(getApplicationContext(), "데이터가 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // 데이터 추가 실패 시 처리할 작업
                                            Toast.makeText(getApplicationContext(), "데이터 추가 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 에러 처리
                        Log.e("Database Error", "Error: " + databaseError.getMessage());
                    }
                });
            }

                //

                //Hash를 만들어서 값들을 넣는다.
            /*
                HashMap<String, Object> data = new HashMap<>();
                data.put("UID", UID);
                data.put("text", text);
                data.put("time", time);

                textView1.setText("UID = "+ UID + "\n" +"text = " + text + "\n" +"time = " + time);
                editText1.setText(null);
                editText2.setText(null);

                postReference.push().setValue(data)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // 성공적으로 데이터가 추가된 경우 처리할 작업
                                Toast.makeText(getApplicationContext(), "데이터가 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // 데이터 추가 실패 시 처리할 작업
                                Toast.makeText(getApplicationContext(), "데이터 추가 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

             */

        });

/*
        //Post하위를 읽어오자
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot datasnapshot, @Nullable String previousChildName) {
                arrayList.clear();
                for(DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    arrayList.add(post);
                }

                // 데이터를 가져온 후에 textView에 표시하는 로직
                if (arrayList.size() > 0) {
                    for (int i=0;i<arrayList.size(); i++) {
                        Log.e("osslog1",arrayList.get(i).toString());
                    }

                } else {
                    // 데이터가 없을 때 처리할 내용
                }
                //Log.e("osslog",snapshot.toString() + "added");
                //textView1.setText(snapshot.getValue().toString());
            }
            // 새로운 하위 데이터가 추가될 때 호출됨
            // dataSnapshot을 이용하여 추가된 데이터를 처리

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("osslog", snapshot.toString() + "changed");
            }
            // 하위 데이터가 변경될 때 호출됨
            // dataSnapshot을 이용하여 변경된 데이터를 처리

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.e("osslog",snapshot.toString() + "removed");
            }
            // 하위 데이터가 제거될 때 호출됨
            // dataSnapshot을 이용하여 제거된 데이터를 처리
            // 제거할 때는 (주의사항: 매개인자로 String이 필요없다!!!)
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("osslog", snapshot.toString() + "moved");
            }
            // (order_by_child 혹은 order_by_value로 불러와서) 하위 데이터의 순서가 변경될 때 호출됨
            // dataSnapshot을 이용하여 순서가 변경된 데이터를 처리

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            // 데이터를 읽는 중에 에러가 발생할 때 호출됨
        };
        database.getReference("post").child("NormalPost").addChildEventListener(childEventListener);
*/
        //데이터 읽어와서 배열에 저장
        /*
        database.getReference("Post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    arrayList.add(post);
                }

                // 데이터를 가져온 후에 textView에 표시하는 로직
                if (arrayList.size() > 0) {
                    for (int i=0;i<arrayList.size(); i++) {
                        Log.e("osslog1",arrayList.get(i));
                    }

                } else {
                    // 데이터가 없을 때 처리할 내용
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 오류 처리
            }

        }); //뭐노ㅗ
        */

        //연습1: DB작동 확인을 위한 쓰기
/*
       myRef.setValue("Hello, World!");
*/
        //연습2: child를 이용한 Node 생성
/*
        myRef.child("message1").setValue("Hello, World! changed2");
        myRef.child("message2").setValue("Hello, World!2");
        myRef.child("message3").child("message3-1").child("message3-2").setValue("Hello. World3");
*/
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
        for(int i=10 ;i>0; i--) {
            myRef.child("room1").child("messages").push().setValue("msg"+i);
        }
*/
        //정렬하기(안된다..)
        //1. 자식으로 정렬
/*
        Query query = myRef.child("room1").orderByChild("messages");
*/
        // 2. 키값에 따른 정렬
/*
        Query query = myRef.child("room1").child("messages").orderByKey();
*/
        // 3. Value값에 따른 정렬
/*
        Query query = myRef.child("room1").child("messages").orderByValue();
*/

        //연습6: 데이터베이스를 읽어오는 방법( ValueEventListener()를 이용한다)

        /*
        ValueEventListener postListener = new ValueEventListener() {
            //ValueEventListener: 해당 위치의 값이 바뀌거나 새로생겼을 떄 값을 읽어오는 Listener
            // Listener를 지우기 전까지 계속해서 값의 update를 대기하며 값을 수신한다.
            //이벤트 콜백:onDataChange
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // ..

                Log.e("osslog1",dataSnapshot.toString());
                // 결과: key = message, value = Hello, World! changed
                Log.e("osslog2",dataSnapshot.getValue().toString());
                // 결과 Hello, World! changed
                Log.e("osslog3",dataSnapshot.getKey());
                // 결과: message
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        myRef.child("message1").addValueEventListener(postListener);
        //값의 update를 기다린다.
        //myRef(default Node) 자식의 message1의 값이 변경되면 읽어온다.
        // 막내Node여야 한다.
        */


        //연습 7: childEventListener
        // (설정된 Node밑에있는 자식 Node들을 하니씩 Snapshot으로 가져오는 Listener)
        // 자식 Node가
        // 1) 추가될 때 (onChildAdded())
        // 2) 바뀔 때 (onChildChanged())
        // 3) 없어졌을 때(onChildRenoved())
        // 4) 옮겨졌을 때 (onChildMoved())
        //textView1.setText(myRef.getValue().toString());
/*
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("osslog",snapshot.toString() + "added");
                //textView1.setText(snapshot.getValue().toString());
            }
            // 새로운 하위 데이터가 추가될 때 호출됨
            // dataSnapshot을 이용하여 추가된 데이터를 처리

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("osslog", snapshot.toString() + "changed");
            }
            // 하위 데이터가 변경될 때 호출됨
            // dataSnapshot을 이용하여 변경된 데이터를 처리

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.e("osslog",snapshot.toString() + "removed");
            }
            // 하위 데이터가 제거될 때 호출됨
            // dataSnapshot을 이용하여 제거된 데이터를 처리
            // 제거할 때는 (주의사항: 매개인자로 String이 필요없다!!!)
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("osslog", snapshot.toString() + "moved");
            }
            // (order_by_child 혹은 order_by_value로 불러와서) 하위 데이터의 순서가 변경될 때 호출됨
            // dataSnapshot을 이용하여 순서가 변경된 데이터를 처리

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            // 데이터를 읽는 중에 에러가 발생할 때 호출됨
        };
        myRef.addChildEventListener(childEventListener);
        */
        //1. myRef.child("room1")일 때 -> 10개{ key = -NjzNx2gHg7nDcTqojUR, value = 테스트중이야 }
        //2. myRef.child("room1")일 때 -> 1개에 value가 여러개 { key = message, value = {~~=null, ~=fdfd ....}
        // 데이터베이스 참조에 ChildEventListener를 연결
        //!! myRef("default" Node -> room1 -> messages 하위 (key, value)와 연결시킨다!!
        //myRef(default Node) -> "room1" -> "message"아래 n개의 (key , value)가 출력된다.


        // 연습8: database에 있는 값을 지우는 방법( setValue(null)을 해준다)
        /*
        myRef.child("userinfo2").child("id").setValue(null);
        myRef.setValue(null); //싹다 지워짐
        */


        //연습9: Listener를 지우는 방법
/*
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("osslog",snapshot.toString() + "added");
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("osslog", snapshot.toString() + "changed");
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.e("osslog",snapshot.toString() + "removed");
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("osslog", snapshot.toString() + "moved");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef.child("room1").child("messages").addChildEventListener(childEventListener);

        myRef.child("room1").child("messages").removeEventListener(childEventListener);
        //같은 경로에서 사용한 EventListener을 넣어주면 callback 대기를 종료한다.
*/
    }
}