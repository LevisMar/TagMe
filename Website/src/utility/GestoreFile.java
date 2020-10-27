package utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.servlet.http.Part;

/**
 * @author Leonardo
 * 
 * Questo gestore ha il compito di effetture varie operazioni 
 * che prevedono interazioni con il file system.
 */
public class GestoreFile 
{
	
	/**
	 * Un file viene salvato per convenzione con un nome 
	 * in maiuscolo e senza spazi vuoti
	 * @param fileName da esaminare
	 */
	public String pulisciFileName(String fileName)
	{
		String cleanedFileName = fileName;
		// Lista di caratteri speciali non consentiti
		String[] blackList = {"\\","/",":","?","*","?","\"","<",">","|"," "};
		// Per ciascun carattere presente nella lista
		for(String s: blackList)
		{
			// Se è presente nella lista lo rimpiazzo
			cleanedFileName = cleanedFileName.replace(s, "-");
		}
		return cleanedFileName;
	}
	
	/**
	 * Cerca un file a partire da una determinata directory,
	 * acquisendo in input un nome di file.
	 * Restituisce il primo file riscontato.
	 * 
	 * @param percorso - directory su cui ricercare elementi
	 * @param nomeFileTarget - parte del nome del file
	 * @return nome del file completo
	 */
	public File ricercaFile(String percorso, String nomeFileTarget)
	{
		File elementoDirectory = new File(percorso);
		// GESTIONE CASI BASE
		if(!elementoDirectory.exists())
		{
			return null;
		}
		if(elementoDirectory.isFile())
		{
			String fileName = elementoDirectory.getName();
			if(fileName.contains(nomeFileTarget))
			{
				return elementoDirectory;
			}
			else
			{
				return null;
			}
		}
		// FINE GESTIONE CASI BASE
		// L'elemento di directory costituisce una sotto-directory
		File sottoDirectory = elementoDirectory;
		for(File elemento: sottoDirectory.listFiles())
		{
			// RICORSIONE
			File result = ricercaFile(elemento.getAbsolutePath(),nomeFileTarget);
			if(result!=null)
			{
				return result;
			}
		}
		return null;
	}
	
	/**
	 * Cerca un file acquisendo in input dei filtri, partendo da una directory radice.
	 * Restituisce una lista dei pathname relativa a tutti file riscontrati.
	 * 
	 * @param percorso - percorso assoluto
	 * @param filtri - stringhe che devono essere contenute nel nome del file
	 * @return lista dei pathname dei file trovati
	 */
	public void ricercaCompletaFile(String percorso, List<String> filtri, List<File> risultati)
	{
		File elementoDirectory = new File(percorso);
		// GESTIONE CASI BASE
		if(!elementoDirectory.exists())
		{
			return;
		}
		if(elementoDirectory.isFile())
		{
			String fileName = elementoDirectory.getName();
			boolean trovato= true;
			for(String filtro:filtri)
			{
				if(!fileName.contains(filtro))
				{
					trovato = false;
					break;
				}
			}
			if(trovato)
			{
				risultati.add(elementoDirectory);
			}
			return;
		}
		// FINE GESTIONE CASI BASE		
		// L'elemento di directory costituisce una sotto-directory
		File sottoDirectory = elementoDirectory;
		for(File elemento: sottoDirectory.listFiles())
		{
			ricercaCompletaFile(elemento.getAbsolutePath(),filtri,risultati);
		}
	}
	
	/**
	 * Cancella un determinato file all'interno del sistema
	 * @param path - percorso di ricerca
	 * @param fileNameTarget - nome del file da cancellare
	 * @return
	 */
	public void deleteFiles(String path, String fileNameTarget)
	{
		File elementoDirectory = new File(path);
		// GESTIONE CASI BASE
		if(!elementoDirectory.exists())
		{
			return;
		}
		if(elementoDirectory.isFile())
		{
			String fileName = pulisciFileName(elementoDirectory.getName());
			if(fileName.contains(fileNameTarget))
			{
				elementoDirectory.delete();
			}
			return;
		}
		// FINE GESTIONE CASI BASE
		// L'elemento di directory costituisce una sotto-directory
		File sottoDirectory = elementoDirectory;
		for(File elemento: sottoDirectory.listFiles())
		{
			deleteFiles(elemento.getAbsolutePath(),fileNameTarget);
		}
	}

	/**
	 * Crea una nuova cartella se non già presente.
	 * 
	 * @param path in cui creare la cartella
	 * @param name da associare alla cartella
	 * @return
	 */
	public boolean creaNuovaCartella(String path, String name)
	{
		File folder = new File(path);
		String newPath = folder+"\\"+name;
		File newDirectory = new File(newPath);
		if(!newDirectory.exists())
		{
			newDirectory.mkdirs();
			return true;
		}
		return false;
	}
	
	/**
	 * Crea un file immagine di default per l'istanza che si sta
	 * registrando nel DB (BookUser, Author, Publisher, Book)
	 * 
	 * @param save_path percorso in cui salvare il file
	 * @param id che identifica l'entità registrata
	 * @return
	 */
	public boolean createDefaultFile(String save_path, String id)
	{
		String pathFolder = getWorkpath() + save_path;
		try 
		{
			// Immagine di default generale
			Path FROM = Paths.get(pathFolder + "default.png");
			// Immagine di default specifica dell'entità
			Path TO = Paths.get(pathFolder + id +".png");
			if(!TO.toFile().exists())
			{
				Files.copy(FROM,TO);
			}
			else
			{
				// Immagine già presente
				return false;
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Impossibile creare immagine di defeault.");
			e.printStackTrace();
		}

		return true;
	}
	
	/**
	 * Tenta l'upload di un file immagine all'interno del sito
	 * 
	 * @param save_path parte essenziale del percorso in cui viene
	 * 		  salvata l'immagine
	 * @param id che identifica a chi sto aggiornando l'immagine
	 * @param file da caricare
	 * 
	 * @return true se il file viene caricato, false altrimenti
	 */
	public boolean uploadFile(String save_path, String id, Part file)
	{
		// Percorso in cui salvare l'immagine
		String pathFolder = getWorkpath() + save_path;
		try 
		{
			// Percorso in cui andrà salvata l'immagine
			Path TO = Paths.get(pathFolder + id +".png");
			System.out.println("ATTENZIONE PERCORSO SCRITTURA: " + TO.toString());
		    File f = new File(TO.toString());
		    // Ottengo il file da caricare
		    InputStream input = file.getInputStream();
		    // Lo copio nel percorso specificato, rimpiazzandolo il precedente
		    Files.copy(input, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    return true;
		} 
		catch (IOException e) 
		{
			System.out.println("Impossibile creare l'immagine");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return percorso assoluto della cartella WebContent
	 */
	public String getWorkpath()
	{
		String url = getClass().getResource("").toString();
		url = url.replace("file:/", "").replace("%20", " ");
		url = url.substring(0,url.indexOf("Progetti"));
		url += "Progetti/GestioneLibri/WebContent/";
		return url;
	}
}