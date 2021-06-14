package com.mouad.trackapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mouad.trackapp.MainActivity;
import com.mouad.trackapp.MessageActivity;
import com.mouad.trackapp.Model.User;
import com.mouad.trackapp.R;
import com.mouad.trackapp.RegisterActivity;

import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    private Boolean ischat;

    public UserAdapter(Context mContext,List<User> mUsers,Boolean ischat){
        this.mContext=mContext;
        this.mUsers=mUsers;
        this.ischat=ischat;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user=mUsers.get(position);
        holder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);

        }




        if (ischat){
            if (user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else{
                holder.img_off.setVisibility(View.VISIBLE);
                holder.img_on.setVisibility(View.GONE);
            }
        }
        else{
            holder.img_off.setVisibility(View.GONE);
            holder.img_on.setVisibility(View.GONE);
        }

        FirebaseUser fuser1= FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(fuser1.getUid());
        ((DatabaseReference) rootRef).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(user.getId())) {
                    holder.remove_friend.setVisibility(View.VISIBLE);
                    holder.add_friend.setVisibility(View.GONE);
                }else{
                    holder.remove_friend.setVisibility(View.GONE);
                    holder.add_friend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("id",user.getId());
                hashMap.put("email",user.getEmail());
                hashMap.put("username",user.getUsername());
                hashMap.put("status",user.getStatus());
                hashMap.put("imageURL",user.getImageURL());

                reference.child("Friends").child(fuser1.getUid()).child(user.getId()).setValue(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                //Toast.makeText(null,"added",Toast.LENGTH_SHORT).show();
                            }
                        }
                );



            }
        });

        holder.remove_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference reference=FirebaseDatabase.getInstance().getReference();




                reference.child("Friends").child(fuser1.getUid()).child(user.getId()).removeValue();

            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mContext, MessageActivity.class);
                String useriid=user.getId();
                intent.putExtra("id",useriid);



                mContext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        private ImageButton  remove_friend;
        private ImageButton  invited_friend;
        private ImageButton  add_friend;
        private TextView  info;
        private ImageView img_on;
        private ImageView img_off;

        public ViewHolder(View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);
            img_on=itemView.findViewById(R.id.img_on);
            img_off=itemView.findViewById(R.id.img_off);
            info=itemView.findViewById(R.id.info);
            add_friend=itemView.findViewById(R.id.add_friend);
            invited_friend=itemView.findViewById(R.id.invited_friend);
            remove_friend=itemView.findViewById(R.id.remove_friend);

        }
    }
}
