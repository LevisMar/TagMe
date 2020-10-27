package com.example.tagme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import static com.example.tagme.Utility.setLogout;
import static com.example.tagme.Utility.setNextButton;

public class TransactionType extends AppCompatActivity
{
    // Immagini che rappresentano la rispettiva scelta
    ImageView prestito, restituzione;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_type_activity);
        prestito = findViewById(R.id.prestito);
        restituzione = findViewById(R.id.restituzione);
        // Imposto alcuni elementi nella schermata
        TextView logout = findViewById(R.id.logout);
        setLogout(logout);
        setPrestito();
        setRestituzione();
        setBackButton();
        Button nextActivityButton = findViewById(R.id.next_activity_button);
        setNextButton(nextActivityButton, SearchBook.class);
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
        // Controllo se precedentemente era già stata impostata una scelta
        String transaction_type = SaveSharedPreference.getTransactionType(getApplicationContext());
        // Se era già impostata, attivo la rispettiva casella
        if(transaction_type!=null)
        {
            if(transaction_type.equals("prestito"))
            {
                prestito.setVisibility(View.VISIBLE);
            }
            else if(transaction_type.equals("restituzione"))
            {
                restituzione.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Metodo che imposta l'azione da compiere quando
     * l'opzione prestito viene selezionata
     */
    public void setPrestito()
    {
        LinearLayout prestitoLL = findViewById(R.id.prestitoLL);
        prestitoLL.setOnClickListener(v ->
        {
            // Se l'opzione restituzione era stata selezionata
            if(restituzione.getVisibility() == View.VISIBLE)
            {
                // La disabilito
                restituzione.setVisibility(View.INVISIBLE);
            }
            // Abilito l'opzione prestito
            prestito.setVisibility(View.VISIBLE);
            // Salvo l'opzione nelle SharedPreferences
            SaveSharedPreference.setTransactionType(getApplicationContext(), "prestito");
        });
    }

    /**
     * Metodo che imposta l'azione da compiere quando
     * l'opzione restituzione viene selezionata
     */
    public void setRestituzione()
    {
        LinearLayout restituzioneLL = findViewById(R.id.restituzioneLL);
        restituzioneLL.setOnClickListener(v ->
        {
            // Se l'opzione prestito era stata selezionata
            if(prestito.getVisibility() == View.VISIBLE)
            {
                // La disabilito
                prestito.setVisibility(View.INVISIBLE);
            }
            // Abilito l'opzione restituzione
            restituzione.setVisibility(View.VISIBLE);
            // Salvo l'opzione nelle SharedPreferences
            SaveSharedPreference.setTransactionType(getApplicationContext(), "restituzione");
        });
    }

    /**
     * Metodo che imposta il comportamento da attribuire
     * al pulsante back, presente nella schermata
     */
    public void setBackButton()
    {
        Button back = findViewById(R.id.back_activity_button);
        back.setOnClickListener(v -> onBackPressed());
    }
}