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
import com.mouad.trackapp.Adapter.FriendAdapter;
import com.mouad.trackapp.Adapter.UserAdapter;
import com.mouad.trackapp.Model.Friend;
import com.mouad.trackapp.Model.User;
import com.mouad.trackapp.R;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private List<Friend> mUsers;
    EditText search_users;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView=view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers=new ArrayList<>();
        readUsers();
        search_users=view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchUsers(String toString) {
        FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference().child("Friends").orderByChild("username")
                .startAt(toString).endAt(toString+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //mUsers.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Friend user = snapshot1.getValue(Friend.class);
                    if (!user.getId().equals(fuser.getUid())) {
                        mUsers.add(user);
                    }
                }
                friendAdapter = new FriendAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(friendAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readUsers() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Friends").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Friend user=snapshot.getValue(Friend.class);
                    assert user!=null;
                    assert firebaseUser !=null;
                    if(!user.getId().equals(firebaseUser.getUid())){
                        mUsers.add(user);
                    }
                }
                friendAdapter=new FriendAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(friendAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}