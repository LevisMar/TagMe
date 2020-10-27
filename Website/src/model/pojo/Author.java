package model.pojo;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author Leonardo
 *
 * Classe che definisce l'Entità autore,
 * ovvero colui che ha scritto uno o più libri
 */
@Entity
@Table(name = "authors")
public class Author 
{
	@Id
	@Column(name="id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;
	
	@Column(name="name")
	protected String name;
	
	@Column(name="biography")
	protected String biography;
	
	@ManyToMany(mappedBy = "authors", cascade=CascadeType.ALL)
    private Set<Book> books = new HashSet<>();
	
	/**
	 * Costruttore senza parametri
	 */
	public Author() {};
	
	/**
	 * Costruttore con parametro essenziale
	 * 
	 * @param name da associare all'autore
	 */
	public Author(String name)
	{
		this.name = name;
	}
	
	/**
	 * Costruttore completo
	 * 
	 * @param name da associare all'autore
	 * @param biography da associare all'autore
	 */
	public Author(String name, String biography)
	{
		this.name = name;
		this.biography = biography;
	}

	/**
	 * @return l'id associato all'autore
	 */
	public int getId() 
	{
		return id;
	}

	/**
	 * @return il nome associato all'autore
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * Imposta il nome dell'autore
	 * @param name da impostare
	 */
	public void setName(String name) 
	{
		this.name = name;
	}
	
	/**
	 * @return la biografia dell'autore
	 */
	public String getBiography() 
	{
		return biography;
	}

	/**
	 * Imposta la biografia dell'autore
	 * @param biography da impostare
	 */
	public void setBiography(String biography) 
	{
		this.biography = biography;
	}

	/**
	 * @return la lista dei libri scritti dall'autore
	 */
	public Set<Book> getBooks() 
	{
		return books;
	}

	/**
	 * Imposta la lista dei libri scritti dall'autore
	 * @param books lista dei libri
	 */
	public void setBooks(Set<Book> books) 
	{
		this.books = books;
	}

	/**
	 * Aggiunge il libro specificato nella lista dei 
	 * libri scritta dall'autore
	 * 
	 * @param b libro da aggiungere
	 * @return true se il libro viene aggiunto, false altrimenti
	 */
	public boolean addBook(Book b) 
	{
		// Se il libro è già inserito, evito di proseguire
		if(this.books.contains(b))
		{
			return false;
		}
		// Il libro non è già presente nella lista
		else
		{
			// Aggiungo il libro alla lista
			this.books.add(b);
			// Aggiungo l'autore, tra gli autori associati al libro
	        b.getAuthors().add(this);
	        return true;
		}
    }
 
	/**
	 * Rimuove un libro dalla lista dei libri
	 * scritti dall'autore
	 * 
	 * @param book libro da rimuovere
	 */
    public void removeBook(Book book) 
    {
    	// Rimuovo il libro dalla lista
    	this.books.remove(book);
    	// Rimuovo l'autore dalla lista degli autori associati al libro
        book.getAuthors().remove(this);
    }

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(this == obj)
		{
			return true;
		}
		if(obj == null)
		{
			return false;
		}
		if(getClass() != obj.getClass())
		{
			return false;
		}
		Author other = (Author) obj;
		if (name == null) 
		{
			if(other.name != null)
			{
				return false;
			}
		} 
		else if(!name.equals(other.name))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString() 
	{
		return "Author [name=" + name + "]";
	}
    	
}