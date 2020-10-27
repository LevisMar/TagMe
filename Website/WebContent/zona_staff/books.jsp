<%@ page import="org.apache.taglibs.standard.tag.common.core.ChooseTag" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>Libri</title>
		<!-- Tell the browser to be responsive to screen width -->
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<%@ include file="/reference.html" %>
		<script type="text/javascript" src="/gestionelibri/js/publishers.js"> </script>
		<script type="text/javascript" src="/gestionelibri/js/book.js"> </script>
		<script type="text/javascript" src="/gestionelibri/js/author.js"> </script>
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
						<div class="col-md-10 col-md-offset-1 col-sm-10 col-sm-offset-1 col-xs-10 col-xs-offset-1 margtopx1 border_y background_g">
		    				<form action="books" name="books" method="POST">
								<div id="searchBook" class="col-md-12 col-sm-12 col-xs-12 border_b_y">
					      			<p class="line"> ricerca libro </p>
					      			<div class="col-md-2 col-sm-3 col-xs-12">
									    <select name="seltype" class="border_lg text-bold dark form-control">
										   	<option class="option" value="Title" selected>TITOLO</option>
											<option class="option" value="Barcode">BARCODE</option>
										</select>
									</div>
									<div class="col-md-8 col-sm-7 col-xs-12" id="name_auth">
								       	<input type="text" id="parameter" name="parameter" class="border_lg form-control"/>
									</div>
									<div class="col-md-2 col-sm-2 col-xs-12 text-center">
										<button type="submit" class="btn btn_wgreen_b">RICERCA</button>
									</div>
					      		</div>
					      		<div class="col-md-12 col-sm-12 col-xs-12">
						      		<div class="row border_t_y row_height">
							      		<div class="col-md-3 col-sm-4 col-xs-12 same_height_left">
							      			<p class="line"> ricerca avanzata </p>
							      			<c:choose>
												<c:when test="${not empty BookGenre}">
													<div class="col-md-12 col-sm-12 col-xs-12">
														<p class="text-bold"> GENERE: </p>
														<div class="form-group">
															<c:forEach items="${BookGenre}" var="book_genre">
												            	<div class="checkbox">
												                    <label class="checkbox">${book_genre.name}
												                      	<input type="checkbox" name="genres" value="${book_genre.name}">
  																		<span class="checkmark"></span>
												                	</label>
												            	</div>
															</c:forEach>
											            </div>
													</div>
												</c:when>
										        <c:otherwise>
										        	<div class="col-md-12 col-sm-12 col-xs-12">
														<h1 class="text-center"><span>Non sono stati trovati generi</span></h1>
										     		</div>
												</c:otherwise>
											</c:choose>
											<hr class="gold">
											<div id="auth_box" class="col-md-12 col-sm-12 col-xs-12">
								      			<label for="author">AUTORE: </label>
												<div id="name_auth_1" class="input-group input-group-green">
													<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
													<input type="text" id="search_a_1" name="author" class="form-control" size="30" maxlength="30"/>
												</div>
								      		</div>
											<hr class="gold">
								      		<div id="publ_box" class="col-md-12 col-sm-12 col-xs-12">
								      			<label for="publisher">Editore </label>
												<div class="input-group input-group-green" id="name_publ">
													<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
													<input type="text" id="search_p" name="publisher" class="form-control" size="50" maxlength="50"/>
												</div>
								      		</div>
											<hr class="gold">
								      		<div class="col-md-12 col-sm-12 col-xs-12">
								      			<label for="edition">EDIZIONE </label>
												<div class="input-group input-group-green">
													<span class="input-group-addon"><strong>N°</strong></span>
													<input type="number" name="edition" id="edition" class="form-control" min="1" max="99" size="2"/>
												</div>
								      		</div>          
							      		</div>
							      		<div class="col-md-9 col-sm-8 col-xs-12 same_height_right">
											<c:choose>
												<c:when test="${not empty Libri}">
													<c:forEach items="${Libri}" var="libro">
														<div id="book_cover" class="col-md-3 col-sm-4 col-xs-6">
															<div class="row">
																<div class="col-md-12 col-sm-12 col-xs-12">
									   								<a href="book_profile?n_book=${libro.barcode}">
									   									<img id="img_user" title="${libro.name}" src="/gestionelibri/img/books/cover/img_${libro.barcode}.png" class="img-responsive border">
									   								</a>
									   							</div>
								    						</div>
														</div>
													</c:forEach>
												</c:when>
										        <c:otherwise>
										        	<div class="col-md-12 col-sm-12 col-xs-12">
														<h1 class="text-center"><span>Non sono stati trovati libri</span></h1>
										     		</div>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</div>
							</form>
						</div>
     				</div>
				</section>
			</div>
		</div>
	</body>
</html>