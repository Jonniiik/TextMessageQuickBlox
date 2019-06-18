package com.eugeneponomarev.textmessagequickblox.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eugeneponomarev.textmessagequickblox.Holder.QBUsersHolder;
import com.eugeneponomarev.textmessagequickblox.R;
import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

public class ChatMessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBChatMessage> qbChatMessages;

    public ChatMessageAdapter(Context context, ArrayList<QBChatMessage> qbChatMessages) {
        this.context = context;
        this.qbChatMessages = qbChatMessages;
    }

    @Override
    public int getCount() {
        return qbChatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (qbChatMessages.get(position).getSenderId().equals(QBChatService.getInstance().getUser().getId())) {
                view = inflater.inflate(R.layout.list_send_message, null);
                BubbleTextView bubbleTextView = (BubbleTextView) view.findViewById(R.id.bubbleTextMessageSend);
                bubbleTextView.setText(qbChatMessages.get(position).getBody());
            } else {
                view = inflater.inflate(R.layout.list_get_message, null);
                BubbleTextView bubbleTextMessageSend = (BubbleTextView) view.findViewById(R.id.bubbleTextMessageSend);
                bubbleTextMessageSend.setText(qbChatMessages.get(position).getBody());
                TextView messageUser = (TextView)view.findViewById(R.id.messageUser);
                messageUser.setText(QBUsersHolder.getInstance().getUserById(qbChatMessages.get(position).getSenderId()).getFullName());

            }

        }
        return view;
    }
}
