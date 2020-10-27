package controller.staff;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import controller.ServletBase;
import model.businessLogic.GestoreStaff;

/**
 * Superclasse di servlet che gestiscono richieste in cui
 * l'utente deve essere già autenticato.
 */
public abstract class StaffBase extends ServletBase 
{
	private GestoreStaff gS = new GestoreStaff();
	private static final long serialVersionUID = 1L;

	@Override
	 public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	 {
		// Se l'utente ha i requisiti necessari
		if(checkAccess(request))
			operationGet(request,response);
		// Altrimenti lo reindirizzo al login
		else 		
			response.sendRedirect("/gestionelibri/welcome");
	 }

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// Se l'utente ha i requisiti necessari
		if(checkAccess(request))
			operationPost(request,response);
		// Altrimenti lo reindirizzo al login
		else
			response.sendRedirect("/gestionelibri/welcome");
	}

	/**
	 * Gestisce una richiesta HTTP GET autorizzata,
	 * forwordando/reindirizzando al termine se necessario.
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	protected abstract void operationGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
	
	/**
	 * Gestisce una richiesta HTTP POST autorizzata,
	 * forwordando/reindirizzando al termine se necessario.
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	protected abstract void operationPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

	@Override
	protected boolean checkAccess(HttpServletRequest request) 
	{
		// Prendo l'username di chi è loggato
		String username = getStaffFromSession(request);
		// Se è valido
		if(username != null && gS.getStaffFromUsername(username)!=null)
			return true;
		else
			return false;
	}
	
}