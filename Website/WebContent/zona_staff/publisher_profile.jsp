<%@ page import="org.apache.taglibs.standard.tag.common.core.ChooseTag" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>Profilo editore</title>
		<!-- Tell the browser to be responsive to screen width -->
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<%@ include file="/reference.html" %>
		<script type="text/javascript" src="/GestioneLibri/js/publishers.js"> </script>	
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
								<div id="publ_prof" class="col-md-12 col-md-12 col-xs-12">
									<form action="publisher_profile" name="upload_file" method="POST" onSubmit="return verifica_modify();" enctype="multipart/form-data">
										<div class="row row_height border_b_y">	
											<div id="img_prof" class="col-md-3 col-sm-4 col-xs-12 same_height">
			      								<div class="col-md-12 col-md-offset-0 col-sm-12 col-sm-offset-0 col-xs-8 col-xs-offset-2">
			      									<img id="img_output" alt="immagine profilo" src="/gestionelibri/img/publishers/img_${Editore.id}.png" class="img-responsive">
			      								</div>
			      								<div id="bnt_up" class="col-md-12 col-sm-12 col-xs-12 upload-btn-wrapper2 hidden">
													<button type="button" class="btn center-block">CARICA UN FILE</button>
													<input type="file" name="img_to_up" id="file_upload" accept="image/*" onchange="loadFile(event)"/>
												</div>
		      								</div>
											<div id="info" class="col-md-9 col-sm-8 col-xs-12 same_height">
				      							<div class="col-md-8 col-sm-12 col-xs-12 info_box">
							      					<div class="form-group">
														<label for="publ_name">Nome </label>
														<div class="input-group input-group-green">
															<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
															<input type="text" name="publ_name" id="publ_name" class="form-control" size="50" readonly required value="${Editore.name}"/>
														</div>
													</div>
							      				</div>
							      				<div class="col-md-8 col-sm-12 col-xs-12 info_box" id="info_box">
												    <div class="form-group">
														<label for="country">Paese </label>
														<div class="input-group input-group-green">
															<span class="input-group-addon"><i class="glyphicon glyphicon-flag"></i></span>
															<input type="text" name="country" id="country" class="form-control" size="50" readonly required value="${Editore.country}"/>
															<select class="form-control hidden" id="country_sel" name="country_sel">
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
												</div>
							      				<div id="submit_btn" class="col-md-12 col-sm-10 col-xs-12 margtopx1">
									      			<div class="col-md-6 col-sm-6 col-xs-12 margbottomx1">
									      				<button type="button" class="btn btn_red center-block" onclick="modify()">MODIFICA DATI</button>
									      			</div>
									      			<div class="col-md-6 col-sm-6 col-xs-12 margbottomx1">
									      				<button type="submit" class="btn btn_wgreen_b center-block">CONFERMA MODIFICA</button>
									      			</div>
									      		</div>
							      			</div>
										</div>
									</form>
								</div>
								<div class="col-md-12 col-md-12 col-xs-12">
									<div class="row border_t_y">
										<c:choose>
				  							<c:when test="${not empty Editore.books}">
											    <h3 class="text-center">LIBRI DI ${Editore.name}</h3>
											    <c:forEach items="${Editore.books}" var="book">
													<div id="book" class="col-md-12 col-sm-4 col-xs-6">
														<div class="col-md-2">
															<a href="../book/book_profile?n_book=${book.barcode}">
						      									<img title="${book.name}" src="/gestionelibri/img/books/cover/img_${book.barcode}.png" class="img-responsive border">
						      								</a>	
						      							</div>
						      							<div id="book_info" class="col-md-10">
						      								<div class="col-md-12 book_name">
						      									<span>
						      										<a href="../book/book_profile?n_book=${book.barcode}">
						      											${book.name} (${book.edition}° Edizione)
						      										</a>
						      									</span>
						      								</div>
						      								<div class="col-md-12 book_detail">
							      								<span> 
							      									di
							      									<c:forEach items="${book.authors}" var="author">
							      										<a href="../author/author_profile?name=${author.name}"> ${author.name} </a>
																	</c:forEach>
							      								</span>
						      								</div>
						      								<div class="col-md-12 book_detail">
						      									<span>
						      										Editore:
						      										<a href="publisher_profile?name=${book.publisher.name}"> ${book.publisher.name} </a>
						      									</span>
						      								</div>
						      								<div class="col-md-12 book_detail">
						      									<span>
						      										Categoria: ${book.category.name}
						      									</span>
						      								</div>
						      								<div class="col-md-12 book_detail">
						      									<span>
						      										ISBN: ${book.barcode}
						      									</span>
						      								</div>
						      							</div>
													</div>
												</c:forEach>
											</c:when>  
											<c:otherwise>
												<h3 class="text-center">AL MOMENTO L'EDITORE NON HA ANCORA LIBRI ASSOCIATI</h3>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
						</div>
     				</div>
				</section>
			</div>
		</div>
	</body>
</html>