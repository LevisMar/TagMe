package utility;

import java.util.Date;
import javax.persistence.Persistence;

/**
 * @author Leonardo
 * 
 * La seguente classe ha il compito di creare e fornire l'unica istanza di tipo EntityManagerFactory,
 * da condividere in tutta l'applicazione.
 * Il design pattern applicato è 'SINGLETON'!
 * 
 * I motivi dell'applicazione di questo specifico pattern sono i seguenti:
 * -il processo di creazione dell'entityManagerFactory risulta essere un'operazione onerosa,  
 * -le specifiche definiscono tale istanza un thread-safe object,
 * -nel file 'persistence.xml', è definita una sola unità di persistenza, quindi un solo database a cui collegarsi.
 */
 
public class EntityManagerFactory 
{
	private static javax.persistence.EntityManagerFactory emf = null;
	
	private EntityManagerFactory(){}
	
	/**
	 * Restituisce, se presente, l'istanza creata precentemente dell'Entity
	 * Manager Factory, altrimenti tenta di crearla
	 * 
	 * @return EntityManagerFactory
	 */
	public static javax.persistence.EntityManagerFactory getIstance()
	{
		// Se l'istanza non è stata ancora creata
		if(emf==null)
		{
			// Provo a crearla
			try
			{
				System.out.println("Genero l'Entity Manager Factory in data: " + new Date());
				emf  = (javax.persistence.EntityManagerFactory) Persistence.createEntityManagerFactory("percistenceUnit");
			}
			catch(Exception ex)
			{
				System.out.println("Errore nella creazione dell'Entity Manager Factory. Verificare connessione database." + ex );
				return null;
			}
		}
		return emf;
	}
}