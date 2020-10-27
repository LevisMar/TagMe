package controller.staff.publisher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import controller.staff.StaffBase;
import model.businessLogic.GestoreEditori;
import utility.GestoreFile;
import model.pojo.Publisher;
import utility.StatoRegistrazione;
import utility.StatoRegistrazione.Stato;

/**
 * Servlet che gestisce le richieste di visualizzazione
 * e modifica del profilo di un determinato editore presente
 * all'interno del DB
 */
@WebServlet("/staff/publisher/publisher_profile")
@MultipartConfig
public class PublisherProfile extends StaffBase 
{
	private static final long serialVersionUID = 1L;
	private GestoreEditori gE;
	private Publisher p;
	private List<Locale> countries;
	
	public void init(ServletConfig config) throws ServletException 
	{
		gE = new GestoreEditori();
		p = new Publisher();		
		// Prendo la lista dei codici ISO dei paesi
		String[] countryCodes = Locale.getISOCountries();
		// Conterrà la lista dei paesi
		countries = new ArrayList<Locale>();
		// Per ogni codice paese
		for (String countryCode : countryCodes) 
		{
			// Dal codice prendo il rispettivo paese
		    Locale obj = new Locale("", countryCode);
		    // Aggiungo il paese alla lista di tutti i paesi
		    countries.add(obj);
		}
	}

	@Override
	protected void operationGet(HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			// Se nell'url c'è un parametro che indica il nome dell'editore
			if(request.getParameter("name")!=null)
			{
				// Cerco l'editore nel DB
				p = gE.getPublisherFromName(request.getParameter("name"));
			}
			// Se l'editore è definito
			if(p!=null)
			{
				// Restituisco alla pagina le info dell'editore
				request.setAttribute("Editore", p);
			}
			else
			{
				// Imposto il messaggio di errore
				request.setAttribute("errormsg", "Editore non trovato");
			}
			// Restituisco alla pagina la lista dei paesi
			request.setAttribute("Countries", countries);
			this.callJSP("/zona_staff/publisher_profile.jsp", request, response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void operationPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		// Se non trovo l'editore da modificare
		if(p.getName() == null)
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Si è verificato un errore con l\\'editore che si vuole modificare");
		}
		else
		{
			// Se i dati ricevuti dal client sono validi
			if(checkInput(request))
			{
				// Conterrà l'eventuale nuovo nome che si vuole associare all'editore
				String name = request.getParameter("publ_name");
				// Conterrà l'eventuale nuovo paese che si vuole associare all'editore
				String country = request.getParameter("country_sel");
				// Prende l'input type file dalla pagina
				Part filePart = request.getPart("img_to_up");				
				// Se i dati dell'editore sono cambiati
				if(!p.getName().equals(name) || !p.getCountry().equals(country))
				{
					// Conterrà il messaggio da restituire al client
					String msg = "";
					// Creo l'istanza editore con i nuovi dati
					Publisher updated = new Publisher(name, country);
					// Conterrà l'esito dell'aggiornamento
					StatoRegistrazione esito = null;
					// Tento l'aggiornamento dei dati
					esito = gE.updatePublisher(p, updated);
					// In base all'esito dell'inserimento imposto il messaggio da ritornare al client
					msg = esito.message;
					// Se l'esito è negativo
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
			    	// Conterrà l'esito dell'upload dell'immagine
					boolean result = false;
					GestoreFile gF = new GestoreFile();
					// Tento l'aggiornamento dell'immagine di profilo
			    	result = gF.uploadFile("img/publishers/", "img_" + p.getId(), filePart);
			    	// Se l'upload riesce
				    if(result)
					{
				    	// Imposto il messaggio di successo
						request.setAttribute("okmsg", "Immagine caricata con successo, Se non viene visualizzata provare a ricaricare la pagina.");
					}
					else
					{
						// Imposto il messaggio di errore
						request.setAttribute("errormsg","Si è verificato un errore durante la creazione dell\\'immagine");
					}
			    }
			}		
		}
		try 
		{
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
		if(p.getName().equals(request.getParameter("publ_name")) && p.getCountry().equals(request.getParameter("country_sel")) && request.getPart("img_to_up").getSize() == 0)
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "I dati non sono cambiati");
			return false;
		}
		// Conterrà l'esito della verifica del paese
		boolean country_ok = false;
		// Se è stato specificato un paese
		if(!request.getParameter("country_sel").isEmpty())
		{
			// Lo confronto con ciascun paese già presente nella lista
			for(Locale country_el : countries)
			{
				// Prendo un paese dalla lista
				String country_comp = country_el.getISO3Country() + " - " + country_el.getDisplayCountry();
				// Se sono uguali allora è valido
				if(request.getParameter("country_sel").equals(country_comp))
				{
					country_ok = true;
				}
			}
		}
		// Se non l'ho trovato
		if(!country_ok)
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Non è stato specificato un paese, o il paese scelto non è presente nella lista dei paesi disponibili");
			return false;
		}		
		// Controllo se il nome dell'editore è specificato
		if(request.getParameter("publ_name").isEmpty())
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Il server non ha ricevuto alcuni dati necessari, compila almeno il campo nome");
			return false;
		}		
		// Gli input sono validi
		return true;
	}
}