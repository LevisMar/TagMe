package test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import org.hibernate.Session;

import model.pojo.Author;
import model.pojo.Book;
import model.pojo.BookCategory;
import model.pojo.BookTransaction;
import model.pojo.BookUser;
import model.pojo.Publisher;
import model.pojo.StaffMember;
import utility.EntityManagerFactory;

@SuppressWarnings("unused")
public class Prova {

	public static void main(String[] args) throws Exception 
	{
//		System.out.println("INIZIO PROVA");
//		addAuthor("Martin Flower", null);
//		System.out.println("FINE PROVA");
		
//		System.out.println("INIZIO PROVA");
//		addPublisher("Pearson", "USA");
//		System.out.println("FINE PROVA");
		
//		System.out.println("INIZIO PROVA");
//		addStaffMember(creaEsempioStaff());
//		System.out.println("FINE PROVA");
		
//		System.out.println("INIZIO PROVA");
//		addUser(creaEsempioUser());
//		System.out.println("FINE PROVA");
		
//		System.out.println("INIZIO PROVA");
//		Author a = getAuthor("Martin Flower", null);
//		System.out.println(a.getName());
		
		Book b = creaEsempioLibro();
		System.out.println("INIZIO PROVA");
		System.out.println(addBook(b));
		System.out.println("FINE PROVA");
		
//		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
//		Book b = em.find(Book.class, 90);
//		System.out.println(b.getAuthors().toString());
//		em.close();
		
//		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();
////		BookTransaction bt = em.find(BookTransaction.class, 13);
////		System.out.println(bt.getProduct().toString());
//		Book b = em.find(Book.class, 90);
//		StaffMember s = em.find(StaffMember.class, 1);
//		BookUser u = em.find(BookUser.class, 1);
//		em.close();
//		System.out.println(addTransaction(b, s, u, Codice.PRESTITO));
		
//		BookRegistry t = em.find(BookRegistry.class, "1234567891234");
//		System.out.println(t.getBook().toString());
		
//		Book b = em.find(Book.class, 51);
//		System.out.println(b.getPublisher().toString());
//		
//		Author a = em.find(Author.class, 11);
//		for (Iterator<Book> it = a.getBooks().iterator(); it.hasNext(); ) 
//		{
//			Book f = it.next();
//			System.out.println(f.getName());
//		}
//		
//		em.close();
	}
	
	private static Book creaEsempioLibro() throws ParseException
	{
	    Book b1 = new Book();
//	    Author a1 = new Author("Martin Flower", null);
//	    Author a2 = new Author("Leonardo Mazzaracchio", null);
//	    Set<Author> authors = new HashSet<Author>();
//		authors.add(a1);
//		authors.add(a2);
		
		b1.setName("UML Distilled");
		b1.setDescription("prova prova");
//		b1.setAuthors(authors);
		b1.setEdition(5);
		BookCategory bc = new BookCategory("informatica");
		b1.setCategory(bc);
		b1.setBarcode("1234567891274");
		b1.setQuantity(5);
		return b1;
	}
	
	public static boolean addBook(Book b) throws Exception
	{
		EntityManager em = EntityManagerFactory.getIstance().createEntityManager();	
		em.getTransaction().begin();

		try 
		{
			
//			Publisher p = em.createQuery(
//				        "select p " +
//				        "from Publisher p " +
//				        "where p.name = :name or p.country = :country", Publisher.class)
//				.setParameter( "name", "Pearson")
//				.setParameter( "country", null)
//				.getSingleResult();
//			
//			b.setPublisher(p);
//
//			Book addon1 = new Book();
//			addon1 = b;
//			em.persist(addon1);
//			
//			Author a = em.createQuery(
//			        "select a " +
//			        "from Author a " +
//			        "where a.name = :name or a.biography = :biography", Author.class)
//			.setParameter( "name", "Martin Flower")
//			.setParameter( "biography", null)
//			.getSingleResult();
//			
//			a.setBooks(new ArrayList<Book>());
//			a.getBooks().add(addon1);
//			
//			addon1.setAuthors(new ArrayList<Author>());
//			addon1.getAuthors().add(a);
//
//			em.persist(a);
			
			Book b1 = em.find(Book.class, 90);

//			Author a = new Author();
//			a.setName("PROVA PROVA");
//			
//			em.persist(a);
			
			Author a = em.find(Author.class, 47);
			
			a.addBook(b);
//			a.getBooks().add(b);
//			b.getAuthors().add(a);

			em.merge(b);
			
			em.getTransaction().commit();

			em.close();
			return true;
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		    em.getTransaction().rollback();
			em.close();
			return false;
		}
	}
	
	public static void addPublisher(String name, String country) throws Exception
	{
		EntityManager em = EntityManagerFactory.getIstance().createEntityManager();	
		Publisher p = new Publisher(name, country);
		em.getTransaction().begin();
		
		try 
		{
		    em.persist(p);
		    em.getTransaction().commit();
		} catch(Exception e) 
		{
		    em.getTransaction().rollback();
		}
		
//		em.close();
	}
	
	public static Publisher getPublisher(String name, String country) throws Exception
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	

