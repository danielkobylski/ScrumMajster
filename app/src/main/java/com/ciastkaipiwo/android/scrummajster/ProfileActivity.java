package com.ciastkaipiwo.android.scrummajster;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private static final String USER="com.ciastkaipiwo.android.scrummajster.user";

    TextView mLast, mFirst, mMail;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mUser = getIntent().getParcelableExtra(USER);

        mFirst = (TextView) findViewById(R.id.fname);
        mLast = (TextView) findViewById(R.id.lname);
        mMail = (TextView) findViewById(R.id.pmail);

        mFirst.setText(mUser.getFirstName());
        mLast.setText(mUser.getLastName());
        mMail.setText(mUser.getMail());

    }


    public static Intent newIntent(Context packageContext, User user){
        Intent intent = new Intent(packageContext, ProfileActivity.class);
        intent.putExtra(USER, user);
        return intent;
    }
}
