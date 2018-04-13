<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org">
<head>
<title>Hello Page</title>
<link rel="stylesheet" th:href="@{/css/main.css}"
	href="../../css/main.css" />
<script src="webjars/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="webjars/bootstrap/css/bootstrap.min.css" />
</head>
<body>
	<nav class="navbar navbar-inverse">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">Spring Boot</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="/">Home</a></li>
					<li><a href="#about">About</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container">
		<div class="starter-template">
			<h1>Query Fabcar Ledger</h1>
			<div id="body">
				<div id="content">
					<div th:replace="fragments/indexForm :: ${u1==null || !u1.loginStatus} ? 'loginForm' : 'queryForm'">
  &copy; 2016 The Static Templates
</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>