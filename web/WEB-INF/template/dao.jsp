<%--
  Created by IntelliJ IDEA.
  User: lookitsmarc
  Date: 18/05/2017
  Time: 15:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns:c="http://java.sun.com/jsp/jstl/core">
<head>
    <title>$Title$</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
    <script src='https://www.google.com/recaptcha/api.js'></script>
</head>
<body>
<nav class="navbar navbar-toggleable-md navbar-light bg-faded">
    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <a class="navbar-brand" href="#">Tinwork</a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link" href="/tinwork/home">Presentation <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/tinwork/sign">Signup</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/tinwork/login">Signin</a>
            </li>
        </ul>
    </div>
</nav>
    <c:set var = "password" value="${constraint.getConstrain('password')}"></c:set>
    <c:set var = "mail"  value="${constraint.getConstrain('mail')}"></c:set>
    <c:set var = "captcha"  value="${constraint.getConstrain('captcha')}"></c:set>
    <c:set var = "mulpwd" value="${constraint.getConstrain('mulPwd')}"></c:set>

    <form method="post" action="/tinwork/dao">
        <c:if test="${password eq true}">
            <div class="input-group">
                <span class="input-group-addon" id="password">Password</span>
                <input type="text" class="form-control" placeholder="password" name="password" aria-describedby="basic-addon1" required>
            </div>
        </c:if>
        <c:if test="${mail eq true}">
            <div class="input-group">
                <span class="input-group-addon" id="mail">Mail</span>
                <input type="text" class="form-control" placeholder="Mail" name="mail" aria-describedby="basic-addon1" required>
            </div>
        </c:if>
        <c:if test="${captcha eq true}">
            <div class="input-group">
                <span class="input-group-addon" id="captcha">Catpcha</span>
                <div class="g-recaptcha" data-sitekey="6LfmAiIUAAAAAAIzd9O1o_0lu63QWhx5GYB5frIf"></div>
            </div>
        </c:if>
        <c:if test="${mulpwd eq true}">
            <c:forEach var = "i" begin = "1" end = "${constraint.getMulPwdLength()}">
                <div class="input-group">
                    <span class="input-group-addon" id="mulpwd">Password ${i}</span>
                    <input type="text" class="form-control" placeholder="Mail" name="passwords-${i}" aria-describedby="basic-addon1" required>
                </div>
            </c:forEach>
        </c:if>
        <!-- Send back the bean.. As it's not present within the other request ... :(-->
        <input type="hidden" name="constraint" value="${constraint}">
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
    <script src="/tinwork/public/js/validation.js"></script>
</body>
</html>
