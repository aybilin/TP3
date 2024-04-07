package com.example.tp3;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Récupérer l'URL du fichier à télécharger depuis l'intention
        String fileUrl = intent.getStringExtra("fileUrl");

        // Télécharger le fichier
        downloadFile(fileUrl);

        return START_NOT_STICKY;
    }

    private void downloadFile(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // Récupérer le nom du fichier depuis l'URL
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

            // Créer un fichier local pour enregistrer le téléchargement
            File file = new File(getExternalFilesDir(null), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);

            // Copier les données du flux de connexion vers le fichier local
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            // Fermer les flux
            outputStream.close();
            inputStream.close();

            // Afficher un message indiquant que le téléchargement est terminé
            Toast.makeText(this, "Téléchargement terminé: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

            // Vous pouvez maintenant analyser le fichier téléchargé ici ou dans une autre méthode

        } catch (IOException e) {
            e.printStackTrace();
            // Afficher un message d'erreur en cas d'échec du téléchargement
            Toast.makeText(this, "Erreur de téléchargement", Toast.LENGTH_SHORT).show();
        }
    }
}
