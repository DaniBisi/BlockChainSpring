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
				<span>Transaction ID</span> <span th:text="${txId}" th:remove="tag">title</span>
				<h1>All cars in ledger:</h1>
				<table id="response">
					<tr>
					<td>Key</td>
					<td>Color</td>
					<td>Make</td>
					<td>Model</td>
					<td>Owner</td>
					</tr>
					<tr th:each="employ : ${records}">
					<td th:text="${employ.ID}"></td>
					<td th:text="${employ.IdHash}"></td>
					<td th:text="${employ.agency}"></td>
					<td th:text="${employ.name}"></td>
					<td th:text="${employ.date}"></td>
					</tr>
				</table>
				<span th:text="${record}" th:remove="tag">title</span>

			</div>

		</div>
	</div>
	</div>
</body>
</html>


