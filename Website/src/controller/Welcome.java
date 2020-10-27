package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet che risponde alle richieste
 * per/da la pagina di benvenuto
 */
@WebServlet("/welcome")
public class Welcome extends ServletBase 
{
	private static final long serialVersionUID = 1L;

    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
    	// Se c'è già un membro dello staff loggato precedentemente
    	if(getStaffFromSession(request)!=null)
    	{
    		// Reindirizzo alla home dello staff
			response.sendRedirect("/gestionelibri/staff/home_staff");
    	}
    	else
    	{
    		// Eseguo il forward verso la pagina di benvenuto
        	this.callJSP("/welcome.jsp", request, response);
    	}
	}

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	@Override
	protected boolean checkAccess(HttpServletRequest request) 
	{
		return true;
	}
}