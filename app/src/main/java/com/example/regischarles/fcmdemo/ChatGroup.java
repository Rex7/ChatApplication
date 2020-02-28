package com.example.regischarles.fcmdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ChatGroup extends AppCompatActivity  implements View.OnClickListener{
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    ArrayList<Message> chatMessages=new ArrayList<>();
    String chatRoom;
    int chatRoomId;
    Button send;
    EditText enterMessage;
    SessionManage sessionManage;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);
        recyclerView=findViewById(R.id.recycleChat);
        send=findViewById(R.id.send);
        enterMessage=findViewById(R.id.enterMessage);
        sessionManage = new SessionManage(getApplicationContext());
        send.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatRoom=getIntent().getExtras().getString("chatRoom");
        chatRoomId=getIntent().getExtras().getInt("name");

        //setting a empty adpater

        recyclerView.setAdapter(chatAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        RequestQueue myRequestQueue = VolleySingle.getInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rexmyapp.000webhostapp.com/getChatMessages.php", new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                String message;


                try {
                    JSONArray jsonArray = new JSONArray(response);



                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jobject = jsonArray.getJSONObject(i);

                       chatMessages.add(new Message(jobject.getInt("UserId"),jobject.getString("message")));
                    }
                    Log.v("HomePageDemo","ArraySize"+chatMessages.size());


                    chatAdapter = new ChatAdapter(chatMessages, ChatGroup.this);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(chatAdapter.getItemCount()-1);






                } catch (JSONException e) {
                    e.printStackTrace();      }











            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> data = new HashMap<>();
                data.put("chatroomId",String.valueOf(chatRoomId) );


                return data;

            }
        };
        myRequestQueue.add(stringRequest);



broadcastReceiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
          handlePush(intent);
    }
};
    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }
    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("pushNotification"));


    }

    @Override
    public void onClick(View v) {
        Log.v("payloadData","chat messages"+chatMessages.size());
        SendRequestToServer sendRequestToServer=new SendRequestToServer(getApplicationContext(),sessionManage,enterMessage.getText().toString(),recyclerView,chatAdapter,chatMessages);
        sendRequestToServer.sendToServer();

        enterMessage.setText("");


    }

    private void handlePush(Intent intent){
        String message=intent.getExtras().getString("message");
        String title=intent.getExtras().getString("title");
        int userId=Integer.parseInt(title);
            chatMessages.add(new Message(userId,message));
            chatAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chatAdapter.getItemCount()-1);
            Toast.makeText(getApplicationContext(),"message"+message,Toast.LENGTH_LONG).show();
        Log.v("payloadData","babe"+message);



    }
}