		Publisher p = new Publisher();
		try
		{
			p = em.createQuery(
			        "select p " +
			        "from Publisher p " +
			        "where p.name = :name or p.country = :country", Publisher.class)
			.setParameter( "name", name)
			.setParameter( "country", country)
			.getSingleResult();
		}
		catch(NoResultException e)
		{
			if(true)
			{
				p.setName(name);
				p.setCountry(country);
				em.getTransaction().begin();
				em.persist(p);
				em.getTransaction().commit();
				getAuthor(name, country);
			}
		}
	    return p;
	}
	
	public static void addAuthor(String name, String bio) throws Exception
	{
		EntityManager em = EntityManagerFactory.getIstance().createEntityManager();	
		Author a = new Author(name, bio);
		em.getTransaction().begin();
		
		try 
		{
		    em.persist(a);
		    em.getTransaction().commit();
		} catch(Exception e) 
		{
		    em.getTransaction().rollback();
		}
		
//		em.close();
	}
	
	public static Author getAuthor(String name, String bio) throws Exception
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	

		Author a = new Author();
		try
		{
			a = em.createQuery(
			        "select a " +
			        "from Author a " +
			        "where a.name = :name or a.biography = :biography", Author.class)
			.setParameter( "name", name)
			.setParameter( "biography", bio)
			.getSingleResult();
		}
		catch(NoResultException e)
		{
			if(true)
			{
				a.setName(name);
				a.setBiography(bio);
				em.getTransaction().begin();
				em.persist(a);
				em.getTransaction().commit();
				getAuthor(name, bio);
			}
		}
		
		
		// Lancia un'eccezione se non trova niente... DA RIVEDERE
//		if(a==null && insert == true)
//		{
//			addAuthor(name, bio);
//			a = getAuthor(name, bio, false);
//		}
	    return a;
	}
	
	public static void addUser(BookUser user) throws Exception
	{
		EntityManager em = EntityManagerFactory.getIstance().createEntityManager();
		em.getTransaction().begin();
		
		try 
		{
		    em.persist(user);
		    em.getTransaction().commit();
		} catch(Exception e) 
		{
		    em.getTransaction().rollback();
		}
		
//		em.close();
	}
	
	public static BookUser getUser(String phonenumber) throws Exception
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	

		BookUser u = new BookUser();
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
//			if(true)
//			{
//				p.setName(name);
//				p.setCountry(country);
//				em.getTransaction().begin();
//				em.persist(p);
//				em.getTransaction().commit();
//				getAuthor(name, country);
//			}
		}
	    return u;
	}
	
	private static BookUser creaEsempioUser() throws ParseException
	{
	    BookUser bu = new BookUser();
		
	    bu.setFirstname("Leonardo");
	    bu.setLastname("Mazzaracchio");
	    bu.setEmail("leonard_94@live.it");
	    bu.setPhonenumber("3925952564");
		
		return bu;
	}
	
	public static void addStaffMember(StaffMember staffMember) throws Exception
	{
		EntityManager em = EntityManagerFactory.getIstance().createEntityManager();	
		em.getTransaction().begin();
		
		try 
		{
		    em.persist(staffMember);
		    em.getTransaction().commit();
		} catch(Exception e) 
		{
		    em.getTransaction().rollback();
		}
		
//		em.close();
	}
	
	public static StaffMember getStaffMember(String username) throws Exception
	{
		EntityManager em  = EntityManagerFactory.getIstance().createEntityManager();	

		StaffMember s = new StaffMember();
		try
		{
			s = em.createQuery(
			        "select s " +
			        "from Staff s " +
			        "where s.username = :username", StaffMember.class)
			.setParameter("username", username)
			.getSingleResult();
		}
		catch(NoResultException e)
		{
//			if(true)
//			{
//				p.setName(name);
//				p.setCountry(country);
//				em.getTransaction().begin();
//				em.persist(p);
//				em.getTransaction().commit();
//				getAuthor(name, country);
//			}
		}
	    return s;
	}
	
	private static StaffMember creaEsempioStaff() throws ParseException
	{
		StaffMember s = new StaffMember();
		
	    s.setFirstname("Tony");
	    s.setLastname("Stark");
	    s.setEmail("Tony_Stark@live.it");
	    s.setUsername("TonyStark");
	    s.setPassword("iosonoironman");
		
		return s;
	}
	
//	private static boolean addTransaction(Book b, StaffMember s, BookUser u, Codice cod)
//	{
//		boolean result = false;
//		
//		BookTransaction bt = new BookTransaction();
//	    b.setQuantity(4);
//		bt.setProduct(b);
//		bt.setStaffMember(s);
//		bt.setUser(u);
//		bt.setTransCode(cod);
//		bt.setData(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
//		bt.setNote(null);
//		
//		EntityManager em = EntityManagerFactory.getIstance().createEntityManager();	
//		em.getTransaction().begin();
//		
//		try 
//		{
//		    em.persist(bt);
//		    em.clear();
//		    org.hibernate.Session et = (org.hibernate.Session) em.unwrap(org.hibernate.Session.class);
//			//effettuo l'operazione di aggiornamento.
//			et.update(b);
//		    em.getTransaction().commit();
//		    result = true;
//		    System.out.println(bt.getProduct().getBarcode());
//		    
//		} catch(Exception e) 
//		{
//			e.printStackTrace();
//		    em.getTransaction().rollback();
//		}
//		
//		return result;
//	}

}
