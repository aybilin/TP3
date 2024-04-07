package com.example.tp3;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;

public class PdfParser {

    public static String extractTextFromPDF(InputStream inputStream) throws IOException {
        try {
            // Charger le document PDF à partir du flux d'entrée
            PDDocument document = PDDocument.load(inputStream);

            // Créer un objet PDFTextStripper pour extraire le texte
            PDFTextStripper textStripper = new PDFTextStripper();

            // Extraire le texte du document PDF
            String text = textStripper.getText(document);

            // Fermer le document PDF
            document.close();

            return text;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

