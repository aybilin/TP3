package com.example.tp3;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SaisieFragment extends Fragment {

    private EditText editTextNom,editTextPrenom , editTextDate , editTextPhone , editTextMail;
    CheckBox checkBoxSport, checkBoxMusique, checkBoxCinema, checkBoxLecture, checkBoxJeuxVideo;

    RadioButton radioButtonOui , radioButtonNon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saisie, container, false);

        editTextNom = view.findViewById(R.id.editTextNom);
        editTextPrenom = view.findViewById(R.id.editTextPrenom);
        editTextDate = view.findViewById((R.id.editTextDate));
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextMail = view.findViewById(R.id.editTextMail);
        checkBoxSport = view.findViewById(R.id.checkBoxSport);
        checkBoxMusique = view.findViewById(R.id.checkBoxMusique);
        checkBoxCinema = view.findViewById(R.id.checkBoxCinema);
        checkBoxLecture = view.findViewById(R.id.checkBoxLecture);
        checkBoxJeuxVideo = view.findViewById(R.id.checkBoxJeuxVideo);
        radioButtonOui = view.findViewById(R.id.radioButtonoui);
        radioButtonNon = view.findViewById(R.id.radioButtonnon);

        Button buttonEnvoyer = view.findViewById(R.id.buttonEnvoyer);
        buttonEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs saisies dans les champs nom et prénom
                String nom = editTextNom.getText().toString();
                String prenom = editTextPrenom.getText().toString();
                String date = editTextDate.getText().toString();
                String phone = editTextPhone.getText().toString();
                String email = editTextMail.getText().toString();

                ArrayList<String> centresInteretSelectionnes = new ArrayList<>();
                if (checkBoxSport.isChecked()) {
                    centresInteretSelectionnes.add("Sport");
                }
                if (checkBoxMusique.isChecked()) {
                    centresInteretSelectionnes.add("Musique");
                }
                if (checkBoxCinema.isChecked()) {
                    centresInteretSelectionnes.add("Cinéma");
                }
                if (checkBoxLecture.isChecked()) {
                    centresInteretSelectionnes.add("Lecture");
                }
                if (checkBoxJeuxVideo.isChecked()) {
                    centresInteretSelectionnes.add("Jeux Vidéo");
                }
                String sync = null;

                if (radioButtonOui.isChecked()) {
                    sync = "Synchroniser automatiquement.";
                }
                if (radioButtonNon.isChecked()) {
                    sync = "Ne pas synchroniser automatiquement.";
                }

                // Créer un bundle pour passer les données au fragment d'affichage
                Bundle bundle = new Bundle();
                bundle.putString("nom", nom);
                bundle.putString("prenom", prenom);
                bundle.putString("date", date);
                bundle.putString("phone", phone);
                bundle.putString("email", email);
                bundle.putStringArrayList("centresInteret", centresInteretSelectionnes);
                bundle.putString("sync", sync);
                // Créer une instance du fragment d'affichage et lui passer les données
                AffichageFragment affichageFragment = new AffichageFragment();
                affichageFragment.setArguments(bundle);

                // Charger le fragment d'affichage
                ((MainActivity) requireActivity()).loadFragment(affichageFragment);
            }
        });

        return view;
    }
}
