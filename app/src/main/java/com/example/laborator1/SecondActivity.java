package com.example.laborator1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.i(TAG, "onCreate() called");

        Button buttonOpenThirdActivity = findViewById(R.id.buttonOpenThirdActivity);
        buttonOpenThirdActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                intent.putExtra("message", "Hello from SecondActivity!");
                intent.putExtra("num1", 5);
                intent.putExtra("num2", 10);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        Button buttonBackToMainActivity = findViewById(R.id.buttonBackToMainActivity);
        buttonBackToMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navighează înapoi la MainActivity
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);  // Deschide MainActivity
                finish();  // Închide SecondActivity
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v(TAG, "onActivityResult() called. RequestCode: " + requestCode + ", ResultCode: " + resultCode);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String responseMessage = data.getStringExtra("responseMessage");
            int sum = data.getIntExtra("sum", 0);

            Log.v(TAG, "Received responseMessage: " + responseMessage + ", Sum: " + sum);


            Toast.makeText(this, responseMessage + " Sum: " + sum, Toast.LENGTH_LONG).show();
        } else {
            Log.v(TAG, "onActivityResult() - No valid result received.");
        }
    }
}
