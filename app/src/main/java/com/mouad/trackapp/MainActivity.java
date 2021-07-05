package com.mouad.trackapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mouad.trackapp.Fragments.FriendsFragment;
import com.mouad.trackapp.Fragments.ProfileFragment;
import com.mouad.trackapp.Fragments.UsersFragment;
import com.mouad.trackapp.Model.User;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView usermane;
    FirebaseUser fUser;
    DatabaseReference databasereference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isNetworkAvailable()){
            startActivity(new Intent(MainActivity.this,GameMenuActivity.class));
            System.out.println("connected\n\n\n\n\n\n\n");
        }

        Toolbar mytoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle(R.string.Profile);
        getSupportActionBar().show();


        profileImage= findViewById(R.id.profile_image);
        usermane=findViewById(R.id.username);
        fUser= FirebaseAuth.getInstance().getCurrentUser();

        databasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid());
        databasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                usermane.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        TabLayout tabLayout=findViewById(R.id.tab_layout);
        ViewPager viewPager=findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());


        viewPagerAdapter.addFragment(new FriendsFragment(),getString(R.string.Friends));
        viewPagerAdapter.addFragment(new ProfileFragment(),getString(R.string.Profile));
        viewPagerAdapter.addFragment(new UsersFragment(),getString(R.string.users));


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuitem) {
        switch(menuitem.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1=new Intent(this,SpashScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
                return true;
        }
        return false;
    }
    public static class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private  ArrayList<String> titles;

         public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();


        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
    private void changestatus(String statusNewValue){

        databasereference=FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid());
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

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }



}