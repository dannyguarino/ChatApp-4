package com.mouad.trackapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mouad.trackapp.Adapter.UserItemAdapter;
import com.mouad.trackapp.Model.User;
import com.mouad.trackapp.R;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {
    private RecyclerView myrecyclerView;
    private UserItemAdapter myuserAdapter;
    private List<User> listUsers;
    EditText search_string_users;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_users, container, false);
        myrecyclerView=view.findViewById(R.id.recycle_view);
        myrecyclerView.setHasFixedSize(true);
        myrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listUsers=new ArrayList<>();
        readAllUsers();
        search_string_users=view.findViewById(R.id.search_users);
        search_string_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsersByString(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchUsersByString(String toString) {
        FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username")
                .startAt(toString).endAt(toString+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listUsers.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        User user = snapshot1.getValue(User.class);
                        if (!user.getId().equals(fuser.getUid())) {
                            listUsers.add(user);
                        }
                    }
                myuserAdapter = new UserItemAdapter(getContext(), listUsers);
                    myrecyclerView.setAdapter(myuserAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readAllUsers() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);

                    if(!user.getId().equals(firebaseUser.getUid())){
                        listUsers.add(user);
                    }
                }
                myuserAdapter=new UserItemAdapter(getContext(),listUsers);
                myrecyclerView.setAdapter(myuserAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}