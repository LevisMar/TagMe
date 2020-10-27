package model.businessLogic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import model.pojo.Author;
import utility.EntityManagerFactory;
import utility.StatoRegistrazione;
import utility.StatoRegistrazione.Stato;

/**
 * @author Leonardo
 * 
 * Questo gestore gestisce le seguenti operazioni relative l'entità Author :
 * <ul>
 * 	<li>Registrazione autore</li>
 *  <li>Trovare un autore usando il suo nome</li>
 *  <li>Trovare tutti gli autori presenti nel database</li>
 *  <li>Determinare il numero di autori presenti nel database</li>
 *  <li>Ricercare tutti i nomi degli autori che contengono determinati caratteri (autocomplete)</li>
 *  <li>Modifica attributi autore</li>
 * </ul>
 */
public class GestoreAutori 
{
	/**
	 * Costruttore vuoto
	 */
	public GestoreAutori(){}
	
	/**
	 * Se i dati sono validi, e non è già presente,
	 * registra un autore all'interno del database
	 * 
	 * @param a entità autore da registrare
	 * @return l'esito della registrazione
	 * @throws Exception
	 */
	public StatoRegistrazione insertAuthor(Author a) throws Exception
	{
		// Conterrà l'esito della registrazione
		StatoRegistrazione stato;
		// Se esista già un editore con quel nome
		if(getAuthorFromName(a.getName())!=null)
		{
			stato = new StatoRegistrazione(Stato.exist, "Esiste già un autore con questi dati"); 
		}
		else
		{
			EntityManager em = EntityManagerFactory.getIstance().createEntityManager();
			try
			{
				em.getTransaction().begin();
				// Rende l'entità persistente, producendo una query di insert
				em.persist(a);
			    em.getTransaction().commit();
				stato =  new StatoRegistrazione(Stato.ok, "Autore registrato con successo");
			}
			catch (Exception e ) 
			{
				e.printStackTrace();
			    em.getTransaction().rollback();
			    stato =  new StatoRegistrazione(Stato.error, "Si è verificato un errore durante l'operazione: " + e.getMessage());
			}
			em.close();
		}
		return stato;
	}
	
	/**
	 * Verifica se è presente un autore con
	 * il nome specificato
	 * 
	 * @param name da usare per la ricerca
	 * @return l'autore associato al nome se esiste, null altrimenti
	 */
	public Author getAuthorFromName(String name)
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	
		// Conterrà l'autore da restituire, se trovato
		Author a;
		try
		{
			a = em.createQuery(
			        "select a " +
			        "from Author a " +
			        "where a.name = :name", Author.class)
			.setParameter( "name", name)
			.getSingleResult();
		}
		catch(NoResultException | NonUniqueResultException et)
		{
			return null;
		}
	    return a;
	}
	
	/**
	 * @return la lista degli tutti gli autori prelevati dal DB
	 * @throws Exception
	 */
	public List<Author> getAllAuthor() throws Exception
	{
		// Variabile che conterrà le info prelevate dal DB
		List<Author> autori = new ArrayList<Author>();
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		try 
		{
			// Creo la query di select
			autori = em.createQuery("from Author").getResultList(); 
		} 
		catch (Exception e) 
		{
			throw e;
		} 
		finally 
		{
			em.close();
		}				
		return autori;
	}
	
	/**
	 * @return il numero di autori 
	 * presenti all'interno del database
	 */
	public int getAuthorsSize()
	{
		try 
		{
			// Restituisco il numero di autori presenti nel DB
			return getAllAuthor().size();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Metodo usato nell'autocomplete durante la ricerca dell'autore.
	 * Restituisce la lista degli autori il cui nome inizia con il parametro term.
	 * 
	 * @param term stringa con cui l'elemento deve iniziare
	 * @return una lista di elementi che iniziano con la stringa data
	 * @throws Exception
	 * @throws SQLException
	 */
	public List<String> GetInfoAutocomplete(String term) throws Exception, SQLException
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	
		// Conterrà la lista di elementi che iniziano con la stringa data
		List<String> lista;
		try
		{
			lista = em.createQuery(
			        "select a.name " +
			        "from Author a " +
			        "where a.name LIKE :name", String.class)
			.setParameter( "name", "%" + term + "%")
			.getResultList();
		}
		catch(NoResultException e)
		{
			em.close();
			return null;
		}
		em.close();
	    return lista;
	}
	
	/**
	 * Aggiorna i dati di un autore registrato nel sistema
	 * 
	 * @param old autore con i dati da aggiornare
	 * @param update autore con i nuovi dati
	 * @return l'esito dell'aggiornamento
	 */
	public StatoRegistrazione updateAuthor(Author old, Author updated)
	{
		// Conterrà l'esito dell'aggiornamento
		StatoRegistrazione stato;
		// Se l'autore cambia nome, ma è già stato associato ad un altro
		if(!old.getName().equals(updated.getName()) && getAuthorFromName(updated.getName())!=null)
		{
			return stato = new StatoRegistrazione(Stato.exist, "Esiste già un autore con questo nome"); 
		}
		// Imposto i nuovi dati
		old.setName(updated.getName());
		old.setBiography(updated.getBiography());		
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		em.getTransaction().begin();
		em.clear();
		org.hibernate.Session s = (org.hibernate.Session) em.unwrap(org.hibernate.Session.class);
		// Effettuo l'operazione di aggiornamento.
		s.merge(old);
		em.getTransaction().commit();
		em.close();
		stato =  new StatoRegistrazione(Stato.ok, "Autore aggiornato con successo");
		
		return stato;
	}
}