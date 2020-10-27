package utility.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import model.pojo.Book;

/**
 * @author Leonardo
 * 
 * Classe che definisce un Predicate per la
 * ricerca di uno o più libri utilizzando il titolo
 */
public class SearchBookByTitle implements SearchPredicateI<Book>
{
	// Titolo del libro
	private String title;
	
	/**
	 * Costruttore con parametro essenziale
	 * @param title del libro
	 */
	public SearchBookByTitle(String title) 
	{
		super();
		this.title = title;
	}

	@Override
	public Predicate getPredicate(CriteriaBuilder cB, Root<Book> root) 
	{
		// Creo un predicato per cercare un libro che contenga quel titolo
		Predicate book_title = cB.like(root.get("name"), "%"+ title +"%");
		return book_title;
	}
}