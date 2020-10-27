package controller.staff.publisher;

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
import model.pojo.Publisher;
import utility.GestoreFile;
import utility.StatoRegistrazione;
import utility.StatoRegistrazione.Stato;

/**
 * Servlet che gestisce le richieste di visualizzazione
 * degli editori presenti all'interno del DB, ed eventuale
 * inserimento di un nuovo editore
 */
@WebServlet("/staff/publisher/publishers")
@MultipartConfig
public class Publishers extends StaffBase 
{
	private static final long serialVersionUID = 1L;
	private GestoreEditori ge;
	private List<Locale> countries;
	
	public void init(ServletConfig config) throws ServletException 
	{
		ge = new GestoreEditori();
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
			// Conterrà la lista degli editore da restituire
			List<Publisher> editori = new ArrayList<Publisher>();
			// Prelevo la lista degli editori
			editori = ge.getAllPublisher();
			// Restituisco alla pagina le info degli editori
			request.setAttribute("Editori", editori);
			// Restituisco alla pagina la lista dei paesi
			request.setAttribute("Countries", countries);			
			this.callJSP("/zona_staff/publishers.jsp", request, response);
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
			// Conterrà il nome dell'editore
			String name = request.getParameter("name");
			// Conterrà il paese da associare all'editore
			String country_r = request.getParameter("country");
			// Se mancano alcuni dati necessari
			if(name.isEmpty() || country_r.isEmpty())
			{
	        	msg = "Il server non ha ricevuto alcuni dati necessari, compila i campi";
	        	// Imposto il messaggio di errore
	        	request.setAttribute("errormsg",msg);
			}
			else
			{
				// Prendo la parte di codice ISO del paese
				String code = country_r.substring(0, 3);
				// Prendo la lista dei codici ISO dei paesi
				String[] countryCodes = Locale.getISOCountries();
				// Conterrà il paese se trovato
				String country = null;
				// Per ogni codice paese
				for (String countryCode : countryCodes) 
				{
					// Dal codice prendo il rispettivo paese
				    Locale obj = new Locale("", countryCode);
				    // Se è uguale a quello specificato dal client
				    if(code.equals(obj.getISO3Country()))
				    {
				    	// Genero la stringa completa da salvare nel DB
				    	country = obj.getISO3Country() + " - " + obj.getDisplayCountry();
				    }
				}
				// Se trovo il paese
				if(country!=null)
				{
					// Creo l'istanza di editore da aggiungere
					Publisher p = new Publisher(name, country);
					// Conterrà l'esito dell'inserimento dell'editore
					StatoRegistrazione esito = null;
					// Tento l'inserimento dei dati
					esito = ge.insertPublisher(p);
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
						GestoreFile gF = new GestoreFile();
						// Prende l'input file inviato dal client, se presente
						Part filePart = request.getPart("img_to_up");
						// Conterrà l'esito dell'upload dell'immagine
					    boolean result;
					    // Se c'è un file da caricare, ne eseguo l'upload
					    if(filePart.getSize() > 0)
					    {
					    	result = gF.uploadFile("img/publishers/", "img_" + p.getId(), filePart);
					    }
					    // Altrimenti prendo l'immagine di default e ne eseguo l'upload
					    else
					    {
					    	result = gF.createDefaultFile("img/publishers/", "img_" + p.getId());
					    }
					    // Se l'upload dell'immagine riesce
						if(result)
						{
							// Imposto il messaggio di successo
							request.setAttribute("okmsg",msg);
						}
						else
						{
							// Imposto il messaggio di errore
							request.setAttribute("errormsg","Editore registrato con successo, ma si è verificato un errore durante la creazione dell\\'immagine");
						}
					}
				}
				// Se il paese selezionato non viene trovato
				else
				{
		        	msg = "Il server non ha trovato il paese selezionato";
					// Imposto il messaggio di errore
		        	request.setAttribute("errormsg",msg);
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