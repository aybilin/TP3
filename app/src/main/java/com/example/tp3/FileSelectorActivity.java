package com.example.tp3;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.IOException;
import java.io.InputStream;

public class FileSelectorActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_FILE = 1;
    private TextView textViewFileContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        Button buttonSelectFile = findViewById(R.id.button_select_file);
        textViewFileContent = findViewById(R.id.text_view_file_content);

        buttonSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Tous les types de fichiers
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                // Récupérer le contenu du fichier et l'afficher dans le TextView
                String fileContent = readFileContent(fileUri);
                textViewFileContent.setText(fileContent);
            }
        }
    }

    private String readFileContent(Uri fileUri) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(fileUri);

            if (inputStream != null) {
                PdfReader pdfReader = new PdfReader(inputStream);
                PdfDocument pdfDocument = new PdfDocument(pdfReader);

                int numPages = pdfDocument.getNumberOfPages();

                for (int i = 1; i <= numPages; i++) {
                    stringBuilder.append(PdfTextExtractor.getTextFromPage(pdfDocument.getPage(i)));
                }

                pdfDocument.close();
                pdfReader.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
