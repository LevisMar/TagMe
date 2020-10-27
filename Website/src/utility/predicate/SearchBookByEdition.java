package utility.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import model.pojo.Book;

/**
 * @author Leonardo
 * 
 * Classe che definisce un Predicate per la
 * ricerca di uno o più libri utilizzando l'edizione
 */
public class SearchBookByEdition implements SearchPredicateI<Book>
{
	// Edizione associata al libro
	int edition;
	
	/**
	 * Costruttore con parametro essenziale
	 * @param edition del libro da cercare
	 */
	public SearchBookByEdition(int edition) 
	{
		super();
		this.edition = edition;
	}
	
	@Override
	public Predicate getPredicate(CriteriaBuilder cB, Root<Book> root) 
	{
		// Genero un predicate per cercare libri che sono dell'edizione specificata
		Predicate book_edition = cB.equal(root.get("edition"), edition);
		return book_edition;
	}
}