<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <!-- 
        Page metadata and external stylesheets 
        - Bootstrap for layout 
        - Font Awesome for icons 
        - Custom styles for theme look 
    -->
    <meta charset="UTF-8">
    <title>LoFi – Home</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        .card{background:#f3ece2;opacity:.85}
    </style>
</head>

<body class="vh-100 d-flex flex-column h-100" style="background:rgba(0,0,0,.5)">

<!-- 
    Background layers:
    - First: blurred background image
    - Second: semi-transparent dark overlay
-->
<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed;
            filter:blur(5px);z-index:0;"></div>
<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:rgba(0,0,0,.55);z-index:0;"></div>

<!-- 
    Main wrapper for all content, positioned above background layers 
-->
<div class="d-flex flex-column h-100" style="z-index:1;position:relative;">

    <!-- 
        Header: 
        - LoFi logo and name on left 
        - Logout button on right
    -->
    <header class="container-fluid py-2">
        <div class="d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center">
                <img src="img/LoFi.png" alt="LoFi logo" class="rounded-circle me-2" style="height:48px;">
                <span class="h4 mb-0 text-white">Local Food Finder</span>
            </div>
            <form action="Logout" method="get" class="m-0">
                <button type="submit" class="btn btn-sm btn-outline-light">
                    <i class="fas fa-sign-out-alt me-1"></i> Logout
                </button>
            </form>
        </div>
    </header>

    <!-- 
        Main content section 
        - Dynamically shows options based on user role
    -->
    <main class="container my-auto flex-grow-1 overflow-auto pt-5">
        <div class="row g-4 justify-content-center">

            <!-- 
                Card 1: Search food spots 
            -->
            <div class="col-10 col-md-4">
                <div class="card text-center">
                    <div class="card-body">
                        <h5 class="card-title mb-3">Search</h5>
                        <a href="search.jsp" class="btn btn-dark">Find Food Spot</a>
                    </div>
                </div>
            </div>

            <!-- 
                Card 2: Manage profile 
            -->
            <div class="col-10 col-md-4">
                <div class="card shadow text-center">
                    <div class="card-body">
                        <h5 class="card-title mb-3">Manage Profile</h5>
                        <a href="profile.jsp" class="btn btn-dark">Go to Profile</a>
                    </div>
                </div>
            </div>

            <!-- 
                Card 3: My Favourites (visible only to 'customer' role)
            -->
            <%-- ==================== CHANGE IMPLEMENTED HERE ==================== --%>
            <c:if test="${sessionScope.role == 'customer'}">
                <div class="col-10 col-md-4">
                    <div class="card shadow text-center">
                        <div class="card-body">
                            <h5 class="card-title mb-3">My Favourites</h5>
                            <a href="${pageContext.request.contextPath}/FavouriteServlet"
                               class="btn btn-dark">View Favourites</a>
                        </div>
                    </div>
                </div>
            </c:if>
            <%-- ================================================================= --%>

            <!-- 
                Card 4: Vendor dashboard (visible only to 'vendor' role)
            -->
            <c:if test="${sessionScope.role == 'vendor'}">
                <div class="col-10 col-md-4">
                    <div class="card shadow text-center bg-warning-subtle">
                        <div class="card-body">
                            <h5 class="card-title mb-3">Vendor Dashboard</h5>
                            <a href="VendorDashboardServlet" class="btn btn-warning">Manage My Submissions</a>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- 
                Card 5: Admin dashboard (visible only to 'admin' role)
            -->
            <c:if test="${sessionScope.role == 'admin'}">
                <div class="col-10 col-md-4">
                    <div class="card shadow text-center bg-info-subtle">
                        <div class="card-body">
                            <h5 class="card-title mb-3">Review Pending Submissions</h5>
                            <a href="adminDashboard.jsp" class="btn btn-info">Admin Panel</a>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- 
            Section: Customer quick favourite preview 
            - Only shown to 'customer' role 
            - Shows cards linking to individual MenuServlet views
        -->
        <c:if test="${sessionScope.role == 'customer'}">
            <hr class="text-white my-4">
            <h4 class="text-white text-center mb-3">Your Favourite Spots</h4>
            <div class="row row-cols-1 justify-content-center row-cols-md-3 g-3">
                <c:forEach var="f" items="${requestScope.quickFavs}">
                    <div class="col">
                        <div class="card h-100 text-center">
                            <a href="MenuServlet?id=${f.spotId}" class="stretched-link"></a>
                            <div class="card-body d-flex align-items-center justify-content-center">
                                <h6 class="card-title mb-0 fw-semibold">${f.restaurantName}</h6>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <!-- 
                    Fallback: if no favourites in request scope
                -->
                <c:if test="${empty quickFavs}">
                    <p class="text-center text-white-50">No favourites yet.</p>
                </c:if>
            </div>
        </c:if>
    </main>

    <!-- 
        Footer section with contact info
    -->
    <footer class="text-center text-white-50 py-3">
        © 2025 LoFi&nbsp;· Contact:
        <a href="mailto:support@lofi.my" class="text-white-50">support@lofi.my</a>
    </footer>
</div>
</body>
</html>
