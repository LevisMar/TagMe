package webservice;

import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import model.businessLogic.GestoreLibreria;
import model.pojo.Author;
import model.pojo.Book;
import utility.Json;

/**
 * Web Service che gestisce le richieste 
 * di ricerca di un libro da parte degli utenti app
 */
@Path("/searchBook")
public class SearchBook 
{
	// Metodo HTTP POST
	@POST
	// Path: http://localhost/<appln-folder-name>/searchBook/doSearch
	@Path("/doSearch")
	// Produce un JSON come risposta
	@Produces(MediaType.APPLICATION_JSON)
	// Consuma dei dati provenienti da una form
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	/**
	 * Ricerca un determinato, usando il barcode ricevuto dall'app client
	 * 
	 * @param barcode di cui cercare il libro
	 * @return un oggetto JSON che indica l'esito della ricerca
	 * @throws Exception
	 */
	public static JSONObject doSearch(@FormParam("barcode") String barcode) throws Exception
	{	
		// Contiene la risposta da inviare all'applicazione android
		JSONObject response = new JSONObject();
		// Se il barcode del libro da cercare è specificato
		if(barcode!=null)
		{
			GestoreLibreria gL = new GestoreLibreria();
			// Conterrà il libro desiderato, se viene trovato
			Book b = null;
			b = gL.getBookFromBarcode(barcode);
			// Se il libro non viene trovato
			if(b == null)
			{
				response = Json.constructMsgJSON("msg", "Il libro non è stato trovato");
			}
			// Raccolgo ulteriori dati
			else
			{
				// Conterrà la lista degli autori associati al libro trovato
				Set<Author> autori = b.getAuthors();
				// Creo un JSONArray che conterrà la lista degli autori sotto forma di JSON
				JSONArray lista_autori = new JSONArray();
				// Aggiungo ciascun autore nell'array
				for(Author a : autori)
				{
					lista_autori.put(a.getName());
				}
				// Genero la risposta che verrà restituita all'app client
				response = Json.constructBookJSON("book", b.getName(), lista_autori, b.getBarcode());
			}
		}
		else
		{
			// Genero la risposta che verrà restituita all'app client
			response = Json.constructMsgJSON("msg", "Dati non ricevuti dal server");
		}		
		return response;
	}
}