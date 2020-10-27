package model.pojo;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Leonardo
 *
 * Classe che definisce l'Entità transazione,
 * ovvero l'elemento che permette di registrare quando un
 * determinato utente prende in prestito o restituisce un libro
 */
@Entity
@Table(name = "transaction")
public class BookTransaction
{
	/**
	 * Identifica univocamente una transazione all'interno del DB
	 */
	@Id
	@Column(name="id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;
	
	/**
	 * Id associato al prodotto di cui si sta effettuando la transazione 
	 */
	
	protected Book product;
	
	/**
	 * Data in cui viene effettuata la transazione
	 */
	@Column(name="data")
	protected Date data;
	
	/**
	 * Id associato all'utente che sta effettuando la transazione 
	 */
	protected BookUser user;
	
	/**
	 * Codice utilizzato per distinguere il tipo di transazione
	 */
	public enum Codice 
	{
		ACQUISTO,
		RESO,
		VENDITA,
		PRESTITO,
		RESTITUZIONE
	}

	@Enumerated(EnumType.STRING)
	@Column(name="codice")
	protected Codice transCode;
	
	/**
	 * Fornisce ulteriori informazioni sulla transazione avvenuta
	 */
	@Column(name="note")
	protected String note;
	
	protected StaffMember staffMember;
	
	/**
	 * Costruttore vuoto
	 */
	public BookTransaction() {}

	
	@Id
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}

	@OneToOne
	@JoinColumn(name="product")
	public Book getProduct() 
	{
		return product;
	}

	public void setProduct(Book product) 
	{
		this.product = product;
	}

	public Date getData() 
	{
		return data;
	}

	public void setData(Date data) 
	{
		this.data = data;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
	public BookUser getUser() 
	{
		return user;
	}

	public void setUser(BookUser user) 
	{
		this.user = user;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="codice")
	/**
	 * Ho rimesso @Column perchè altrimenti
	 * recordTransaction di GestioneLibreria
	 * non riconosceva dove salvare il codice
	 * nel DB
	 */
	public Codice getTransCode() 
	{
		return transCode;
	}

	public void setTransCode(Codice cod) 
	{
		this.transCode = cod;
	}

	public String getNote() 
	{
		return note;
	}

	public void setNote(String note) 
	{
		this.note = note;
	}

	/**
	 * @return l'id del personale staff che ha eseguito la transazione
	 */
	@ManyToOne()
    @JoinColumn(name = "staffMember")
	public StaffMember getStaffMember() 
	{
		return staffMember;
	}

	/**
	 * Imposta l'id del personale staff che sta eseguendo la transazione
	 * @param staffId del personale
	 */
	public void setStaffMember(StaffMember staffMember) 
	{
		this.staffMember = staffMember;
	}

	@Override
	public String toString()
	{
		return "Transazione [productId = " + this.product.getId() + ", data = " + this.data + ", userId = " + this.user.getEmail()
				+ ", transCode = " + this.transCode + ", note = " + this.note + ", staffId = " + this.staffMember.getFirstname() + this.staffMember.getLastname() + "]";
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof BookTransaction) 
		{
			if(((BookTransaction) o).getId()==this.getId())
				return true;
			else
				return false;
        } 
		else 
			return false;
	}

}