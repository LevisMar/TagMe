package controller.staff;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.businessLogic.GestoreAutori;
import model.businessLogic.GestoreEditori;
import model.businessLogic.GestoreLibreria;
import model.businessLogic.GestoreUtenti;

/**
 * Servlet che risponde alle richieste di 
 * reindirizzamento alla home
 */
@WebServlet("/staff/home_staff")
public class HomeStaff extends StaffBase 
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void operationGet(HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			// Conterrà alcuni dati da mostrare nella homepage
			List<Integer> dati = new ArrayList<Integer>();
			// Prendo il numero di utenti registrati nel sistema
			dati.add(new GestoreUtenti().getUsersSize());
			// Prendo il numero di autori registrati nel sistema
			dati.add(new GestoreAutori().getAuthorsSize());
			// Prendo il numero di editori registrati nel sistema
			dati.add(new GestoreEditori().getPublishersSize());
			// Prendo il numero di libri registrati nel sistema
			dati.add(new GestoreLibreria().getBooksSize());
			// Restituisco alla pagina le info raccolte
			request.setAttribute("Dati", dati);
			this.callJSP("/zona_staff/home_staff.jsp", request, response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void operationPost(HttpServletRequest request, HttpServletResponse response){}
    
}