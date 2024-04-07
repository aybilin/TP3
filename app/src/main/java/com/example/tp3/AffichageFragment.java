package com.example.tp3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AffichageFragment extends Fragment {

    private TextView textViewNom;
    private TextView textViewPrenom;
    Button valider, download;
    private File file;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affichage, container, false);

        textViewNom = view.findViewById(R.id.textViewNom);
        textViewPrenom = view.findViewById(R.id.textViewPrenom);
        TextView textViewCentresInteret = view.findViewById(R.id.textViewCentresInteret);
        TextView textViewSync = view.findViewById(R.id.textViewSync);
        valider = view.findViewById(R.id.buttonValider);
        download = view.findViewById(R.id.buttonDownload);


        // Récupérer les valeurs passées depuis le fragment de saisie
        Bundle bundle = getArguments();
        if (bundle != null) {
            String nom = bundle.getString("nom");
            String prenom = bundle.getString("prenom");
            String sync = bundle.getString("sync");

            // Afficher les valeurs dans les TextView appropriés
            textViewNom.setText("Nom: " + nom);
            textViewPrenom.setText("Prénom: " + prenom);
            textViewSync.setText(sync);
            ArrayList<String> centresInteret = bundle.getStringArrayList("centresInteret");
            if (centresInteret != null && !centresInteret.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Centres d'intérêt: ");
                for (String centre : centresInteret) {
                    stringBuilder.append(centre).append(", ");
                }
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                textViewCentresInteret.setText(stringBuilder.toString());
            }
        }
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Générer le contenu du fichier avec les données affichées
                StringBuilder data = new StringBuilder();
                data.append(textViewNom.getText()).append("\n");
                data.append(textViewPrenom.getText()).append("\n");
                data.append(textViewCentresInteret.getText()).append("\n");
                data.append(textViewSync.getText()).append("\n");

                // Enregistrer le fichier avec les données
                if (saveDataToFile(data.toString())) {
                    Toast.makeText(requireContext(), "Fichier généré avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la génération du fichier", Toast.LENGTH_SHORT).show();
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null && file.exists()) {
                    // Vérifier si l'autorisation WRITE_EXTERNAL_STORAGE a été accordée
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Demander l'autorisation WRITE_EXTERNAL_STORAGE si elle n'a pas été accordée
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                        Toast.makeText(requireContext(), "Dowloand11", Toast.LENGTH_SHORT).show();

                    } else {
                        // L'autorisation WRITE_EXTERNAL_STORAGE est déjà accordée, procéder au téléchargement
                        Toast.makeText(requireContext(), "Dowloand", Toast.LENGTH_SHORT).show();
                        startDownload();
                    }
                } else {
                    Toast.makeText(requireContext(), "Le fichier n'existe pas", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    private boolean saveDataToFile(String data) {
        file = new File(requireContext().getFilesDir(), "donnees_utilisateur.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void startDownload() {
        // Créer un Intent pour l'action de visualisation
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Spécifier le type MIME du fichier à télécharger
        intent.setDataAndType(FileProvider.getUriForFile(requireContext(),
                requireContext().getApplicationContext().getPackageName() + ".provider", file), "text/plain");
        // Autoriser l'accès temporaire au fichier pour l'application de visualisation
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Démarrer l'activité pour visualiser le fichier
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // L'autorisation WRITE_EXTERNAL_STORAGE a été accordée, procéder au téléchargement
                startDownload();
            } else {
                // L'utilisateur a refusé l'autorisation WRITE_EXTERNAL_STORAGE, affichez un message d'erreur ou demandez à nouveau l'autorisation
                Toast.makeText(requireContext(), "Permission refusée pour enregistrer le fichier", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
