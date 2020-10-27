package utility;

/**
 * @author Leonardo
 *
 * Classe utilizzata per definire il risultato della registrazione,
 * all'interno del DB, delle seguenti entità:
 * <ul>
 * 	<li>StaffMember</li>
 *  <li>BookUser</li>
 *  <li>Author</li>
 *  <li>Publisher</li>
 * </ul>
 */
public class StatoRegistrazione
{
	// Definisce anche i possibili errori che si possono trovare durante la registrazione
	public enum Stato {ok,phonenumber,email,username,error,exist};
	// Conterrà il codice da restituire al client
	public Stato code;	
	// Contiene il messaggio di errore/successo da restituire al client
	public String message;

	/**
	 * Crea un'istanza contenente il risultato della registrazione
	 * di una delle Entità possibili
	 * 
	 * @param code di errore/successo
	 * @param message da restituire all'utente
	 */
	public StatoRegistrazione(Stato code, String message) 
	{
		super();
		this.code = code;
		this.message = message;
	}		
}