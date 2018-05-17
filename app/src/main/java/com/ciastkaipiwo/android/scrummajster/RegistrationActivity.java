package com.ciastkaipiwo.android.scrummajster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity  {

    private EditText mMail, mPassword, mFirstName, mLastName;
    private Button mRegisterButton;
    private User mUser;
    private static final String USER = "com.ciastkaipiwo.android.scrummajster.user" ;
    public String mUrl = "http://s12.mydevil.net:8080/";
    private List<User> mAllUserList = new ArrayList<User>();
    private List<String> mAllMail = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registration);

        mMail = (EditText) findViewById(R.id.mail);
        mPassword = (EditText) findViewById(R.id.password);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        allUser();
        for(int i=0;i<mAllUserList.size();i++){
            mAllMail.add(mAllUserList.get(i).getMail());
        }



        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View
                .OnClickListener(){

            @Override
            public void onClick(View view) {
                String mail = mMail.getText().toString();

                if (mAllUserList.contains(mail)){
                    Toast.makeText(RegistrationActivity.this,"This mail alredy exist",Toast.LENGTH_SHORT).show();
                }
                else{
                    mUser = new User(0,mail,mPassword.getText().toString(),mFirstName.getText().toString(),mLastName.getText().toString());
                    addNewUser(mUser);
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public void allUser(){
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
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
                                RegistrationActivity.this,
                                "Error while getting projects data",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
    public void addNewUser(User user) {
        JSONObject params = new JSONObject();

        try {
            params.put("mail", user.getMail());
            params.put("password", user.getPassword());
            params.put("firstName", user.getFirstName());
            params.put("lastName", user.getLastName());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, mUrl+"users/add", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ResponseError: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        Volley.newRequestQueue(this).add(jsonObjReq);
    }
}
