// Crea un modal dialog con un messaggio di errore
function errormsg(message)
{
	// Cerco il dialog
	var modal = document.getElementById("modal-danger");
	// Se non è presente genero il dialog
	if(modal == null)
	{
		// Creo il dialog/modal che mostra il messaggio di errore
		modal = '<div class="modal modal-danger fade" id="modal-danger">' +
						'<div class="modal-dialog modal-sm">' + 
							'<div class="modal-content">' + 
								'<div class="modal-header">' + 
									'<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
										'<span aria-hidden="true">&times;</span>' +
									'</button>' +
									'<h4 class="modal-title text-center">ATTENZIONE!!</h4>' +
								'</div>' +
								'<div class="modal-body" id="modal-body">' +
									'<p id="modal-body-msg" class="text-center">' + message + '</p>' +
								'</div>' +
							'</div>' +
						'</div>' +
					'</div>';
		
		// Aggiungo il dialog alla pagina
		$('body').append(modal);
		// Mostro il dialog
		$('#modal-danger').modal('show');
	}
	// Se è già presente il dialog
	else
	{
		// Inserisco il nuovo messaggio
		document.getElementById("modal-body-msg").innerHTML = message;
		// Mostro il dialog
		$('#modal-danger').modal('show');
	}
}

// Crea un modal dialog con un messaggio di successo
function okmsg(message)
{
	// Cerco il dialog
	var modal = document.getElementById("modal-success");
	// Se non è presente genero il dialog
	if(modal == null)
	{
		// Creo il dialog/modal che mostra il messaggio di errore
		modal = '<div class="modal modal-success fade" id="modal-success">' +
						'<div class="modal-dialog modal-sm">' + 
							'<div class="modal-content">' + 
								'<div class="modal-header">' + 
									'<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
										'<span aria-hidden="true">&times;</span>' +
									'</button>' +
									'<h4 class="modal-title text-center">SUCCESSO!!</h4>' +
								'</div>' +
								'<div class="modal-body" id="modal-body">' +
									'<p id="modal-body-msg" class="text-center">' + message + '</p>' +
								'</div>' +
							'</div>' +
						'</div>' +
					'</div>';
		
		// Aggiungo il dialog alla pagina
		$('body').append(modal);
		// Mostro il dialog
		$('#modal-success').modal('show');
	}
	// Se è già presente il dialog
	else
	{
		// Inserisco il nuovo messaggio
		document.getElementById("modal-body-msg").innerHTML = message;
		// Mostro il dialog
		$('#modal-success').modal('show');
	}
}