<%@ page import="org.apache.taglibs.standard.tag.common.core.ChooseTag" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>Editori</title>
		<!-- Tell the browser to be responsive to screen width -->
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<%@ include file="/reference.html" %>
		<script type="text/javascript" src="/gestionelibri/js/publishers.js"> </script>	
	</head>
	
	<c:choose>				  
		<c:when test="${not empty errormsg}">
		    <body class="hold-transition skin-blue sidebar-mini" onload="errormsg('${errormsg}')">
		</c:when>		
		<c:when test="${not empty okmsg}">
		    <body class="hold-transition skin-blue sidebar-mini" onload="okmsg('${okmsg}')">
		</c:when>		  
		<c:otherwise>
			<body class="hold-transition skin-blue sidebar-mini">
		</c:otherwise>
	</c:choose>
		<div class="wrapper">
			<jsp:include page="header.jsp"/>
			<!-- Content Wrapper. Contains page content -->
	  		<div class="content-wrapper">
				<!-- Main content -->
	    		<section class="content">
	    			<div class="row">
						<div class="col-md-10 col-md-offset-1 col-xs-10 col-xs-offset-1 margtopx1">
							<div class="row border_y background_g">	
								<div id="searchBar" class="col-md-8 col-xs-12">
				      				<p class="line"> ricerca editore </p>
									<div class="col-md-9 col-sm-9 col-xs-8" id="name_publ">
							           	<input type="text" id="search_p" name="search_p" class="form-control"/>
									</div>
									<div class="col-md-2 col-sm-2 col-xs-2 text-center">
										<button class="btn btn_wgreen_b" type="button" onclick="search()">RICERCA</button>
									</div>
				      			</div>
				      			<div id="addEntity" class="col-md-4 col-xs-12">
				      				<button type="button" class="btn btn-lg btn_blue center-block" data-toggle="modal" data-target="#modal_add">
  										AGGIUNGI EDITORE
									</button>
				      			</div>
				      		</div>
				      		<div class="row margtopx2 border_y background_g">
								<c:choose>
									<c:when test="${not empty Editori}">
										<c:forEach items="${Editori}" var="editore">
											<div id="entity_box" class="col-md-2 col-sm-3 col-xs-6">
												<div class="row">
													<div class="col-md-12 col-sm-12 col-xs-12">
														<a href="publisher_profile?name=${editore.name}">
					      									<img id="img_user" alt="immagine profilo" src="/gestionelibri/img/publishers/img_${editore.id}.png" class="img-responsive border_g_s">
					      									<span class="col-md-12 col-sm-12 col-xs-12 text-center background_grey border_g_s text-bold dark">${editore.name}</span> 
						    							</a>
				      								</div>
				      							</div>
											</div>
										</c:forEach>
									</c:when>							        
							        <c:otherwise>
							        	<div class="col-md-12 col-sm-12 col-xs-12">
											<h1 class="text-center"><span>Non sono stati trovati editori</span></h1>
							   			</div>
									</c:otherwise>
								</c:choose>
							</div>	
						</div>
     				</div>
				</section>
			</div>
		</div>
		
		<!-- Modal di aggiunta autore -->
		<div class="modal fade" id="modal_add" role="dialog">
			<form action="publishers" method="POST" onSubmit="return verifica_add();" enctype="multipart/form-data">
				<div class="modal-dialog modal-md">
					<div class="modal-content">
						<div class="modal-header riquadro_b text-center">
							<button type="button" class="close" data-dismiss="modal">  &times;  </button>
							<h3 class="modal-title coursive white text-bold">AGGIUNGI EDITORE</h3>
						</div>
						<div class="modal-body">
							<div class="panel-body">
								<div class="col-md-12 col-sm-12 col-xs-12">
									<div id="img_prof" class="col-md-4 col-sm-4 col-xs-12 same_height">
		      							<div class="col-md-12 col-sm-12 col-xs-12">
		      								<img id="img_output" alt="immagine profilo" src="/gestionelibri/img/publishers/default.png" class="img-responsive">
		      							</div>
		      							<div class="col-md-12 col-sm-12 col-xs-12 upload-btn-wrapper2">
										  	<button type="button" class="btn btn-sm center-block">CARICA UN FILE</button>
										  	<input type="file" name="img_to_up" id="file_upload" accept="image/*" onchange="loadFile(event)"/>
										</div>
		      						</div>
					      			<div id="info" class="col-md-8 col-sm-8 col-xs-12 same_height">
					      				<div class="col-md-12 info_box">
					      					<label for="name">Nome </label>
											<div class="input-group input-group-green">
												<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
												<input type="text" name="name" class="form-control" size="50" maxlength="50"/>
											</div>
					      				</div>
					      				<div class="col-md-12 info_box">
											<div class="form-group">
										    	<label for="biography">Paese</label>
										    	<div class="input-group input-group-green">
													<span class="input-group-addon"><i class="glyphicon glyphicon-flag"></i></span>
													<select class="form-control" name="country">
										    			<c:choose>
															<c:when test="${not empty Countries}">
																<c:forEach items="${Countries}" var="country">
																	<option class="option">${country.getISO3Country()} - ${country.getDisplayCountry()}</option>
																</c:forEach>
															</c:when>
															<c:otherwise>
											        			<option class="option">La lista dei paesi non è stata trovata</option>
															</c:otherwise>
														</c:choose>
													</select>
												</div>
											</div>				      				
								      		<div class="col-md-12 col-sm-12 col-xs-12">
									      		<button type="submit" class="btn btn_wgreen_b btn-md btn-outline dark center-block">
									           		CONFERMA
									           	</button>
								      		</div>
										</div>
					      			</div>	
				      			</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>		
	</body>
</html>