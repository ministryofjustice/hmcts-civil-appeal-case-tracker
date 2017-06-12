<%@ page errorPage="ExceptionHandler.jsp" %>
<%
    session.removeAttribute("UserName");
    session.invalidate();
%>
<jsp:forward page="loginform.jsp"/>