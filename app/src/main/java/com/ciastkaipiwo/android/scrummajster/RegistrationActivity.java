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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity  {

    private EditText mMail, mPassword, mFirstName, mLastName;
    private Button mRegisterButton;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL = "http://192.168.8.100:8080/users/add";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registration);

        mMail = (EditText) findViewById(R.id.mail);
        mPassword = (EditText) findViewById(R.id.password);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                new CreateNewUser().execute();
            }
        });
    }



class CreateNewUser extends AsyncTask<String, String, String> {
    boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegistrationActivity.this);
            pDialog.setMessage("Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

    @Override
    protected String doInBackground(String... strings) {

        String mail = mMail.getText().toString();
        String password = mPassword.getText().toString();
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();


        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("mail", mail));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("first_name", firstName));
        params.add(new BasicNameValuePair("last_name", lastName));

        JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL,"POST", params);
        Log.d("Create Response", json.toString());

        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);


                finish();
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
}


