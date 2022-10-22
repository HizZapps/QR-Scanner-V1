package com.example.qr_scanner1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

public class ScannerActivity extends AppCompatActivity {

    private final int CAMERA_REQUEST_CODE = 101;
    CodeScannerView codeScannerView;
    private CodeScanner mCodeScanner;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        codeScannerView = findViewById(R.id.scanner_view);
        textView = findViewById(R.id.mTextView);


        checkCameraPermission();

    }

    private void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            startScanner();
        }
    }

    private void startScanner(){
        mCodeScanner = new CodeScanner(this, codeScannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
//                        Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
            //textView.setText(result.getText());
            Intent intent = new Intent();
            intent.putExtra("scanResult", result.getText());
            setResult(RESULT_OK, intent);
            finish();
        }));

        codeScannerView.setOnClickListener(view -> {
            mCodeScanner.releaseResources();
            mCodeScanner.startPreview();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}