package model.businessLogic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import model.pojo.Publisher;
import utility.EntityManagerFactory;
import utility.StatoRegistrazione;
import utility.StatoRegistrazione.Stato;

/**
 * @author Leonardo
 * 
 * Questo gestore gestisce le seguenti operazioni relative l'entità Publisher :
 * <ul>
 * 	<li>Registrazione editore</li>
 *  <li>Trovare un editore usando il suo nome</li>
 *  <li>Trovare tutti gli editori presenti nel database</li>
 *  <li>Determinare il numero di editori presenti nel database</li>
 *  <li>Ricercare tutti i nomi degli editori che contengono determinati caratteri (autocomplete)</li>
 *  <li>Modifica attributi editore</li>
 * </ul>
 */
public class GestoreEditori 
{
	/**
	 * Costruttore vuoto
	 */
	public GestoreEditori(){}
	
	/**
	 * Se i dati sono validi, e non è già presente,
	 * registra un editore all'interno del database
	 * 
	 * @param p entità editore da registrare
	 * @return l'esito della registrazione
	 * @throws Exception
	 */
	public StatoRegistrazione insertPublisher(Publisher p) throws Exception
	{
		// Conterrà l'esito della registrazione
		StatoRegistrazione stato;
		// Se esista già un editore con quel nome
		if(getPublisherFromName(p.getName())!=null)
		{
			stato = new StatoRegistrazione(Stato.exist, "Esiste già un editore con questi dati"); 
		}
		else
		{
			EntityManager em = EntityManagerFactory.getIstance().createEntityManager();
			try
			{
				em.getTransaction().begin();
				// Rende l'entità persistente, producendo una query di insert
				em.persist(p);
			    em.getTransaction().commit();
				stato =  new StatoRegistrazione(Stato.ok, "Editore registrato con successo");
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
	 * Verifica se è presente un editore con
	 * il nome specificato
	 * 
	 * @param name da usare per la ricerca
	 * @return l'editore associato al nome se esiste, null altrimenti
	 */
	public Publisher getPublisherFromName(String name)
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	
		// Conterrà l'editore da restituire, se trovato
		Publisher p;
		try
		{
			p = em.createQuery(
			        "select p " +
			        "from Publisher p " +
			        "where p.name = :name", Publisher.class)
			.setParameter( "name", name)
			.getSingleResult();
		}
		catch(NoResultException | NonUniqueResultException et)
		{
			return null;
		}
	    return p;
	}
	
	/**
	 * @return la lista degli tutti gli editori prelevati dal DB
	 * @throws Exception
	 */
	public List<Publisher> getAllPublisher() throws Exception
	{
		// Variabile che conterrà le info prelevate dal DB
		List<Publisher> publishers = new ArrayList<Publisher>();
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		try 
		{
			// Creo la query di select
			publishers = em.createQuery("from Publisher").getResultList(); 
		} 
		catch (Exception e) 
		{
			throw e;
		} 
		finally 
		{
			em.close();
		}
		return publishers;
	}
	
	/**
	 * @return il numero di editori 
	 * presenti all'interno del database
	 */
	public int getPublishersSize()
	{
		try 
		{
			// Restituisco il numero di editori presenti nel DB
			return getAllPublisher().size();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Metodo usato nell'autocomplete durante la ricerca dell'editore.
	 * Restituisce la lista degli editori il cui nome inizia con il parametro term.
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
			        "select p.name " +
			        "from Publisher p " +
			        "where p.name LIKE :name", String.class)
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
	 * Aggiorna i dati di un editore registrato nel sistema
	 * 
	 * @param old editore con i dati da aggiornare
	 * @param update editore con i nuovi dati
	 * @return l'esito dell'aggiornamento
	 */
	public StatoRegistrazione updatePublisher(Publisher old, Publisher updated)
	{
		// Conterrà l'esito dell'aggiornamento
		StatoRegistrazione stato;
		// Se l'editore cambia nome, ma è già stato associato ad un altro
		if(!old.getName().equals(updated.getName()) && getPublisherFromName(updated.getName())!=null)
		{
			return stato = new StatoRegistrazione(Stato.exist, "Esiste già un editore con questo nome"); 
		}
		// Imposto i nuovi dati
		old.setName(updated.getName());
		old.setCountry(updated.getCountry());		
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		em.getTransaction().begin();
		em.clear();
		org.hibernate.Session s = (org.hibernate.Session) em.unwrap(org.hibernate.Session.class);
		// Effettuo l'operazione di aggiornamento
		s.merge(old);
		em.getTransaction().commit();
		em.close();
		stato =  new StatoRegistrazione(Stato.ok, "Editore aggiornato con successo");
		
		return stato;
	}
}