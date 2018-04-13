<!DOCTYPE html>
<html lang="en"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/indexForm">
<body>
	<div th:fragment="loginForm">
		<form method="post" action="#" th:action="@{/login}"
			th:object="${login}">
			<div class="form-group">
				<label for="exampleTextarea">Name:</label> <input type="text"
					th:field="*{userName}" class="form-control" />
			</div>
			<div class="form-group">
				<label for="exampleTextarea">Password:</label> <input
					type="password" th:field="*{password}" class="form-control" />
			</div>
			<input type="submit" id="login" value="Login" />

		</form>
	</div>
	<div th:fragment="queryForm">
		<p class="lead">
			Hello <span th:text="${user}"></span> use this form to query the
			ledger
		</p>
		<form method="post" action="#" th:action="@{/query}"
			th:object="${query}">
			<div class="form-group">
				<label for="exampleTextarea">Query:</label> <select
					th:field="*{query}" class="form-control">
					<option value="queryVisit">Query one Visit</option>
					<option value="queryAllVisits">Query all visit</option>
					<option value="mercedes">Query last visit</option>
				</select>
			</div>
			<div class="form-group">
				<label for="exampleTextarea">Arguments:</label> <input type="text"
					th:field="*{args}" class="form-control" /> </br> </br> <input type="submit"
					id="query" value="Query" />
			</div>
		</form>
	</div>
</body>
</html>