package com.eugeneponomarev.textmessagequickblox.Adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<QBUser> qbUserArrayList;

    public ContactsAdapter(Context context, ArrayList<QBUser> qbUserArrayList) {
        this.context = context;
        this.qbUserArrayList = qbUserArrayList;
    }

    @Override
    public int getCount() {
        return qbUserArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return qbUserArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_contacts, null);

            ImageView imageContacts;
            TextView textViewListContacts_NameUsers;
            TextView textViewListContacts_status;

            textViewListContacts_NameUsers = (TextView) view.findViewById(R.id.textViewListContacts_NameUsers);
            textViewListContacts_status = (TextView) view.findViewById(R.id.textViewListContacts_status);

            imageContacts = (ImageView) view.findViewById(R.id.imageContacts);

            textViewListContacts_NameUsers.setText(qbUserArrayList.get(position).getFullName());

            ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
            int randomColor = colorGenerator.getRandomColor();

            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .round();

            TextDrawable drawable = builder.build(textViewListContacts_NameUsers.getText().toString().substring(0,2).toUpperCase(), randomColor);

            imageContacts.setImageDrawable(drawable);

        }

        return view;
    }

}
