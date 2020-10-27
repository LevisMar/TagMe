package com.example.tagme;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.polyak.iconswitch.IconSwitch;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cz.msebera.android.httpclient.Header;
import static com.example.tagme.Utility.isNotNull;
import static com.example.tagme.Utility.setLogout;
import static com.example.tagme.Utility.setNextButton;
import static com.example.tagme.Utility.showNegativePopup;
import static com.example.tagme.Utility.showToast;
import static com.polyak.iconswitch.IconSwitch.Checked.LEFT;
import static com.polyak.iconswitch.IconSwitch.Checked.RIGHT;

public class SearchUser extends AppCompatActivity
{
    IconSwitch iconSwitch;
    EditText searchBoxET;
    String search_box;
    CircularProgressButton searchButton;
    LinearLayout foundUserLayout;
    TextView bookUserTV, userSearchValueTV;
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user_activity);
        TextView logout = findViewById(R.id.logout);
        // Imposto il pulsante di logout
        setLogout(logout);
        iconSwitch = findViewById(R.id.icon_switch);
        iconSwitch.setChecked(LEFT);
        searchBoxET = findViewById(R.id.search_box);
        // Imposto lo switch per la scelta tra email e telefono
        setIconSwitch();
        Button nextActivityButton = findViewById(R.id.next_activity_button);
        // Imposto il pulsante
        setNextButton(nextActivityButton, TransactionType.class);
        // Imposto il pulsante per la ricerca dell'utente
        setSearchButton();
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
        // Se in precedenza si era già cercato un utente
        if(SaveSharedPreference.getBookUser(getApplicationContext())!=null)
        {
            // Recupero i dati della ricerca precedente
            String bookUser = SaveSharedPreference.getBookUser(getApplicationContext());
            String userSearchValue = SaveSharedPreference.getUserSearchValue(getApplicationContext());
            // Lo imposto nella schermata
            setFoundUser(bookUser, userSearchValue);
            // Imposto il pulsante per resettare la ricerca
            setResetButton();
        }
    }

    /**
     * Imposta lo switch che permette di scegliere se ricercare
     * un utente utilizzando la sua mail o il numero di telefono
     */
    public void setIconSwitch()
    {
        iconSwitch.setCheckedChangeListener(current ->
        {
            switch (current)
            {
                case LEFT:
                    searchBoxET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;
                case RIGHT:
                    searchBoxET.setInputType(InputType.TYPE_CLASS_PHONE);
                    searchBoxET.setKeyListener(DigitsKeyListener.getInstance(false,true));
                    break;
            }
        });
    }

    /**
     * Metodo che imposta il pulsante di
     * ricerca di un utente
     */
    public void setSearchButton()
    {
        searchButton = findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(v ->
        {
            // Prendo il tipo di parametro inserito
            String type = null;
            // Se l'utente ha switchato per l'email
            if(iconSwitch.getChecked() == LEFT)
            {
                // Imposto il tipo di ricerca
                type = "email";
            }
            // Se l'utente ha switchato per il numero di telefono
            if(iconSwitch.getChecked() == RIGHT)
            {
                // Imposto il tipo di ricerca
                type = "phone";
            }
            // Prendo il testo inserito nella search box
            search_box = searchBoxET.getText().toString();
            // Se i parametri necessari hanno valori diversi da null
            if(isNotNull(type) && isNotNull(search_box))
            {
                Pattern pattern;
                Matcher mat = null;
                // Se è stata scelta la mail
                if(type.equals("email"))
                {
                    // Controllo se la mail inserita è accettabile
                    pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                    mat = pattern.matcher(search_box);
                }
                // Se è stato scelto il numero di telefono
                if(type.equals("phone"))
                {
                    // Controllo se il numero di telefono inserito è valido
                    pattern = Pattern.compile("[0-9]{10}");
                    //Pattern cellulare 555-5555555
                    //Pattern.compile("[0-9]{3}-[0-9]{7}");
                    mat = pattern.matcher(search_box);
                }
                // Se il valore inserito non è valido
                if(!mat.matches())
                {
                    showToast("Il formato del testo inserito nella search box non è valido", Toast.LENGTH_SHORT);
                }
                else
                {
                    //TEST
                    // Avvio l'animazione del pulsante
                    //searchButton.startAnimation();
                    //testRedmi(search_box);

                    // Avvio l'animazione del pulsante
                    searchButton.startAnimation();
                    // Instanzia un oggetto Http Request Param
                    RequestParams params = new RequestParams();
                    // Imposta il parametro Http type per la ricerca
                    params.put("type", type);
                    // Imposta il parametro Http searchbox per filtrare i risultati
                    params.put("searchbox", search_box);
                    // Invoca il Web Service REST con i parametri Http
                    ricercaUtente(params);
                }
            }
            else
            {
                showToast("Non lasciare i campi vuoti.", Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * Metodo che effettua un'invocazione
     * ad un webservice REST che ricerca
     * un utente all'interno del database
     *
     * @param params della richiesta
     */
    public void ricercaUtente(RequestParams params)
    {
        // Effettua una chiamata al webservice REST usando un oggetto AsyncHttpClient
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utility.getUrlToServer() + "searchUser/doSearch", params ,new JsonHttpResponseHandler()
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
                        // Resetto il pulsante di ricerca
                        searchButton.revertAnimation();
                        searchButton.setBackgroundResource(R.drawable.sl_orange_bg);
                        showNegativePopup(SearchUser.this, response.getString("msg"));
                    }
                    // Utente trovato
                    if(tag.equals("user"))
                    {
                        // Prendo i dati ricevuti
                        String user_firstname = response.getString("user_firstname");
                        String user_lastname = response.getString("user_lastname");
                        String book_user = user_firstname + " " + user_lastname;
                        // Li salvo nelle SharedPreferences
                        SaveSharedPreference.setSearchedUser(getApplicationContext(), book_user, search_box);
                        // Mostro la schermata con i dati ricevuti
                        revealFoundUser(book_user, search_box);
                    }
                }
                catch (JSONException e)
                {
                    // Resetto il pulsante di ricerca
                    searchButton.revertAnimation();
                    searchButton.setBackgroundResource(R.drawable.sl_orange_bg);
                    showNegativePopup(SearchUser.this, "Codice errore: " + statusCode + "." + e.toString());
                    e.printStackTrace();
                }
            }

            // Quando la risposta restituita dal webservice ha Http response code diverso da '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                // Resetto il pulsante di ricerca
                searchButton.revertAnimation();
                searchButton.setBackgroundResource(R.drawable.sl_orange_bg);
                showNegativePopup(SearchUser.this, "Codice errore: " + statusCode + "." + throwable.toString());
            }
        });
    }

    /**
     * Metodo che crea un effetto per mostrare i dati dell'utente ricevuti
     */
    private void revealFoundUser(String bookUser, String searchBox)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            searchButton.setElevation(0f);
        }
        setFoundUser(bookUser, searchBox);
        int x = foundUserLayout.getWidth();
        int y = foundUserLayout.getHeight();
        int startX = (int) (getFinalWidth() / 2 + searchButton.getX());
        int startY = (int) (getFinalHeight() / 2 + searchButton.getY());
        float radius = Math.max(x, y) * 1.2f;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            Animator reveal = ViewAnimationUtils.createCircularReveal(foundUserLayout, startX, startY, 0, radius);
            reveal.setDuration(800);
            reveal.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    setResetButton();
                }
            });
            reveal.start();
        }
    }

    /**
     * Metodo che imposta la schermata in base ai dati
     * dell'utente ricercato, ricevuti dal server
     *
     * @param bookUser dati dell'utente
     * @param searchBox valore usato per la ricerca
     */
    private void setFoundUser(String bookUser, String searchBox)
    {
        foundUserLayout = findViewById(R.id.foundUserLayout);
        foundUserLayout.setVisibility(View.VISIBLE);
        bookUserTV = findViewById(R.id.book_user);
        bookUserTV.setText(bookUser);
        userSearchValueTV = findViewById(R.id.user_search_value);
        userSearchValueTV.setText(searchBox);
    }

    /**
     * Metodo che imposta l'azione da compiere quando si
     * preme sul tasto di reset, ovvero cancellare la ricerca
     * utente avvenuta per farne una nuova
     */
    private void setResetButton()
    {
        resetButton = findViewById(R.id.resetButton);
        resetButton.setVisibility(View.VISIBLE);
        resetButton.setOnClickListener(v ->
        {
            // Resetto la caselle per la nuova ricerca
            bookUserTV.setText("");
            userSearchValueTV.setText("");
            searchBoxET.setText("");
            // Resetto il pulsante di ricerca
            searchButton.revertAnimation();
            searchButton.setBackgroundResource(R.drawable.sl_orange_bg);
            foundUserLayout.setVisibility(View.INVISIBLE);
            //Rimuovo l'utente cercato dalle SharedPreferences
            SaveSharedPreference.removeSearchedUser(getApplicationContext());
            resetButton.setVisibility(View.INVISIBLE);
        });
    }

    private int getFinalWidth()
    {
        return (int) getResources().getDimension(R.dimen.get_width);
    }

    private int getFinalHeight()
    {
        return (int) getResources().getDimension(R.dimen.get_height);
    }

    /**
     * Esegue il test su dispositivi reali
     * @param search_box valore usato per la ricerca
     */
    private void testRedmi(String search_box)
    {
        new Handler().postDelayed(() ->
        {
            if(search_box.equals("leonard_94@live.it") || search_box.equals("3925952564"))
            {
                SaveSharedPreference.setSearchedUser(getApplicationContext(), "Leonardo Mazzaracchio", search_box);
                revealFoundUser("Leonardo Mazzaracchio", search_box);
            }
            else
            {
                searchButton.revertAnimation();
                searchButton.setBackgroundResource(R.drawable.sl_orange_bg);
                showNegativePopup(SearchUser.this, "Utente non trovato");
            }
        }, 1600);
    }
}