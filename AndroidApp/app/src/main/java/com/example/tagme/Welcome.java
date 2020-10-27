package com.example.tagme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import static com.example.tagme.Utility.setNextButton;

public class Welcome extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        Button nextActivityButton = findViewById(R.id.next_activity_button);
        setNextButton(nextActivityButton, SearchUser.class);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        // Se non c'è un utente loggato
        if(!(SaveSharedPreference.getLoggedStatus(getApplicationContext())))
        {
            // Reindirizzo all'activity il login
            startActivity(new Intent(this, Login.class));
        }
        else
        {
            // Prendo l'username dell'utente loggato
            String username = SaveSharedPreference.getLoggedUser(getApplicationContext());
            TextView usernameTV = findViewById(R.id.username);
            // Lo imposto nella schermata
            usernameTV.setText(username);
        }
    }
}