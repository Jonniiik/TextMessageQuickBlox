package com.eugeneponomarev.textmessagequickblox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class RegistrationActivity extends AppCompatActivity {

    EditText editUsernameRegistration;
    EditText editLoginRegistration;
    EditText editEmailRegistration;
    EditText editPasswordRegistration;

    Button buttonComeBack;
    Button buttonRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerSession();
        initializationComponents();
        clickButton();
    }

    private void clickButton() {
        buttonComeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationApp();
            }
        });
    }

    private void initializationComponents() {
        editUsernameRegistration = (EditText) findViewById(R.id.editUsernameRegistration);
        editLoginRegistration = (EditText) findViewById(R.id.editLoginRegistration);
        editEmailRegistration = (EditText) findViewById(R.id.editEmailRegistration);
        editPasswordRegistration = (EditText) findViewById(R.id.editPasswordRegistration);

        buttonComeBack = (Button) findViewById(R.id.buttonComeBack);
        buttonRegistration = (Button) findViewById(R.id.buttonRegistration);
    }

    private void registrationApp() {
        String login = editLoginRegistration.getText().toString();
        String password = editPasswordRegistration.getText().toString();
        String email = editEmailRegistration.getText().toString();
        String username = editUsernameRegistration.getText().toString();

        QBUser qbUser = new QBUser(login, password);
        qbUser.setFullName(username);
        qbUser.setEmail(email);


        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Toast.makeText(RegistrationActivity.this, "Sing Up successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(RegistrationActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerSession() {
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR", e.getMessage());
            }
        });
    }

}
