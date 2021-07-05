package com.mouad.trackapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mouad.trackapp.Adapter.MessageItemAdapter;
import com.mouad.trackapp.Model.Chat;
import com.mouad.trackapp.Model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference databasereference;
    Intent intent;
    ImageButton button_send;
    EditText message;
    List<Chat> listChat;
    MessageItemAdapter messageAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar mytoolbar;

        //find the toolbar item
        mytoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle(R.string.Messages);

        //Viewing the tool bar item
        getSupportActionBar().show();


        recyclerView=findViewById(R.id.recycler_view);
        //Avoid unnecessary layout passes by setting setHasFixedSize to true when changing the contents of the adapter does not change it's height or the width.
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());

        //populates myrecycleview from the top
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        profileImage=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        button_send=findViewById(R.id.btn_send);
        message=findViewById(R.id.text_send);
        intent=getIntent();
        String userid=intent.getStringExtra("id");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        button_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String message1=message.getText().toString();
                if(!message1.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,message1);
                }else{
                    Toast.makeText(MessageActivity.this,R.string.empty_message,Toast.LENGTH_SHORT).show();
                }
                message.setText("");
            }
        });





        databasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        databasereference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profileImage.setImageResource(R.mipmap.ic_launcher);

                }else{
                    //set profile image
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profileImage);

                }

                readMessages(firebaseUser.getUid(),userid,user.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void sendMessage(String senderId,String receiverId,String message){

        DatabaseReference datareference=FirebaseDatabase.getInstance().getReference().child("Chats");
        HashMap<String,Object> hashMapfirebase=new HashMap<>();

        hashMapfirebase.put("sender",senderId);
        hashMapfirebase.put("receiver",receiverId);
        hashMapfirebase.put("message",message);
        //push give an arbitaire id
        datareference.child("Chats").push().setValue(hashMapfirebase);
    }

    private void readMessages(String myId,String userId,String imagelink){
        listChat=new ArrayList<>();
        DatabaseReference datareference=FirebaseDatabase.getInstance().getReference().child("Chats").child("Chats");
        datareference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listChat.clear();
                Chat chatitem=new Chat();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    chatitem=snapshot.getValue(Chat.class);
                    if (chatitem.getReceiver().equals(myId) && chatitem.getSender().equals(userId) ||
                            chatitem.getReceiver().equals(userId) && chatitem.getSender().equals(myId)){
                        listChat.add(chatitem);
                    }
                    messageAdapter =new MessageItemAdapter(MessageActivity.this,listChat,imagelink);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void changestatus(String statusNewValue){

        databasereference=FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMapUpdated=new HashMap<>();
        hashMapUpdated.put("status",statusNewValue);
        databasereference.updateChildren(hashMapUpdated);
    }

    @Override
    protected void onResume(){
        super.onResume();
        changestatus("online");
    }

    @Override
    protected void onPause(){
        super.onPause();
        changestatus("offline");
    }

}


