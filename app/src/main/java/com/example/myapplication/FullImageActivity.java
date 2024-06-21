package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class FullImageActivity extends AppCompatActivity {
    ImageView fullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        fullImage = findViewById(R.id.full_image);

        String imageUriStr = getIntent().getStringExtra("image");
        Uri imageUri = Uri.parse(imageUriStr);
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            fullImage.setImageBitmap(selectedImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}