<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Submission Received - LoFi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body  { background:rgba(0,0,0,.5) }
        .card { background:#f3ece2; opacity:.90; }
    </style>
</head>
<body class="vh-100 d-flex align-items-center">

<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed;
            filter:blur(5px); z-index:0;"></div>
<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:rgba(0,0,0,.55); z-index:0;"></div>

<div class="container" style="z-index:1; position:relative;">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card text-center shadow-lg">
                <div class="card-body p-5">
                    <c:choose>
                        <c:when test="${not empty message}">
                            <div class="alert alert-success">
                                <h4 class="alert-heading">Success!</h4>
                                <p>${message}</p>
                            </div>
                            <a href="VendorDashboardServlet" class="btn btn-primary mt-3">Return to Dashboard</a>
                            
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-danger">
                                <h4 class="alert-heading">Error!</h4>
                                <p>An unexpected error occurred. Please try submitting again.</p>
                            </div>
                             <a href="newSubmission.jsp" class="btn btn-warning mt-3">Back to Submission Form</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>