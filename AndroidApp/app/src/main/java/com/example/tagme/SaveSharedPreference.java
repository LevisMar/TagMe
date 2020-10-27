package com.example.tagme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.Set;

/**
 * Classe utilizzata per salvare e recuperare dati
 * in formato chiave-valore, all'interno della memoria locale.
 */
class SaveSharedPreference
{
    // Stringhe a cui associo valori univoci per i dati da salvare
    private static final String LOGGED_IN_PREF = "logged_in_status";
    private static final String LOGGED_USER_IN_PREF = "logged_user";
    private static final String BOOK_USER = "bookUser";
    private static final String USER_SEARCH_VALUE = "searchValue";
    private static final String TRANSACTION_TYPE = "typeTransaction";
    private static final String BOOK_LIST = "bookList";

    /**
     * Restituisce le preferenze salvate
     *
     * @param context dell'applicazione
     * @return le SharedPreferences
     */
    private static SharedPreferences getPreferences(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Imposta lo status di login, e l'utente che lo esegue
     *
     * @param context dell'applicazione
     * @param username dell'utente loggato
     */
    static void setLoggedIn(Context context, String username)
    {
        // Prendo l'editor per salvare variabili nelle SharedPreferences
        SharedPreferences.Editor editor = getPreferences(context).edit();
        // Imposto l'utente che si è loggato
        editor.putString(LOGGED_USER_IN_PREF, username);
        // Imposto che il login è avvenuto
        editor.putBoolean(LOGGED_IN_PREF, true);
        // Applico le modifiche
        editor.apply();
    }

    /**
     * Restituisce lo status del login
     *
     * @param context dell'applicazione
     * @return un booleano che indica se il login è stato fatto
     */
    static boolean getLoggedStatus(Context context)
    {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    /**
     * Restituisce l'user loggato
     *
     * @param context dell'applicazione
     * @return l'username dell'utente loggato
     */
    static String getLoggedUser(Context context)
    {
        return getPreferences(context).getString(LOGGED_USER_IN_PREF, null);
    }

    /**
     * Imposta i parametri utilizzati per la ricerca
     * di un utente all'interno del database
     *
     * @param context dell'applicazione
     * @param bookUser parametro usato per la ricerca
     * @param searchValue tipo di parametro usato per la ricerca (email/tel)
     */
    static void setSearchedUser(Context context, String bookUser, String searchValue)
    {
        // Prendo l'editor per salvare variabili nelle SharedPreferences
        SharedPreferences.Editor editor = getPreferences(context).edit();
        // Imposto l'utente cercato nel DB
        editor.putString(BOOK_USER, bookUser);
        // Imposto il valore usato per la ricerca dell'utente
        editor.putString(USER_SEARCH_VALUE, searchValue);
        // Applico le modifiche
        editor.apply();
    }

    /**
     * Restituisce il parametro usato per la ricerca dell'utente
     *
     * @param context dell'applicazione
     * @return il parametro usato in formato stringa
     */
    static String getBookUser(Context context)
    {
        return getPreferences(context).getString(BOOK_USER, null);
    }
    /**
     * Restituisce il tipo di parametro usato per la ricerca
     * dell'utente (email/tel)
     *
     * @param context dell'applicazione
     * @return il parametro usato in formato stringa
     */
    static String getUserSearchValue(Context context)
    {
        return getPreferences(context).getString(USER_SEARCH_VALUE, null);
    }

    /**
     * Resetta il parametro utilizzato per la ricerca dell'utente
     *
     * @param context dell'applicazione
     */
    static void removeSearchedUser(Context context)
    {
        // Prendo l'editor per salvare variabili nelle SharedPreferences
        SharedPreferences.Editor editor = getPreferences(context).edit();
        // Rimuovo l'utente cercato nel DB
        editor.remove(BOOK_USER);
        // Rimuovo il valore usato per la ricerca dell'utente
        editor.remove(USER_SEARCH_VALUE);
        // Applico le modifiche
        editor.apply();
    }

    /**
     * Imposta il tipo di transazione che si desidera effettuare
     *
     * @param context dell'applicazione
     * @param typeTransaction il tipo di transazione (prestito/restituzione)
     */
    static void setTransactionType(Context context, String typeTransaction)
    {
        // Prendo l'editor per salvare variabili nelle SharedPreferences
        SharedPreferences.Editor editor = getPreferences(context).edit();
        // Imposto il tipo di transazione
        editor.putString(TRANSACTION_TYPE, typeTransaction);
        // Applico le modifiche
        editor.apply();
    }

    /**
     * Restituisce il tipo di transazione che si sta effettuando
     *
     * @param context dell'applicazione
     * @return il tipo di transazione
     */
    static String getTransactionType(Context context)
    {
        return getPreferences(context).getString(TRANSACTION_TYPE, null);
    }

    /**
     * Imposta la lista di libri (barcode) su cui effettuare la transazione
     *
     * @param context dell'applicazione
     * @param bookList lista dei libri in formato barcode
     */
    static void setBookList(Context context, Set<String> bookList)
    {
        // Prendo l'editor per salvare variabili nelle SharedPreferences
        SharedPreferences.Editor editor = getPreferences(context).edit();
        // Imposto la lista dei libri
        editor.putStringSet(BOOK_LIST, bookList);
        // Applico le modifiche
        editor.apply();
    }

    /**
     * Restituisce la lista dei libri su cui si vuole
     * effettuare la transazione
     *
     * @param context dell'applicazione
     * @return la lista dei libri in formato barcode
     */
    static Set<String> getBookList(Context context)
    {
        return getPreferences(context).getStringSet(BOOK_LIST, null);
    }

    /**
     * Rimuove tutte le variabili salvate finora
     * nelle SharedPreferences
     *
     * @param context dell'applicazione
     */
    static void removePreferences(Context context)
    {
        // Prendo l'editor delle SharedPreferences
        SharedPreferences.Editor editor = getPreferences(context).edit();
        // Rimuovo tutte le variabili salvate
        editor.clear();
        // Applico le modifiche
        editor.apply();
    }
}