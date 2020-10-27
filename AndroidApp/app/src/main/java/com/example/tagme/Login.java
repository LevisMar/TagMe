package com.example.tagme;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cz.msebera.android.httpclient.Header;
import static com.example.tagme.Utility.*;

public class Login extends AppCompatActivity
{
    // Pulsante per invio credenziali
    CircularProgressButton circularProgressButton;
    // Caselle di testo per inserimento credenziali
    EditText usernameET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        setConfermaButton();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // Controllo se c'è un utente già loggato
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext()))
        {
            showToast("Bentornato.\nUsa il logout per disconnetterti", Toast.LENGTH_LONG);
            // Vado alla prossima Activity
            startActivity(new Intent(this, Welcome.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }
    }

    /**
     * Imposta il pulsante di conferma, associando un evento onClick
     * il quale prende i parametri inseriti e tenta l'invio di
     * quest'ultimi al web service.
     */
    public void setConfermaButton()
    {
        circularProgressButton = findViewById(R.id.buttonConferm);
        circularProgressButton.setOnClickListener(v ->
        {
            // Prende il valore contenuto nell'username Edit View
            String username = usernameET.getText().toString();
            // Prende il valore contenuto nella password Edit View
            String password = passwordET.getText().toString();
            // Instanzia un oggetto Http Request Param
            RequestParams params = new RequestParams();
            // Se username Edit View e password Edit View hanno valori diversi da null
            if(isNotNull(username) && isNotNull(password))
            {
                //TEST
                // Avvio l'animazione del pulsante
                //circularProgressButton.startAnimation();
                //testRedmi(username, password);

                //ORIGINALE
                // Avvio l'animazione del pulsante
                circularProgressButton.startAnimation();
                // Imposta il parametro Http username con il valore della Edit View username
                params.put("username", username);
                // Imposta il parametro Http password con il valore della Edit View password
                params.put("password", password);
                // Invoca il Web Service REST con i parametri Http
                invocaWS(params);
            }
            else
            {
                showToast("Inserisci username e password, non lasciare i campi vuoti.", Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * Metodo che effettua un'invocazione
     * ad un webservice REST che verifica l'username
     * e la password per il login.
     *
     * @param params della richiesta
     */
    public void invocaWS(RequestParams params)
    {
        // Effettua una chiamata al webservice REST usando un oggetto AsyncHttpClient
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utility.getUrlToServer() + "login/dologin", params ,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                try
                {
                    // Oggetto JSON con la risposta dal webservice
                    String tag = response.getString("tag");
                    // Se è presente un messaggio di errore
                    if(tag.equals("msg"))
                    {
                        // Resetto il pulsante
                        circularProgressButton.revertAnimation();
                        circularProgressButton.setBackgroundResource(R.drawable.sl_orange_bg);
                        showNegativePopup(Login.this, response.getString("msg"));
                    }
                    // Login effettuato con successo
                    if(tag.equals("staff_logged"))
                    {
                        // Prendo l'username
                        String staff_username = response.getString("staff_username");
                        // Imposto lo status di login con l'username
                        SaveSharedPreference.setLoggedIn(getApplicationContext(), staff_username);
                        revealView();
                        startActivity(new Intent(Login.this, Welcome.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        // Resetto il pulsante
                        circularProgressButton.revertAnimation();
                        circularProgressButton.setBackgroundResource(R.drawable.sl_orange_bg);
                    }
                }
                catch (JSONException e)
                {
                    // Resetto il pulsante
                    circularProgressButton.revertAnimation();
                    circularProgressButton.setBackgroundResource(R.drawable.sl_orange_bg);
                    showNegativePopup(Login.this, "Codice errore: " + statusCode + "." + e.toString());
                    e.printStackTrace();
                }
            }

            // Quando la risposta restituita dal webservice ha Http response code diverso da '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                showToast("" + statusCode, Toast.LENGTH_LONG);
                // Resetto il pulsante
                circularProgressButton.revertAnimation();
                circularProgressButton.setBackgroundResource(R.drawable.sl_orange_bg);
                showNegativePopup(Login.this, "Codice errore " + statusCode + ":\n" + throwable.toString());
            }
        });
    }

    /**
     * Metodo che crea un effetto per mostrare la schermata successiva
     */
    private void revealView()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            circularProgressButton.setElevation(0f);
        }
        View revealView = findViewById(R.id.revealView);
        revealView.setVisibility(View.VISIBLE);
        int x = revealView.getWidth();
        int y = revealView.getHeight();
        int startX = (int) (getFinalWidth() / 2 + circularProgressButton.getX());
        int startY = (int) (getFinalHeight() / 2 + circularProgressButton.getY());
        float radius = Math.max(x,y) * 1.2f;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            Animator reveal = ViewAnimationUtils.createCircularReveal(revealView, startX, startY, 0, radius);
            reveal.setDuration(800);
            reveal.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    revealView.setVisibility(View.INVISIBLE);
                }
            });
            reveal.start();
        }
    }

    /**
     * Esegue il test su dispositivi reali
     * @param username inserito
     * @param password inserita
     */
    private void testRedmi(String username, String password)
    {
        new Handler().postDelayed(() ->
        {
            if(username.equals("admin") && password.equals("admin"))
            {
                SaveSharedPreference.setLoggedIn(getApplicationContext(), username);
                revealView();
                startActivity(new Intent(Login.this, Welcome.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                circularProgressButton.revertAnimation();
                circularProgressButton.setBackgroundResource(R.drawable.sl_orange_bg);
            }
            else
            {
                circularProgressButton.revertAnimation();
                circularProgressButton.setBackgroundResource(R.drawable.sl_orange_bg);
                showNegativePopup(Login.this, "username e password sono sbagliati");
            }
        }, 1600);
    }

    private int getFinalWidth()
    {
        return (int) getResources().getDimension(R.dimen.get_width);
    }

    private int getFinalHeight()
    {
        return (int) getResources().getDimension(R.dimen.get_height);
    }
}