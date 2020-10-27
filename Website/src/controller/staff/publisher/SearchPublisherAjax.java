package controller.staff.publisher;

import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import controller.staff.StaffBase;
import model.businessLogic.GestoreEditori;

/**
 * Servlet che gestisce le richieste Ajax
 * ricevute per la ricerca di un editore nel DB,
 * il cui nome comprende i caratteri inviati nella richiesta
 */
@WebServlet(urlPatterns={"/staff/publisher/search_publisher", "/staff/book/search_publisher"})
public class SearchPublisherAjax extends StaffBase 
{
	private static final long serialVersionUID = 1L;
	private GestoreEditori ge;
	
	public void init(ServletConfig config) throws ServletException 
	{
		ge = new GestoreEditori();
	}

	@Override
	protected void operationGet(HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			// Quello che la servlet deve stampare
			PrintWriter out = response.getWriter();			
			// Contiene i caratteri che l'utente sta digitando
			String term = request.getParameter("term");
			// Contiene la lista degli elementi prelevati
			List<String> lista = ge.GetInfoAutocomplete(term);			
			// Array in formato ["stringa", "stringa", "stringa"] che conterrà  il risultato da inserire
			JSONArray arrayObj=new JSONArray();
	        // Inserisco gli utenti prelevati nell'array da restituire
			for(String elem : lista)
			{
				arrayObj.put(elem);
			}
	        // Restituisco l'array
			out.println(arrayObj);
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void operationPost(HttpServletRequest request, HttpServletResponse response){}

}