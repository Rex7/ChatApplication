package com.example.regischarles.fcmdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendRequestToServer {

    RequestQueue myRequestQueue = VolleySingle.getInstance().getRequestQueue();
    Context ctx;
    SessionManage sessionManage;
    String userInput;
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    ArrayList<Message> chatMessages;

public SendRequestToServer(){}
    public SendRequestToServer(Context ctx ,SessionManage sessionManage,String userInput,RecyclerView recyclerView,ChatAdapter chatAdapter,ArrayList<Message>chatMessages) {
       this.ctx=ctx;
       this.sessionManage=sessionManage;
       this.userInput=userInput;
       this.chatMessages=chatMessages;
       this.recyclerView=recyclerView;
       this.chatAdapter=chatAdapter;
    }

    public void sendToServer(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rexmyapp.000webhostapp.com/notification.php", new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
              Log.v("ResponseServer",response);
              int userid=Integer.parseInt(sessionManage.getUserDetail().get("userId"));
              Log.v("payloadData",""+chatMessages.size());
//             chatMessages.add(new Message(userid,userInput));
//             chatAdapter.notifyDataSetChanged();
//             recyclerView.smoothScrollToPosition(chatAdapter.getItemCount()-1);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx.getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> data = new HashMap<>();
                data.put("chatMessage",userInput);
                data.put("userId",sessionManage.getUserDetail().get("userId") );


                return data;

            }
        };
        myRequestQueue.add(stringRequest);


    }
    public void notificationFromServer(String message,String userId){
   if(chatMessages==null){
       Log.v("payloadData","func"+userId);
       Log.v("payloadData","func"+message);
       Log.v("payloadData","its empty");
       Log.v("payloadData",""+chatMessages.get(0).getMessage());

   }
   else{
       chatMessages.add(new Message(Integer.parseInt(userId),message));
       chatAdapter.notifyDataSetChanged();
       recyclerView.smoothScrollToPosition(chatAdapter.getItemCount()-1);
       Log.v("payloadData","its empty");
   }


    }
}
