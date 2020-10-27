package utility.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import model.pojo.Book;
import model.pojo.Book_;
import model.pojo.Publisher;
import model.pojo.Publisher_;

/**
 * @author Leonardo
 * 
 * Classe che definisce un Predicate per la
 * ricerca di uno o più libri utilizzando l'editore
 */
public class SearchBookByPublisher implements SearchPredicateI<Book>
{
	// Nome dell'editore
	String publisher_name;
	
	/**
	 * Costruttore con parametro essenziale
	 * @param publisher_name dell'editore
	 */
	public SearchBookByPublisher(String publisher_name) 
	{
		super();
		this.publisher_name = publisher_name;
	}

	@Override
	public Predicate getPredicate(CriteriaBuilder cB, Root<Book> root) 
	{
		// Faccio il join con la tabella Publisher per raccogliere ulteriori informazioni
		Join<Book, Publisher> bookpublisherJoin = root.join( Book_.PUBLISHER );
		// Genero un predicate per cercare libri che hanno l'editore specificato
		Predicate book_publisher = cB.equal(bookpublisherJoin.get(Publisher_.NAME), publisher_name);
		return book_publisher;
	}
}