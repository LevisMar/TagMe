<%@ page import="org.apache.taglibs.standard.tag.common.core.ChooseTag" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>Welcome</title>
		<!-- Tell the browser to be responsive to screen width -->
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<%@ include file="/reference.html"%>
	</head>
	
	<c:choose>				  
		<c:when test="${not empty errormsg}">
		    <body class="hold-transition login-page" onload="errormsg('${errormsg}')">
		</c:when>
		<c:otherwise>
			<body class="hold-transition login-page">
		</c:otherwise>
	</c:choose>
		<div class="login-box">
			<div class="login-logo">
				<img class="img-responsive" alt="sito" src="/gestionelibri/img/icone/logo2.png">
			</div>
  			<!-- /.login-logo -->
  			<div class="login-box-body border_y">
    			<h1 class="line dark text-bold"> LOGIN </h1>
  				<div class="row">
					<form action="/gestionelibri/login" method="POST" autocomplete="off">      
      					<div class="row">
		    				<div class="col-md-10 col-md-offset-1 col-xs-10 col-xs-offset-1 border_b_y margtopx2">
								<div class="form-label-group">
									<input type="text" id="user" name="username" class="border_g dark text-bold full_width padx1" placeholder="Username" required autofocus>
									<span class="fa fa-user form-control-feedback green_light padx1_2"></span>
								</div>
								<div class="form-group has-feedback">
			      				</div>
			  				</div>	
		  				</div>
	      				<div class="row">
							<div class="col-md-10 col-md-offset-1 col-xs-10 col-xs-offset-1 border_t_y">
								<div class="form-label-group margtopx1">
									<input type="password" id="pwd" name="password" class="border_g dark text-bold full_width padx1" placeholder="Password" required>
									<span class="fa fa-lock form-control-feedback green_light padx2"></span>
								</div>
						  	</div>
					  	</div>		  	
		  				<div class="row">
							<div class="col-md-6 col-md-offset-3 col-xs-6 col-xs-offset-3 padx1">
								<button type="submit" class="btn btn_wgreen_b btn-lg btn-block dark text-shadow">CONFERMA</button>
							</div>
						</div>
    				</form>
   				</div>
  			</div>
		</div>
	</body>
</html>