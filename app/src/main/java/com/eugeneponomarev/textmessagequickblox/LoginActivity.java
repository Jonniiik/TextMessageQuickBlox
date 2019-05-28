package com.eugeneponomarev.textmessagequickblox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class LoginActivity extends AppCompatActivity {

    static final String APP_ID = "77081";
    static final String AUTH_KEY = "MSg6XOcm39E4pca";
    static final String AUTH_SECRET = "XBUU6UTMjxQOxzr";
    static final String ACCOUNT_KEY = "PipPeLKi3JjbTM9R_1GL";

    EditText editLogin;
    EditText editPasswordLogin;

    Button buttonLogin;

    TextView textViewForgotPassword;
    TextView textViewSingUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializationFramework();
        initializationComponents();
        clickButton();
    }

    private void initializationFramework() {
        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

    private void initializationComponents() {
        editLogin = (EditText) findViewById(R.id.editLogin);
        editPasswordLogin = (EditText) findViewById(R.id.editPasswordLogin);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        textViewSingUp = (TextView) findViewById(R.id.textViewSingUp);
    }

    private void clickButton() {
        textViewSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginApp();
            }
        });
    }

    private void loginApp() {
        String login = editLogin.getText().toString();
        String password = editPasswordLogin.getText().toString();

        QBUser qbUser = new QBUser(login, password);
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
