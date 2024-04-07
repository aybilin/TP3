package com.example.tp3;

import android.Manifest;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import java.io.OutputStream;
import java.util.ArrayList;

public class AffichageFragment extends Fragment {

    private static final int REQUEST_CODE_CREATE_PDF = 1;
    private TextView textViewNom , textViewPrenom , textViewDate , textViewPhone , textViewMail;
    TextView textViewCentresInteret, textViewSync;
    Button valider, download , upload;
    private File file;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affichage, container, false);

        textViewNom = view.findViewById(R.id.textViewNom);
        textViewPrenom = view.findViewById(R.id.textViewPrenom);
        textViewDate = view.findViewById(R.id.editTextDate);
        textViewPhone = view.findViewById(R.id.editTextPhone);
        textViewMail = view.findViewById(R.id.editTextMail);
        textViewCentresInteret = view.findViewById(R.id.textViewCentresInteret);
        textViewSync = view.findViewById(R.id.textViewSync);
        valider = view.findViewById(R.id.buttonValider);
        upload = view.findViewById(R.id.buttonUpload);
        download = view.findViewById(R.id.buttonDownload);



        // Récupérer les valeurs passées depuis le fragment de saisie
        Bundle bundle = getArguments();
        if (bundle != null) {
            String nom = bundle.getString("nom");
            String prenom = bundle.getString("prenom");
            String date = bundle.getString("date");
            String phone = bundle.getString("phone");
            String email = bundle.getString("email");
            String sync = bundle.getString("sync");

            // Afficher les valeurs dans les TextView appropriés
            textViewNom.setText(nom);
            textViewPrenom.setText(prenom);
            textViewSync.setText(sync);
            textViewDate.setText(date);
            textViewPhone.setText(phone);
            textViewMail.setText(email);
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
                    download.setEnabled(true);
                    //Intent intent = new Intent(requireContext(), FileSelectorActivity.class);
                    //startActivity(intent);
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

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer un Intent pour démarrer l'autre activité
                Intent intent = new Intent(requireContext(), FileSelectorActivity.class);
                startActivity(intent);
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
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        // Spécifier le type MIME du fichier à créer (PDF ici)
        intent.setType("application/pdf");
        // Ajouter les permissions nécessaires
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        // Créer un nom de fichier par défaut pour le document
        String fileName = "donnees_utilisateur.pdf";
        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        // Démarrer l'activité pour créer le document
        startActivityForResult(intent, REQUEST_CODE_CREATE_PDF);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_PDF && resultCode == Activity.RESULT_OK) {
            if (file != null && file.exists()) {
                // Récupérer l'URI du document créé
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        // Ouvrir un flux de sortie pour écrire les données dans le document PDF
                        OutputStream outputStream = requireContext().getContentResolver().openOutputStream(uri);
                        if (outputStream != null) {
                            // Écrire les données dans le fichier PDF
                            writeDataToPdf(outputStream);
                            // Fermer le flux de sortie une fois l'écriture terminée
                            outputStream.close();
                            // Afficher un message de succès
                            Toast.makeText(requireContext(), "Fichier PDF enregistré avec succès", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Afficher un message en cas d'erreur lors de l'enregistrement du fichier PDF
                        Toast.makeText(requireContext(), "Erreur lors de l'enregistrement du fichier PDF", Toast.LENGTH_SHORT).show();
                    } catch (DocumentException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }


    private void writeDataToPdf(File file) {
        try {
            // Ouvrir un flux de sortie vers le fichier PDF
            OutputStream outputStream = new FileOutputStream(file);

            // Appeler la fonction qui écrit les données dans le fichier PDF
            writeDataToPdf(outputStream);

            // Fermer le flux de sortie
            outputStream.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour écrire les données dans un flux de sortie pour le fichier PDF
    private void writeDataToPdf(OutputStream outputStream) throws IOException, DocumentException {
        // Créer un document iText
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        // Ouvrir le document
        document.open();

        // Ajouter les données dans le document
        addContentToPdf(document);

        // Fermer le document
        document.close();
    }

    // Méthode pour ajouter le contenu au document PDF
    private void addContentToPdf(Document document) throws DocumentException {
        // Ajouter le contenu dans le document
        document.add(new Paragraph("Données de l'utilisateur :"));
        document.add(Chunk.NEWLINE); // Saut de ligne

        // Récupérer les données à partir des TextView
        String nom = textViewNom.getText().toString();
        String prenom = textViewPrenom.getText().toString();
        String centresInteret = textViewCentresInteret.getText().toString();
        String sync = textViewSync.getText().toString();

        // Ajouter les données dans le document
        document.add(new Paragraph(nom));
        document.add(new Paragraph(prenom));
        document.add(new Paragraph( centresInteret));
        document.add(new Paragraph(sync));
    }


}
