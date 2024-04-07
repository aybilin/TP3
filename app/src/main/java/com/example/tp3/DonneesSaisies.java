package com.example.tp3;

import java.io.Serializable;
import java.util.ArrayList;

public class DonneesSaisies implements Serializable {
    private String nom;
    private String prenom;
    private String date_naissance;
    private String email;
    private ArrayList<String> centre_interet;
    private String sync;

    public DonneesSaisies() {
    }

    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    // Ajoutez d'autres champs selon vos besoins

    // Constructeur, getters et setters
}