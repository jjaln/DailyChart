package com.jjaln.dailychart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class APIActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apikey);

        EditText bithumb_access = (EditText) findViewById(R.id.bithumb_access);
        EditText bithumb_secret = (EditText) findViewById(R.id.bithumb_secret);
        EditText upbit_access = (EditText) findViewById(R.id.upbit_access);
        EditText upbit_secret = (EditText) findViewById(R.id.upbit_secret);

        Button button = findViewById(R.id.apibutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                bundle.putString("bit_acc", bithumb_access.getText().toString());
                bundle.putString("bit_sec", bithumb_secret.getText().toString());
                bundle.putString("up_acc", upbit_access.getText().toString());
                bundle.putString("up_sec", upbit_secret.getText().toString());
                intent.putExtra("apikey", bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
