package controller;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.businessLogic.GestoreStaff;
import model.businessLogic.GestoreStaff.StatoLogin;

/**
 * Servlet che gestisce le richieste di login
 * da parte dei membri dello staff.
 */
@WebServlet("/login")
public class Login extends ServletBase
{
	private static final long serialVersionUID = 1L;
	private GestoreStaff gS;
	
	public void init(ServletConfig config) throws ServletException 
	{
		gS = new GestoreStaff();
	}

	@Override
	protected boolean checkAccess(HttpServletRequest request) 
	{
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	{
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	{
		// Se c'è già un membro dello staff loggato
		if(getStaffFromSession(request)!=null)
		{
			try 
			{
				// Reindirizzo alla home dello staff
				response.sendRedirect("/gestionelibri/staff/home_staff");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			// Prendo l'username inserito
			String username = request.getParameter("username");
			// Prendo la password digitata
			String password = request.getParameter("password");
			// Conterrà il risultato dell'operazione di login
			StatoLogin stato = StatoLogin.error;
			// Tento il login utilizzando i dati forniti
			stato = gS.login(username, password);
			// Conterrà l'eventuale messaggio di errore
			String errormsg = null;
			// In base al risultato del login
			switch(stato)
			{
				case ok:
					// Apro una sessione
					HttpSession session = request.getSession();
					// Imposto la sua durata
					session.setMaxInactiveInterval(7*24*60*60);
					// Creo l'attributo nome utente di sessione
					session.setAttribute("staffMemb",username);
					// Creo il cookie per la sessione persistente
					Cookie c = new Cookie("JSESSIONID",session.getId());
					// Tempo scadenza cookie pari
					c.setMaxAge((7*24*60*60));  
					response.addCookie(c);
					break;
				case nouser:
					errormsg = "Username inesistente";
					break;
				case nopsw:
					errormsg = "Password non valida";
					break;
				case error:
					errormsg = "Si è verificatoo un errore";
					break;
			}
			// Se ci sono stati problemi
			if(stato != StatoLogin.ok)
			{
				// Imposto il messaggio di errore
				request.setAttribute("errormsg",errormsg);
				try 
				{
					this.callJSP("/welcome.jsp", request, response);
				} 
				catch (ServletException | IOException e) 
				{
					e.printStackTrace();
				}
			} 
			else
			{
				try 
				{
					// Reindirizzo alla home dello staff
					response.sendRedirect("/gestionelibri/staff/home_staff");
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}				
			}
		}
	}
}