package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe astratta che implementa e definisce alcuni metodi di base utili
 * e essenziali per una corretta gestione della servlet.
 * Costituisce una superclasse di tutte le servlet implementate.
 * NOTA:
 * In realtà i metodi doGet() e doPost() vengono ripetuti,
 * in quanto sono già definiti nella classe astratta <code>HttpServlet </code>
 * che si eredita.
 */
public abstract class ServletBase extends HttpServlet
{
	private static final long serialVersionUID = 1L;
		
	/**
	 * Il metodo doGet gestite richieste HTTP GET.
	 * 
	 * All'interno di questa architettura, verranno effettuate richieste di questo tipo
	 * solamente per recuperare informazioni dal server, senza generare alcun effetto collaterale.
	 * 
	 * Possibili scenari sono i seguenti:
	 * <ul>
	 * 	<li>L'utente clicca un link per visualizzare un contenuto tramite collegamento ipertestuale.</li>
	 * 	<li>L'utente digita manualmente un url per visualizzare un contenuto</li>
	 * </ul>
	 */
	protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	/**
	 * Il metodo doPost gestisce richieste HTTP POST.
	 * 
	 * All'interno di questa architettura, verranno effettuate richieste di questo tipo
	 * per eseguire operazioni, da eseguire in maniera atomica tramite transazioni,
	 * che modificano lo stato persistente del DB,
	 * oppure per gestire casi particolari in cui si desidera che i parametri
	 * non compaiano nell'url, come nel caso di una richiesta di login.
	 * 	 
	 * Possibili scenari sono i seguenti:
	 * <ul>
	 * 	<li>L'utente compila i dati di un form e li invia alla servlet per eseguire un'elaborazione.</li>
	 * </ul>
	 */
	protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	/**
	 * Verifica se un utente è autorizzato ad emettere una richiesta.
	 * 
	 * @param request
	 * @return <code> true </code> l'utente è autorizzato ad effettuare la richiesta alla servlet.
	 * 		   <code> false </code> l'utente non è autorizzato.
	 */
	protected abstract boolean checkAccess(HttpServletRequest request);
		
	/**
	 * Forworda ad una pagina JSP.
	 * 
	 * @param indirizzo della pagina a cui forwordare
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected final void callJSP(String indirizzo, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher rd = request.getRequestDispatcher(indirizzo);
	    rd.forward(request, response);
	}
		
	/**
	 * Il controllo rimane alla servlet,
	 * viene semplicemente incluso nella response
	 * l'output della pagina jsp invocata.
	 * 
	 * @param indirizzo della pagina jsp da includere
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected final void includeJSP(String indirizzo, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher rd = request.getRequestDispatcher(indirizzo);
	    rd.include(request, response);
	}
		
	/**
	 * Invoca implicitamente il metodo doGet
	 * per ricaricare i dati. Questo metodo può essere richiamato,
	 * al termine di un'elaborazione doPost.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected final void refreshJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request,response);
	}
	
	/**
	 * Se un membro dello staff risulta loggato,
	 * restituisce il suo username.
	 * 
	 * @param request
	 * @return l'username del membro dello staff loggato
	 */
	protected final String getStaffFromSession(HttpServletRequest request)
	{
		String staffMemb = null;
		// Con il parametro false si limita a restituire null, in caso di sessione non attiva
		HttpSession session = request.getSession(false);
		// Se la sessione è attivo
		if(session != null)
		{
			// Prendo l'username salvato nella sessione
			staffMemb = (String) session.getAttribute("staffMemb");
		}
		return staffMemb;
	}
}