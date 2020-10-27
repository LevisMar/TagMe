/**
 * Funzione che ricerca le info di un utente in base ai parametri di ricerca
 * 
 * @param type indica il tipo di ricerca
 */
function search(type) 
{
	//alert(type.tagName);
	// Vuol dire che sto cercando tramite una user_box
	if(type.nodeName === 'SPAN')
	{
		// Indica il tipo di ricerca
		var par_1 = "user_box";
		var par_2 = type.getAttribute("id");
		// In questo modo prendo lo span (col nome) che c'è dentro lo span
		var par_3 = type.getElementsByTagName("span")[0].innerHTML;
		//alert(par_1 + " - " + par_3)
	}
	// Vuol dire che sto cercando tramite la search_box
	if(type.nodeName === 'BUTTON')
	{
		// Indica il tipo di ricerca
		var par_1 = "search_box";
		// Prendo la select a cui corrisponde il tipo di ricerca
		var select = document.getElementsByName("seltype")[0];
		// Prendo l'opzione selezionato dalla select
		var par_2 = select.options[select.selectedIndex].text;
		// Prendo il parametro inserito dall'utente
		var par_3 = document.getElementById("search_u").value;
		//alert(par_1 + " - " + par_2)
	}
	// Se non è vuoto
	if(par_1 != "" || par_2 != "" || par_3 != "")
	{
		// Invio una richiesta Ajax al server per cercare l'utente
		$.ajax({
	        type: 'POST',
	        url: "search_user", 
	        data: { type: par_1, par_2 : par_2, par_3 : par_3 },
	        dataType: 'json',
	        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
	        success: function(jsonObj) 
	        {
	        	// In caso di successo riempio i relativi campi della pagina 
	        	if(jsonObj.tag == "okmsg")
	        	{
		        	window.location.href = jsonObj.msg;
	        	}
	        	// Altrimenti stampo messaggio di errore
	        	if(jsonObj.tag == "errormsg")
	        	{
	        		errormsg(jsonObj.msg);
	        	}
	        },
	        error: function(jqXHR, textStatus, errorThrown) 
	        {
	        	alert("E' evvenuto un errore. Stato della chiamata: "+ textStatus);    
	        }
	    });
	}
	// Se non è stato inserito un parametro di ricerca
	else
	{
		errormsg("Riempi il campo per la ricerca");
	}
}

/**
 * Rende modificabili le informazioni associate all'utente
 */
function modify()
{
	// Rendo modificabili gli input
	document.getElementById('firstname').readOnly = false;
	document.getElementById('lastname').readOnly = false;
	document.getElementById('email').readOnly = false;
	document.getElementById('phonenumber').readOnly = false;
	// Rendo modificabile l'immagine di profilo
	document.getElementById('bnt_up').classList.remove("hidden");
}

/**
 * Esegue una verifica sui dati prima di inviarli al server
 * per l'aggiunta di un utente
 * 
 * @returns true se i dati sono validi, false altrimenti
 */
function verifica_add()
{
	return check_file();
}

/**
 * Esegue una verifica sui dati prima di inviarli al server
 * per la modifica dei dati di un utente
 * 
 * @returns true se i dati sono validi, false altrimenti
 */
function verifica_modify()
{
	// Se l'utente non ha abilitato la modifica delle informazioni
	if( document.getElementById('bnt_up').classList.contains('hidden') || document.getElementById('firstname').readOnly === true)
	{
		errormsg("Non hai modificato nessuna informazione. Clicca sul pulsante MODIFICA");
		return false;
	}
	else
	{
		return check_file();
	}	
}

/**
 * Metodo che controlla il file di cui l'utente
 * vuole eseguire l'upload, per l'immagine di profilo
 * 
 * @returns true se è valido, false altrimenti
 */
function check_file()
{
	// Prendo il file caricato
	var fileInput = document.getElementById("file_upload");
	// Controllo che il file non superi la dimensione massima
	if(fileInput.files[0].size > 10000 * 1024)
	{
		errormsg("Le dimensioni del file sono troppo grandi! sono accettati file di dimensione < 10 MB");
		return false;			
	}
	// Controllo che il file sia del formato giusto
	var file = fileInput.files[0];
	var fileType = file["type"];
	var ValidImageTypes = ["image/gif", "image/jpeg", "image/png"];
	if($.inArray(fileType, ValidImageTypes) < 0) 
	{
		errormsg("I formati validi sono: gif, jpeg, png");
		return false;
	}
	return true;
}

/**
 * Carica l'anteprima di un file prima che venga fatto l'upload
 */
var loadFile = function(event) 
{
    var output = document.getElementById('img_output');
    output.src = URL.createObjectURL(event.target.files[0]);
};