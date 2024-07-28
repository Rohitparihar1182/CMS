<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>CMS - Login</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link
            href="https://fonts.googleapis.com/css2?family=Mr+Dafoe&family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
            rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formStyle.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css">
        <style>
            #open-modal{
                width: 45px;
                outline: none;
                border: none;
                background: transparent;
                cursor: pointer;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin: 20px 0;
            }
            table, th, td{
                border: 1px solid #ccc;
            }
            .dark table, .dark th, .dark td {
                border: 1px solid #444;
            }
            th, td {
                padding: 12px;
                text-align: left;
            }
            th{
                background-color: #d2d2d2;
            }
            .dark th {
                background-color: #111;
                color: #fff;
            }
            td{
                background-color: #f4f4f4;
            }
            .dark td {
                background-color: #212121;
            }
            tr:nth-child(even) {
                background-color: #333;
            }
            .checkbox {
                text-align: center;
            }
            input[type="checkbox"] {
                width: 20px;
                height: 20px;
            }
            input[type="submit"] {
                padding: 10px 20px;
                background-color: #007BFF;
                color: #fff;
                border: none;
                border-radius: 5px;
                transition: all 150ms linear;
                cursor: pointer;
            }
            input[type="submit"]:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <%@ include file="./dashboardHeader.jsp" %>
        <section>
            <div class="container">
                <div style="display: flex; justify-content: space-between; align-items: center">
                    <div>
                        <h1>Welcome, <%= request.getAttribute("name") %></h1>
                        <p>Your uid is: <%= request.getAttribute("uid") %></p>
                    </div>
                    <div>                    
                        <button type="button" role="button" title="Delete Account" id="open-modal"><img alt="delete" src="./assets/delete-icon.jpg" /></button>
                    </div>
                </div>
                <%
            // Retrieve the games and registered games from the request attributes
            Map<String, String> games = (Map<String, String>) request.getAttribute("games");
            List<String> registeredGames = (List<String>) request.getAttribute("registerdGames");
            if(registeredGames == null) registeredGames = new ArrayList<>();

            if (games != null && registeredGames != null) {
                %>
                <form action="GameServlet" method="post">
                    <table>
                        <tr>
                            <th>Game ID</th>
                            <th>Game Name</th>
                            <th>Register</th>
                        </tr>
                        <%
                            for (Map.Entry<String, String> entry : games.entrySet()) {
                                String gameId = entry.getKey();
                                String gameName = entry.getValue();
                                boolean isRegistered = registeredGames.contains(gameId);
                        %>
                        <tr>
                            <td><%= gameId %></td>
                            <td><%= gameName %></td>
                            <td class="checkbox">
                                <input type="checkbox" name="games" value="<%= gameId %>" <%= isRegistered ? "checked" : "" %> />
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </table>
                    <input type="hidden" name="uid" value="<%= request.getAttribute("uid") %>" />
                    <input type="submit" value="Submit" />
                </form>
                <%
                    } else {
                %>
                <p>No games available.</p>
                <%
                    }
                %>
            </div>
        </section>


        <script defer src="${pageContext.request.contextPath}/js/modal.js"></script>
    </body>
</html>

