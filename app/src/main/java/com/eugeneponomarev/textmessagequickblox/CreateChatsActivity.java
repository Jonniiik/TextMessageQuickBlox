package com.eugeneponomarev.textmessagequickblox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.eugeneponomarev.textmessagequickblox.Adapter.CreateChatsAdapter;
import com.eugeneponomarev.textmessagequickblox.Common.Common;
import com.eugeneponomarev.textmessagequickblox.Holder.QBUsersHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class CreateChatsActivity extends AppCompatActivity {

    @Override
    public void onResume() {
        super.onResume();
        retrieveContacts();
    }

    ListView listContacts;
    FloatingActionButton buttonAddContactsInChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chats);


        retrieveContacts();

        listContacts = (ListView) findViewById(R.id.listContacts);
        listContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        buttonAddContactsInChat = (FloatingActionButton) findViewById(R.id.buttonAddContactsInChat);
        buttonAddContactsInChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChatAndAddUsers();

            }
        });
    }

    private void createChatAndAddUsers() {
        int countChoise = listContacts.getCount();

        if (listContacts.getCheckedItemPositions().size() == 1)
            createPrivateChat(listContacts.getCheckedItemPositions());
        else if (listContacts.getCheckedItemPositions().size() > 1)
            createGroupChat(listContacts.getCheckedItemPositions());
        else
            Toast.makeText(CreateChatsActivity.this, "Please select friend to chat", Toast.LENGTH_SHORT).show();
    }

    private void retrieveContacts() {
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUsersHolder.getInstance().putUsers(qbUsers);

                ArrayList<QBUser> qbUserWithoutCurrent = new ArrayList<>();
                for (QBUser user : qbUsers) {
                    if (!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin()))
                        qbUserWithoutCurrent.add(user);
                }

                CreateChatsAdapter createChatsAdapter = new CreateChatsAdapter(CreateChatsActivity.this, qbUserWithoutCurrent);
                listContacts.setAdapter(createChatsAdapter);
                createChatsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR", e.getMessage());
            }
        });
    }

    private void createGroupChat(SparseBooleanArray checkedItemPositions) {
        final ProgressDialog mDialog = new ProgressDialog(CreateChatsActivity.this);
        mDialog.setMessage("Please waiting ...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        int countChoice = listContacts.getCount();
        ArrayList<Integer> occupantIdsList = new ArrayList<>();
        for (int i = 0; i < countChoice; i++) {
            if (checkedItemPositions.get(i)) {
                QBUser user = (QBUser) listContacts.getItemAtPosition(i);
                occupantIdsList.add(user.getId());
            }
        }

        //Create Chat Dialog
        QBChatDialog dialog = new QBChatDialog();
        dialog.setName(Common.createChatDialogName(occupantIdsList));
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdsList);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                mDialog.dismiss();
                Toast.makeText(CreateChatsActivity.this, "Create chat successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR", e.getMessage());
            }
        });

        //startActivity(new Intent(CreateChatsActivity.this, MainActivity.class));
    }

    private void createPrivateChat(SparseBooleanArray checkedItemPositions) {

        int countChoice = listContacts.getCount();
        for (int i = 0; i < countChoice; i++) {
            if (checkedItemPositions.get(i)) {
                QBUser user = (QBUser) listContacts.getItemAtPosition(i);

                QBChatDialog dialog = DialogUtils.buildPrivateDialog(user.getId());
                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        Toast.makeText(CreateChatsActivity.this, "Create Private chat successfully", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(CreateChatsActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("ERROR", e.getMessage());
                    }
                });
            }
        }

    }
}
