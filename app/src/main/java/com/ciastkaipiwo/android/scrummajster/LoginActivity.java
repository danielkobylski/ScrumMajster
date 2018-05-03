package com.ciastkaipiwo.android.scrummajster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText mMail, mPassword;
    private Button mSignUpButtom, mLoginButtom;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://192.168.8.101:80/login.php";
    private static final String TAKE_USER_URL = "http://192.168.8.101:80/takeuser.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USER = "user";
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_USER_MAIL = "mail";
    private static final String TAG_USER_FIRST_NAME = "first_name";
    private static final String TAG_USER_LAST_NAME = "last_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        mMail = (EditText) findViewById(R.id.log_mail);
        mPassword = (EditText) findViewById(R.id.log_password);
        mSignUpButtom = (Button) findViewById(R.id.signup_button);
        mSignUpButtom.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(intent);
            }
        });


        mLoginButtom = (Button) findViewById(R.id.login_button);
        mLoginButtom.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //new LoginUser().execute();
                new TakeUser().execute();

            }
        });
    }

/**
    class LoginUser extends AsyncTask<String, String, String> {
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String mail = mMail.getText().toString();
            String password = mPassword.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("mail", mail));
            params.add(new BasicNameValuePair("password", password));

            JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL,"POST", params);
            Log.d("Create Response", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {


                    //Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    //startActivity(i);



                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String message) {

            pDialog.dismiss();

        }

    }
**/
    class TakeUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String mail = mMail.getText().toString();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mail", mail));
            JSONObject json = jsonParser.makeHttpRequest(TAKE_USER_URL, "GET", params);
            Log.d("Create Response", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {


                    JSONArray userObj = json.getJSONArray(TAG_USER);
                    for(int i=0; i < userObj.length(); i++) {
                        JSONObject jsonobject = userObj.getJSONObject(i);
                        String user_id       = jsonobject.getString("user_id");
                        String mail_user    = jsonobject.getString("mail");
                        String password  = jsonobject.getString("password");
                        String first_name = jsonobject.getString("first_name");
                        String last_name = jsonobject.getString("last_name");
                    }






                } else {
                    // failed to update product
                }
            }

            catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }

}
