package com.eugeneponomarev.textmessagequickblox.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.eugeneponomarev.textmessagequickblox.R;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBChatDialog> qbChatDialogs;

    public ChatAdapter(Context context, ArrayList<QBChatDialog> qbChatDialogs) {
        this.context = context;
        this.qbChatDialogs = qbChatDialogs;
    }

    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatDialogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_chat_dialog, null);

            TextView textViewListChatDialog_NameChat;
            TextView textViewListChatDialog_LastMessage;

            ImageView imageChat;

            textViewListChatDialog_LastMessage = (TextView) view.findViewById(R.id.textViewListChatDialog_LastMessage);
            textViewListChatDialog_NameChat = (TextView) view.findViewById(R.id.textViewListChatDialog_NameChat);

            imageChat = (ImageView) view.findViewById(R.id.imageChat);

            textViewListChatDialog_LastMessage.setText(qbChatDialogs.get(position).getLastMessage());
            textViewListChatDialog_NameChat.setText(qbChatDialogs.get(position).getName());

            ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
            int randomColor = colorGenerator.getRandomColor();

            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .round();

            TextDrawable drawable = builder.build(textViewListChatDialog_NameChat.getText().toString().substring(0,2).toUpperCase(), randomColor);

            imageChat.setImageDrawable(drawable);
        }
        return view;
    }
}
