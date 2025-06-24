<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Results - LoFi</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body  { background:rgba(0,0,0,.5); }
        .card { background:#f3ece2; opacity:.85; }
        .card:hover { opacity:1; }
    </style>
</head>
<body class="vh-100 d-flex flex-column h-100 overflow-auto">

<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed; filter:blur(5px); z-index:0;"></div>
<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:rgba(0,0,0,.55); z-index:0;"></div>

<div class="d-flex flex-column h-100" style="z-index:1; position:relative;">

    <header class="container-fluid py-2">
        <div class="d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center">
                <a href="search.jsp" class="btn btn-sm btn-outline-light me-3">
                    <i class="fas fa-arrow-left"></i> Back to Categories
                </a>
                <img src="img/LoFi.png" alt="LoFi" class="rounded-circle me-2" style="height:48px;">
                <span class="h4 mb-0 text-white">Local Food Finder</span>
            </div>
            <form action="Logout" method="post" class="m-0">
                <button class="btn btn-sm btn-outline-light"><i class="fa fa-sign-out-alt me-1"></i> Logout</button>
            </form>
        </div>
    </header>

    <main class="container my-4 flex-grow-1 overflow-auto pt-3">
        <h2 class="text-center text-white mb-4">
            Showing Results for: <span class="text-warning">${empty cuisine ? 'All Cuisines' : cuisine}</span>
        </h2>

        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
            <c:choose>
                <c:when test="${not empty spots}">
                    <c:forEach var="spot" items="${spots}">
                        <div class="col">
                            <div class="card h-100 shadow-lg">
                                <img src="${spot.photoUrl}" class="card-img-top" style="height: 200px; object-fit: cover;" alt="${spot.restaurantName}">
                                <div class="card-body">
                                    <h5 class="card-title">${spot.restaurantName}</h5>
                                    <p class="card-text">
                                        <i class="fas fa-map-marker-alt text-danger me-2"></i>${spot.address}
                                    </p>
                                    <p class="card-text">
                                        <i class="fas fa-star text-warning me-2"></i>${spot.rating} / 5.0
                                    </p>
                                </div>
                                <div class="card-footer bg-transparent border-top-0 pb-3">
                                    <a href="MenuServlet?id=${spot.spotId}" class="btn btn-dark w-100">View Menu</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12">
                        <div class="alert alert-warning text-center">
                            <h4 class="alert-heading">No Food Spots Found</h4>
                            <p>We couldn't find any food spots matching your criteria. Try a different category!</p>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <footer class="text-center text-white-50 py-3 mt-auto">
        © 2025 LoFi · Contact: <a href="mailto:support@lofi.my" class="text-white-50">support@lofi.my</a>
    </footer>
</div>
</body>
</html>