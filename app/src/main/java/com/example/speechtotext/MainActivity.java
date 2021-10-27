package com.example.speechtotext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvNote;
    ImageButton ibVoice;
    int REQUEST_CODE_VOICE = 100;

    // Get results from voice
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == MainActivity.RESULT_OK) {
                        Intent data = result.getData();
                        ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        tvNote.setText(matches.get(0));
                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvNote = (TextView) findViewById(R.id.tvNote);
        ibVoice = (ImageButton) findViewById(R.id.ibVoice);

        ibVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup request permission to use voice
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_VOICE);

            }
        });
    }

    //  User's reply about permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_VOICE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Mời nói");
            try{
                someActivityResultLauncher.launch(intent);
            }catch (Exception e){
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://market.android.com/details?id=APP_PACKAGE_NAME"));
//                startActivity(browserIntent);
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(this, "Voice N/A", Toast.LENGTH_SHORT).show();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}