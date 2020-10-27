package model.businessLogic;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import model.pojo.BookUser;
import utility.EntityManagerFactory;
import utility.StatoRegistrazione;
import utility.StatoRegistrazione.Stato;

/**
 * @author Leonardo
 * 
 * Questo gestore gestisce le seguenti operazioni relative l'entità BookUser:
 * <ul>
 * 	<li>Registrazione utente</li>
 *  <li>Trovare un utente usando la sua email</li>
 *  <li>Trovare un utente usando il suo numero di telefono</li>
 *  <li>Trovare un utente usando il suo id</li>
 *  <li>Modifica attributi utente</li>
 *  <li>Trovare tutti gli utenti presenti nel database</li>
 *  <li>Determinare il numero di utenti presenti nel database</li>
 * </ul>
 */
public class GestoreUtenti 
{
	/**
	 * Costruttore vuoto
	 */
	public GestoreUtenti(){}
	
	/**
	 * Se i dati sono validi, e non è già presente,
	 * registra un utente all'interno del database
	 * 
	 * @param u entità utente da registrare
	 * @return l'esito della registrazione
	 * @throws Exception
	 */
	public StatoRegistrazione insertUser(BookUser u) throws Exception
	{
		// Conterrà l'esito della registrazione
		StatoRegistrazione stato;
		// Se l'email specificata è già associata a un utente
		if(u.getEmail()!="" && getUserFromEmail(u.getEmail())!=null)
		{
			stato = new StatoRegistrazione(Stato.email, "Esiste già un utente con questa email"); 
		}
		// Se il numero di telefono specificato è già associato a un utente
		else if(u.getPhonenumber()!="" && getUserFromPhone(u.getPhonenumber())!=null)
		{
			stato = new StatoRegistrazione(Stato.phonenumber, "Esiste già un utente con questo numero"); 
		}
		else
		{
			EntityManager em = EntityManagerFactory.getIstance().createEntityManager();
			try
			{
				em.getTransaction().begin();
				// Rende l'entità persistente, producendo una query di insert
				em.persist(u);
			    em.getTransaction().commit();
				stato =  new StatoRegistrazione(Stato.ok, "Utente registrato con successo");
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
	 * Verifica se è presente un utente con
	 * l'email specificata
	 * 
	 * @param email da usare per la ricerca
	 * @return l'utente associato all'email se esiste, null altrimenti
	 */
	public BookUser getUserFromEmail(String email)
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	
		// Conterrà l'utente da restituire, se trovato
		BookUser u;
		try
		{
			u = em.createQuery(
			        "select u " +
			        "from BookUser u " +
			        "where u.email = :email", BookUser.class)
			.setParameter( "email", email)
			.getSingleResult();
		}
		catch(NoResultException e)
		{
			return null;
		}
	    return u;
	}
	
	/**
	 * Verifica se è presente un utente con
	 * il numero di telefono specificato
	 * 
	 * @param phonenumber da usare per la ricerca
	 * @return l'utente associato al telefono se esiste, null altrimenti
	 */
	public BookUser getUserFromPhone(String phonenumber)
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	
		// Conterrà l'utente da restituire, se trovato
		BookUser u;
		try
		{
			u = em.createQuery(
			        "select u " +
			        "from BookUser u " +
			        "where u.phonenumber = :phonenumber", BookUser.class)
			.setParameter( "phonenumber", phonenumber)
			.getSingleResult();
		}
		catch(NoResultException e)
		{
			return null;
		}
	    return u;
	}
	
	/**
	 * Verifica se è presente un utente con l'id specificato
	 * 
	 * @param id associato all'utente
	 * @param firstname nome
	 * @param lastname cognome
	 * @return l'utente associato all'id se esiste, null altrimenti
	 */
	public BookUser getUserFromId(int id, String firstname, String lastname)
	{		
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	
		// Conterrà l'utente da restituire, se trovato
		BookUser u = em.find(BookUser.class,id);
		// Se l'utente è stato trovato e sono stati specificati nome e cognome
		if(u != null && !firstname.isEmpty() && !lastname.isEmpty())
		{
			// Verifico che combaciano con quelli trovati
			if(!u.getFirstname().equals(firstname) && !u.getLastname().equals(lastname))
			{
				u = null;
			}
		}		
	    return u;
	}
	
	/**
	 * Aggiorna i dati di un utente registrato nel sistema
	 * 
	 * @param old utente con i dati da aggiornare
	 * @param update utente con i nuovi dati
	 * @return l'esito dell'aggiornamento
	 */
	public StatoRegistrazione updateUser(BookUser old, BookUser updated)
	{
		// Conterrà l'esito dell'aggiornamento
		StatoRegistrazione stato;		
		// Controllo se l'utente ha deciso di cambiare la sua email
		if(!old.getEmail().equals(updated.getEmail()))
		{
			// Controllo che l'email non sia già associata ad un altro utente
			if(updated.getEmail()!="" && getUserFromEmail(updated.getEmail())!=null)
			{
				return stato = new StatoRegistrazione(Stato.email, "Esiste già un utente con questa email"); 
			}
		}		
		//Controllo se l'utente ha deciso di cambiare il suo numero telefonico
		if(!old.getPhonenumber().equals(updated.getPhonenumber()))
		{
			// Controllo che il telefono non sia già associato ad un altro utente
			if(updated.getPhonenumber()!="" && getUserFromPhone(updated.getPhonenumber())!=null)
			{
				return stato = new StatoRegistrazione(Stato.phonenumber, "Esiste già un utente con questo numero"); 
			}
		}
		// Imposto i nuovi dati
		old.setFirstname(updated.getFirstname());
		old.setLastname(updated.getLastname());
		old.setEmail(updated.getEmail());
		old.setPhonenumber(updated.getPhonenumber());		
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		em.getTransaction().begin();
		em.clear();
		org.hibernate.Session s = (org.hibernate.Session) em.unwrap(org.hibernate.Session.class);
		// Effettuo l'operazione di aggiornamento
		s.merge(old);
		em.getTransaction().commit();
		em.close();
		stato =  new StatoRegistrazione(Stato.ok, "Utente aggiornato con successo");
		
		return stato;
	}
	
	/**
	 * @return la lista degli tutti gli utenti prelevati dal DB
	 * @throws Exception
	 */
	public List<BookUser> getAllUser() throws Exception
	{
		// Variabile che conterrà le info prelevate dal DB
		List<BookUser> users = new ArrayList<BookUser>();
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		try 
		{
			// Creo la query di select
			users = em.createQuery("from BookUser").getResultList(); 
		} 
		catch (Exception e) 
		{
			throw e;
		} 
		finally 
		{
			em.close();
		}				
		return users;
	}
	
	/**
	 * @return il numero di utenti 
	 * presenti all'interno del database
	 */
	public int getUsersSize()
	{
		try 
		{
			// Restituisco il numero di utenti presenti nel DB
			return getAllUser().size();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
	}
	
}