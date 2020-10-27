package utility.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import model.pojo.Author;
import model.pojo.Author_;
import model.pojo.Book;
import model.pojo.Book_;

/**
 * @author Leonardo
 * 
 * Classe che definisce un Predicate per la
 * ricerca di uno o più libri utilizzando l'autore
 */
public class SearchBookByAuthor implements SearchPredicateI<Book>
{
	// Nome dell'autore
	String author_name;

	/**
	 * Costruttore con parametro essenziale
	 * @param author_name del libro da cercare
	 */
	public SearchBookByAuthor(String author_name) 
	{
		super();
		this.author_name = author_name;
	}

	@Override
	public Predicate getPredicate(CriteriaBuilder cB, Root<Book> root) 
	{
		// Faccio il join con la tabella Author per raccogliere ulteriori informazioni
		Join<Book, Author> bookauthorJoin = root.join( Book_.AUTHORS );
		// Genero un predicate per cercare libri che hanno l'autore specificato
		Predicate book_author = cB.equal(bookauthorJoin.get(Author_.NAME), author_name);
		return book_author;
	}
}