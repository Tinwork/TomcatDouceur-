<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: lookitsmarc
  Date: 02/04/2017
  Time: 23:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tinwork shorter URL</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
    <link rel="stylesheet" href="/tinwork/public/css/style.css">
    <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
</head>
<body>
<c:set var="message" value="${success.getMessage()}"/>
<c:set var="status" value="${success.getStatus()}"/>
<c:set var="error" value="${error.getError()}"/>
<nav class="navbar navbar-toggleable-md navbar-light bg-faded">
    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <a class="navbar-brand" href="#">
        <img src="/tinwork/public/image/logo.jpg" width="30" height="30" alt="">
        Tinwork
    </a>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="/tinwork/home">Presentation <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/tinwork/sign">Signup</a>
            </li>
        </ul>
        <form class="form-inline" id="no-margin" action="login" method="POST">
            <input class="form-control mr-sm-2" type="text" name="username" placeholder="Username" required>
            <input class="form-control mr-sm-2" type="password" name="password" placeholder="Password" required>
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Sign in</button>
        </form>
    </div>
</nav>
<div class="container-fluid">
    <div class="jumbotron jumbotron-fluid">
        <div class="container">
            <h2 class="display-6 center-text">404 not found !</h2>
            <c:if test = "${error != null}">
                <div class="alert alert-danger" role="alert">
                    <p>${error}</p>
                </div>
            </c:if>
        </div>
    </div>
</div>
<div class="bottom bg-faded">
    <img src="/tinwork/public/image/logo.jpg" width="30" height="30" alt="">
    Copyright tinwork
</div>
</body>
</html>
