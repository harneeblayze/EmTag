package com.stickercamera.app.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.skykai.stickercamera.R;
import com.stickercamera.app.camera.ui.Utils;
import com.stickercamera.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import chipset.potato.Potato;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Developer: chipset
 * Package : com.stickercamera.app.ui
 * Project : EmTag
 * Date : 3/10/16
 */

public class LoginActivity extends BaseActivity {

    @InjectView(R.id.username_edit_text)
    EditText usernameEditText;
    @InjectView(R.id.password_edit_text)
    EditText passwordEditText;
    @InjectView(R.id.login_button)
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
            }

            if (username.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
            }

            if (!username.isEmpty() && !password.isEmpty()) {
                ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Utils.urlLogin).build();
                LoginInterface loginInterface = adapter.create(LoginInterface.class);

                loginInterface.login(username, password, new ResponseCallback() {
                    @Override
                    public void success(Response response) {
                        if (dialog.isShowing()) {
                            dialog.hide();
                        }
                        BufferedReader reader;
                        StringBuilder sb = new StringBuilder();
                        try {
                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
                            String line;
                            try {
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        final String result = sb.toString();
                        if (result.contains("please enter a correct username and password")) {
                            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                            passwordEditText.setText("");
                            usernameEditText.setText("");
                        } else if (result.contains("{\"emrals\": 1.0,")) {
                            try {
                                JSONObject json = new JSONObject(result);
                                String picture = json.getString(getString(R.string.key_picture));
                                String fname = json.getString(getString(R.string.key_fname));
                                String emrals = json.getString(getString(R.string.key_emrals));
                                String xp = json.getString(getString(R.string.key_xp));
                                String lname = json.getString(getString(R.string.key_lname));
                                Potato.potate(getApplicationContext()).Preferences().putSharedPreference(getString(R.string.pref_login), false);
                                Potato.potate(getApplicationContext()).Preferences().putSharedPreference(getString(R.string.key_username), username);
                                Potato.potate(getApplicationContext()).Preferences().putSharedPreference(getString(R.string.key_emrals), emrals);
                                Potato.potate(getApplicationContext()).Preferences().putSharedPreference(getString(R.string.key_fname), fname);
                                Potato.potate(getApplicationContext()).Preferences().putSharedPreference(getString(R.string.key_lname), lname);
                                Potato.potate(getApplicationContext()).Preferences().putSharedPreference(getString(R.string.key_picture), picture);
                                Potato.potate(getApplicationContext()).Preferences().putSharedPreference(getString(R.string.key_xp), xp);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (dialog.isShowing()) {
                            dialog.hide();
                        }
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });


            }
        });
    }


    private interface LoginInterface {
        @FormUrlEncoded
        @POST("/api/login/")
        void login(@Field("username") String username, @Field("password") String password, ResponseCallback callback);
    }
}
