package com.example.tp3;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.io.File;
import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_FILE = 1;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://votreapi.com/") // Remplacez cette URL par votre propre URL d'API
                .build();

        apiService = retrofit.create(ApiService.class);

        // Lancer le sélecteur de fichiers lors du démarrage de l'activité
        openFilePicker();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Tous les types de fichiers
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            uploadFile(fileUri);
        }
    }

    private void uploadFile(Uri fileUri) {
        File file = new File(getPathFromUri(fileUri));

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        Call<ResponseBody> call = apiService.uploadFile(filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Gérer la réponse
                if (response.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "Upload réussi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadActivity.this, "Erreur lors de l'upload", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Gérer l'échec de l'upload
                Toast.makeText(UploadActivity.this, "Erreur lors de l'upload : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getPathFromUri(Uri uri) {
        String filePath = "";
        String scheme = uri.getScheme();
        if ("content".equals(scheme)) {
            String[] projection = {MediaStore.Images.Media.DATA};
            try {
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equals(scheme)) {
            filePath = uri.getPath();
        }
        return filePath;
    }
}
