package com.example.tagme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cz.msebera.android.httpclient.Header;
import static com.example.tagme.Utility.isNotNull;
import static com.example.tagme.Utility.setLogout;
import static com.example.tagme.Utility.showInformativePopup;
import static com.example.tagme.Utility.showNegativePopup;
import static com.example.tagme.Utility.showToast;

public class Summary extends AppCompatActivity
{
    // TextView presenti nella schermata per i dati raccolti
    TextView transactionTV, NameuserTV, userKeyValueTV;
    // Conterrà la lista dei libri
    RecyclerView mRecyclerView;
    // Sarà l'adapter da usare per la lista sopra
    ListAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    // Contiene tutti gli elementi all'interno del RecyclerView
    ArrayList<ListItem> listItems = new ArrayList<>();
    // Contiene tutta la lista dei libri da registrare
    Set<String> listBook;
    // Rappresenta il pulsante per registrare la transazione
    CircularProgressButton circularProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        // Imposto alcuni elementi nella schermata
        setTransaction();
        setUserData();
        TextView logout = findViewById(R.id.logout);
        setLogout(logout);
        setBackButton();
        buildRecyclerView();
        mAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                // Prendo l'elemento cliccato
                ListItem book = listItems.get(position);
                // Mostro le info del libro
                showInformativePopup(Summary.this, book.getBookInfo());
            }

            @Override
            public void onDeleteClick(int position){}
        });
        setConfermaButton();
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
    }

    /**
     * Metodo che setta, all'interno della schermata,
     * il tipo di transazione da effettuare
     */
    public void setTransaction()
    {
        transactionTV = findViewById(R.id.transactionType);
        // Recupero il tipo di transazione salvata precedentemente
        String transactionType = SaveSharedPreference.getTransactionType(getApplicationContext());
        // Se è definito
        if(transactionType!=null)
        {
            // Imposto il testo in base al valore recuperato
            if(transactionType.equals("prestito"))
            {
                transactionTV.setText(R.string.load);
            }
            else if(transactionType.equals("restituzione"))
            {
                transactionTV.setText(R.string.restitution);
            }
        }
        else
        {
            transactionTV.setText(R.string.not_available);
        }
    }

    /**
     * Metodo che setta i dati dell'utente soggetto
     * alla transazione all'interno della schermata
     */
    public void setUserData()
    {
        NameuserTV = findViewById(R.id.nameUser);
        userKeyValueTV = findViewById(R.id.userKeyValue);
        // Recupero il nome/cognome dell'utente salvato precedentemente
        String nameUser = SaveSharedPreference.getBookUser(getApplicationContext());
        // Recupero il valore usato per la ricerca (email/tel)
        String userKeyValue = SaveSharedPreference.getUserSearchValue(getApplicationContext());
        // Se non ci sono
        if(nameUser == null && userKeyValue == null)
        {
            NameuserTV.setText(R.string.not_available);
            NameuserTV.setAllCaps(true);
            userKeyValueTV.setVisibility(View.GONE);
        }
        // Altrimenti li setto nella schermata
        else
        {
            NameuserTV.setText(nameUser);
            userKeyValueTV.setText(userKeyValue);
        }
    }

    /**
     * Metodo che costruisce la lista con tutti i barcode dei
     * libri di cui effettuare la transazione, all'interno
     * della schermata.
     */
    public void buildRecyclerView()
    {
        /*
         * Prendo la lista dei libri salvata precedentemente,
         * ogni stringa rappresenta un libro che al momento
         * della ricerca era in formato JSON, poi salvato in
         * formato stringa per poter essere salvato nelle
         * SharedPreferences
         */
        Set<String> listBookSaved = SaveSharedPreference.getBookList(getApplicationContext());
        // Se la lista è vuota
        if(listBookSaved == null)
        {
            // La creo nuova
            listBook = new HashSet<>();
        }
        else
        {
            /*
             * Creo una nuova lista da quella recuperata,
             * per evitare incongruenze con quella salvata
             */
            listBook = new HashSet<>(listBookSaved);
            // Per ciascun libro
            for(String book: listBook)
            {
                try
                {
                    // Lo riconverto in formato JSON
                    JSONObject obj = new JSONObject(book);
                    // Lo aggiungo alla lista
                    listItems.add(new ListItem(obj, "false"));
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        // Imposto la view per vedere la lista dei libri
        mRecyclerView = findViewById(R.id.book_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ListAdapter(listItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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

    /**
     * Metodo che imposta il comportamento da attribuire
     * al pulsante di conferma, presente nella schermata
     */
    public void setConfermaButton()
    {
        circularProgressButton = findViewById(R.id.buttonStartTransaction);
        circularProgressButton.setOnClickListener(v ->
        {
            // Prende il tipo di transazione da effettuare
            String transactionType = transactionTV.getText().toString();
            // Prende il valore chiave per la ricerca dell'utente
            String userKeyValue = userKeyValueTV.getText().toString();
            // Prende la lista dei barcode dei libri da aggiungere
            JSONArray barcodeList = new JSONArray();
            for(ListItem book : listItems)
            {
                // Prendo il barcode dalla lista
                String barcode = book.getmBarcode();
                barcodeList.put(barcode);
            }
            // Prendo l'username del membro dello staff che effettuerà l'operazione
            String staffName = SaveSharedPreference.getLoggedUser(Summary.this);
            // Instanzia un oggetto Http Request Param
            RequestParams params = new RequestParams();
            // Se tutti i parametri sono stati impostati
            if(isNotNull(transactionType) && isNotNull(userKeyValue) && isNotNull(staffName) && barcodeList.length()>0)
            {
                // Avvio l'animazione del pulsante
                circularProgressButton.startAnimation();
                // Aggiungo il tipo di transazione
                params.put("transactionType", transactionType);
                // Aggiungo il valore usato per la ricerca utente
                params.put("userKeyValue", userKeyValue);
                // Aggiungo la lista dei barcode
                params.put("barcodeList", barcodeList.toString());
                // Aggiungo il membro dello staff
                params.put("staffName", staffName);
                // Invoca il Web Service REST con i parametri Http
                recordTransaction(params);
            }
            else
            {
                showToast("Mancano alcuni elementi essenziali", Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * Metodo che invia i dati della transazione al server
     * per poter essere registrata all'interno del database
     * @param params contiene i dati della transazione
     */
    public void recordTransaction(RequestParams params)
    {
        // Effettua una chiamata al webservice REST usando un oggetto AsyncHttpClient
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utility.getUrlToServer() + "transaction/recordTransaction", params ,new JsonHttpResponseHandler()
        {
            // Quando la risposta restituita dal webservice ha Http response code 200
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                // Reimposto il button per inviare i dati
                circularProgressButton.revertAnimation();
                circularProgressButton.setBackgroundResource(R.drawable.sl_orange_border_bg);
                try
                {
                    // Oggetto JSON con la risposta dal webservice
                    String tag = response.getString("tag");
                    // Se c'è un messaggio di errore
                    if(tag.equals("errormsg"))
                    {
                        showNegativePopup(Summary.this, response.getString("msg"));
                    }
                    // Se c'è un messaggio di successo
                    else if(tag.equals("okmsg"))
                    {
                        showPositivePopup(response.getString("msg"));
                    }
                }
                catch (JSONException e)
                {
                    showNegativePopup(Summary.this, "Codice errore: " + statusCode + "." + e.toString());
                    e.printStackTrace();
                }
            }

            // Quando la risposta restituita dal webservice ha Http response code diverso da 200
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                // Reimposto il button per inviare i dati
                circularProgressButton.revertAnimation();
                circularProgressButton.setBackgroundResource(R.drawable.sl_orange_border_bg);
                showNegativePopup(Summary.this, "Codice errore: " + statusCode + "." + throwable.toString());
            }
        });
    }

    /**
     * Metodo che mostra un dialog, il quale indica
     * una registrazione della transazione avvenuta con successo
     *
     * @param msg da mostrare
     */
    public void showPositivePopup(String msg)
    {
        // Creo il Dialog
        Dialog popupDialog = new Dialog(this);
        popupDialog.setContentView(R.layout.popup_positive);
        Button btnPopupConferm = popupDialog.findViewById(R.id.btnPopupPositive);
        TextView messagePopupTV = popupDialog.findViewById(R.id.messsagePopupTV);
        btnPopupConferm.setOnClickListener(v ->
        {
            // Chiudo il Dialog
            popupDialog.dismiss();
            // Resetto il pulsante di conferma
            circularProgressButton.revertAnimation();
            circularProgressButton.setBackgroundResource(R.drawable.sl_orange_border_bg);
        });
        messagePopupTV.setText(msg);
        Objects.requireNonNull(popupDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDialog.show();
    }
}