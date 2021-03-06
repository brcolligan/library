<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<c:url value="/css/site.css" var="cssURL" />
<link rel="stylesheet" type="text/css" href="${cssURL}">
<title>Loan Return Page</title>
</head>

<body>
	<jsp:include page="/WEB-INF/jsp/header.jsp">
		<jsp:param name="pageTitle" value="Active Loans" />
	</jsp:include>

	<h2>List of Open Loans</h2>

	<table class="tableList">
		<tr>
		<th>Last<br>Name</th>
		<th>First<br>Name</th>
		<th>Tool<br>Name</th>
		<th>Rental<br>Date</th>
		<th>Due<br>Date</th>
		<th>Cleaning<br>Fee </th>
		</tr>
	
		<c:forEach var="loan" items="${loanList}" varStatus="loopStatus">
			<c:url value="/loanReturn" var="loanReturnURL">
			</c:url>
				<c:choose>
				<c:when test="${loopStatus.count % 2 == 0}">
					<c:set var="itemClass" value="even" />     
				</c:when>
				<c:otherwise>
					<c:set var="itemClass" value="odd" />
				</c:otherwise>
			</c:choose>
			
			<tr class = "${itemClass}">
				<td><c:out value="${loan.lastName}" /></td>
				<td><c:out value="${loan.firstName}" /></td>
				<td><c:out value="${loan.toolLoaned}" /></td>
				<td><c:out value="${loan.dateOfLoan}" /></td>
				<td><c:out value="${loan.expectedReturn}" /></td>
				<td><form action="${loanReturnURL}" method="GET">
					<input type ="hidden" id = "loanId" name="loanId" value ="${loan.loanId}">
					<input type="radio" name="cleaningFee" value="true">Yes
					<input type="radio" name="cleaningFee" value="false" checked>No<br>
					<input type="submit" value="Return Loan"></form></td>
			</tr>
			
		</c:forEach>
		
	</table>
	
	<c:url value="/returnedLoanList" var="returnedLoansURL" />
	<h2><a href="${returnedLoansURL}">View Previous Loans</a> </h2>
	
	<c:url value="/homePage" var="homePageURL" />
	<h2><a href="${homePageURL}">Click here to return to the Homepage.</a> </h2>
	
</body>
</html>