package com.eugeneponomarev.textmessagequickblox.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.eugeneponomarev.textmessagequickblox.Adapter.CreateChatsAdapter;
import com.eugeneponomarev.textmessagequickblox.Common.Common;
import com.eugeneponomarev.textmessagequickblox.Holder.QBUsersHolder;
import com.eugeneponomarev.textmessagequickblox.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateChatsFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        retrieveContacts();
    }

    ListView listContacts;
    FloatingActionButton buttonAddContactsInChat;

    public CreateChatsFragment() {
    }

    public static CreateChatsFragment newInstance() {
        return new CreateChatsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_chats, container, false);

        retrieveContacts();

        listContacts = (ListView) view.findViewById(R.id.listContacts);
        listContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        buttonAddContactsInChat = (FloatingActionButton) view.findViewById(R.id.buttonAddContactsInChat);
        buttonAddContactsInChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChatAndAddUsers();

            }
        });

        return view;
    }

    private void createChatAndAddUsers() {
        int countChoise = listContacts.getCount();

        if (listContacts.getCheckedItemPositions().size() == 1)
            createPrivateChat(listContacts.getCheckedItemPositions());
        else if (listContacts.getCheckedItemPositions().size() > 1)
            createGroupChat(listContacts.getCheckedItemPositions());
        else
            Toast.makeText(getActivity(), "Please select friend to chat", Toast.LENGTH_SHORT).show();
    }

    private void createGroupChat(SparseBooleanArray checkedItemPositions) {
        final ProgressDialog mDialog = new ProgressDialog(getActivity());
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
                Toast.makeText(getContext(), "Create chat successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR", e.getMessage());
            }
        });
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
                        Toast.makeText(getContext(), "Create Private chat successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("ERROR", e.getMessage());
                    }
                });
            }
        }
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

                CreateChatsAdapter createChatsAdapter = new CreateChatsAdapter(getContext(), qbUserWithoutCurrent);
                listContacts.setAdapter(createChatsAdapter);
                createChatsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR", e.getMessage());
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        assert getFragmentManager() != null;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.commit();
    }
}
