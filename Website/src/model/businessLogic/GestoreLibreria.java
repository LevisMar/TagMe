package model.businessLogic;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import model.pojo.Book;
import model.pojo.BookCategory;
import model.pojo.BookTransaction;
import model.pojo.BookUser;
import utility.EntityManagerFactory;
import utility.predicate.SearchBookByAuthor;
import utility.predicate.SearchBookByEdition;
import utility.predicate.SearchBookByGenre;
import utility.predicate.SearchBookByPublisher;
import utility.predicate.SearchBookByTitle;

/**
 * @author Leonardo
 * 
 * Questo gestore gestisce le seguenti operazioni relative ad una libreria:
 * <ul>
 * 	<li>Ottenere una lista di tutte le transazioni</li>
 * 	<li>Ottenere una lista di tutte le transazioni di un libro</li>
 * 	<li>Registrare la transazione di uno o più libri</li>
 * 	<li>Aggiunta di un libro</li>
 *  <li>Rimozione di un libro</li>
 *  <li>Modifica di un libro</li>
 *  <li>Ottenere uno o più libri, in base ad uno o più parametri (filtri)</li>
 *  <li>Ottenere un libro tramite il suo barcode</li>
 *  <li>Ottenere tutti i libri presenti nella libreria</li>
 *  <li>Ottenere il numero di libri presenti nella libreria</li>
 *  <li>Ottenere tutti i generi di libri presenti nella libreria</li>
 * </ul>
 */
public class GestoreLibreria
{

	public List<BookTransaction> getListTransaction() 
	{
		return null;
	}

	public List<BookTransaction> getListTransaction(Book t) 
	{
		return null;
	}
	
	/**
	 * Registra una o più transazioni, ovvero prestiti o restituzioni di libri,
	 * all'interno del database
	 * 
	 * @param transactionList lista di transazioni da registrare
	 * @return true se l'operazione va a buon fine, false altrimenti
	 */
	public boolean recordTransaction(ArrayList<BookTransaction> transactionList)
	{
		EntityManager em = EntityManagerFactory.getIstance().createEntityManager();
		em.getTransaction().begin();
		try
		{
			// Registro tutte le transazioni
			for(BookTransaction bt : transactionList)
			{
				// Rende l'entità persistente, producendo una query di insert
				em.persist(bt);
				// Serve perchè altrimenti hanno tutto lo stesso ID quindi non è possibile persistere più transazioni
				em.flush();
	            em.clear();
			}
		    em.getTransaction().commit();
		}
		catch (Exception e ) 
		{
			e.printStackTrace();
		    em.getTransaction().rollback();
			em.close();
		    return false;
		}
		em.close();		
		return true;
	}

	public boolean addProduct(Book b) 
	{		
		EntityManager em = EntityManagerFactory.getIstance().createEntityManager();	
		em.getTransaction().begin();		
		em.merge(b);						
		em.getTransaction().commit();
		em.close();		
		return true;
	}

	public boolean removeProduct(Book t) 
	{
		return false;
	}
	
	/**
	 * Aggiorna i dati di un libro con quelli nuovi inseriti da un membro dello staff
	 * 
	 * @param to_update istanza del libro da aggiornare
	 * @param updated istanza del libro con i nuovi dati da inserire
	 * @return una stringa con l'esito dell'aggiornamento
	 */
	public String updateProduct(Book to_update, Book updated)
	{
		// Se il barcode del libro è da cambiare, ma è già associato ad un altro libro
		if(!to_update.getBarcode().equals(updated.getBarcode()) && getBookFromBarcode(updated.getBarcode())!=null)
		{
			return "Esiste già un libro con questo barcode"; 
		}		
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		em.getTransaction().begin();
		// Se il barcode del libro è da cambiare
		if(!to_update.getBarcode().equals(updated.getBarcode()))
		{
			// É necessario creare una query a mano JPQL, per aggiornare la PK book!!!
			em.createQuery("update Book b set b.barcode = :newbarcode where b.barcode=:oldbarcode")
			.setParameter("oldbarcode", to_update.getBarcode())
			.setParameter("newbarcode", updated.getBarcode())
			.executeUpdate();	
		}
		// Imposto i nuovi dati
		to_update.setName(updated.getName());
		to_update.setBarcode(updated.getBarcode());
		to_update.setDescription(updated.getDescription());
		to_update.setCategory(updated.getCategory());
		to_update.setEdition(updated.getEdition());
		to_update.setPublisher(updated.getPublisher());
		to_update.setQuantity(updated.getQuantity());
		to_update.setAuthors(updated.getAuthors());		
		em.clear();
		org.hibernate.Session s = (org.hibernate.Session) em.unwrap(org.hibernate.Session.class);
		// Effettuo l'operazione di aggiornamento
		s.merge(to_update);
		em.getTransaction().commit();
		em.close();		
		return "Aggiornamento effettuato";
	}

	
	/*
	 * Devo scrivere in questo modo il Predicate
	 * perchè importando la libreria, essa andrebbe
	 * in conflitto con l'altra libreria Predicate 
	 * importata, che non può essere eliminata poichè
	 * fa parte di un metodo necessario.
	 */
	public Book getProduct(java.util.function.Predicate<? super Book> query) 
	{
		return null;
	}

