package utility.predicate;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import model.pojo.Book;
import model.pojo.BookCategory;
import model.pojo.Book_;

/**
 * @author Leonardo
 * 
 * Classe che definisce un Predicate per la
 * ricerca di uno o più libri utilizzando il genere/categoria
 */
public class SearchBookByGenre implements SearchPredicateI<Book>
{
	// Contiene la lista di uno o più generi
	List<BookCategory> genres_list = new ArrayList<BookCategory>();
	
	/**
	 * Costruttore con parametro essenziale
	 * @param genres_list lista di generi
	 */
	public SearchBookByGenre(List<String> genres_list) 
	{
		super();
		for(String genre : genres_list)
		{
			BookCategory bC = new BookCategory(genre);
			this.genres_list.add(bC);
		}
	}

	@Override
	public Predicate getPredicate(CriteriaBuilder cB, Root<Book> root) 
	{
		// Se c'è solo un genere
		if(genres_list.size()==1)
		{
			// Genero un predicate per cercare libri di quel genere
			Predicate book_genre = cB.equal(root.get(Book_.CATEGORY), genres_list.get(0));
			return book_genre;
		}
		// Ci sono più generi
		else
		{
			// Creo più predicate
			List<Predicate> genres_predicates = new ArrayList<Predicate>();
			// Per ogni genere presente nella lista
			for(BookCategory genre : genres_list)
			{
				// Genero un predicate per cercare libri di quel genere
				Predicate book_genre = cB.equal(root.get(Book_.CATEGORY), genre);
				// Aggiungo il predicate alla lista
				genres_predicates.add(book_genre);
			}
			// Faccio l'or tra i generi ricercati, poichè un libro ha un genere
			return cB.or(genres_predicates.toArray(new Predicate[]{}));
		}
	}
}