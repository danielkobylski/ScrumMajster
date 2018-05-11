package com.ciastkaipiwo.android.scrummajster;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class TeamConfigActivity extends AppCompatActivity {

    private static final String TEAM_TO_EDIT = "com.ciastkaipiwo.android.scrummajster.team_to_edit";
    private static final String OLD_TEAM = "com.ciastkaipiwo.android.scrummajster.old_team";
    private static final String NEW_TEAM = "com.ciastkaipiwo.android.scrummajster.new_team";

    private EditText mTeamName;
    private Button mSubmitButton;
    private Team mTeamToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_team_config);

        mTeamToEdit = getIntent().getParcelableExtra(TEAM_TO_EDIT);

        mTeamName = (EditText) findViewById(R.id.team_name_config);
        mSubmitButton = (Button) findViewById(R.id.team_submit_button);

        if (mTeamToEdit != null) {
            mTeamName.setText(mTeamToEdit.getName());
        }
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mTeamName.getText().toString();
                Team newTeam = new Team(0,name);
                Intent data = new Intent();
                data.putExtra(NEW_TEAM, newTeam);
                data.putExtra(OLD_TEAM, mTeamToEdit);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }
    public static Team getNewTeam(Intent result) {
        return (Team) result.getParcelableExtra(NEW_TEAM);
    }

    public static Team getOldTeam(Intent result) {
        return (Team) result.getParcelableExtra(OLD_TEAM);
    }

    public static Intent newIntent(Context packageContext, Team team){
        Intent intent = new Intent(packageContext, TeamConfigActivity.class);
        intent.putExtra(TEAM_TO_EDIT,team);

        return intent;
    }



}
