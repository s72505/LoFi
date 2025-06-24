<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>LoFi – My Submissions</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body  { background:rgba(0,0,0,.5) }
        .card { background:#f3ece2; opacity:.85; }
    </style>
</head>
<body class="vh-100 d-flex flex-column h-100 overflow-auto">

<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed;
            filter:blur(5px); z-index:0;"></div>
<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:rgba(0,0,0,.55); z-index:0;"></div>

<div class="d-flex flex-column h-100" style="z-index:1; position:relative;">

    <header class="container-fluid py-2">
        <div class="d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center">
                <img src="img/LoFi.png" alt="LoFi" class="rounded-circle me-2" style="height:48px;">
                <span class="h4 mb-0 text-white">Local Food Finder – Vendor</span>
            </div>
            <form action="Logout" method="get" class="m-0">
                <button class="btn btn-sm btn-outline-light">
                    <i class="fas fa-sign-out-alt me-1"></i> Logout
                </button>
            </form>
        </div>
    </header>

    <main class="container my-4 flex-grow-1 overflow-auto pt-3">
    
        <div class="d-grid gap-2 d-md-flex justify-content-md-end mb-4">
            <a href="newSubmission.jsp" class="btn btn-primary btn-lg shadow">
                <i class="fas fa-plus-circle me-2"></i>Create New Food Spot Submission
            </a>
        </div>

        <div class="card shadow-lg">
            <div class="card-body">

                <h3 class="card-title text-center mb-4">My Food-Spot Submissions</h3>

                <c:choose>

                    <c:when test="${empty submissions}">
                        <p class="text-center text-muted fst-italic mb-0">
                            You have not submitted any food spots yet.
                        </p>
                    </c:when>

                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-striped align-middle">
                                <thead class="table-dark text-center">
                                <tr>
                                    <th>ID</th>
                                    <th>Restaurant</th>
                                    <th>Submitted&nbsp;At</th>
                                    <th>Status</th>
                                    <th>Reason</th>
                                </tr>
                                </thead>
                                <tbody class="table-group-divider text-center">
                                <c:forEach var="s" items="${submissions}">
                                    <tr>
                                        <td>${s.requestID}</td>
                                        <td class="text-start">${s.restaurantName}</td>
                                        <td>${s.submittedTime}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${s.status eq 'approved'}">
                                                    <span class="badge bg-success">Approved</span>
                                                </c:when>
                                                <c:when test="${s.status eq 'rejected'}">
                                                    <span class="badge bg-danger">Rejected</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">Pending</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${empty s.rejectionReason ? '—' : s.rejectionReason}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>

                </c:choose>
            </div>
        </div>
    </main>

    <footer class="text-center text-white-50 py-3 mt-auto">
        © 2025 LoFi · <a href="mailto:support@lofi.my" class="text-white-50">support@lofi.my</a>
    </footer>
</div>
</body>
</html>