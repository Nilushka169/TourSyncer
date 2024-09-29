package com.s22009961.toursyncer;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Gallery extends Fragment {

    private static final int REQUEST_PERMISSION = 100;
    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private List<Uri> imageUris;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        imageUris = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(getContext(), imageUris);
        recyclerView.setAdapter(galleryAdapter);
        checkPermissionAndLoadImages();
        return view;
    }

    private void checkPermissionAndLoadImages() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            loadImages();
        }
    }

    private void loadImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String[] projection = {MediaStore.Images.Media._ID};
                Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                    while (cursor.moveToNext()) {
                        long id = cursor.getLong(idColumn);
                        Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                        imageUris.add(contentUri);
                    }
                    cursor.close();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        galleryAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
