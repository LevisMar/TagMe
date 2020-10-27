package utility;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import model.pojo.BookUser;

/**
 * @author Leonardo
 *
 * Classe utilizzata per generare un oggetto JSON
 * da restituire al client con i dati richiesti o
 * eventuali errori trovati
 */
public class Json 
{

	/**
	 * Metodo per costruire un JSON che contiene un oggetto BookUser
	 * compreso di indirizzo per trovare l'immagine dell'utente
	 * associata al BookUser.
	 * 
	 * @param tag per identificare il tipo di messaggio
	 * @param bU BookUser trovato
	 * @param img_user indirizzo immagine profilo utente
	 * @return un oggetto JSON che contiene le informazioni specificate
	 * @throws JSONException
	 */
	public static JSONObject constructUserJSON(String tag, BookUser bU, String img_user) throws JSONException
	{
		// Creo un JSONObject in cui inserire i dati
		JSONObject obj = new JSONObject();
		try 
		{	
			// Tento di inserire i vari dati ricevuti
			obj.put("tag", tag);
			obj.put("user_firstname", bU.getFirstname());
			obj.put("user_lastname", bU.getLastname());
			obj.put("user_email", bU.getEmail());
			obj.put("user_number", bU.getPhonenumber());			
			obj.put("img_user", img_user);
		} 
		catch (JSONException e) 
		{
			throw e;
		}		
		// Restituisco il JSONObject
		return obj;
	}
	
	/**
	 * Metodo per costruire un JSON che contiene un oggetto StaffMember
	 * 
	 * @param tag per identificare il tipo di messaggio
	 * @param sM StaffMember trovato
	 * @return un oggetto JSON che contiene le informazioni specificate
	 * @throws JSONException
	 */
	public static JSONObject constructStaffJSON(String tag, String s_username) throws JSONException
	{
		// Creo un JSONObject in cui inserire i dati
		JSONObject obj = new JSONObject();
		try 
		{	
			// Tento di inserire i vari dati ricevuti
			obj.put("tag", tag);
			obj.put("staff_username", s_username);
		} 
		catch (JSONException e) 
		{
			throw e;
		}		
		// Restituisco il JSONObject
		return obj;
	}
	
	/**
	 * Metodo per costruire un JSON con messaggio
	 * 
	 * @param tag per identificare il tipo di messaggio
	 * @param msg da restituire al richiedente
	 * @return un oggetto JSON che contiene le informazioni specificate
	 * @throws JSONException 
	 */
	public static JSONObject constructMsgJSON(String tag, String msg) throws JSONException
	{
		// Creo un JSONObject in cui inserire i dati
		JSONObject obj = new JSONObject();
		try 
		{
			// Tento di inserire i vari dati ricevuti
			obj.put("tag", tag);
			obj.put("msg", msg);
		} 
		catch (JSONException e) 
		{
			throw e;
		}
		// Restituisco il JSONObject 
		return obj;
	}
	
	/**
	 * Metodo per costruire un JSON che contiene un oggetto Book
	 * con alcuni informazioni importanti, quali nome del libro,
	 * lista degli autori e barcode
	 * 
	 * @param tag per identificare il tipo di messaggio
	 * @param book_name del libro
	 * @param list_author del libro
	 * @param barcode del libro
	 * @return un oggetto JSON che contiene le informazioni specificate
	 * @throws JSONException
	 */
	public static JSONObject constructBookJSON(String tag, String book_name, JSONArray list_author, String barcode) throws JSONException
	{
		// Creo un JSONObject in cui inserire i dati
		JSONObject obj = new JSONObject();
		try 
		{	
			// Tento di inserire i vari dati ricevuti
			obj.put("tag", tag);
			obj.put("book_name", book_name);
			obj.put("list_author", list_author);
			obj.put("barcode", barcode);
		} 
		catch (JSONException e) 
		{
			throw e;
		}		
		// Restituisco il JSONObject
		return obj;
	}	
}