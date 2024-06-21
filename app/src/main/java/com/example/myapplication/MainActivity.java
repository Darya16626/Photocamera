package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int PHOTO_ID = 123;
    private ArrayList<Uri> images = new ArrayList<>();
    private ArrayAdapter<Uri> adapter;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button cameraOpen = findViewById(R.id.camera_button);
        ListView imageList = findViewById(R.id.image_list);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, images);
        imageList.setAdapter(adapter);

        loadImagesFromCache();

        cameraOpen.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File outputDirectory = getCacheDir();
                imageFile = new File(outputDirectory, "temp_image.jpg");
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.myapplication.fileprovider", imageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PHOTO_ID);
            }
        });

        imageList.setOnItemClickListener((parent, view, position, id) -> {
            Uri imageUri = images.get(position);
            Intent intent = new Intent(MainActivity.this, FullImageActivity.class);
            intent.putExtra("image", imageUri.toString());
            startActivity(intent);
        });
    }

    private void loadImagesFromCache() {
        File cacheDir = getCacheDir();
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                images.add(FileProvider.getUriForFile(this, "com.example.myapplication.fileprovider", file));
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_ID && resultCode == RESULT_OK) {
            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.myapplication.fileprovider", imageFile);
                images.add(imageUri);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
