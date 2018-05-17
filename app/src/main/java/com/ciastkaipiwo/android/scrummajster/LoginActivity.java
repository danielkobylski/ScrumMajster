package com.ciastkaipiwo.android.scrummajster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText mMail, mPassword;
    private Button mSignUpButtom, mLoginButtom;
    private List<User> mAllUserList = new ArrayList<User>();
    private User mUser;
    private static final String USER = "com.ciastkaipiwo.android.scrummajster.user" ;
    private List<String> mAllMail = new ArrayList<String>();
    public String mUrl = "http://s12.mydevil.net:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        getAllUser();
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

                for(int i=0;i<mAllUserList.size();i++){
                    mAllMail.add(mAllUserList.get(i).getMail());
                }

                String mail = mMail.getText().toString();
                String password = mPassword.getText().toString();
                if (mAllMail.contains(mail)) {
                    for (int i = 0; i < mAllUserList.size(); i++) {
                        if (mAllUserList.get(i).getMail().equals(mail)) {
                            mUser = mAllUserList.get(i);
                            if (mUser.getPassword().equals(password)) {
                                Intent intent = MainActivity.newIntent(LoginActivity.this, mUser);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else{
                       Toast.makeText(LoginActivity.this, "Mail is incorrect or accont is not create",Toast.LENGTH_LONG).show();
                    }

            }
        });
    }


    public static Intent newIntent(Context packageContext, User user) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
        intent.putExtra(USER, user);
        return intent;
    }
    public void getAllUser(){

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        mAllUserList.clear();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mUrl+"users/all",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            Log.d("Response", String.valueOf(response.length()));
                            for(int i=0;i<response.length();i++){
                                mAllUserList.add(new User(response.getJSONObject(i)));
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                        Toast.makeText(
                                LoginActivity.this,
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);

    }



}
