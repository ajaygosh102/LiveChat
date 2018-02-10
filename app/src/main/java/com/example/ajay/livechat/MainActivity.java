package com.example.ajay.livechat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.refresh:
                progressBar.setVisibility(View.VISIBLE);

                handler=new Handler();
                runnable=new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        progressBar.setVisibility(View.GONE);
                    }
                };

                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(runnable);
                    }
                },3000,1000);
                Toast.makeText(getApplicationContext(),"Refreshing",Toast.LENGTH_SHORT).show();
                break;
            case R.id.createnew:







                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);

                alert.setMessage("Enter the Room Name");
                alert.setTitle("Create New Chatroom");


                final EditText input = new EditText(MainActivity.this);
                alert.setView(input);

                alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                        String inputName = input.getText().toString();
                        Toast.makeText(getApplicationContext(), "New Chatroom created ", Toast.LENGTH_SHORT).show();

                        Map<String,Object>map=new HashMap<String,Object>();
                        //add more rooms here
                        map.put(inputName,"");
                        myRef.updateChildren(map);
                    }

                });alert.show();







               // Toast.makeText(getApplicationContext(),"starred",Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                Intent intent =new Intent(MainActivity.this,login_Activity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Logout sucessfully",Toast.LENGTH_LONG).show();
                break;

            case R.id.share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://github.com/ajaygosh102";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ChatRooM");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));


               // Toast.makeText(getApplicationContext(),"all mail",Toast.LENGTH_LONG).show();
                break;
            case R.id.about:

                LayoutInflater layoutInflater=getLayoutInflater();
                View view=layoutInflater.inflate(R.layout.about,null);
                TextView textView=(TextView)view.findViewById(R.id.abouttv);
                textView.setText(R.string.about);

                final Toast toast=new Toast(getApplicationContext());
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.FILL,0,0);
                toast.show();

                    new CountDownTimer(20000, 2000)
                    {

                        public void onTick(long millisUntilFinished) {toast.show();}
                        public void onFinish() {toast.show();}

                    }.start();


               // Toast.makeText(getApplicationContext(),"trash",Toast.LENGTH_LONG).show();
                break;
                    }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    ListView chatrooms;
    String usernamerec;
    String email;
    String urlrec;
   // Uri propic;
  //  ImageView profileico;
    ProgressBar progressBar;
    Handler handler;
    Runnable runnable;
    Timer timer;

    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private ArrayAdapter<String>arrayAdapter;
    private ArrayList<String>lisofrooms=new ArrayList<>();
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drower);


        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);



        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
      // setSupportActionBar(toolbar);
        progressBar=(ProgressBar)findViewById(R.id.loading);
        //profileico=(ImageView)navigationView.findViewById(R.id.drawericon);
        usernamerec=getIntent().getExtras().get("username").toString();
        email=getIntent().getExtras().get("email").toString();
        urlrec=getIntent().getExtras().get("propicurl").toString();
//      Uri  propic=getIntent().getData();



        Toast.makeText(this,usernamerec,Toast.LENGTH_LONG).show();



        //drawerlayout


      //  ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();






        View hView =  navigationView.inflateHeaderView(R.layout.navigation_header);
        ImageView imgvw = (ImageView)hView.findViewById(R.id.drawericon);
        TextView usernametv=(TextView)hView.findViewById(R.id.usertv);
        TextView emailtv1=(TextView)hView.findViewById(R.id.emailtv);


        emailtv1.setText(email);
        usernametv.setText(usernamerec);
        Picasso.with(MainActivity.this).load(urlrec).into(imgvw);




        chatrooms=(ListView)findViewById(R.id.listview);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lisofrooms);
        chatrooms.setAdapter(arrayAdapter);


        progressBar.setVisibility(View.VISIBLE);

        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
          timer.cancel();
          progressBar.setVisibility(View.GONE);
            }
        };

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },4000,1000);



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set=new HashSet<String>();
                Iterator iterator=dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                lisofrooms.clear();
                lisofrooms.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



        chatrooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(MainActivity.this,Chat_room.class);
                intent.putExtra("room_name",((TextView)view).getText().toString());
                intent.putExtra("user_name",usernamerec);
                intent.putExtra("propicurl",urlrec);
                startActivity(intent);
            }
        });



    }

  /*  public void savebutton(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("Enter Your Message");
        alert.setTitle("Enter Your Title");

        alert.setView(edittext);

        alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
              //  Editable YouEditTextValue = edittext.getText();
                //OR
                String YouEditTextValue = edittext.getText().toString();
            }
        });alert.show();
    }*/

    // String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    //time.setText(currentDateTimeString);

       // Map<String,Object>map=new HashMap<String,Object>();
        //add more rooms here
       // map.put(usernamerec,"");
       // myRef.updateChildren(map);

}

