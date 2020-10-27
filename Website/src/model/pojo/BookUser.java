package model.pojo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Leonardo
 *
 * Classe che definisce l'Entità utente,
 * ovvero colui che prende in prestito o restituisce un libro
 */
@Entity
@Table(name = "users")
public class BookUser
{
	/**
	 * Identifica univocamente un utente all'interno del DB
	 */
	@Id
	@Column(name="id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;
	
	/**
	 * Nome dell'utente che verrà registrato
	 */
	@Column(name="firstname")
	protected String firstname;
	
	/**
	 * Cognome dell'utente che verrà registrato
	 */
	@Column(name="lastname")
	protected String lastname;
	
	/**
	 * Permetterà all'utente di ricevere info, o recuperare password
	 */
	@Column(name="email",nullable = false, unique = true)
	protected String email;
	
	@Column(name="phonenumber",nullable = false, unique = true)
	public String phonenumber;
	
	private List<BookTransaction> transactions = new ArrayList<BookTransaction>();
	
	/**
	 * Costruttore vuoto
	 */
	public BookUser() {};
	
	/**
	 * Costruttore con parametri
	 * 
	 * @param firstname nome
	 * @param lastname cognome
	 * @param email
	 */
	public BookUser(String firstname, String lastname, String email)
	{
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
	}
	
	/**
	 * Costruttore con parametri
	 * 
	 * @param firstname nome
	 * @param lastname cognome
	 * @param email
	 * @param phonenumber
	 */
	public BookUser(String firstname, String lastname, String email, String phonenumber)
	{
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phonenumber = phonenumber;
	}
	
	@Id
	@Column(name="id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname) 
	{
		this.firstname = firstname;
	}

	public String getLastname() 
	{
		return lastname;
	}

	public void setLastname(String lastname) 
	{
		this.lastname = lastname;
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}

	public String getPhonenumber() 
	{
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) 
	{
		this.phonenumber = phonenumber;
	}	

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BookTransaction> getTransactions() 
	{
		return transactions;
	}

	public void setTransactions(List<BookTransaction> transactions) 
	{
		this.transactions = transactions;
	}

	@Override
	public String toString()
	{
		return "Utente [first name = " + this.firstname + ", second name = " + this.lastname + ", email = " + this.email
				+ ", phonenumber = " + this.phonenumber + "]";
	}

	public boolean equals(BookUser user) 
	{
		if(user.getId() == this.getId())
			return true;
		else
			return false;
	}

}