package com.mouad.trackapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mouad.trackapp.MessageActivity;
import com.mouad.trackapp.Model.Friend;
import com.mouad.trackapp.R;

import java.util.List;

public class FriendItemAdapter extends RecyclerView.Adapter<FriendItemAdapter.ViewHolder> {
    private Context context;
    private List<Friend> listUsers;


    public FriendItemAdapter(Context context, List<Friend> listUsers){
        this.context=context;
        this.listUsers=listUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.friend_item,parent,false);
        return new FriendItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend user=listUsers.get(position);
        holder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(context).load(user.getImageURL()).into(holder.profile_image);

        }

        


            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(user.getId()).child("status");

            final  String[] s = {""};


            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    s[0] =dataSnapshot.getValue(String.class);
                    System.out.println(s[0]);
                    if (s[0].equalsIgnoreCase("online")){
                        holder.imgon.setVisibility(View.VISIBLE);
                        holder.imgoff.setVisibility(View.GONE);
                    }else{
                        holder.imgoff.setVisibility(View.VISIBLE);
                        holder.imgon.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
















        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, MessageActivity.class);
                String useriid=user.getId();
                intent.putExtra("id",useriid);



                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        private ImageButton  remove_friend;
        private ImageButton  invited_friend;
        private ImageButton  add_friend;
        private TextView  info;
        private ImageView imgon;
        private ImageView imgoff;

        public ViewHolder(View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.my_profile_image);
            imgon=itemView.findViewById(R.id.my_img_on);
            imgoff=itemView.findViewById(R.id.my_img_off);
            info=itemView.findViewById(R.id.info);
            add_friend=itemView.findViewById(R.id.add_friend);
            invited_friend=itemView.findViewById(R.id.invited_friend);
            remove_friend=itemView.findViewById(R.id.remove_friend);

        }
    }
}