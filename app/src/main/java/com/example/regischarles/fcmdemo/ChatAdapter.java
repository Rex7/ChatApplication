package com.example.regischarles.fcmdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ViewHolders>  {
   private ArrayList<Message> chatMessages;
    private int SELF=100;
  private  Context ctx;
    SessionManage sessionManage;
    public ChatAdapter(ArrayList<Message> chatMessages, Context mainActivity) {
        this.chatMessages=chatMessages;
        this.ctx=mainActivity;
        sessionManage = new SessionManage(ctx.getApplicationContext());


    }

    @Override
    public int getItemViewType(int position) {
 if(chatMessages.get(position).getUserId()==Integer.parseInt(sessionManage.getUserDetail().get("userId"))){
     return SELF;
 }
 else {
     return position;
 }

    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (i == SELF) {
            // self message
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_self, viewGroup, false);
        } else {
            // others message
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.other_chat, viewGroup, false);
        }


        return new ViewHolders(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolders viewHolders, int i) {
        Message message = chatMessages.get(i);
        viewHolders.message.setText(message.getMessage());
        viewHolders.timestamp.setText("- "+message.getName());


    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }
    public class ViewHolders extends RecyclerView.ViewHolder  {
        TextView name;
        Context ct;
        TextView message, timestamp;

        public ViewHolders(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
        }




            // bottomSheet.show(medicineActivity.getSupportFragmentManager(), bottomSheet.getTag());
            //  Toast.makeText(ct, " " + medNames.get(pos).getDrugName() + "\n Price " + medNames.get(pos).getPrice(), Toast.LENGTH_LONG).show();


        }



