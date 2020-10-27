<%@ page import="org.apache.taglibs.standard.tag.common.core.ChooseTag" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>Home</title>
		<!-- Tell the browser to be responsive to screen width -->
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<%@ include file="/reference.html" %>
	</head>
	
	<!-- corrisponde al if-else -->
	<c:choose>
		<c:when test="${not empty msg}">
		    <body class="hold-transition skin-blue sidebar-mini" onload="errormsg('${msg}')">
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
	      			<!-- Main row -->
	      			<div class="row">
			    		<div id="desc" class="col-md-10 col-md-offset-1">
			    			<p class="dark welcome_txt text-center shadow_wh">
			    				Benvenuto all'interno della 
			    				<span class="green_light shadow_sm">libreria</span> <br>
			    				sono stati registrati:
			    			</p>
			    		</div>
			    	</div>
			    	<div class="row">
				    	<div class="col-lg-4 col-lg-offset-2 col-xs-6">
				        	<!-- small box -->
				        	<div class="small-box bg-aqua">
				            	<div class="inner">
				            		<h3>${Dati.get(0)}</h3>
									<h4 class="text-bold">UTENTI</h4>
				            	</div>
				            	<div class="icon">
				            		<i class="fa fa-group"></i>
				            	</div>
				            	<a href="/gestionelibri/staff/user/users" class="small-box-footer h4">
				            		More info <i class="fa fa-arrow-circle-right"></i>
				            	</a>
				          	</div>
				        </div>
				        <div class="col-lg-4 col-xs-6">
				        	<!-- small box -->
				        	<div class="small-box bg-green">
				            	<div class="inner">
				            		<h3>${Dati.get(1)}</h3>
									<h4 class="text-bold">AUTORI</h4>
				            	</div>
				            	<div class="icon">
				            		<i class="fa fa-pencil"></i>
				            	</div>
				            	<a href="/gestionelibri/staff/author/authors" class="small-box-footer h4">
				            		More info <i class="fa fa-arrow-circle-right"></i>
				            	</a>
				          	</div>
				        </div>
				    </div>
				    <div class="row">
				    	<div class="col-lg-4 col-lg-offset-2 col-xs-6">
				        	<!-- small box -->
				        	<div class="small-box bg-yellow">
				            	<div class="inner">
				            		<h3>${Dati.get(2)}</h3>
									<h4 class="text-bold">EDITORI</h4>
				            	</div>
				            	<div class="icon">
				            		<i class="fa fa-newspaper-o"></i>
				            	</div>
				            	<a href="/gestionelibri/staff/publisher/publishers" class="small-box-footer h4">
				            		More info <i class="fa fa-arrow-circle-right"></i>
				            	</a>
				          	</div>
				        </div>
				        <div class="col-lg-4 col-xs-6">
				        	<!-- small box -->
				        	<div class="small-box bg-red">
				            	<div class="inner">
				              		<h3>${Dati.get(3)}</h3>
									<h4 class="text-bold">LIBRI</h4>
				            	</div>
				            	<div class="icon">
				            		<i class="fa fa-book"></i>
				            	</div>
				           		<a href="/gestionelibri/staff/book/books" class="small-box-footer h4">
				           			More info <i class="fa fa-arrow-circle-right"></i>
				           		</a>
				        	</div>
				        </div>
				        <!-- ./col -->
					</div>
	      		</section>
	      	</div>
		</div>
	</body>
</html>