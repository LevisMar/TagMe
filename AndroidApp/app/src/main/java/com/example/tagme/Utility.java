package com.example.tagme;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

/**
 * Classe in cui vado ad inserire funzioni che
 * possono essere riutilizzate dalle varie Activity
 */
class Utility
{
    /**
     * Restituisce l'URL per connettersi con il webservice.
     * Poi va specificata la servlet/webservice a cui inviare
     * la richiesta.
	 *
	 * Inserisci l'ip address del server su cui è caricato il
	 * progetto di GestioneLibri
     *
     * @return l'URL per connettersi al webservice
     */
    static String getUrlToServer()
    {
        return "http://ip-address:8080/gestionelibri/rest/";
    }

    /**
     * Mostra un popup in fondo alla schermata, che scompare
     * dopo un determinato tempo.
     *
     * @param message da mostrare nella schermata
     * @param duration del popup
     */
    static void showToast(String message, int duration )
    {
        Toast toast = Toast.makeText(GlobalApplication.getAppContext(), message, duration);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    /**
     * Verifica se la stringa in input è nulla
     *
     * @param txt stringa da controllare
     * @return true se non è nulla, false altrimenti
     */
    static boolean isNotNull(String txt)
    {
        return txt!=null && txt.trim().length()>0;
    }

    /**
     * Imposta il logout quando si clicca il pulsante,
     * cancellando tutti i dati salvati fino a quel momento
     * e reindirizzando verso la schermata di login.
     *
     * @param logout dove viene impostato il logout
     */
    static void setLogout(TextView logout)
    {
        // Recupero il context dell'app
        Context context = GlobalApplication.getAppContext();
        // Quando si clicca sul pulsante
        logout.setOnClickListener(v ->
        {
            // Rimuovo tutte le variabili salvate finora nelle SharedPreferences
            SaveSharedPreference.removePreferences(context);
            // Reindirizzo alla schermata di login
            context.startActivity(new Intent(context, Login.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        });
    }

    /**
     * Imposta il pulsante Next sulla schermata, per
     * navigare verso la prossima activity.
     *
     * @param nextActivityButton il pulsante da impostare
     * @param nextActivity l'activity verso cui navigare
     */
    static void setNextButton(Button nextActivityButton, Class nextActivity)
    {
        // Recupero il context dell'app
        Context context = GlobalApplication.getAppContext();
        // Imposto che quando si clicca sul pulsante, si reindirizza alla prossima activity
        nextActivityButton.setOnClickListener(v -> context.startActivity(new Intent(context, nextActivity).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)));
    }

    /**
     * Mostra un dialog/popup di informazione
     * all'interno della schermata.
     *
     * @param context activity su cui mostrare il dialog/popup
     * @param msg messaggio da mostrare all'interno del dialog/popup
     */
    static void showInformativePopup(Context context, String msg)
    {
        // Creo il dialog
        Dialog popupDialog = new Dialog(context);
        // Imposto lo stile
        popupDialog.setContentView(R.layout.popup_informative);
        Button btnPopupClose = popupDialog.findViewById(R.id.btnPopupConferm);
        TextView messagePopupTV = popupDialog.findViewById(R.id.messsagePopupTV);
        // Imposto che quando si clicca sul pulsante, il dialog viene chiuso
        btnPopupClose.setOnClickListener(v -> popupDialog.dismiss());
        // Imposto il testo da visualizzare sul dialog
        messagePopupTV.setText(msg);
        Objects.requireNonNull(popupDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Mostro il dialog
        popupDialog.show();
    }

    /**
     * Mostra un dialog/popup di errore
     * all'interno della schermata.
     *
     * @param context activity su cui mostrare il dialog/popup
     * @param msg messaggio da mostrare all'interno del dialog/popup
     */
    static void showNegativePopup(Context context, String msg)
    {
        // Creo il dialog
        Dialog popupDialog = new Dialog(context);
        // Imposto lo stile
        popupDialog.setContentView(R.layout.popup_negative);
        Button btnPopupRetry = popupDialog.findViewById(R.id.btnPopupRetry);
        TextView messagePopupTV = popupDialog.findViewById(R.id.messsagePopupTV);
        // Imposto che quando si clicca sul pulsante, il dialog viene chiuso
        btnPopupRetry.setOnClickListener(v -> popupDialog.dismiss());
        // Imposto il testo da visualizzare sul dialog
        messagePopupTV.setText(msg);
        Objects.requireNonNull(popupDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Mostro il dialog
        popupDialog.show();
    }
}