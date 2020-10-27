package controller.staff.author;

import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import controller.staff.StaffBase;
import model.businessLogic.GestoreAutori;

/**
 * Servlet che gestisce le richieste Ajax
 * ricevute per la ricerca di un autore nel DB,
 * il cui nome comprende i caratteri inviati nella richiesta
 */
@WebServlet(urlPatterns={"/staff/author/search_author", "/staff/book/search_author"})
public class SearchAuthorAjax extends StaffBase 
{
	private static final long serialVersionUID = 1L;       
	private GestoreAutori ga;
	
	public void init(ServletConfig config) throws ServletException 
	{
		ga = new GestoreAutori();
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
			// System.out.println("Dati: " + term);
			// Contiene la lista degli elementi prelevati (nomi autori)
			List<String> lista = ga.GetInfoAutocomplete(term);			
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