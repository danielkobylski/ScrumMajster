package com.ciastkaipiwo.android.scrummajster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class TeamAddActivity extends AppCompatActivity {

    private static final String NEW_TEAM = "com.ciastkaipiwo.android.scrummajster.new_team";
    private EditText mTeamName, mKey;
    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_add);
    }
}
