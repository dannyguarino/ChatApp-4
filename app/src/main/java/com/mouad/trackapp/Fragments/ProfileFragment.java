package com.mouad.trackapp.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mouad.trackapp.ChangePasswordActivity;
import com.mouad.trackapp.FirstActivity;
import com.mouad.trackapp.MainActivity;
import com.mouad.trackapp.Model.User;
import com.mouad.trackapp.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    CircleImageView my_image_profile;
    TextView my_username;
    DatabaseReference databasereference;
    FirebaseUser firebaseuser;
    Button button_change_password;

    StorageReference storageReferencefirebase;
    private static int IMAGE_REQUEST=1;
    private Uri imagelink;
    private StorageTask uploadTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        my_image_profile=view.findViewById(R.id.profile_image);
        my_username=view.findViewById(R.id.username);

        storageReferencefirebase= FirebaseStorage.getInstance().getReference().child("uploads");

        firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        databasereference=FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseuser.getUid());
        databasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                my_username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    int i=R.mipmap.ic_launcher;
                    my_image_profile.setImageResource(i);
                }else{
                    Glide.with(getContext()).load(user.getImageURL()).into(my_image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        my_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        button_change_password=view.findViewById(R.id.btn_change_password);
        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    private void openImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("uploading...");
        pd.show();

        if (imagelink != null){
            final StorageReference fileReference=storageReferencefirebase.child(System.currentTimeMillis()+"."+getFileExtension(imagelink));
            uploadTask=fileReference.putFile(imagelink);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();


                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadlink = task.getResult();
                        String mUri=downloadlink.toString();
                        databasereference=FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseuser.getUid());
                        HashMap<String,Object> mapupdated=new HashMap<>();
                        mapupdated.put("imageURL",mUri);
                        databasereference.updateChildren(mapupdated);
                        pd.dismiss();
                    }else{
                        Toast.makeText(getContext(), getString(R.string.failed),Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else{
            Toast.makeText(getContext(), getString(R.string.notsetected),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData()!=null){
            imagelink=data.getData();
            if (uploadTask!=null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), getString(R.string.inprogress),Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
    }
}