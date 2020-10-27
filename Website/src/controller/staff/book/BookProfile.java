package controller.staff.book;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import controller.staff.StaffBase;
import model.businessLogic.GestoreAutori;
import model.businessLogic.GestoreEditori;
import model.businessLogic.GestoreLibreria;
import model.pojo.Author;
import model.pojo.Book;
import model.pojo.BookCategory;
import model.pojo.Publisher;
import utility.GestoreFile;

/**
 * Servlet che gestisce le richieste di visualizzazione
 * e modifica del profilo di un determinato libro presente
 * all'interno del DB
 */
@WebServlet("/staff/book/book_profile")
@MultipartConfig
public class BookProfile extends StaffBase 
{
	private static final long serialVersionUID = 1L;
	private List <BookCategory> categorie;
	private GestoreLibreria gL;
	private Book current_b;
	
	public void init(ServletConfig config) throws ServletException 
	{
		gL = new GestoreLibreria();
		current_b = new Book();
		categorie = gL.getAllCategory();
	}

	@Override
	protected void operationGet(HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			// Se nell'url c'è un parametro che indica il barcode del libro
			if(request.getParameter("n_book")!=null)
			{
				// Recupero il libro dal barcode
				current_b = gL.getBookFromBarcode(request.getParameter("n_book"));
			}
			// Se è definita un'istanza libro
			if(current_b!=null)
			{
				// Restituisco alla pagina le info del libro
				request.setAttribute("Libro", current_b);
			}
			else
			{
				// Imposto il messaggio di errore
				request.setAttribute("errormsg", "Libro non trovato");
			}
			// Resituisco anche la lista dei generi di libri, in caso di modifica
			request.setAttribute("Categories", categorie);			
			this.callJSP("/zona_staff/book_profile.jsp", request, response);	
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void operationPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		// Se non trovo il libro da modificare
		if(current_b.getName() == null)
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Si è verificato un errore con il libro che si vuole modificare");
		}
		else
		{
			// Conterrà il libro con le informazioni aggiornate
			Book b_updated = checkInput(request);
			// Se il controllo degli input va bene, ho l'istanza con i dati ricevuti
			if(b_updated != null)
			{
				// Evito di proseguire se i dati sono rimasti gli stessi
				if(current_b.isEquals(b_updated) && request.getPart("img_to_up").getSize() == 0)
				{
					// Imposto il messaggio di errore
					request.setAttribute("errormsg", "I dati non sono cambiati");
				}
				else
				{
					// Se i dati del libro sono cambiati
					if(!current_b.isEquals(b_updated))
					{
						// Tento l'update dei suoi dati
						String result = gL.updateProduct(current_b, b_updated);
						// L'aggiornamento è riuscito
						if(result.equals("Aggiornamento effettuato"))
						{
							// Imposto il messaggio di successo
							request.setAttribute("okmsg", result);
						}
						else
						{
							// Imposto il messaggio di errore
							request.setAttribute("errormsg", result);
						}
					}					
					// Se viene caricata un immagine di profilo
				    if(request.getPart("img_to_up").getSize() > 0)
				    {
				    	// Conterrà l'esito dell'upload dell'immagine
						boolean result = false;
						GestoreFile gF = new GestoreFile();
						// Tento l'aggiornamento dell'immagine di profilo
				    	result = gF.uploadFile("img/books/cover/", "img_" + current_b.getBarcode(), request.getPart("img_to_up"));
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
	 * Controlla gli input ricevuti da un client,
	 * verificando che essi siano validi per aggiornare
	 * i dati di un libro
	 * 
	 * @param request che contiene gli input inviati
	 * @return l'istanza di un libro se i dati sono validi,
	 * 		   null altrimenti
	 */
	protected Book checkInput(HttpServletRequest request)
	{					
		// Raccolta dei dati ricevuti dal client
		String bookname = request.getParameter("bookname");
		String description = request.getParameter("description");
		String category = request.getParameter("category_sel");
		String publisher = request.getParameter("publisher");
		String barcode = request.getParameter("barcode");
		String n_auth = request.getParameter("n_auth");
		
		// Questi due parametri devono essere presenti
		if(bookname.isEmpty() || barcode.isEmpty())
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Mancano alcuni dati necessari, come il nome del libro o il barcode");
			return null;
		}
		// La categoria deve essere tra quelle disponibili
		if(category.isEmpty() || !categorie.contains(new BookCategory(category)))
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "La categoria specificata non può essere associata");
			return null;
		}
		int edition = 0, quantity = 0;
		// Tento di convertire la quantità e l'edizione da stringa a intero
		try
		{
			edition = Integer.parseInt(request.getParameter("edition"));
			quantity = Integer.parseInt(request.getParameter("quantity"));
		}
		catch (NumberFormatException e)
		{
		    edition = 0;
		    quantity = 0;
		}
		// L'edizione o la quantità non possono essere negative
		if(edition < 1 || quantity < 0)
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "I valori inseriti per edizione o per quantità non possono essere accettati");
			return null;
		}
		// Il barcode deve essere di 13 caratteri
		if(barcode.length() < 13 || barcode.length() > 13)
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Il barcode deve avere una lunghezza pari a 13");
			return null;
		}
		Publisher p = new Publisher();
		GestoreEditori gE = new GestoreEditori();
		// Cerco l'editore nel DB
		p = gE.getPublisherFromName(publisher);
		// Se non lo trovo
		if(p == null)
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Editore non trovato nel DB");
			return null;
		}

		// Conterrà la lista degli autori presi dalla pagina
		Set<Author> authors = new HashSet<Author>();
		GestoreAutori gA = new GestoreAutori();
		// Se non c'è un numero di autori
		if(n_auth.isEmpty())
		{
			// Vuol dire che l'autore è uno solo, e se il campo non è vuoto
			if(!request.getParameter("author_1").isEmpty())
			{
				// Cerco l'autore nel DB
				Author a = gA.getAuthorFromName(request.getParameter("author_1"));
				// Se lo trovo lo aggiungo alla lista
				if(a != null)
				{
					authors.add(a);
				}
				else
				{
					// Imposto il messaggio di errore
					request.setAttribute("errormsg", "Editore " + request.getParameter("author_1") + " non trovato");
					return null;
				}
			}
		}
		// Se ci sono più autori
		else
		{
			// Prendo il numero di autori
			int n = Integer.parseInt(n_auth);
			for(int i = 1; i <= n; i++)
			{
				// Prendo un autore dall'input ricevuto
				String author = request.getParameter("author_" + i);
				// Se l'autore è specificato e non è già presente nella lista che si sta creando
				if(!author.isEmpty() && !authors.contains(new Author(author)))
				{
					// Cerco l'autore nel DB
					Author a = gA.getAuthorFromName(author);
					// Se lo trovo lo aggiungo alla lista
					if(a != null)
					{
						authors.add(a);
					}
					else
					{
						// Imposto il messaggio di errore
						request.setAttribute("errormsg", "Autore " + author + " non trovato");
						return null;
					}
				}
			}
		}
		// Se non è stato aggiunto nessun autore alla lista
		if(authors.isEmpty())
		{
			// Imposto il messaggio di errore
			request.setAttribute("errormsg", "Non hai specificato nessun autore");
			return null;
		}
		
		// è tutto ok con i parametri ricevuti
		Book b = new Book();
		b.setName(bookname);
		b.setDescription(description);
		b.setCategory(new BookCategory(category));
		b.setEdition(edition);
		b.setQuantity(quantity);
		b.setPublisher(p);
		b.setBarcode(barcode);
		b.setAuthors(authors);
		
		return b;
	}
}