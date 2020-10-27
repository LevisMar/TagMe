package controller.staff.book;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
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
 * Servlet che gestisce le richieste di
 * aggiungere un libro al database
 */
@WebServlet("/staff/book/add_book")
@MultipartConfig
public class AddBook extends StaffBase 
{
	private static final long serialVersionUID = 1L;
	private List <BookCategory> categorie;
	private GestoreLibreria gL;
	
	public void init(ServletConfig config) throws ServletException 
	{
		gL = new GestoreLibreria();
		categorie = gL.getAllCategory();
	}

	@Override
	protected void operationGet(HttpServletRequest request, HttpServletResponse response) 
	{
		try 
		{
			// Resituisco la lista dei generi di libri
			request.setAttribute("Categories", categorie);
			this.callJSP("/zona_staff/add_book.jsp", request, response);
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
			// Se gli input sono validi conterrà l'istanza libro da aggiungere nel db
			Book b = checkInput(request);
			if(b != null)
			{
				// Aggiungo il libro nel DB
				boolean result = gL.addProduct(b);
				// L'upload è riuscito
				if(result)
				{
					GestoreFile gF = new GestoreFile();
					// Ottengo l'input file se l'utente ha aggiunto un'immagine da associare al libro
					Part filePart = request.getPart("img_to_up");
					// Conterrà l'esito dell'upload
				    boolean upload;
				    // Se c'è un file da caricare, ne faccio l'upload
				    if(filePart.getSize() > 0)
				    {
				    	upload = gF.uploadFile("img/books/cover/", "img_" + b.getBarcode(), filePart);
				    }
				    // Altrimenti prendo l'immagine di default, e ne faccio l'upload
				    else
				    {
				    	upload = gF.createDefaultFile("img/books/cover/", "img_" + b.getBarcode());
				    }
				    // Se l'upload è riuscito
					if(upload)
					{
						// Imposto il messaggio di successo
						request.setAttribute("okmsg", "Inserimento effettuato con successo.");
					}
					else
					{
						// Imposto il messaggio di errore
						request.setAttribute("errormsg","Libro registrato con successo, ma si è verificato un errore durante la creazione dell\\'immagine");
					}
				}
				else
				{
					// Imposto il messaggio di errore
					request.setAttribute("errormsg", "Si è verificato un errore durante l\\'inserimento del libro nel DB.");
				}
			}

			// Il messaggio da restituire l'ho già impostato, faccio il refresh
			this.refreshJSP(request, response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Controlla gli input ricevuti da un client,
	 * verificando che essi siano validi per creare
	 * un'istanza libro
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
			request.setAttribute("errormsg", "Mancano alcuni dati necessari, come il nome del libro o il barcode");
			return null;
		}
		// La categoria deve essere tra quelle disponibili
		if(category.isEmpty() || !categorie.contains(new BookCategory(category)))
		{
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
			request.setAttribute("errormsg", "I valori inseriti per edizione o per quantità non possono essere accettati");
			return null;
		}
		// Il barcode deve essere di 13 caratteri
		if(barcode.length() < 13 || barcode.length() > 13)
		{
			request.setAttribute("errormsg", "Il barcode deve avere una lunghezza pari a 13");
			return null;
		}
		// Verifico se il barcode specificato esiste già nel DB
		if(gL.getBookFromBarcode(barcode)!=null)
		{
			request.setAttribute("errormsg", "Esiste già un libro con questo barcode");
			return null;
		}
		Publisher p = new Publisher();
		GestoreEditori gE = new GestoreEditori();
		// Cerco l'editore nel DB
		p = gE.getPublisherFromName(publisher);
		// Se non lo trovo
		if(p == null)
		{
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
			request.setAttribute("errormsg", "Non hai specificato nessun autore");
			return null;
		}
		// È tutto ok con i parametri ricevuti
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