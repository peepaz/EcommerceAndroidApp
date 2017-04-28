package com.lilyondroid.lily.authenticate;

/**
 * Created by jason on 21/04/2017.
 */

    import android.app.ProgressDialog;
    import android.net.Uri;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;

    import android.content.Intent;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.lilyondroid.lily.Config;
    import com.lilyondroid.lily.R;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.IOException;

    import butterknife.ButterKnife;
    import butterknife.Bind;
    import okhttp3.Call;
    import okhttp3.Callback;
    import okhttp3.MediaType;
    import okhttp3.OkHttpClient;
    import okhttp3.Request;
    import okhttp3.RequestBody;
    import okhttp3.Response;
    import okhttp3.ResponseBody;

public class ActivityLogin extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    private String userToken;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    @Bind(R.id.btn_cancel) Button _cancelButton;
    private static final String Tag = "ptasdevz";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
            }
        });


        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                //TODO: Implement registration process via the app.
                /*
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                */
            }
        });
    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ActivityLogin.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        OkHttpClient client = Config.getOkHttpClient();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n" +
                "Content-Disposition: form-data; name=\"username\"\r\n\r\n"+email+"\r\n" +
                "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"password\"\r\n\r\n"+password+"\r\n" +
                "------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url(Config.LILY_SERVER + "/api-token-auth/")
                .post(body)
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                onLoginFailed();
                                progressDialog.dismiss();
                            }
                        }, 3000);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String message = response.message();
                ResponseBody bodyResp = response.body();
                final String bodyData =  bodyResp.string();

                ActivityLogin.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                if (message.equalsIgnoreCase("OK")){
                                    try {

                                        String [] emailParts = email.split("@");
                                        String name  = emailParts[0];

                                        JSONObject userLoginData = new JSONObject(bodyData);
                                        userLoginData.put("user_email",email);
                                        userLoginData.put("user_name",name);

                                        //store token for other oprations
                                        userToken = userLoginData.getString("token");

                                        onLoginSuccess(userLoginData.toString());
                                    } catch (JSONException e) {
                                        onLoginFailed();
                                    }

                                }
                                else onLoginFailed();

                                progressDialog.dismiss();
                            }
                        }, 3000);

                    }
                });


                //Todo:Finish up getting user data from server.
                /*
                OkHttpClient client = Config.getOkHttpClient();
                Request okRequest = new Request.Builder()
                        .url(Config.LILY_SERVER +"/api/users/")
                        .get()
                        .addHeader("authorization", token)
                        .build();
                 */

            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String resp) {
        _loginButton.setEnabled(true);

        Intent data = new Intent();

        data.setData(Uri.parse(resp));
        setResult(RESULT_OK, data);

        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
