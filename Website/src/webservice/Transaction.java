package webservice;

import java.util.ArrayList;
import java.util.Calendar;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import model.businessLogic.GestoreLibreria;
import model.businessLogic.GestoreStaff;
import model.businessLogic.GestoreUtenti;
import model.pojo.Book;
import model.pojo.BookTransaction;
import model.pojo.BookTransaction.Codice;
import model.pojo.BookUser;
import model.pojo.StaffMember;
import utility.Json;

/**
 * Web Service che gestisce le richieste 
 * di registrazione di transazioni da parte 
 * degli utenti app
 */
@Path("/transaction")
public class Transaction 
{
	// Contiene la risposta da inviare all'applicazione android
	static JSONObject response = new JSONObject();
	
	// Metodo HTTP POST
	@POST
	// Path: http://localhost/<appln-folder-name>/transaction/recordTransaction
	@Path("/recordTransaction")
	// Produce un JSON come risposta
	@Produces(MediaType.APPLICATION_JSON)
	// Consuma dei dati provenienti da una form
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	/**
	 * Registra all'interno del database una transazione ricevuta dall'app mobile
	 * 
	 * @param transactionType tipo di transazione
	 * @param userKeyValue valore per la ricerca dell'utente
	 * @param barcodeList, lista dei barcode di libri da aggiungere. Il tipo di parametro
	 * 		  non è List<String> perchè FormParam accetta solo tipi String e int, quindi nel
	 * 		  client metto i barcode in un JSONArray che poi converto in stringa, da riconvertire qui
	 * @param staffName username del membro dello staff che registra la transazione
	 * @return un oggetto JSON che indica l'esito della registrazione
	 * @throws Exception
	 */
	public static JSONObject recordTransaction(@FormParam("transactionType") String transactionType, 
											   @FormParam("userKeyValue") String userKeyValue, 
											   @FormParam("barcodeList") String barcodeList, 
											   @FormParam("staffName") String staffName) throws Exception
	{	
		// Conterrà la lista di tutti i barcode dei libri interessati dalla transazione
		JSONArray listBarcode = new JSONArray(barcodeList);
		// Controllo se i parametri inviati sono diversi da null
		if(transactionType!=null && userKeyValue!=null && listBarcode.length() > 0 && staffName!=null)
		{
			// Conterrà la lista delle trasanzioni valide da registrare
			ArrayList<BookTransaction> transactionList = checkInput(transactionType, userKeyValue, listBarcode, staffName);
			// Se è presente almeno un elemento nella lista
			if(transactionList != null)
			{
				GestoreLibreria gL = new GestoreLibreria();
				// Conterrà l'esito della registrazione delle transizioni
				boolean result =  gL.recordTransaction(transactionList);
				// Se la registrazione è avvenuta con successo
				if(result)
				{
					response = Json.constructMsgJSON("okmsg", "Transazione registrata con successo");
				}
				else
				{
					response = Json.constructMsgJSON("errormsg", "Si sono verificati errori con le transazioni");
				}
			}
		}
		else
		{
			response = Json.constructMsgJSON("errormsg", "il server non ha ricevuto i dati");
		}
		
		return response;
	}
	
	/**
	 * Verifica se gli input ricevuti dall'app client, 
	 * riguardo i dati della transazioni, sono validi
	 * 
	 * @param transactionType tipo di transazione
	 * @param userKeyValue valore per la ricerca dell'utente
	 * @param listBarcode lista dei barcode dei libri da registrare
	 * @param staffName username del membro dello staff che registra la transazione
	 * @return la lista con le transazioni valide
	 * @throws JSONException
	 */
	public static ArrayList<BookTransaction> checkInput(String transactionType, String userKeyValue, JSONArray listBarcode, String staffName) throws JSONException
	{		
		GestoreUtenti gU = new GestoreUtenti();
		// Conterrà l'utente interessato dalla transazione, se trovato
		BookUser bU;
		// Se per la ricerca viene usata la mail
		if(userKeyValue.contains("@"))
		{
			bU = gU.getUserFromEmail(userKeyValue);
		}
		// La ricerca viene fatta usando il numero di telefono
		else
		{
			// Modifico il numero, in modo che rispetti il formato del DB (555-5555555)
			userKeyValue = userKeyValue.substring(0, 3) + "-" + userKeyValue.substring(3, userKeyValue.length());
			System.out.println("Ricerco utente con " + userKeyValue);
			bU = gU.getUserFromPhone(userKeyValue);
		}
		// L'utente non esiste nel DB
		if(bU==null)
		{
			response = Json.constructMsgJSON("errormsg", "" + userKeyValue);
			return null;
		}
		GestoreStaff gS = new GestoreStaff();
		// Controllo che il membro dello staff esista nel DB
		StaffMember sM = gS.getStaffFromUsername(staffName);
		// Se il membro dello staff non esiste nel DB
		if(sM==null)
		{
			response = Json.constructMsgJSON("errormsg", "Il membro dello staff non è stato trovato all'interno del database.");
			return null;
		}		
		// Conterrà il codice per la transazione
		Codice transCode;
		// Se è un prestito
		if(transactionType.equals("prestito"))
		{
			transCode = Codice.PRESTITO;
		}
		// Se è una restituzione
		else if(transactionType.equals("restituzione"))
		{
			transCode = Codice.RESTITUZIONE;
		}
		// Se il tipo di transazione non è valida
		else
		{
			response = Json.constructMsgJSON("errormsg", "Codice di transazione non valido");
			return null;
		}		
		// Controllo che tutti i barcode ricevuti siano associati a libri esistenti
		GestoreLibreria gL = new GestoreLibreria();
		// Conterrà la lista dei libri trovati
		ArrayList<Book> bookList = new ArrayList<Book>();
		// Per ciascun barcode presente nella lista
		for(int i=0; i<listBarcode.length(); i++)
		{
			// Prendo un barcode dalla lista
			String barcode = listBarcode.getString(i);
			// Cerco il rispettivo libro nel database
			Book book = gL.getBookFromBarcode(barcode);
			// Se non esiste un libro con quel barcode
			if(book==null)
			{
				response = Json.constructMsgJSON("errormsg", "Non è stato trovato un libro con barcode: " + barcode);
				return null;
			}
			else
			{
				// Lo aggiungo alla lista di quelli trovati
				bookList.add(book);
			}
		}
		// Prendo la data corrente in formato sql
		java.sql.Date currentTime = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		// Conterrà la lista delle transazioni da registrare
		ArrayList<BookTransaction> transactionsList = new ArrayList<BookTransaction>();
		// Per ciascun libro trovato
		for(Book book : bookList)
		{
			// Creo l'istanza di transazione
			BookTransaction bT = new BookTransaction();
			bT.setProduct(book);
			bT.setStaffMember(sM);
			bT.setUser(bU);
			bT.setTransCode(transCode);
			bT.setData(currentTime);
			// Aggiungo la transazione alla lista
			transactionsList.add(bT);
		}		
		return transactionsList;		
	}
}