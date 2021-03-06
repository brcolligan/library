<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<c:url value="/css/site.css" var="cssURL" />
<link rel="stylesheet" type="text/css" href="${cssURL}">
<title>Toolbox</title>
</head>

<body>
	<jsp:include page="/WEB-INF/jsp/header.jsp">
		<jsp:param name="pageTitle" value="Checkout" />
	</jsp:include>

	<h2>Items in Your Toolbox</h2>
	<c:url value="/addTools" var="addTools"></c:url>
	<c:url value="/clearCart" var="clearCart"></c:url>
	
	<table class="tableList">
		<tr>
		<th><a href="${addTools}">
			<c:url value="/img/add-icon.png" var="addIcon" />
    		<img align="right" src="${addIcon}" id="addIcon"/></a>Tool<br>Name</th>
		<th>Tool<br>Category</th>
		<th>Due<br>Date</th>
		<th ><a href="${clearCart}">
			<c:url value="/img/removeAll-icon.png" var="removeAllIcon" />
    		<img align="right"  src="${removeAllIcon}" id="removeAllIcon"/></a>Remove<br>Item</th>
		</tr>
		
		<c:forEach var="addedTool" items="${shoppingCart}"
			varStatus="loop">

			<c:url value="/removeFromCart" var="removeTools">
				<c:param name="toolInventoryId" value="${addedTool.toolInventoryId}" />
				<c:param name="toolInventoryIndex" value="${loop.index}" />
			</c:url>
			
			<tr>
				<td><c:out value="${addedTool.toolName}" /></td>
				<td><c:out value="${addedTool.toolCategoryName}" /></td>
				<td><c:out value="${addedTool.toolDueDate}" /></td>
				<td> <a href="${removeTools}">
				<c:url value="/img/remove-icon.png" var="removeIcon" />
    			<img class = icon src="${removeIcon}" id="removeIcon"/></a></td>
			</tr>
		</c:forEach>
	</table>

	<h2>Renter's Information</h2>

	<div class="fieldGroup">
	
		<c:url value="/checkoutTools" var="formActionURL" />
		<form class="form" action="${formActionURL}" method="POST"  >
			All Fields Are Required
			<div class="fieldGroup">
				<input type="hidden" value="${shoppingCart }" />
			</div>
			<div class="fieldGroup">
				<input type="text" name="firstName" id="firstName" placeholder="First Name" required/>*
			</div>
			<div class="fieldGroup">
				<input type="text" name="lastName" id="lastName" placeholder="Last Name" required/>*
			</div>

			<div class="fieldGroup">
				<input type="text" name="license" id="license" placeholder="Driver's license #" required/>*
			</div>

			<div class="fieldGroup">
				<input type="tel" name="phone" id="phone" placeholder="Phone #" size = "20" required/>*
			</div>

			<div class="fieldGroup">
				<input type="submit" value="Submit Loan Request" />
			</div>
		</form>
	</div>
	<c:url value="/homePage" var="homePageURL" />
	<h2><a href="${homePageURL}">Click here to return to the Homepage.</a> </h2>
	
</body>
</html>