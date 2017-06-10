<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: lookitsmarc
  Date: 15/05/2017
  Time: 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
    <link rel="stylesheet" href="/tinwork/public/css/style.css">
    <link rel="stylesheet" href="/tinwork/public/css/dashboard.css">
    <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
</head>
<body>
    <c:set var = "token" value="${userstate.getToken()}"></c:set>
    <c:set var = "username" value="${userstate.getUsername()}"></c:set>
    <c:set var = "mail" value="${userstate.getMail()}"></c:set>
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
        </div>
    </nav>
    <div class="container-fluid">
        <div class="jumbotron jumbotron-fluid">
            <div class="container">
                <h3 class="display-6">Profile</h3>
                <div class="row">
                    <div class="col">
                        <!-- user card -->
                        <div id="card" data-token="<c:out value="${token}"/>">
                            <img class="card-img-top" src="/tinwork/public/image/default-user.jpg" alt="Card image cap">
                            <div class="card-block">
                                <h4 class="card-title">Dashboard</h4>
                                <p class="card-text">Welcome <c:out value="${username}"></c:out></p>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="form-group row">
                            <label for="username" class="col-2 col-form-label">Username</label>
                            <div class="col-10">
                                <input class="form-control" type="text" value="${username}" id="username" name="username">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="password" class="col-2 col-form-label">Old password</label>
                            <div class="col-10">
                                <input class="form-control" type="text" value="" id="old-password" name="old-password">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="password" class="col-2 col-form-label">Password</label>
                            <div class="col-10">
                                <input class="form-control" type="text" value="" id="password" name="password">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="mail" class="col-2 col-form-label">Email</label>
                            <div class="col-10">
                                <input class="form-control" type="text" value="${mail}" id="mail" name="mail">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary" id="submit-update">Submit</button>
                    </div>
                </div>

            </div>
        </div>
        <form action="csv" method="post" enctype="multipart/form-data">
            <div class="input-group">
                <span class="input-group-addon" id="basic-addon1">CSV</span>
                <input type="file" class="form-control" placeholder="Link" name="csv" aria-describedby="basic-addon1" required>
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
        <button type="button" class="btn btn-secondary" id="geturl">Get URL</button>
        <table id="url">
            <thead>
                <tr>
                    <th>Original link</th>
                    <th>Short link</th>
                    <th>Original link</th>
                    <th>#</th>
                </tr>
            </thead>
            <tbody id="body"></tbody>
        </table>
    </div>
    <script type="text/javascript" src="/tinwork/public/js/dashboard.js"></script>
</body>
</html>
