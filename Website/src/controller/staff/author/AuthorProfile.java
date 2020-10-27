package controller.staff.author;

import java.io.IOException;
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
 * e modifica del profilo di un determinato autore presente
 * all'interno del database
 */
@WebServlet("/staff/author/author_profile")
@MultipartConfig
public class AuthorProfile extends StaffBase 
{
	private static final long serialVersionUID = 1L;
	private GestoreAutori gA;
	private Author a;
	
	public void init(ServletConfig config) throws ServletException 
	{
		gA = new GestoreAutori();
		a = new Author();
	}

	@Override
	protected void operationGet(HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			// Se nell'url c'è un parametro name
			if(request.getParameter("name")!=null)
			{
				// Cerco l'autore all'interno del DB
				a = gA.getAuthorFromName(request.getParameter("name"));
			}
			// Se è stata trovata un'entità Autore
			if(a!=null)
			{
				// Restituisco alla pagina le info dell'autore
				request.setAttribute("Autore", a);
			}
			else
			{
				// Imposto il messaggio di errore
				request.setAttribute("errormsg", "Autore non trovato");
			}
			// Forwardo alla pagina per mostrare le info
			this.callJSP("/zona_staff/author_profile.jsp", request, response);	
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void operationPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		// Se non trovo l'autore da modificare
		if(a.getName() == null)
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Si è verificato un errore con autore che si vuole modificare");
		}
		else
		{
			// Se i dati ricevuti dal client sono validi
			if(checkInput(request))
			{
				// Conterrà l'eventuale nuovo nome che si vuole associare all'autore
				String name = request.getParameter("name");
				// Conterrà l'eventuale nuova biografia che si vuole associare all'autore
				String biography = request.getParameter("biography");
				// Prende l'input type file dalla pagina
				Part filePart = request.getPart("img_to_up");				
				// Se i dati dell'autore sono cambiati
				if(!a.getName().equals(name) || !a.getBiography().equals(biography))
				{
					// Conterrà il messaggio da restituire al client
					String msg = "";
					// Creo l'istanza Autore con i nuovi dati
					Author updated = new Author(name, biography);
					// Conterrà l'esito dell'aggiornamento
					StatoRegistrazione esito = null;
					// Tento l'aggiornamento dei dati
					esito = gA.updateAuthor(a, updated);
					// In base all'esito dell'inserimento imposto il messaggio da ritornare al client
					msg = esito.message;
					// Se ha un esito negativo
					if(!esito.code.equals(Stato.ok))
					{
						// Imposto il messaggio di errore
						request.setAttribute("errormsg",msg);
					}
					else
					{
						// Imposto il messaggio di successo
						request.setAttribute("okmsg",msg);
					}
				}				
				// Se viene caricata un immagine di profilo
			    if(filePart.getSize() > 0)
			    {
			    	// Contiene il risultato dell'upload
					boolean result = false;
					GestoreFile gF = new GestoreFile();
					// Tenta l'aggiornamento dell'immagine di profilo
			    	result = gF.uploadFile("img/authors/", "img_" + a.getId(), filePart);
			    	// Se l'esito è positivo
				    if(result)
					{
						request.setAttribute("okmsg", "Immagine caricata con successo, Se non viene visualizzata provare a ricaricare la pagina.");
					}
					else
					{
						request.setAttribute("errormsg","Si è verificato un errore durante la creazione dell\\'immagine");
					}
			    }
			}
		}
		try 
		{
			// Ricarica la pagina
			this.refreshJSP(request, response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Controlla gli input inviati dal client in una richiesta POST
	 * 
	 * @param request che contiene gli input inviati
	 * @return true se gli input sono validi, false altrimenti
	 * @throws IOException
	 * @throws ServletException
	 */
	protected boolean checkInput(HttpServletRequest request) throws IOException, ServletException
	{
		// Evito di proseguire se i dati sono uguali
		if(a.getName().equals(request.getParameter("name")) && a.getBiography().equals(request.getParameter("biography")) && request.getPart("img_to_up").getSize() == 0)
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "I dati non sono cambiati");
			return false;
		}		
		// Controllo se il nome dell'autore è specificato
		if(request.getParameter("name").isEmpty())
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Il server non ha ricevuto alcuni dati necessari, compila almeno il campo nome");
			return false;
		}		
		return true;
	}
}