package model.pojo;

import java.util.HashSet;
import java.util.Set;
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
 * Classe che definisce l'Entità editore,
 * ovvero colui che ha pubblicato uno o più libri
 */
@Entity
@Table(name = "publishers")
public class Publisher 
{	
	@Id
	@Column(name="id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;
	
	@Column(name="name")
	protected String name;
	
	@Column(name="country")
	protected String country;
	
	@OneToMany(mappedBy="publisher",cascade={CascadeType.ALL},orphanRemoval=true)
    Set<Book> books = new HashSet<Book>();

	/**
	 * Costruttore vuoto
	 */
	public Publisher() {}
	
	/**
	 * Costruttore con parametri
	 * 
	 * @param name dell'editore
	 */
	public Publisher(String name) 
	{
		this.name = name;
	}

	/**
	 * Costruttore con parametri
	 * 
	 * @param name dell'editore
	 * @param country dell'editore
	 */
	public Publisher(String name, String country) 
	{
		super();
		this.name = name;
		this.country = country;
	}

	public int getId() 
	{
		return id;
	}
	
	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getCountry() 
	{
		return country;
	}

	public void setCountry(String country) 
	{
		this.country = country;
	}

	public Set<Book> getBooks() 
	{
		return books;
	}

	public void setBooks(Set<Book> books) 
	{
		this.books = books;
	}

	@Override
	public String toString() 
	{
		return "Publisher [Nome editore = " + this.name + ", country = " + this.country + "]";
	}
	
}