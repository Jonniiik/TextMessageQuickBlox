package com.eugeneponomarev.textmessagequickblox.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.eugeneponomarev.textmessagequickblox.Adapter.ContactsAdapter;
import com.eugeneponomarev.textmessagequickblox.CreateChatsActivity;
import com.eugeneponomarev.textmessagequickblox.Holder.QBUsersHolder;
import com.eugeneponomarev.textmessagequickblox.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {


    ListView listContacts;
    LinearLayout linearLayoutAddGroupChat;
    ImageView imageViewAddGroupChat;


    public ContactsFragment() {
    }

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        retrieveContacts();

        listContacts = (ListView) view.findViewById(R.id.listContacts);
        linearLayoutAddGroupChat = (LinearLayout) view.findViewById(R.id.linearLayoutAddGroupChat);
        linearLayoutAddGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  createPrivateChat(listContacts.getCheckedItemPositions());
                loadFragment();
                //startActivity(new Intent(getActivity(), CreateChatsActivity.class));
            }
        });
        return view;
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
                ContactsAdapter contactsAdapter = new ContactsAdapter(getContext(), qbUserWithoutCurrent);
                listContacts.setAdapter(contactsAdapter);
                contactsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR", e.getMessage());
            }
        });
    }

    private void loadFragment() {
        Fragment fragment = new CreateChatsFragment();
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.commit();
    }
}
