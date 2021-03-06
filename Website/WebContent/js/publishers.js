/**
 * Funzione che imposta l'autocomplete sulla casella di ricerca dell'editore
 * 
 * @returns una lista contenente tutti i suggerimenti per l'utente
 */
$( function() 
{
	// Quando metto il focus su una delle caselle di ricerca
	$( "input[id^='search_p']" ).on("focus", function()
	{
		// Conterrà l'id del div su cui "appendere" la lista dell'autocomplete
		var appendTo = "#name_publ";
		// Prendo l'id della casella su cui è attivo il focus
		var fieldid = document.activeElement.id;
		// Imposto l'autocomplete
	   	$(this).autocomplete({
	   		// Autofocus sul primo risultato
		  	autoFocus: true,
		  	// Lunghezza minima per generare risultati
		  	minLength: 1,
		  	appendTo: appendTo,
		  	// La fonte dell'autocomplete è una funzione ajax
			source: function(request, response) 
			{
				$.ajax({
			    	type: "GET",
			        url: "search_publisher",
			        dataType: "json",
			        /* 
			        	I dati inviati sono:
			        	- term, quello che l'utente digita;
			        */
			        data: { term : request.term },
			        
			        success: function( risposta, textStatus) 
			        {
		            	response(risposta);
			        },
			        
			        error: function(textStatus, errorThrown)
			        {
			            errormsg(errorThrown);
			        }
			    });
			},
	   	});
	});
});

/**
 * Quando clicco su un riquadro editore, rindirizzo l'utente 
 * verso la pagina di profilo di quest'ultimo
 */
function search()
{
	var name = document.getElementById("search_p").value;
	if(name != "")
	{
		window.location.href = "publisher_profile?name=" + name;
	}
}

/**
 * Rende modificabili le informazioni associate all'autore
 */
function modify()
{
	// Rendo modificabili gli input
	document.getElementById('publ_name').readOnly = false;
	document.getElementById('country').remove();
	// Rendo modificabile il paese
	document.getElementById('country_sel').classList.remove("hidden");	
	// Rendo modificabile l'immagine di profilo
	document.getElementById('bnt_up').classList.remove("hidden");
}

/**
 * Esegue una verifica sui dati prima di inviarli al server
 * per l'aggiunta di un editore
 * 
 * @returns true se i dati sono validi, false altrimenti
 */
function verifica_add()
{
	return check_file();
}

/**
 * Esegue una verifica sui dati prima di inviarli al server
 * per la modifica dei dati di un editore
 * 
 * @returns true se i dati sono validi, false altrimenti
 */
function verifica_modify()
{
	// Se l'utente non ha abilitato la modifica delle informazioni
	if(document.getElementById('bnt_up').classList.contains('hidden') || document.getElementById('publ_name').readOnly === true)
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