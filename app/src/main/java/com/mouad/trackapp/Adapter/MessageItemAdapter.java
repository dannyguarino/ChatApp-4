package com.mouad.trackapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mouad.trackapp.Model.Chat;
import com.mouad.trackapp.R;

import java.util.List;

public class MessageItemAdapter extends RecyclerView.Adapter<MessageItemAdapter.ViewHolder> {

    public final static int MSG_LEFT=0;
    public final static int MSG_RIGHT=1;
    private Context context;
    private List<Chat> listChat;
    private String imagelink;
    FirebaseUser firebaseuser;

    public MessageItemAdapter(Context context, List<Chat> listChat, String imagelink){
        this.context=context;
        this.listChat=listChat;
        this.imagelink=imagelink;
    }

    @NonNull
    @Override
    public MessageItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==MSG_RIGHT){

        View view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
        return new MessageItemAdapter.ViewHolder(view);}
        else {
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new MessageItemAdapter.ViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageItemAdapter.ViewHolder holder, int position) {
        Chat chatitem=listChat.get(position);
        holder.show_message.setText(chatitem.getMessage());
        if(imagelink.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imagelink).into(holder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return listChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);

        }
    }

    @Override
    public int getItemViewType(int positon){
        firebaseuser=FirebaseAuth.getInstance().getCurrentUser();
        if(listChat.get(positon).getSender().equals(firebaseuser.getUid())){
            return MSG_RIGHT;
        }
        else{
            return MSG_LEFT;
        }
    }

}
