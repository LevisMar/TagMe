package com.example.tagme;

import androidx.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Descrive un oggetto libro che andrà
 * a comporre quella lista dei libri di cui
 * si vuole effettuare una transazione
 */
class ListItem
{
    private int mImageResource;
    // Contiene il barcode del libro
    private String mBarcode;
    // Contiene il titolo del libro
    private String mBookTitle;
    // Contiene gli autori del libro
    private String mAuthorList;
    // Indica se l'elemento della lista è rimovibile
    private String mIsEraseable;

    /**
     * Costruttore dell'elemento della lista
     *
     * @param book da inserire all'interno della riga
     * @param eraseable indica se è rimovibile dalla lista
     * @throws JSONException errori nella gestione del JSON
     */
    ListItem(JSONObject book, String eraseable) throws JSONException
    {
        mImageResource = R.drawable.ic_book_white;
        mBarcode = book.getString("barcode");
        mBookTitle = book.getString("book_name");
        mAuthorList = book.getJSONArray("list_author").toString();
        mIsEraseable = eraseable;
    }

    /**
     * @return l'immagine associata alla riga del libro
     */
    int getmImageResource()
    {
        return mImageResource;
    }

    /**
     * @return il barcode associato al libro
     */
    String getmBarcode()
    {
        return mBarcode;
    }

    /**
     * @return il titolo associato al libro
     */
    private String getmBookTitle()
    {
        return mBookTitle;
    }

    /**
     * @return la lista degli autori associata al libro
     */
    private String getmAuthorList()
    {
        return mAuthorList;
    }

    /**
     * Restituisce informazioni associate al libro, in
     * un formato descrittivo (titolo, autori)
     *
     * @return una stringa contenente le informazioni del libro
     */
    String getBookInfo()
    {
        String result = "TITOLO:\n " + getmBookTitle() + "\n\nAUTORI: \n";
        String author_list = getmAuthorList().substring(1, getmAuthorList().length()-1);
        result += author_list;
        return  result;
    }

    /**
     * Converto i dati associati al libro nel formato JSON
     *
     * @return un oggetto JSON con i dati del libro
     */
    JSONObject getBookToJson()
    {
        JSONObject obj = new JSONObject();
        try
        {
            JSONArray author = new JSONArray(mAuthorList);
            // Tento di inserire i vari dati del libro
            obj.put("tag", "book");
            obj.put("book_name", mBookTitle);
            obj.put("list_author", author);
            obj.put("barcode", mBarcode);
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * @return true se l'elemento è rimovibile,
     *         false altrimenti
     */
    String ismIsEraseable()
    {
        return mIsEraseable;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        ListItem other = (ListItem) obj;
        if (mBarcode == null)
        {
            return other.mBarcode == null;
        }
        else return mBarcode.equals(other.mBarcode);
    }
}