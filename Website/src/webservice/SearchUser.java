package webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONObject;
import model.businessLogic.GestoreUtenti;
import model.pojo.BookUser;
import utility.Json;

/**
 * Web Service che gestisce le richieste 
 * di ricerca di un utente da parte degli utenti app
 */
@Path("/searchUser")
public class SearchUser 
{
	// Metodo HTTP POST
	@POST
	// Path: http://localhost/<appln-folder-name>/searchUser/doSearch
	@Path("/doSearch")
	// Produce un JSON come risposta
	@Produces(MediaType.APPLICATION_JSON)
	// Consuma dei dati provenienti da una form
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	/**
	 * Esegue la ricerca di un determinato utente all'interno del database
	 * 
	 * @param type di parametro da usare per la ricerca
	 * @param searchBox valore chiave inserito dall'utente
	 * @return un oggetto JSON che indica l'esito della ricerca
	 * @throws Exception
	 */
	public static JSONObject doSearch(@FormParam("type") String type, @FormParam("searchbox") String searchBox) throws Exception
	{	
		// Contiene la risposta da inviare all'applicazione android
		JSONObject response = new JSONObject();
		// Controllo se i parametri inviati sono diversi da null
		if(type!=null && searchBox!=null)
		{
			GestoreUtenti gU = new GestoreUtenti();
			// Conterrà l'utente desiderato, se viene trovato
			BookUser bU = null;
			// Se la ricerca avviene per email
			if(type.equals("email"))
			{
				// Cerco l'utente nel DB
				bU = gU.getUserFromEmail(searchBox);
			}
			// Se la ricerca avviene per numero di telefono
			else if(type.equals("phone"))
			{
				// Modifico il numero, in modo che rispetti il formato del DB (555-5555555)
				searchBox = searchBox.substring(0, 3) + "-" + searchBox.substring(3, searchBox.length());
				// Cerco l'utente nel DB
				bU = gU.getUserFromPhone(searchBox);
			}
			// Se l'utente non viene trovato
			if(bU == null)
			{
				response = Json.constructMsgJSON("msg", "L'utente non è stato trovato");
			}
			else
			{
				response = Json.constructUserJSON("user", bU, "null");
			}
		}
		else
		{
			response = Json.constructMsgJSON("msg", "Dati non ricevuti dal server");
		}
		
		return response;
	}
}