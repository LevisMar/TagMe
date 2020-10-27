package model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Leonardo
 *
 * Classe che definisce l'Entità BookCategory,
 * ovvero un categoria a cui uno o più libri 
 * possono essere associati
 */
@Entity
@Table(name = "book_category")
public class BookCategory 
{
	@Id
	@Column(name="name")
	protected String name;

	/**
	 * Costruttore vuoto
	 */
	public BookCategory() {}
	
	/**
	 * Costruttore con parametri
	 * 
	 * @param name da dare alla categoria
	 */
	public BookCategory(String name)
	{
		this.name = name;
	}
	
	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
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
		BookCategory other = (BookCategory) obj;
		if(name == null) 
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
}