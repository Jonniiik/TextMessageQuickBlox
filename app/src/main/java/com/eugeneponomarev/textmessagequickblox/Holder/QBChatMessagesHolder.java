package com.eugeneponomarev.textmessagequickblox.Holder;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QBChatMessagesHolder {
    private static QBChatMessagesHolder instance;
    private HashMap<String, ArrayList<QBChatMessage>> qbChatMessageArrayList;

    public static synchronized QBChatMessagesHolder getInstance() {
        QBChatMessagesHolder qbChatMessagesHolder;
        synchronized (QBChatMessagesHolder.class) {
            if (instance == null)
                instance = new QBChatMessagesHolder();
            qbChatMessagesHolder = instance;
        }
        return qbChatMessagesHolder;
    }

    private QBChatMessagesHolder(){
        this.qbChatMessageArrayList = new HashMap<>();
    }

    public void putMessages(String dialogId, ArrayList<QBChatMessage> qbChatMessages){
        this.qbChatMessageArrayList.put(dialogId, qbChatMessages);
    }

    public void putMessage(String dialogId, QBChatMessage qbChatMessage){
        List listResult = (List) this.qbChatMessageArrayList.get(dialogId);
        assert listResult != null;
        listResult.add(qbChatMessage);
        ArrayList<QBChatMessage> listAdded = new ArrayList(listResult.size());
        listAdded.addAll(listResult);
        putMessages(dialogId,listAdded);
    }

    public ArrayList<QBChatMessage> getChatMessageByDialogId(String dialogId){
        return (ArrayList<QBChatMessage>)this.qbChatMessageArrayList.get(dialogId);
    }
}
