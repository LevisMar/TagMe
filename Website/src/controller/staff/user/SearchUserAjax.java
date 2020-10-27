package controller.staff.user;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import controller.staff.StaffBase;
import model.businessLogic.GestoreUtenti;
import model.pojo.BookUser;
import utility.Json;

/**
 * Servlet che gestisce le richieste Ajax
 * ricevute per la ricerca di un utente nel DB,
 * il cui nome comprende i caratteri inviati nella richiesta
 */
@WebServlet("/staff/user/search_user")
public class SearchUserAjax extends StaffBase 
{
	private static final long serialVersionUID = 1L;
	private GestoreUtenti gu;
	
	public void init(ServletConfig config) throws ServletException 
	{
		gu = new GestoreUtenti();
	}

	@Override
	protected void operationGet(HttpServletRequest request, HttpServletResponse response){}

	@Override
	protected void operationPost(HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			// Conterrà l'utente ricercato, se viene trovato
			BookUser bu = null;
			// Conterrà un messaggio da restituire al client
			JSONObject result = new JSONObject();
			// Conterrà la modalità di ricerca che il client effettua
			String type = request.getParameter("type");
			// Se è una ricerca tramite user_box
			if(type.equals("user_box"))
			{
				int id = 0; String firstname = ""; String lastname = "";
				// Contiene l'id dell'utente
				String par_2 = request.getParameter("par_2");
				// Prendo l'id dell'utente
				id = Integer.parseInt(par_2.substring(par_2.lastIndexOf("_") + 1));
				// Contiene il nome e cognome dell'utente
				String par_3 = request.getParameter("par_3");
				// Prendo il nome dell'utente
				firstname = par_3.substring( 0, par_3.indexOf("<"));
				// Prendo il cognome dell'utente
				lastname = par_3.substring(par_3.lastIndexOf(">") + 1);
				//Se non sono vuoti
				if(id!=0 && !firstname.isEmpty() && !lastname.isEmpty())
				{
					// Cerco l'utente nel DB
					bu = gu.getUserFromId(id, firstname, lastname);
				}
				else
				{
					// Imposto il messaggio di errore
					result = Json.constructMsgJSON("errormsg", "Mancano alcuni dati utili per la ricerca");
				}
			}
			// Se è una ricerca tramite search_box
			if(type.equals("search_box"))
			{
				// Definisce in base a cosa fare la ricerca (mail/telefono)
				String searchby = request.getParameter("par_2");
				// Conterrà la mail o il telefono dell'utente da ricercare
				String parameter = request.getParameter("par_3");
				if(searchby.equals("MAIL"))
				{
					// Cerco l'utente nel DB
					bu = gu.getUserFromEmail(parameter);
				}
				else if(searchby.equals("CELLULARE"))
				{
					// Cerco l'utente nel DB
					bu = gu.getUserFromPhone(parameter);
				}
				else
				{
					// Imposto il messaggio di errore
					result = Json.constructMsgJSON("errormsg", "Il tipo di parametro usato per la ricerca non è adatto");
				}
			}
			// Se non trovo l'utente
			if(bu == null)
			{
				// Imposto il messaggio di errore
				result = Json.constructMsgJSON("errormsg", "Non ho trovato un utente con il parametro dato!");
			}
			else
			{
		    	// Apro una sessione
				HttpSession session = request.getSession();
				// Imposto la sua durata
				session.setMaxInactiveInterval(7*24*60*60);
				// Creo l'attributo nome utente di sessione che sto attualmente visualizzando
				session.setAttribute("current_user",bu);
				// Imposto il messaggio di successo
				result = Json.constructMsgJSON("okmsg", "user_profile");
			}

	    	response.setContentType("application/json");
	    	response.getWriter().print(result);
		} 
		catch (IOException | JSONException e) 
		{
			e.printStackTrace();
		}
	}    
}