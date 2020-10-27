package controller.staff.book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import controller.staff.StaffBase;
import model.businessLogic.GestoreLibreria;
import model.pojo.Book;
import model.pojo.BookCategory;

/**
 * Servlet che gestisce le richieste di visualizzazione
 * dei libri presenti all'interno del database
 */
@WebServlet("/staff/book/books")
public class Books extends StaffBase 
{
	private static final long serialVersionUID = 1L;
	private GestoreLibreria gL;
	private List<Book> libri;
	
	public void init(ServletConfig config) throws ServletException 
	{
		gL = new GestoreLibreria();
		libri = new ArrayList<Book>();
	}

	@Override
	protected void operationGet(HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			// Se la lista dei libri da mostrare non è stata definita
			if(libri.isEmpty())
			{
				// Recupero la lista di tutti i libri
				libri = gL.getAllBooks();
			}
			// Conterrà la lista di tutti i generi di libro
			List<BookCategory> book_genre = new ArrayList<BookCategory>();
			// Recupero anche la lista di tutti i generi di libro
			book_genre = gL.getAllCategory();
			// Restituisco alla pagina le info degli autori
			request.setAttribute("Libri", libri);
			// E dei libri
			request.setAttribute("BookGenre", book_genre);			
			this.callJSP("/zona_staff/books.jsp", request, response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void operationPost(HttpServletRequest request, HttpServletResponse response) 
	{
		// Svuoto la lista dei libri
		libri.clear();		
		// Conterrà la modalità di ricerca che il client effettua
		String search_by = request.getParameter("seltype");
		// Se ricerca tramite barcode
		if(search_by.equals("Barcode"))
		{
			// Conterrà il barcode preso dalla barra di ricerca
			String barcode = request.getParameter("parameter");
			// Dato il barcode cerco il rispettivo libro
			Book b = gL.getBookFromBarcode(barcode);
			// Se il libro non viene trovato
			if(b == null)
			{
				request.setAttribute("errormsg", "Nessun libro trovato con il barcode: " + barcode);
			}
			else
			{
				libri.add(b);
				request.setAttribute("okmsg", "Libro cercato tramite barcode");
			}
		}
		// Se ricerca tramite titolo del libro
		else if(search_by.equals("Title"))
		{
			// Conterrà il titolo preso dalla barra di ricerca
			String title = request.getParameter("parameter");
			// Conterrà l'eventuale lista dei generi per la ricerca avanzata
			List<String> genres_list = new ArrayList<String>();
			// Se la lista non è vuota
			if(request.getParameterValues("genres")!=null)
			{
				// Metto tutti i generi in un array di stringhe
				String[] genres = request.getParameterValues("genres");
				genres_list = Arrays.asList(genres);
			}
			// Conterrà l'autore preso della barra di ricerca
			String author = request.getParameter("author");
			// Conterrà l'editore preso della barra di ricerca
			String publisher = request.getParameter("publisher");
			// Conterrò l'edizione da ricercare
			int edition = 0;
			// Se l'edizione è stata specificata
			if(!request.getParameter("edition").isEmpty())
			{
				// La converto in formato intero
				edition = Integer.parseInt(request.getParameter("edition"));
			}
			// Se non è stato specificato nessun parametro di ricerca
			if(title.isEmpty() && genres_list.isEmpty() && author.isEmpty() && publisher.isEmpty() && edition==0)
			{
				// Prendo tutti i libri
				libri = gL.getAllBooks();
				request.setAttribute("okmsg", "Nessun parametro di ricerca");
			}
			else
			{
				// Prendo i libri che rispettano i requisiti
				libri = gL.getSpecificBook(title, genres_list, author, publisher, edition);
				// Se non trova nessun libro
				if(libri.isEmpty())
				{
					request.setAttribute("errormsg", "Nessun libro trovato con i parametri dati");
				}
				else
				{
					request.setAttribute("okmsg", "Libri cercati tramite ricerca avanzata");
				}
			}
		}
		// Se viene usato un metodo di ricerca non valido
		else
		{
			request.setAttribute("errormsg", "Metodo di ricerca usato non è valido");
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
}