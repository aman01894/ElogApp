package com.example.elogapp.activity.ui.dalily_log;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.elogapp.R;
import com.williamww.silkysignature.views.SignaturePad;

import java.io.ByteArrayOutputStream;

public class SignatureActivity extends Activity {


    private SignaturePad mSilkySignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        mClearButton = findViewById(R.id.clear_button);
        mSaveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancelButton);

        mSilkySignaturePad = findViewById(R.id.signature_pad);
        mSilkySignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //Toast.makeText(SignatureActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });



        mClearButton.setOnClickListener(view -> mSilkySignaturePad.clear());



        mSaveButton.setOnClickListener(view -> {


            Bitmap signatureBitmap = mSilkySignaturePad.getSignatureBitmap();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Intent intent = new Intent();
            intent.putExtra("image64Code", encoded);
            setResult(Activity.RESULT_OK, intent);
            finish();

        });

        cancelButton.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
