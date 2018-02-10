package com.example.ajay.livechat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by ajay on 19/1/18.
 */

public class Chat_room extends AppCompatActivity {
    private String temp_key;
    private TextView textmsg;
    private TextView textmsg2;




   // String[] arry={"dasdsd","xzxs"};

    CheckBox mCheckBox;
    EmojiconEditText inputmsg, emojiconEditText2;
    EmojiconTextView textView;
    ImageView emojiButton;
    ImageView btnsendsms;
    View rootView;
    EmojIconActions emojIcon;




      //  private Button btnsendsms;
      //  private EditText inputmsg;
        private String user_name,room_name;
        private DatabaseReference myRef;
        String propic;
        ImageView urlrec;
    ImageView bmp;

    ListView listView;
    //  private ArrayAdapter<String>arrayAdapter;
       // private ArrayList<String>arrayList=new ArrayList<>();





    @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.chat_room);

       // scrollMyListViewToBottom();
         listView=(ListView)findViewById(R.id.customlist);


            //adapter class


            //add data and view it

          //  ListView  lsNews=(ListView)findViewById(R.id.LVNews);
          //  lsNews.setAdapter(myadapter);//intisal with data



        rootView = findViewById(R.id.root_view);


        emojiButton = (ImageView) findViewById(R.id.emoji_btn);

            btnsendsms=(ImageView)findViewById(R.id.submit_btn);
            inputmsg=(EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        //    textmsg=(TextView) findViewById(R.id.usernametext);
          //  textmsg2=(TextView) findViewById(R.id.usertextview);


        emojIcon = new EmojIconActions(this, rootView, inputmsg, emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });





            user_name=getIntent().getExtras().get("user_name").toString();
            room_name=getIntent().getExtras().get("room_name").toString();
        propic=getIntent().getExtras().get("propicurl").toString();

        URL url = null;
        try {
            url = new URL(propic);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  Picasso.with(Chat_room.this).load(propic).into(urlrec);


        //  Picasso.with(Chat_room.this).load(propic).into(profileico);

            setTitle("Room -"+room_name);


        //    msglist=(ListView)findViewById(R.id.listviewmsg);
          //  arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
         //   msglist.setAdapter(arrayAdapter);




         //   ListAdapter customListAdapter = new customadapter(this,arry);
         //   ListView listView=(ListView) findViewById(R.id.customlist);
         //   listView.setAdapter(customListAdapter);
















        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sentmessage);

            myRef= FirebaseDatabase.getInstance().getReference().child(room_name);

            btnsendsms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,Object>map=new HashMap<String, Object>();
                    temp_key=myRef.push().getKey();
                    myRef.updateChildren(map);

                    DatabaseReference message_root=myRef.child(temp_key);
                    Map<String,Object>map2=new HashMap<String,Object>();
                    map2.put(user_name,"");
                    for(int i=0;i<100;i++) {
                        map2.put(inputmsg.getText().toString(), "");
                        message_root.updateChildren(map2);
                    }




                    inputmsg.setText("");

                    mp.start();


                }
            });


            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    append_chat_conv(dataSnapshot);




                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    append_chat_conv(dataSnapshot);


                    final MediaPlayer mp1 = MediaPlayer.create(Chat_room.this, R.raw.messagereceive);
                    mp1.start();

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }


    ArrayList<customadapter>    listnewsData = new ArrayList<customadapter>();
    MyCustomAdapter myadapter;


    private boolean lemein;
    private String chat_msg,chat_user_name;

    private void append_chat_conv(DataSnapshot dataSnapshot) {

     //   Set<String> set=new HashSet<String>();
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()){
            chat_user_name =(String )(((DataSnapshot)iterator.next()).getKey());

            chat_msg=(String )(((DataSnapshot)iterator.next()).getKey());



            listnewsData.add(new customadapter(1,chat_user_name,chat_msg,lemein,bmp));
            myadapter=new MyCustomAdapter(listnewsData);
            myadapter.notifyDataSetChanged();

            listView.setAdapter(myadapter);



//            textmsg.append(chat_user_name +"\n");
  //          textmsg2.append(chat_msg +"\n");

        }
    //    arrayList.clear();
     //   arrayList.addAll(set);

     //   arrayAdapter.notifyDataSetChanged();

}
    private void scrollMyListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(myadapter.getCount() - 1);
            }
        });
    }

    View myView;



    private class MyCustomAdapter extends BaseAdapter {

        public  ArrayList<customadapter>  listnewsDataAdpater ;

        public MyCustomAdapter(ArrayList<customadapter> listnewsDataAdpater) {
            this.listnewsDataAdpater=listnewsDataAdpater;
        }


        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public customadapter getItem(int position) {
            return listnewsDataAdpater.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            final   customadapter s = listnewsDataAdpater.get(position);
            LayoutInflater mInflater = getLayoutInflater();

            //if(listnewsDataAdpater.get(position).getleMien())
            if(user_name==chat_user_name){
                 myView = mInflater.inflate(R.layout.customrow, null);}
    else {

         myView = mInflater.inflate(R.layout.customrowleft, null);}



                TextView username=( TextView)myView.findViewById(R.id.usernametext);
                username.setText(s.username);

                TextView message=( TextView)myView.findViewById(R.id.msgtext);
                message.setText(s.messages);

                ImageView chaticon=(ImageView)myView.findViewById(R.id.chaticon);

                TextView timer=(TextView)myView.findViewById(R.id.time);

                String date = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

                timer.setText(date);

                //  url.setImageBitmap(s.url);

                Picasso.with(Chat_room.this).load(propic).into(chaticon);

                final ViewGroup.LayoutParams params = message.getLayoutParams();

                if (params != null) {
                    // params.height = 10;
                    //  params.width=50;
                }
                return myView;

           /* }else {
                View myView = mInflater.inflate(R.layout.customrowleft, null);





                final   customadapter s = listnewsDataAdpater.get(position);

            TextView username=( TextView)myView.findViewById(R.id.usernametext);
            username.setText(s.username);

            TextView message=( TextView)myView.findViewById(R.id.msgtext);
            message.setText(s.messages);

            ImageView chaticon=(ImageView)myView.findViewById(R.id.chaticon);


                TextView timer=(TextView)myView.findViewById(R.id.time);

                String date2 = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

                timer.setText(date2);


          //  url.setImageBitmap(s.url);

            Picasso.with(Chat_room.this).load(propic).into(chaticon);

            final ViewGroup.LayoutParams params = message.getLayoutParams();



            if (params != null) {
               // params.height = 10;
              //  params.width=50;
            }
            return myView;


        }*/

        }
    }
}
