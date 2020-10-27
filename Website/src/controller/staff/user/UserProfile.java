package controller.staff.user;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import controller.staff.StaffBase;
import model.businessLogic.GestoreUtenti;
import model.pojo.BookUser;
import utility.GestoreFile;
import utility.StatoRegistrazione;
import utility.StatoRegistrazione.Stato;

/**
 * Servlet che gestisce le richieste di visualizzazione
 * e modifica del profilo di un determinato utente presente
 * all'interno del DB
 */
@WebServlet("/staff/user/user_profile")
@MultipartConfig
public class UserProfile extends StaffBase 
{
	private static final long serialVersionUID = 1L;
	private BookUser current_bu;
	private GestoreUtenti gu;
	
	public void init(ServletConfig config) throws ServletException 
	{
		current_bu = new BookUser();
		gu = new GestoreUtenti();
	}
	
	@Override
	protected void operationGet(HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			// Recupero la sessione in cui è registrato l'utente di cui voglio vedere il profilo
			HttpSession session = request.getSession(false);
			// Se la sessione è valida
			if(session != null)
			{
				// Recupero l'utente che mi interessa
				current_bu = (BookUser) session.getAttribute("current_user");
			}
			// Se ho trovato l'utente che mi interessa
			if(current_bu != null)
			{
				// Restituisco alla pagina le info dell'utente
				request.setAttribute("Utente", current_bu);
			}
			else
			{
				// Imposto il messaggio di errore
				request.setAttribute("errormsg", "Utente non trovato");
			}			
			this.callJSP("/zona_staff/user_profile.jsp", request, response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void operationPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		// Se non trovo l'utente da modificare
		if(current_bu == null)
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Si è verificato un errore con l\\'utente che si vuole modificare");
		}
		else
		{
			// Controllo se almeno un dato è cambiato (compreso il file)
			if(isInputChanged(request,true))
			{
				// Controllo se almeno un dato dell'utente è cambiato (file non compreso)
				if(isInputChanged(request,false))
				{
					// Controllo i dati ricevuti
					BookUser updated = checkUserInput(request);
					// Se i dati ricevuti sono validi
					if(updated != null)
					{
						// Conterrà il messaggio da restituire al client
						String msg = "";
						// Conterrà l'esito dell'aggiornamento
						StatoRegistrazione esito = null;
						// Tento l'aggiornamento dei dati dell'utente
						esito = gu.updateUser(current_bu, updated);
						// In base all'esito dell'inserimento imposto il messaggio da ritornare al client
						msg = esito.message;
						// Se l'aggiornamento non riesce
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
				}
				// Se viene caricata un immagine di profilo
			    if(request.getPart("img_to_up").getSize() > 0)
			    {
			    	// Conterrà l'esito dell'upload
					boolean result = false;
					GestoreFile gF = new GestoreFile();
					// Tento l'aggiornamento dell'immagine di profilo
			    	result = gF.uploadFile("img/users/", "img_" + current_bu.getId(), request.getPart("img_to_up"));
			    	// Se l'upload riesce
				    if(result)
					{
						// imposto il messaggio di successo
						request.setAttribute("okmsg", "Immagine caricata con successo, Se non viene visualizzata provare a ricaricare la pagina.");
					}
					else
					{
						// imposto il messaggio di errore
						request.setAttribute("errormsg","Si è verificato un errore durante la creazione dell\\'immagine");
					}
			    }
			}
			else
			{
				// Imposto il messaggio di errore
				request.setAttribute("errormsg", "I dati non sono cambiati");
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
	 * Verifica se gli input ricevuti dal client, riguardo i dati
	 * dell'utente, sono validi
	 * @param request che contiene gli input inviati
	 * @return l'istanza BookUser se i dati sono validi, null altrimenti
	 * @throws IOException
	 * @throws ServletException
	 */
	protected BookUser checkUserInput(HttpServletRequest request) throws IOException, ServletException
	{
		// Raccolta dei dati ricevuti dal client
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String email = request.getParameter("email");
		String phonenumber = request.getParameter("phonenumber");
		
		// Se manca qualche parametro
		if(firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || phonenumber.isEmpty())
		{
		   	request.setAttribute("errormsg", "Il server non ha ricevuto alcuni dati necessari, compila tutti i campi");
		   	return null;
		}
		// Controllo se la mail è accettabile
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);
        // Se non è accettabile
        if(!mat.matches())
        {
        	request.setAttribute("errormsg","Indirizzo email non valido");
			return null;
        }
        // Controllo se il numero di telefono è valido
        pattern = Pattern.compile("[0-9]{3}-[0-9]{7}");
        mat = pattern.matcher(phonenumber);
        // Se non è valido
        if(!mat.matches())
        {
        	request.setAttribute("errormsg","Numero di telefono non valido, controllare il formato");
			return null;
        }        
        // È tutto ok con i parametri ricevuti
        BookUser bu = new BookUser(firstname, lastname, email, phonenumber);
		return bu;
	}
	
	/**
	 * Verifica se gli input sono cambiati, rispetto a quelli già presenti
	 * 
	 * @param request che contiene gli input inviati
	 * @param checkFile indica se controllare anche la presenza di un file da aggiungere
	 * @return true se almeno un parametro è cambiato, false altrimenti
	 * @throws ServletException 
	 * @throws IOException 
	 */
	protected boolean isInputChanged(HttpServletRequest request, boolean checkFile) throws IOException, ServletException
	{
		if(!request.getParameter("firstname").equals(current_bu.getFirstname()))
		{
			return true;
		}
		if(!request.getParameter("lastname").equals(current_bu.getLastname()))
		{
			return true;
		}
		if(!request.getParameter("email").equals(current_bu.getEmail()))
		{
			return true;
		}
		if(!request.getParameter("phonenumber").equals(current_bu.getPhonenumber()))
		{
			return true;
		}
		if(checkFile && request.getPart("img_to_up").getSize() > 0)
		{
			return true;
		}		
		return false;
	}
}