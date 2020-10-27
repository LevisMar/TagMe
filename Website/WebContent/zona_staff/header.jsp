<%@ page import="org.apache.taglibs.standard.tag.common.core.ChooseTag" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<header class="main-header">
	<!-- Logo -->
	<a href="/gestionelibri/staff/home_staff" class="logo">
		<!-- mini logo for sidebar mini 50x50 pixels -->
      	<span class="logo-mini"><img src="/gestionelibri/img/icone/logo2-sm.png" id="logo" class="img-responsive"/></span>
      	<!-- logo for regular state and mobile devices -->
      	<span class="logo-lg"><img src="/gestionelibri/img/icone/logo2.png" id="logo" class="img-responsive"/></span>
    </a>

	<!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
		<!-- Sidebar toggle button-->
      	<a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        	<span class="sr-only">Menu laterale</span>
      	</a>
		<!-- Navbar Right Menu -->
      	<div class="navbar-custom-menu padx1">
			<c:choose>
				<c:when test="${not empty sessionScope.staffMemb}">
					<ul class="nav navbar-nav border_o">
		        		<li>
		        			<span class="username">
								${sessionScope.staffMemb}
							</span>
		        		</li>
		        		<li>
		        			<button class="btn btn-danger" onclick="window.location.href='/gestionelibri/staff/logout';">LOGOUT</button>
		        		</li>
        			</ul>
				</c:when>		
				<c:otherwise>
				</c:otherwise>
			</c:choose>
      	</div>
	</nav>    
</header>
  
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
   	<!-- sidebar: style can be found in sidebar.less -->
   	<section class="sidebar">
   		<!-- sidebar menu: : style can be found in sidebar.less -->
   		<ul class="sidebar-menu" data-widget="tree">
       		<li class="header">
       			MENU
       		</li>
   			<li>
   				<a href="/gestionelibri/staff/user/users">
   					<i class="fa fa-users"></i> 
   					<span>UTENTI</span>
   				</a>
   			</li>
   			<li>
   				<a href="/gestionelibri/staff/author/authors">
   					<i class="fa fa-pencil"></i> 
   					<span>AUTORI</span>
   				</a>
   			</li>
   			<li>
   				<a href="/gestionelibri/staff/publisher/publishers">
   					<i class="fa fa-newspaper-o"></i> 
   					<span>EDITORI</span>
   				</a>
   			</li>
   			<li>
   				<a href="/gestionelibri/staff/book/add_book">
   					<i class="glyphicon glyphicon-book"></i> 
   					<span>AGGIUNGI LIBRO</span>
   				</a>
   			</li>
   			<li>
   				<a href="/gestionelibri/staff/book/books">
   					<i class="fa fa-book"></i> 
   					<span>LIBRI</span>
   				</a>
   			</li>
   		</ul>
   	</section>
</aside>