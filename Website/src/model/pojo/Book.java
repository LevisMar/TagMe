package model.pojo;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * @author Leonardo
 *
 * Classe che definisce l'Entità libro,
 * ovvero l'elemento essenziale del progetto
 */
@Entity
@Table(name = "books")
public class Book
{
	/**
	 * Identifica univocamente un prodotto dall'altro.
	 * La scelta di cosa usare come identificativo può variare.
	 */
	@Transient
	protected int id;
	
	/**
	 * Una descrizione breve del prodotto per riconoscere il prodotto.
	 */
	@Column(name="name")
	protected String name;
	
	/**
	 * Una descrizione lunga del prodotto per fornire ulteriori dettagli
	 */
	@Column(name="description")
	protected String description;
	
    private Set<Author> authors = new HashSet<Author>();
	
	@Column(name="edition")
	protected int edition;
	
	protected Publisher publisher;
	
	protected BookCategory category;
	
	@Column(name="quantity")
	protected int quantity;

	protected String barcode;
	
	@OneToOne(mappedBy="product", fetch=FetchType.LAZY)
	BookTransaction transaction;
	
	/**
	 * Costruttore vuoto
	 */
	public Book() {}

	/**
	 * Costruttore con parametri
	 * 
	 * @param name del libro
	 * @param description del libro
	 * @param authors che hanno scritto il libro
	 * @param edition del libro
	 * @param publisher che ha pubblicato il libro
	 * @param category a cui appartiene il libro
	 */
	public Book(String name, String description, int edition, Publisher publisher, BookCategory category, String barcode, int quantity) 
	{
		this.name = name;
		this.description = description;
		this.edition = edition;
		this.publisher = publisher;
		this.category = category;
		this.barcode = barcode;
		this.quantity = quantity;
	}
	
	/* PER TEST, POI CANCELLARE */
	public Book(Book b)
	{
		this.name = b.getName();
		this.description = b.getDescription();
		this.edition = b.getEdition();
		this.publisher = b.getPublisher();
		this.category = b.getCategory();
		this.barcode = b.getBarcode();
		this.quantity = b.getQuantity();
	}

	@Transient
	public int getId() 
	{
		return id;
	}

	@Transient
	public void setId(int id)
	{
		this.id = id;
	}

	@Id
	@Column(name="barcode", nullable = false)
	public String getBarcode()
    {
    	return barcode;
    }
    
    public void setBarcode(String barcode)
    {
    	this.barcode = barcode;
    }

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	@ManyToMany(cascade = {
	        CascadeType.ALL
	    })
    @JoinTable(name = "book_author",
    		joinColumns = { @JoinColumn(name = "books_id", referencedColumnName = "barcode") },
    		inverseJoinColumns = { @JoinColumn(name = "authors_id", referencedColumnName = "id") })
	public Set<Author> getAuthors() 
	{
		return authors;
	}

	public void setAuthors(Set<Author> authors) 
	{
		this.authors = authors;
	}

	public int getEdition() 
	{
		return edition;
	}

	public void setEdition(int edition) 
	{
		this.edition = edition;
	}

	@ManyToOne
	@JoinColumn(name="publishers")
	public Publisher getPublisher() 
	{
		return publisher;
	}

	public void setPublisher(Publisher publisher) 
	{
		this.publisher = publisher;
	}

	@ManyToOne
	@JoinColumn(name="category")
	public BookCategory getCategory() 
	{
		return category;
	}

	public void setCategory(BookCategory category) 
	{
		this.category = category;
	}
		
	public int getQuantity() 
	{
		return quantity;
	}

	public void setQuantity(int quantity) 
	{
		this.quantity = quantity;
	}
	
	/**
	 * Aggiunge l'autore specificato nella lista degli 
	 * autori che hanno scritto il libro
	 * 
	 * @param a autore da aggiungere
	 * @return true se l'autore viene aggiunto, false altrimenti
	 */
	public boolean addAuthor(Author a) 
	{
		// Se l'autore è già inserito, evito di proseguire
		if(authors.contains(a))
		{
			return false;
		}
		// L'autore non è già presente nella lista
		else
		{
			// Aggiungo l'autore alla lista
			authors.add(a);
			// Aggiungo il libro, tra i libri scritti dall'autore
	        a.getBooks().add(this);
	        return true;
		}
    }
 
	/**
	 * Rimuove l'autore dalla lista degli
	 * autori che hanno scritto il libro
	 * 
	 * @param book libro da rimuovere
	 */
    public void removeAuthor(Author a) 
    {
    	// Rimuovo l'autore dalla lista
        this.authors.remove(a);
        // Rimuovo il libro dalla lista dei libri scritti dall'autore
        a.getBooks().remove(this);
    }

	public boolean equals(Book prod) 
	{
		if(prod.getId()==this.getId())
			return true;
		else
			return false;
	}

	@Override
	public String toString()
	{
		return "Libro [barcode = " + this.barcode + ", name = " + this.name + ", description = " + this.description + ", authors = " + this.authors.toString()
				+ ", edition = " + this.edition + ", publisher = " + this.publisher.toString() + ", category = " + this.category + ", quantità = " + this.quantity + "]";
	}

	/**
	 * Esegue un confronto dettagliato tra due libri,
	 * verificando che siano identici in tutti i loro attributi
	 * 
	 * @param b libro con cui eseguire il confronto
	 * @return true se i due libri sono identici, false altrimenti
	 */
	public boolean isEquals(Book b) 
	{
		if(b.getName().equals(this.name) && 
		   b.getDescription().equals(this.description) && 
		   b.getBarcode().equals(this.barcode) &&
		   b.getEdition() == this.edition &&
		   b.getPublisher().name.equals(this.publisher.name) &&
		   b.getCategory().name.equals(this.category.name) &&
		   b.getQuantity() == this.quantity &&
		   b.getAuthors().equals(this.getAuthors()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}