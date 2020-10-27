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
 * Classe che definisce l'Entità membro dello staff,
 * ovvero colui che gestisce la libreria ed esegue le transazioni
 */
@Entity
@Table(name = "staff")
public class StaffMember
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
	
	@Column(name="username")
	protected String username;
	
	@Column(name="password")
	protected String password;
	
	@OneToMany(targetEntity=StaffMember.class, mappedBy = "staffMember", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookTransaction> transactions = new ArrayList<>();

	/**
	 * Costruttore vuoto
	 */
	public StaffMember() {};
	
	/**
	 * Costruttore con parametri
	 * 
	 * @param username del membro dello staff
	 */
	public StaffMember(String username)
	{
		this.username = username;
	}
	
	/**
	 * Costruttore con parametri
	 * 
	 * @param username del membro dello staff
	 * @param password usata durante la registrazione
	 * @param email del membro dello staff
	 */
	public StaffMember(String username, String password, String email)
	{
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	@Id
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

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}
	
	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	@Override
	public String toString()
	{
		return "Utente [first name = " + this.firstname + ", second name = " + this.lastname + ", email = " + this.email
				+ ", username = " + this.username + ", password = " + this.password + "]";
	}

	public boolean equals(StaffMember staff) 
	{
		if(staff.getId() == this.getId())
			return true;
		else
			return false;
	}

}