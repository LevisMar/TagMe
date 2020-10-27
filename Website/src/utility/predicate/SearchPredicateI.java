package utility.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Leonardo
 * 
 * Interfaccia Java che definisce un metodo di base 
 * utile, per generare un predicato per la ricerca
 * all'interno del database.
 * I Predicati specifici che utilizzeranno la classe
 * dovranno fornire un metodo per creare il predicato.
 * 
 * @param <T> classe Java su cui generare il predicato
 */
public interface SearchPredicateI<T> 
{
	/**
	 * Crea il Predicate per la ricerca tramite CriteriaQuery
	 *  
	 * @param cB costruttore della query criteria
	 * @param root corrisponde al from di SQL
	 * @return un Predicate da usare per la CriteriaQuery
	 */
	public Predicate getPredicate(CriteriaBuilder cB, Root<T> root);	
}
