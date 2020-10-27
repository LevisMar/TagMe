package controller.staff.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import controller.staff.StaffBase;
import model.businessLogic.GestoreUtenti;
import model.pojo.BookUser;
import utility.GestoreFile;
import utility.StatoRegistrazione;
import utility.StatoRegistrazione.Stato;

/**
 * Servlet che gestisce le richieste di visualizzazione
 * degli utenti presenti all'interno del DB, ed eventuale
 * inserimento di un nuovo utente
 */
@WebServlet("/staff/user/users")
@MultipartConfig
public class Users extends StaffBase 
{
	private static final long serialVersionUID = 1L;
	private GestoreUtenti gU;
	
	public void init(ServletConfig config) throws ServletException 
	{
		gU = new GestoreUtenti();
	}

	@Override
	protected void operationGet(HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			List<BookUser> users = new ArrayList<BookUser>();
			// Prelevo la lista degli utenti
			users = gU.getAllUser();
			// Restituisco alla pagina le info degli utenti
			request.setAttribute("Utenti", users);
			this.callJSP("/zona_staff/users.jsp", request, response);
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
			// Controllo i dati ricevuti
			BookUser bu = checkUserInput(request);
			// Se i dati ricevuti sono validi
			if(bu != null)
			{
				// Conterrà l'esito dell'aggiornamento
				StatoRegistrazione esito = null;
				// Tento l'inserimento dei dati dell'utente
				esito = gU.insertUser(bu);
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
					// Verifico se il client ha inserito un immagine tramite input file
					Part filePart = request.getPart("img_to_up");
					// Conterrà l'esito dell'upload
				    boolean result;
				    // Se c'è un file da caricare, ne eseguo l'upload
				    if(filePart.getSize() > 0)
				    {
				    	result = gF.uploadFile("img/users/", "img_" + bu.getId(), filePart);
				    }
				    // Altrimenti prendo un'immagine di default e ne eseguo l'upload
				    else
				    {
				    	result = gF.createDefaultFile("img/users/", "img_" + bu.getId());
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
						request.setAttribute("errormsg","Utente registrato con successo, ma si è verificato un errore durante la creazione dell\\'immagine");
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
        // Se la mail non è accettabile
        if(!mat.matches())
        {
        	request.setAttribute("errormsg","Indirizzo email non valido");
			return null;
        }
        // Controllo se il numero di telefono è valido
        pattern = Pattern.compile("[0-9]{3}-[0-9]{7}");
        mat = pattern.matcher(phonenumber);
        // Se il numero di telefono non è valido
        if(!mat.matches())
        {
        	request.setAttribute("errormsg","Numero di telefono non valido, controllare il formato");
			return null;
        }        
        // È tutto ok con i parametri ricevuti
        BookUser bu = new BookUser(firstname, lastname, email, phonenumber);
		return bu;
	}
}