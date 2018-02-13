package com.example.matt.warmup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreateUserActivity extends AppCompatActivity {

    private String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        boolean male = true;

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.gender);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        Toast.makeText(getBaseContext(), "User is male.", Toast.LENGTH_LONG).show();
                        male = true;
                        break;
                    case R.id.female:
                        Toast.makeText(getBaseContext(), "User is female.", Toast.LENGTH_LONG).show();
                        male = false;
                        break;
                }
            }
        });
    }

    public void onClick(View view) {
        EditText input = (EditText) findViewById(R.id.username);
        name = input.getText().toString();
        Toast.makeText(this, "User " + name + " created.", Toast.LENGTH_LONG).show();
    }
}
