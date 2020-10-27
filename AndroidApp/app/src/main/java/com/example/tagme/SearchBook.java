package com.example.tagme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import cz.msebera.android.httpclient.Header;
import static com.example.tagme.Utility.setLogout;
import static com.example.tagme.Utility.setNextButton;
import static com.example.tagme.Utility.showInformativePopup;
import static com.example.tagme.Utility.showNegativePopup;
import static com.example.tagme.Utility.showToast;

public class SearchBook extends AppCompatActivity
{
    RecyclerView mRecyclerView;
    ListAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    // Conterrà la lista dei libri interessati dalla transazione
    ArrayList<ListItem> listItems = new ArrayList<>();
    // Conterrà la lista dei barcode salvati nelle SharedPreferences
    Set<String> listBarcode;
    // Funge da pulsante per avviare la fotocamera, e ricerca libro
    ImageButton cameraButton, searchButton;
    // EditText che conterrà il barcode del libro da cercare
    EditText barcodeET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book_activity);
        TextView logout = findViewById(R.id.logout);
        setLogout(logout);
        barcodeET = findViewById(R.id.barcode);
        // Imposto la camera scan
        setCameraScan();
        // Imposto il pulsante di ricerca del libro
        setSearchButton();
        // Imposto il pulsante per andare indietro
        setBackButton();
        Button nextActivityButton = findViewById(R.id.next_activity_button);
        setNextButton(nextActivityButton, Summary.class);
        buildRecyclerView();

        mAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                // Prendo il libro selezionato
                ListItem book = listItems.get(position);
                // Mostro le info del libro
                showInformativePopup(SearchBook.this, book.getBookInfo());
            }

            @Override
            public void onDeleteClick(int position)
            {
                // Prendo il libro selezionato
                ListItem bookLI = listItems.get(position);
                // Lo rimuovo dalla lista
                listItems.remove(bookLI);
                mAdapter.notifyItemRemoved(position);
                // Lo rimuovo anche da quelli da salvare nelle SharedPreferences
                listBarcode.remove(bookLI.getBookToJson().toString());
                // Salvo la lista
                SaveSharedPreference.setBookList(getApplicationContext(), listBarcode);
            }
        });
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
     * Metodo che imposta la fotocamera per lo scan
     */
    public void setCameraScan()
    {
        cameraButton = findViewById(R.id.camera);
        final Activity activity = this;
        cameraButton.setOnClickListener(v ->
        {
            IntentIntegrator integrator = new IntentIntegrator(activity);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan a barcode");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        });
    }

    /**
     * Metodo che imposta il pulsante di
     * ricerca di un libro
     */
    public void setSearchButton()
    {
        searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(v ->
        {
            // Prendo il barcode del libro da cercare
            String barcode = barcodeET.getText().toString();
            // Se non è stato specificato un barcode
            if(barcode.isEmpty())
            {
                showToast("Inserisci un barcode prima di effettuare una ricerca", Toast.LENGTH_LONG);
            }
            else
            {
                // Verifico se il barcode è già stato cercato
                boolean exist = checkBarcodeExist(barcode);
                if(exist)
                {
                    showToast("libro già presente all'interno della lista", Toast.LENGTH_SHORT);
                }
                else
                {
                    showToast("Avvio ricerca...", Toast.LENGTH_SHORT);
                    // Instanzia un oggetto Http Request Param
                    RequestParams params = new RequestParams();
                    // Imposta il parametro Http username con il valore della Edit View username
                    params.put("barcode", barcode);
                    searchBook(params);
                }
            }
        });
    }

    /**
     * Metodo richiamato quando la fotocamera scan restituisce un risultato o no
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
        {
            // Se l'utente esce dallo scan senza aver trovato niente
            if(result.getContents()==null)
            {
                showToast("Hai annullato lo scan", Toast.LENGTH_SHORT);
            }
            else
            {
                // Instanzia un oggetto Http Request Param
                RequestParams params = new RequestParams();
                // Imposta il parametro Http username con il valore della Edit View username
                params.put("barcode", result.getContents());
                searchBook(params);
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Costruisce la lista dei libri all'avvio della schermata, se presenti
     */
    public void buildRecyclerView()
    {
        // Recupero la lista dei libri, se salvata precedentemente
        Set<String> listBarcodeSaved = SaveSharedPreference.getBookList(getApplicationContext());
        // Se è vuota
        if(listBarcodeSaved == null)
        {
            // La inizializzo
            listBarcode = new HashSet<>();
        }
        else
        {
            // Copio la lista
            listBarcode = new HashSet<>(listBarcodeSaved);
            // Per ogni elemento della lista
            for(String barcode: listBarcode)
            {
                try
                {
                    //Lo aggiungo nella lista della schermata
                    JSONObject obj = new JSONObject(barcode);
                    listItems.add(new ListItem(obj, "true"));
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        mRecyclerView = findViewById(R.id.book_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ListAdapter(listItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Metodo che effettua un'invocazione
     * ad un webservice REST che ricerca
     * un libro all'interno del database
     *
     * @param params della richiesta
     */
    public void searchBook(RequestParams params)
    {
        // Effettua una chiamata al webservice REST usando un oggetto AsyncHttpClient
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utility.getUrlToServer() + "searchBook/doSearch", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try
                {
                    // Oggetto JSON con la risposta dal webservice
                    String tag = response.getString("tag");
                    // Se è presente un messaggio di errore
                    if (tag.equals("msg"))
                    {
                        showNegativePopup(SearchBook.this, response.getString("msg"));
                    }
                    // Libro trovato
                    if (tag.equals("book"))
                    {
                        showBookFoundPopup(response);
                    }
                } catch (JSONException e)
                {
                    showNegativePopup(SearchBook.this, "Codice errore: " + statusCode + "." + e.toString());
                    e.printStackTrace();
                }
            }

            // Quando la risposta restituita dal webservice ha Http response code diverso da '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                showNegativePopup(SearchBook.this, "Codice errore: " + statusCode + "." + throwable.toString());
            }
        });
    }

    /**
     * Metodo che mostra in formato dialog le informazioni
     * del libro trovato tramite ricerca con barcode
     *
     * @param book di cui mostrare le informazioni
     * @throws JSONException in caso di errori
     */
    public void showBookFoundPopup(JSONObject book) throws JSONException
    {
        // Creo il dialog
        Dialog popupDialog = new Dialog(this);
        popupDialog.setContentView(R.layout.popup_positive);
        Button btnPopupPositive = popupDialog.findViewById(R.id.btnPopupPositive);
        btnPopupPositive.setText(R.string.add);
        TextView titlePopupTV = popupDialog.findViewById(R.id.titlePopupTV);
        titlePopupTV.setText(R.string.found);
        TextView messagePopupTV = popupDialog.findViewById(R.id.messsagePopupTV);
        // Creo un nuovo elemento della lista dal libro
        ListItem bookLI = new ListItem(book, "true");
        btnPopupPositive.setOnClickListener(v ->
        {
            // Chiudo il Dialog
            popupDialog.dismiss();
            // Aggiungo l'elemento alla lista
            listItems.add(bookLI);
            mAdapter.notifyItemInserted(listItems.size() - 1);
            // Aggiungo il barcode alla lista
            listBarcode.add(book.toString());
            // Salvo la lista dei barcode
            SaveSharedPreference.setBookList(getApplicationContext(), listBarcode);
        });
        // Recupero le info del libro
        String msg = bookLI.getBookInfo();
        messagePopupTV.setText(msg);
        Objects.requireNonNull(popupDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDialog.show();
    }

    /**
     * Metodo che verifica se il barcode specificato
     * è già stato inserito nella lista,e quindi cercato
     *
     * @param barcode del libro da cercare
     * @return true se è presente nella lista, false altrimenti
     */
    public boolean checkBarcodeExist(String barcode)
    {
        // Conterrà l'esito della verifica
        boolean exist = false;
        // Per ciascun elemento della lista
        for(ListItem book : listItems)
        {
            // Controllo se corrisponde a quello cercato
            if(book.getmBarcode().equals(barcode))
            {
                exist = true;
            }
        }
        return exist;
    }

    /**
     * Metodo che gestisce l'azione da eseguire
     * quando si preme sul pulsante indietro
     */
    public void setBackButton()
    {
        Button back = findViewById(R.id.back_activity_button);
        back.setOnClickListener(v -> onBackPressed());
    }
}