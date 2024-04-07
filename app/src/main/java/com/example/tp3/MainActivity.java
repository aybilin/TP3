package com.example.tp3;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Charge le fragment SaisieFragment au démarrage
        if (savedInstanceState == null) {
            loadFragment(new SaisieFragment());
        }
    }

    // Méthode pour charger un fragment
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        if (!(fragment instanceof SaisieFragment)) {
            transaction.addToBackStack(null); // Ajouter à la pile de retour arrière uniquement si ce n'est pas le fragment SaisieFragment
        }
        transaction.commit();
    }
}
