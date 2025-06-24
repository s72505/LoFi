<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- 
    Guard clause: Only allows access to users with 'customer' role
    Redirects others back to home page
-->
<c:if test="${sessionScope.role ne 'customer'}">
    <c:redirect url="home.jsp"/>
</c:if>

<!DOCTYPE html>
<html lang="en">
    <head>
        <!-- 
            Page metadata and external stylesheets 
            - Bootstrap for layout 
            - Font Awesome for icons 
            - Custom styles for UI look and feel 
        -->
        <meta charset="UTF-8">
        <title>LoFi – My Favourites</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <style>
            body         { background:rgba(0,0,0,.5) }
            .card        { background:#f3ece2; opacity:.85; }
            .fav-img     { object-fit:cover; height:160px; }
            .unfav-btn   { position:absolute; top:.5rem; right:.5rem; z-index:5; }
            .unfav-btn .btn{ background:transparent }
        </style>
    </head>
    <body class="vh-100 d-flex flex-column h-100 overflow-auto">

        <!-- 
            Background layers: 
            - Blurred image and semi-transparent dark overlay 
        -->
        <div class="position-fixed top-0 start-0 w-100 h-100"
             style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed;
                    filter:blur(5px); z-index:0;"></div>
        <div class="position-fixed top-0 start-0 w-100 h-100"
             style="background:rgba(0,0,0,.55); z-index:0;"></div>

        <!-- 
            Main content wrapper with relative positioning above background 
        -->
        <div class="d-flex flex-column h-100" style="z-index:1; position:relative;">

            <!-- 
                Header section with:
                - Back to home button
                - LoFi logo
                - App name
                - Logout button
            -->
            <header class="container-fluid py-1">
                <div class="d-flex justify-content-between align-items-center">
                    <div class="d-flex align-items-center">
                        <a href="Home" class="btn btn-sm btn-outline-light me-2">
                            <i class="fas fa-arrow-left me-1"></i> Back to Home
                        </a>
                        <img src="img/LoFi.png" alt="LoFi logo" class="rounded-circle me-2" style="height:48px;">
                        <span class="h4 mb-0 text-white">Local Food Finder</span>
                    </div>
                    <form action="Logout" method="get" class="m-0">
                        <button class="btn btn-sm btn-outline-light">
                            <i class="fas fa-sign-out-alt me-1"></i> Logout
                        </button>
                    </form>
                </div>
            </header>

            <!-- 
                Main section displaying user's favorite food spots
            -->
            <main class="container my-4 flex-grow-1 overflow-auto pt-3">
                <h3 class="text-white text-center mb-4">My Favourite Spots</h3>

                <!-- 
                    Condition: If no favorites exist, show placeholder
                    Otherwise, display each favorite in card layout
                -->
                <c:choose>
                    <c:when test="${empty favs}">
                        <p class="text-center text-white-50">You have no favourites yet.</p>
                    </c:when>

                    <c:otherwise>
                        <!-- 
                            Responsive grid to display each favorite spot 
                            using Bootstrap columns and card layout
                        -->
                        <div class="row row-cols-1 row-cols-md-3 g-4 justify-content-center">
                            <c:forEach var="f" items="${favs}">
                                <div class="col">
                                    <div class="card h-100 position-relative">

                                        <!-- 
                                            Form to remove favorite spot
                                            - Sends POST to FavouriteServlet with action=del
                                        -->
                                        <form action="${pageContext.request.contextPath}/FavouriteServlet"
                                              method="post" class="unfav-btn">
                                            <input type="hidden" name="action" value="del">
                                            <input type="hidden" name="spotId" value="${f.spotId}">
                                            <button class="btn btn-sm btn-outline-danger" title="Remove">
                                                <i class="fas fa-heart-broken"></i>
                                            </button>
                                        </form>

                                        <!-- 
                                            Display food spot photo
                                            - Uses card-img-top style with fixed height
                                        -->
                                        <img src="${f.photoUrl}" class="card-img-top fav-img" alt="${f.restaurantName}">

                                        <!-- 
                                            Card body:
                                            - Restaurant name
                                            - Rating (star icon)
                                            - Clickable link via stretched-link (redirects to MenuServlet)
                                        -->
                                        <div class="card-body">
                                            <h6 class="card-title mb-1">${f.restaurantName}</h6>
                                            <span class="text-warning">
                                                <i class="fas fa-star"></i> ${f.rating}
                                            </span>
                                            <%-- ========== LINK CORRECTED TO POINT TO MenuServlet ========== --%>
                                            <a href="MenuServlet?id=${f.spotId}" class="stretched-link"></a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </main>

            <!-- 
                Footer section with contact email
            -->
            <footer class="text-center text-white-50 py-3 mt-auto">
                © 2025 LoFi · <a href="mailto:support@lofi.my" class="text-white-50">support@lofi.my</a>
            </footer>
        </div>
    </body>
</html>
