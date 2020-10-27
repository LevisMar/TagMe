package model.businessLogic;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import model.pojo.StaffMember;
import utility.EntityManagerFactory;
import utility.StatoRegistrazione;
import utility.StatoRegistrazione.Stato;

/**
 * @author Leonardo
 * 
 * Questo gestore gestisce le seguenti operazioni relative l'entità StaffMember:
 * <ul>
 * 	<li>Registrazione di un membro dello staff</li>
 *  <li>Trovare un membro dello staff usando la sua email</li>
 *  <li>Trovare un membro dello staff usando il suo username</li>
 *  <li>Modifica attributi membro dello staff</li>
 *  <li>Trovare tutti i membri dello staff presenti nel database</li>
 *  <li>Determinare il numero di membri dello staff presenti nel database</li>
 *  <li>Verifica delle credenziali per il login</li>
 * </ul>
 */
public class GestoreStaff 
{
	/**
	 * Costruttore vuoto
	 */
	public GestoreStaff(){}
	
	/**
	 * Se i dati sono validi, e non è già presente,
	 * registra un membro dello staff all'interno del database
	 * 
	 * @param s entità da registrare
	 * @return l'esito della registrazione
	 * @throws Exception
	 */
	public StatoRegistrazione insertStaffMember(StaffMember s)
	{
		// Conterrà l'esito della registrazione
		StatoRegistrazione stato;
		// Se l'email specificata è già associata a un membro dello staff
		if(s.getEmail()!="" && getStaffFromEmail(s.getEmail())!=null)
		{
			stato = new StatoRegistrazione(Stato.email, "Esiste già un membro dello staff con questa email"); 
		}
		// Se l'username specificato è già associato a un membro dello staff
		else if(s.getUsername()!="" && getStaffFromUsername(s.getUsername())!=null)
		{
			stato = new StatoRegistrazione(Stato.username, "Esiste già un membro dello staff con questo username"); 
		}
		else
		{
			EntityManager em = EntityManagerFactory.getIstance().createEntityManager();
			try
			{
				em.getTransaction().begin();
				// Rende l'entità persistente, producendo una query di insert
				em.persist(s);
			    em.getTransaction().commit();
				stato =  new StatoRegistrazione(Stato.ok, "Membro dello staff registrato con successo");
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
	 * Verifica se è presente un membro 
	 * dello staff con l'email specificata
	 * 
	 * @param email da usare per la ricerca
	 * @return membro dello staff associato all'email se esiste, null altrimenti
	 */
	public StaffMember getStaffFromEmail(String email)
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	
		// Conterrà il membro dello staff da restituire, se trovato
		StaffMember sm;
		try
		{
			sm = em.createQuery(
			        "select s " +
			        "from StaffMember s " +
			        "where s.email = :email", StaffMember.class)
			.setParameter( "email", email)
			.getSingleResult();
		}
		catch(NoResultException e)
		{
			em.close();
			return null;
		}
		em.close();
	    return sm;
	}
	
	/**
	 * Verifica se è presente un membro 
	 * dello staff con l'username specificato
	 * 
	 * @param username da usare per la ricerca
	 * @return l'utente associato al telefono se esiste, null altrimenti
	 */
	public StaffMember getStaffFromUsername(String username)
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	
		// Conterrà il membro dello staff da restituire, se trovato
		StaffMember sm;
		try
		{
			sm = em.createQuery(
			        "select s " +
			        "from StaffMember s " +
			        "where s.username = :username", StaffMember.class)
			.setParameter( "username", username)
			.getSingleResult();
		}
		catch(NoResultException e)
		{
			em.close();
			return null;
		}
		em.close();
	    return sm;
	}
	
	/**
	 * Aggiorna i dati di un membro dello staff registrato nel sistema
	 * 
	 * @param search_par parametro da usare per la ricerca
	 * @param sm membro dello staff con i nuovi dati
	 * @return l'esito dell'aggiornamento
	 */
	public StatoRegistrazione updateStaffMember(String search_par, StaffMember sm)
	{
		// Conterrà l'esito dell'aggiornamento
		StatoRegistrazione stato;
		// Conterrà il membro dello staff da aggiornare
		StaffMember smdb;
		// Se per la ricerca viene utilizzata l'email
		if(search_par.contains("@"))
		{
			smdb = getStaffFromEmail(search_par);
		}
		// Se per la ricerca viene utilizzato l'username
		else
		{
			smdb = getStaffFromUsername(search_par);
		}
		// Se non è stato trovato
		if(smdb==null)
		{
			return stato = new StatoRegistrazione(Stato.error, "Non ho trovato il membro dello staff con il parametro dato"); 
		}
		//Controllo se il membro dello staff ha deciso di cambiare la sua email
		if(!smdb.getEmail().equals(sm.getEmail()))
		{
			// Controllo che l'email non sia già associata ad un altro
			if(sm.getEmail()!="" && getStaffFromEmail(sm.getEmail())!=null)
			{
				return stato = new StatoRegistrazione(Stato.email, "Esiste già un membro dello staff con questa email"); 
			}
		}		
		//Controllo se il membro dello staff ha deciso di cambiare il suo username
		if(!smdb.getUsername().equals(sm.getUsername()))
		{
			// Controllo che l'username non sia già associato ad un altro
			if(sm.getUsername()!="" && getStaffFromUsername(sm.getUsername())!=null)
			{
				return stato = new StatoRegistrazione(Stato.username, "Esiste già un membro dello staff con questo username"); 
			}
		}
		// Imposto i nuovi dati
		smdb.setFirstname(sm.getFirstname());
		smdb.setLastname(sm.getLastname());
		smdb.setEmail(sm.getEmail());
		smdb.setUsername(sm.getUsername());
		smdb.setPassword(sm.getPassword());
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		em.getTransaction().begin();
		em.clear();
		org.hibernate.Session s = (org.hibernate.Session) em.unwrap(org.hibernate.Session.class);
		// Effettuo l'operazione di aggiornamento.
		s.update(smdb);
		em.getTransaction().commit();
		em.close();
		stato =  new StatoRegistrazione(Stato.ok, "Membro dello staff aggiornato con successo");
		
		return stato;
	}
	
	/**
	 * @return la lista di tutti i membri dello staff 
	 * presenti nel database
	 */
	public List<StaffMember> getAllStaffMembers()
	{
		// Variabile che conterrà le info prelevate dal DB
		List<StaffMember> staff_members = new ArrayList<StaffMember>();
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
		try 
		{
			// Creo la query di select
			staff_members = em.createQuery("from Book").getResultList(); 
		} 
		catch (Exception e) 
		{
			throw e;
		} 
		finally 
		{
			em.close();
		}				
		return staff_members;
	}
	
	/**
	 * @return il numero dei membri dello staff 
	 * presenti all'interno del database
	 */
	public int getStaffMembersSize()
	{
		try 
		{
			// Restituisco il numero dei membri dello staff presenti nel DB
			return getAllStaffMembers().size();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	// Possibili codici in seguito al login
	public enum StatoLogin {ok,nouser,nopsw,error};
	
	/**
	 * Verifica le credenziali di accesso di un membro dello staff,
	 * e se valide esegue il login
	 * 
	 * @param username inserito dal membro dello staff
	 * @param password digitata
	 * @return un codice di errore o successo per il login
	 */
	public StatoLogin login(String username,String password)
	{
		EntityManager em = Persistence.createEntityManagerFactory("percistenceUnit").createEntityManager();	
		// Conterrà il membro dello staff da controllare
		StaffMember s = new StaffMember();
		try
		{
			s = em.createQuery(
			        "select s " +
			        "from StaffMember s " +
			        "where s.username = :username", StaffMember.class)
			.setParameter( "username", username)
			.getSingleResult();
		}
		catch(NoResultException e)
		{
			em.close();
			return StatoLogin.nouser;
		}
		em.close();
		// Se la password coincide
		if(s.getPassword().equals(password))
		{
			return StatoLogin.ok;
		}
		else
		{
			return StatoLogin.nopsw;
		}
	}
	
}