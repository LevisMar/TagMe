<%@ page import="org.apache.taglibs.standard.tag.common.core.ChooseTag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>Aggiungi libro</title>
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
		    		<form action="add_book" method="POST" enctype="multipart/form-data" onSubmit="return verifica();">
		      		<!-- Main row -->
		      			<div class="row">
		      				<div id="book_reg" class="col-md-10 col-md-offset-1 col-xs-10 col-xs-offset-1 border_y background_g margtopx1">
		      					<div class="col-md-12 border_b_y">
		      						<div class="row">
			      						<div id="book_cover" class="col-md-4 col-xs-12">
			      							<div class="col-md-10 col-md-offset-1 col-xs-8 col-xs-offset-2">
			      								<img id="img_output" alt="immagine profilo" src="/gestionelibri/img/books/cover/default.png" class="img-responsive">
			      							</div>
			      							<div class="col-md-12 col-xs-12 upload-btn-wrapper2">
												<button type="button" class="btn center-block">AGGIUNGI COPERTINA</button>
												<input type="file" name="img_to_up" accept="image/*" onchange="loadFile(event)"/>
											</div>
			      						</div>
			      						<div id="info" class="col-md-8 col-xs-12">
			      							<div class="col-md-9 col-xs-12 info_box">
			      								<label for="bookname">Nome </label>
												<div class="input-group input-group-green">
													<span class="input-group-addon"><i class="fa fa-book"></i></span>
													<input type="text" name="bookname" id="bookname" class="form-control" size="70" maxlength="70" required/>
												</div>
			      							</div>
			      							<div class="col-md-12 col-xs-12 info_box">
			      								<div class="form-group">
													<label for="description">Descrizione</label>
												    <textarea name="description" class="form-control border_g noresize" rows="3" maxlength="500"></textarea>
												</div>
			      							</div>
			      							<div class="col-md-6 col-xs-12 info_box">
			      								<label for="category">Categoria </label>
												<div class="input-group input-group-green">
													<span class="input-group-addon"><i class="glyphicon glyphicon-flag"></i></span>
													<select class="form-control" name="category_sel">
														<c:choose>
															<c:when test="${not empty Categories}">
																<c:forEach items="${Categories}" var="category">
																	<option class="option">${category.name}</option>
																</c:forEach>
															</c:when>
								        					<c:otherwise>
													        	<option class="option">GENERALE</option>
															</c:otherwise>
														</c:choose>
													</select>
												</div>
				      						</div>
				      						<div class="col-md-3 col-xs-6 info_box">
				      							<label for="edition">Edizione </label>
												<div class="input-group input-group-green">
													<span class="input-group-addon"><strong>N°</strong></span>
													<input type="number" name="edition" class="form-control" value="1" min="1" max="99" required/>
												</div>
				      						</div>
				      						<div class="col-md-3 col-xs-6 info_box">
				      							<label for="quantity">Quantità </label>
												<div class="input-group input-group-green">
													<span class="input-group-addon"><strong>N°</strong></span>
													<input type="number" name="quantity" class="form-control" value="1" min="0" max="99" required/>
												</div>
				      						</div>
				      						<div id="publ_box" class="col-md-7 col-xs-12 info_box">
			      								<label for="publisher">Editore </label>
												<div class="input-group input-group-green" id="name_publ">
													<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
													<input type="text" id="search_p" name="publisher" class="form-control" size="50" maxlength="50" required/>
												</div>
			      							</div>
			      							<div class="col-md-5 col-xs-12 info_box">
				      							<label for="barcode">Barcode </label>
												<div class="input-group input-group-green">
													<span class="input-group-addon"><i class="glyphicon glyphicon-barcode"></i></span>
													<input type="text" name="barcode" class="form-control" size="13" maxlength="13" required/>
												</div>
				      						</div>
			      							<div class="col-md-12 col-xs-12 info_box">
			      								<div class="row">
													<div class="col-md-3 col-md-offset-0 col-xs-6 col-xs-offset-3">
														<label for="n_auth">N° autori </label>
														<div class="input-group input-group-green">
											            	<input type="text" id="n_auth" name="n_auth" class="form-control" size="1" maxlength="1"/>
											                <span class="input-group-btn">
											                	<button type="button" class="btn btn_green" OnClick="aggiungi_campi();">
											                		CREA
											                	</button>
											                </span>
											            </div>
													</div>
				      								<div id="auth_box" class="col-md-9 col-xs-12">
					      								<label>Autori </label>
														<div class="input-group input-group-green" id="name_auth_1">
															<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
															<input type="text" id="search_a_1" name="author_1" class="form-control" size="30" maxlength="30" required/>
														</div>
													</div>
												</div>
			      							</div>
			      						</div>
		      						</div>
		      					</div>
		      					<div class="col-md-12 col-xs-12 border_t_y">
			      					<div class="row margtopx1">
			      						<!-- <div class="col-md-6 col-xs-6">
			      							<button type="reset" class="btn btn-md btn_red center-block">CANCELLA</button>
			      						</div> -->
			      						<div class="col-md-6 col-xs-6">
			      							<button type="button" class="btn btn-md btn_red center-block" OnClick="getBookDetails();">CERCA</button>
			      						</div>
			      						<div class="col-md-6 col-xs-6">
			      							<button type="submit" class="btn btn-md btn_wgreen_b center-block">CONFERMA</button>
			      						</div>
			      					</div>
		      					</div>
		      				</div>
		      			</div>
		      		</form>
				</section>
			</div>
		</div>
	</body>
</html>