<%-- 
    Document   : Factorial
    Created on : 29-Feb-2024, 2:41:35 pm
    Author     : rohit
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            String number = request.getParameter("number");
        int a = 0;
        if(number != null) a = Integer.parseInt(number);
        %>
        <%!
            public long fact(int a){
                if(a < 2) return 1;
                return (long)a*fact(a-1);
            }
        %>
        <%long factorial = fact(a);%>
        Factorial of <%=a%> is <%=factorial%>
    </body>
</html>
