package webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONObject;
import model.businessLogic.GestoreStaff;
import model.businessLogic.GestoreStaff.StatoLogin;
import utility.Json;

/**
 * Web Service che gestisce le richieste 
 * di login da parte degli utenti app
 */
@Path("/login")
public class Login 
{
	// Metodo HTTP POST
	@POST
	// Path: http://localhost/<appln-folder-name>/login/dologin
	@Path("/dologin")
	// Produce un JSON come risposta
	@Produces(MediaType.APPLICATION_JSON)
	// Consuma dei dati provenienti da una form
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	/**
	 * Esegue il login, verificando prima i dati ricevuti dall'app client
	 * @param uname username inserito
	 * @param pwd password digitata
	 * @return un oggetto JSON che indica l'esito del login
	 * @throws Exception
	 */
	public static JSONObject doLogin(@FormParam("username") String uname, @FormParam("password") String pwd) throws Exception
	{	
		// Contiene la risposta da inviare all'applicazione android
		JSONObject response = new JSONObject();
		System.out.println("Tento il login con i dati ricevuti dall'app");
		// Controllo se i parametri inviati sono diversi da null
		if(uname!=null && pwd!=null)
		{
			GestoreStaff gS = new GestoreStaff();
			// Conterrà il risultato dell'operazione di login
			StatoLogin stato = StatoLogin.error;
			// Tento il login utilizzando i dati forniti
			stato = gS.login(uname, pwd);
			switch(stato)
			{
				case ok:
					response = Json.constructStaffJSON("staff_logged", uname);
					break;
				case nouser:
					response = Json.constructMsgJSON("msg", uname + " non è presente nel sistema");
					break;
				case nopsw:
					response = Json.constructMsgJSON("msg", "la password digitata non è corretta");
					break;
				case error:
					response = Json.constructMsgJSON("msg", "si è verificato un errore durante l'autenticazione");
					break;
			}
		}
		else
		{
			response = Json.constructMsgJSON("msg", "username e password non ricevuti dal server");
		}
		
		return response;
	}
}