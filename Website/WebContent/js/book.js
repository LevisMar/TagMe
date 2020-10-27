// Numero di volte che l'utente genera le caselle degli autori
var volte = 0;

function getBookDetails() 
{

	  alert("prova");

}
		
// Genera le caselle membri desiderati
function aggiungi_campi() 
{
	// Contiene il numero di campi da generare
	var n_campi = document.getElementById("n_auth").value;
	//alert(n_campi!="");
	// Contiene la struttura html da generare
	var elenco = "<label>Autori </label>";				
	// Controllo il numero di volte che l'utente genera le caselle
	if(volte>1)
	{
		errormsg("Non è possibile aggiungere i campi due volte, eventualmente aggiornare la pagina");
	}
	else
	{
		if(n_campi!="") 
		{
			volte++;
			for(I=1; I<=n_campi; I++) 
			{
				//contiene i campi autori
				elenco += '<div class="input-group input-group-green" id="name_auth_' + I + '">' +
								'<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>' +
								'<input type="text" id="search_a_' + I + '" name="author_' + I + '" class="form-control" size="30" required/>' +
						  '</div>';
			}
			// Il metodo per inserire la struttura nella pagina può cambiare, a seconda del browser
			if(document.all) 
			{
				document.all('auth_box').innerHTML=elenco;
			}
			else if(document.getElementById) 
			{
				document.getElementById("auth_box").innerHTML = elenco;
			}
			else 
			{
				alert("il tuo browser non supporta questo metodo");
			}
		}
		else
		{
			errormsg("Aggiungi il numero di autori");
			volte = 0;
		}
	}
}

//Esegue una verifica sui dati prima di inviarli al server
function verifica()
{
//	//prendo il file caricato
//	var fileInput = document.getElementById('fileimm');
//	//controllo che il file non superi la dimensione massima
//	if(fileInput.files[0].size > 10000 * 1024){
//		errormsg("Le dimensioni del file sono troppo grandi! sono accettati file di dimensione < 10 MB");
//		return false;			
//	}
//	//controllo che il file sia del formato giusto
//	var file = fileInput.files[0];
//	var fileType = file["type"];
//	var ValidImageTypes = ["image/gif", "image/jpeg", "image/png"];
//	if ($.inArray(fileType, ValidImageTypes) < 0) {
//		errormsg("I formati validi sono: gif, jpeg, png");
//		return false;
//	}
	var barcode = document.getElementsByName('barcode')[0].value;
	if( barcode.length < 13 || barcode.length > 13)
	{
		errormsg("Il barcode deve avere una lunghezza pari a 13");
		return false;
	}
	if(document.getElementById('book_name').readOnly === true)
	{
		errormsg("Non hai modificato nessuna informazione. Clicca sul pulsante MODIFICA");
		return false;
	}
	else
	{
		return true;
	}
}

//Rende modificabili le informazioni associate al libro
function modify_b()
{
	// Rendo modificabili gli input
	document.getElementById('bookname').readOnly = false;
	document.getElementById('description').readOnly = false;
	
	document.getElementById('category').remove();
	// Rendo modificabile la categoria
	document.getElementById('category_sel').classList.remove("hidden");	
	
	document.getElementById('edition').readOnly = false;
	document.getElementById('quantity').readOnly = false;
	document.getElementById('search_p').readOnly = false;
	document.getElementById('b_barcode').readOnly = false;
	
	// Devo rendere modificabili le giuste caselle di autori
	var n = document.getElementById('n_auth').value;
	for(var i=1; i<=n; i++)
	{
		document.getElementById('search_a_' + i).readOnly = false;
	}
	document.getElementById('n_auth').readOnly = false;
	// Rendo modificabile l'immagine di profilo
	document.getElementById('bnt_up').classList.remove("hidden");
	// Rendo possibile aggiungere altre caselle per gli autori
	document.getElementById('btn_add').classList.remove("hidden");
}

function submit_img(input)
{
	//alert(input.value);
	//input.value = "";
	
	/* DEVO FARE I CONTROLLI PER VEDERE SE L'IMMAGINE VA BENE */
	// Prendo il file caricato
	var fileInput = input;
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
	
	var x = document.getElementsByName('upload_file');
	x[0].submit();
}

//Carica l'anteprima di un file prima che venga fatto l'upload
var loadFile = function(event) 
{
    var output = document.getElementById('img_output');
    output.src = URL.createObjectURL(event.target.files[0]);
};