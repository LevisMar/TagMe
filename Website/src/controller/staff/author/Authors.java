package controller.staff.author;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import controller.staff.StaffBase;
import model.businessLogic.GestoreAutori;
import model.pojo.Author;
import utility.GestoreFile;
import utility.StatoRegistrazione;
import utility.StatoRegistrazione.Stato;

/**
 * Servlet che gestisce le richieste di visualizzazione
 * degli autori presenti all'interno del DB, ed eventuale
 * inserimento di un nuovo autore
 */
@WebServlet("/staff/author/authors")
@MultipartConfig
public class Authors extends StaffBase 
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
			// Prelevo la lista degli autori presenti nel DB
			List<Author> autori = new ArrayList<Author>();
			autori = ga.getAllAuthor();
			// Restituisco alla pagina le info degli autori
			request.setAttribute("Autori", autori);
			this.callJSP("/zona_staff/authors.jsp", request, response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void operationPost(HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			// Conterrà il messaggio da restituire al client
			String msg = "";
			// Conterrà il nome dell'autore che si vuole aggiungere
			String name = request.getParameter("name");
			// Conterrà la biografia se specificata
			String biography = request.getParameter("biography");
			// System.out.println("Dati ricevuti: " + name + ", " + biography);
			// Almeno il nome deve essere specificato
			if(name.isEmpty())
			{
				// System.out.println("Il server non ha ricevuto alcuni dati necessari");
	        	msg = "Il server non ha ricevuto alcuni dati necessari, compila almeno il campo nome";
	        	request.setAttribute("errormsg",msg);
			}
			else
			{
				// Creo la nuova istanza dell'autore
				Author a = new Author(name, biography);
				// Conterrà l'esito della registrazione del nuovo autore
				StatoRegistrazione esito = null;
				// Tento l'inserimento dell'autore nel database
				esito = ga.insertAuthor(a);
				// In base all'esito dell'inserimento imposto il messaggio da ritornare al client
				msg = esito.message;
				// Se l'esito è negativo
				if(!esito.code.equals(Stato.ok))
				{
					request.setAttribute("errormsg",msg);
				}
				else
				{
					GestoreFile gF = new GestoreFile();
					// Prende l'input file inviato dal client, se presente
					Part filePart = request.getPart("img_to_up");
					// Conterrà l'esito dell'upload dell'immagine
				    boolean result;
				    // Se c'è un file da caricare, ne eseguo l'upload
				    if(filePart.getSize() > 0)
				    {
				    	result = gF.uploadFile("img/authors/", "img_" + a.getId(), filePart);
				    }
				    // Altrimenti prendo l'immagine di default e ne eseguo l'upload
				    else
				    {
				    	result = gF.createDefaultFile("img/authors/", "img_" + a.getId());
				    }
					if(result)
					{
						// Imposto il messaggio di successo
						request.setAttribute("okmsg",msg);
					}
					else
					{
						// Imposto il messaggio di errore
						request.setAttribute("errormsg","Autore registrato con successo, ma si è verificato un errore durante la creazione dell\\'immagine");
					}
				}
			}
			this.refreshJSP(request, response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}