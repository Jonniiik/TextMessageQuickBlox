package com.eugeneponomarev.textmessagequickblox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.eugeneponomarev.textmessagequickblox.Adapter.ChatMessageAdapter;
import com.eugeneponomarev.textmessagequickblox.Common.Common;
import com.eugeneponomarev.textmessagequickblox.Holder.QBChatMessagesHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;

public class ChatMessageActivity extends AppCompatActivity {

    QBChatDialog qbChatDialog;
    ListView listMessage;
    ImageButton smileButton;
    ImageButton sendMessageButton;
    TextView editTextMessage;
    ChatMessageAdapter adapterChatMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        initComponents();
        initChatDialog();
        retrieveMessage();

        clickButton();
    }

    private void initComponents() {
        listMessage = (ListView) findViewById(R.id.listMessage);
        smileButton = (ImageButton) findViewById(R.id.smileButton);
        sendMessageButton = (ImageButton) findViewById(R.id.sendMessageButton);
        editTextMessage = (TextView) findViewById(R.id.editTextMessage);
    }

    private void initChatDialog() {
        qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(Common.DIALOG_EXTRA);
        qbChatDialog.initForChat(QBChatService.getInstance());

        QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {

            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                Log.e("ERROR", e.getMessage());
            }
        });

        qbChatDialog.addMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                QBChatMessagesHolder.getInstance().putMessage(qbChatMessage.getDialogId(), qbChatMessage);
                ArrayList<QBChatMessage> arrayListMessage = QBChatMessagesHolder.getInstance().getChatMessageByDialogId(qbChatMessage.getDialogId());
                adapterChatMessage = new ChatMessageAdapter(getBaseContext(), arrayListMessage);
                listMessage.setAdapter(adapterChatMessage);
                adapterChatMessage.notifyDataSetChanged();
            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                Log.e("ERROR", e.getMessage());
            }
        });


    }

    private void retrieveMessage() {
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(1000);

        if (qbChatDialog != null) {
            QBRestChatService.getDialogMessages(qbChatDialog, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    QBChatMessagesHolder.getInstance().putMessages(qbChatDialog.getDialogId(), qbChatMessages);
                    adapterChatMessage = new ChatMessageAdapter(getBaseContext(), qbChatMessages);
                    listMessage.setAdapter(adapterChatMessage);
                    adapterChatMessage.notifyDataSetChanged();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e("ERROR", e.getMessage());
                }
            });
        }
    }

    private void clickButton() {
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(editTextMessage.getText().toString());
                chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                chatMessage.setSaveToHistory(true);

                try {
                    qbChatDialog.sendMessage(chatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                QBChatMessagesHolder.getInstance().putMessage(qbChatDialog.getDialogId(), chatMessage);
                ArrayList<QBChatMessage> messages = QBChatMessagesHolder.getInstance().getChatMessageByDialogId(qbChatDialog.getDialogId());
                adapterChatMessage = new ChatMessageAdapter(getBaseContext(), messages);
                listMessage.setAdapter(adapterChatMessage);
                adapterChatMessage.notifyDataSetChanged();

                editTextMessage.setText(null);
                editTextMessage.setFocusable(true);
            }
        });

        smileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
