<%@ page import="org.apache.taglibs.standard.tag.common.core.ChooseTag" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>Profilo utente</title>
		<!-- Tell the browser to be responsive to screen width -->
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<%@ include file="/reference.html" %>
		<script type="text/javascript" src="/gestionelibri/js/user.js"> </script>	
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
						<div class="col-md-10 col-md-offset-1 col-sm-10 col-sm-offset-1 col-xs-10 col-xs-offset-1">
							<div class="row margtopx2 border_y background_g">
								<div id="user_prof" class="col-md-12 col-sm-12 col-xs-12">
									<form action="user_profile" name="upload_file" method="POST" onSubmit="return verifica_modify();" enctype="multipart/form-data">
										<div class="row row_height padx1 border_b_y">	
											<div id="img_prof" class="col-md-3 col-sm-4 col-xs-12 same_height">
					      						<div class="col-md-12 col-md-offset-0 col-sm-12 col-sm-offset-0 col-xs-8 col-xs-offset-2">
					      							<img id="img_output" alt="immagine profilo" src="/gestionelibri/img/users/img_${Utente.id}.png" class="img-responsive center-block">
					      						</div>
					      						<div id="bnt_up" class="col-md-12 col-sm-12 col-xs-12 upload-btn-wrapper2 hidden">
													<button type="button" class="btn center-block">CARICA UN FILE</button>
													<input type="file" name="img_to_up" id="file_upload" accept="image/*" onchange="loadFile(event)"/>
												</div>
					      					</div>
					      					<div id="info" class="col-md-9 col-sm-8 col-xs-12 same_height">
					      						<div class="col-md-6 col-sm-12 col-xs-12 info_box">
					      							<label for="firstname">Nome </label>
													<div class="input-group input-group-green">
														<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
														<input type="text" name="firstname" id="firstname" class="form-control" size="50" readonly required value="${Utente.firstname}"/>
													</div>
					      						</div>
					      						<div class="col-md-6 col-sm-12 col-xs-12 info_box">
					      							<label for="lastname">Cognome </label>
													<div class="input-group input-group-green">
														<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
														<input type="text" name="lastname" id="lastname" class="form-control" size="50" readonly required value="${Utente.lastname}"/>
													</div>
					      						</div>
					      						<div class="col-md-6 col-sm-12 col-xs-12 info_box">
					      							<label for="email">E-mail </label>
													<div class="input-group input-group-green">
														<span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
														<input type="email" name="email" id="email" class="form-control" size="50" readonly required value="${Utente.email}"/>
													</div>
						      					</div>
						      					<div class="col-md-6 col-sm-12 col-xs-12 info_box">
						      						<label for="phonenumber">Cellulare <i title="formato 555-5555555">&#x1F6C8</i></label>
													<div class="input-group input-group-green">
														<span class="input-group-addon"><i class="glyphicon glyphicon-earphone"></i></span>
														<input type="tel" name="phonenumber" id="phonenumber" class="form-control" pattern="[0-9]{3}-[0-9]{7}" readonly required value="${Utente.phonenumber}"/>
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
								<div class="col-md-12 col-sm-12 col-xs-12">
									<c:choose>
										<c:when test="${not empty Utente.transactions}">
											<div class="row padx1 border_t_y">
												<h2 class="text-center">TRANSAZIONI ASSOCIATE ALL'UTENTE</h2>
											</div>									
									    	<table class="table table-hover table-bordered table-el">
										    	<thead>
										    		<tr>
														<th>N</th>
										                <th>LIBRO</th>
										                <th>TRANSAZIONE</th>
										                <th>DATA</th>
										                <th>ESEGUITO DA</th>
													</tr>
										    	</thead>
						                		<tbody>
													<c:forEach var="transaction" items="${Utente.transactions}" varStatus="loop">
														<tr>
															<td>
																<span id="trans${loop.index+1}">${loop.index+1}</span>
															</td>
											                <td class="text-center">
																<a id="transBook${loop.index+1}" href="../book/book_profile?n_book=${transaction.product.barcode}">
											   						${transaction.product.name}
											   					</a>
															</td>
											                <td>
											                	<span id="transCode${loop.index+1}">${transaction.transCode}</span>
											                </td>
											                <td>
											                	<span id="transData${loop.index+1}">
											                		<fmt:formatDate value="${transaction.data}" pattern="dd MMMM yyyy" />
											                	</span>
											                </td>
											                <td>
											                	<span id="transStaff${loop.index+1}">${transaction.staffMember.firstname} ${transaction.staffMember.lastname}</span>
											                </td>
										                </tr>
													</c:forEach>
						              			</tbody>
											</table>
										</c:when>									  
										<c:otherwise>		
											<div class="row padx1 border_t_y">
												<h2 class="text-center">NON CI SONO TRANSAZIONI ASSOCIATE ALL'UTENTE</h2>
											</div>
										</c:otherwise>							  
									</c:choose>		
								</div>
							</div>	
						</div>
     				</div>
				</section>
			</div>
		</div>
	</body>
</html>