	public List<Book> getProducts(java.util.function.Predicate<? super Book> query) 
	{
		return null;
	}
	
	/**
	 * Recupera il libro, o la lista dei libri, che rispettano 
	 * i requisiti di ricerca ricevuti dal client.
	 * 
	 * @param title del libro da cercare, o parole di cui può esssere composto
	 * @param genres_list dei libri da cercare
	 * @param author di cui cercare i libri
	 * @param publisher di cui cercare i libri
	 * @param edition di cui cercare i libri	 * 
	 * @return una lista di uno o più libri che rispettano i requisiti dati
	 */
	public List<Book> getSpecificBook(String title, List<String> genres_list, String author, String publisher, int edition)
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<Book> cQ = cB.createQuery(Book.class);
		Root<Book> root = cQ.from(Book.class);		
		// Conterrà la lista dei predicati per filtrare i risultati
		List<Predicate> book_predicates = new ArrayList<Predicate>();
		// Se è stato specificato un titolo da cercare
		if(!title.isEmpty())
		{
			SearchBookByTitle sB = new SearchBookByTitle(title);
			// Lo inserisco nella lista
			book_predicates.add(sB.getPredicate(cB, root));
		}
		// Se sono stati specificati uno o più generi
		if(!genres_list.isEmpty())
		{
			SearchBookByGenre sB = new SearchBookByGenre(genres_list);
			book_predicates.add(sB.getPredicate(cB, root));			
		}
		// Se è stato specificato un autore
		if(!author.isEmpty())
		{
			SearchBookByAuthor sB = new SearchBookByAuthor(author);
			book_predicates.add(sB.getPredicate(cB, root));
		}
		// Se è stato specificato un editore
		if(!publisher.isEmpty())
		{
			SearchBookByPublisher sB = new SearchBookByPublisher(publisher);
			book_predicates.add(sB.getPredicate(cB, root));
		}
		// Se è stata specificata un'edizione
		if(edition!=0)
		{
			SearchBookByEdition sE = new SearchBookByEdition(edition);
			book_predicates.add(sE.getPredicate(cB, root));
		}
		// Eseguo la ricerca utilizzando i filtri specificati
		cQ.select(root).where(cB.and(book_predicates.toArray(new Predicate[]{})));
		List<Book> results = em.createQuery(cQ).getResultList();		
		return results;
	}
	
	/**
	 * Cerca all'interno della libreria, un libro che ha il barcode
	 * uguale a quello specificato
	 * 
	 * @param barcode del libro da ricercare	 * 
	 * @return il libro corrispondente se trovato, null altrimenti
	 */
	public Book getBookFromBarcode(String barcode)
	{
		// Conterrà il libro trovato utilizzando il barcode
		Book b = new Book();
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		b = em.find(Book.class, barcode);
		return b;
	}
	
	/**
	 * @return la lista di tutti i libri presenti all'interno della libreria
	 */
	public List<Book> getAllBooks()
	{
		// Variabile che conterrà le info prelevate dal DB
		List<Book> libri = new ArrayList<Book>();
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		try 
		{
			// Creo la query di select
			libri = em.createQuery("from Book").getResultList(); 
		} 
		catch (Exception e) 
		{
			throw e;
		} 
		finally 
		{
			em.close();
		}
		return libri;
	}
	
	/**
	 * @return il numero di libri presenti all'interno del database
	 */
	public int getBooksSize()
	{
		try 
		{
			// Restituisco il numero di libri presenti nel DB
			return getAllBooks().size();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	/** 
	 * @return la lista di tutte i possibili generi,
	 * associabili ad un libro
	 */
	public List<BookCategory> getAllCategory()
	{
		// Variabile che conterrà le info prelevate dal DB
		List<BookCategory> categorie = new ArrayList<BookCategory>();
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		try 
		{
			// Creo la query di select
			categorie = em.createQuery("from BookCategory").getResultList(); 
		} 
		catch (Exception e) 
		{
			throw e;
		} 
		finally 
		{
			em.close();
		}
		return categorie;
	}

}