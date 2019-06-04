package com.eugeneponomarev.textmessagequickblox;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.eugeneponomarev.textmessagequickblox.Fragments.ChatFragment;
import com.eugeneponomarev.textmessagequickblox.Fragments.ContactsFragment;
import com.eugeneponomarev.textmessagequickblox.Fragments.SettingFragment;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_chats:
                    mTextMessage.setText(R.string.title_chats);

                    return true;
                case R.id.navigation_contacts:
                    mTextMessage.setText(R.string.title_contacts);
                    loadFragment(ContactsFragment.newInstance());
                    return true;
                case R.id.navigation_setting:
                    mTextMessage.setText(R.string.title_setting);
                    loadFragment(SettingFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigationMainActivity = findViewById(R.id.navigationMainActivity);
        mTextMessage = findViewById(R.id.message);
        navigationMainActivity.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationMainActivity.setOnNavigationItemSelectedListener(navListener);

        loadFragment(ChatFragment.newInstance());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_chats:
                    loadFragment(ChatFragment.newInstance());
                    break;
                case R.id.navigation_contacts:
                    loadFragment(ContactsFragment.newInstance());
                    break;
                case R.id.navigation_setting:
                    loadFragment(SettingFragment.newInstance());
                    break;
            }
            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.commit();
    }

}
