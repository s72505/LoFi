<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${spot.restaurantName} - Menu</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body  { background:rgba(0,0,0,.5); }
        .card { background:#f3ece2; opacity:.90; }
        .hero-image {
            height: 350px;
            background-size: cover;
            background-position: center;
            color: white;
            display: flex;
            align-items: flex-end;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.8);
        }
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
            <a href="javascript:history.back()" class="btn btn-sm btn-outline-light me-3">
                <i class="fas fa-arrow-left"></i> Back to Results
            </a>
            <form action="Logout" method="get" class="m-0">
                <button class="btn btn-sm btn-outline-light"><i class="fa fa-sign-out-alt me-1"></i> Logout</button>
            </form>
        </div>
    </header>

    <main class="container-fluid p-0 flex-grow-1 overflow-auto">
        <div class="hero-image" style="background-image: linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.6)), url('${spot.photoUrl}');">
            <div class="container pb-4">
                <h1 class="display-4 fw-bold">${spot.restaurantName}</h1>
                <p class="lead"><i class="fas fa-map-marker-alt fa-fw me-2"></i>${spot.address}</p>
                <p class="fs-5"><i class="fas fa-star fa-fw me-2 text-warning"></i>${spot.rating} / 5.0</p>
            </div>
        </div>

        <div class="container py-4">
             <h2 class="text-center text-white mb-4">Our Menu</h2>
             <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                <c:forEach var="item" items="${menuItems}">
                    <div class="col">
                        <div class="card h-100 shadow">
                            <img src="${item.image_url}" class="card-img-top" style="height: 180px; object-fit: cover;" alt="${item.dish_name}">
                            <div class="card-body">
                                <h5 class="card-title">${item.dish_name}</h5>
                                <p class="card-text">${item.description}</p>
                            </div>
                            <div class="card-footer bg-dark text-white">
                                <span class="fs-5 fw-bold">RM ${String.format("%.2f", item.price)}</span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                 <c:if test="${empty menuItems}">
                     <div class="col-12">
                         <div class="alert alert-light text-center">No menu items have been listed for this food spot yet.</div>
                     </div>
                 </c:if>
             </div>
        </div>
        
        <%-- ================================================= --%>
        <%-- ========== NEW REVIEW SECTION STARTS HERE ========== --%>
        <%-- ================================================= --%>
        <div class="container py-4">
             <h2 class="text-center text-white mb-4">Reviews & Ratings</h2>
             
             <c:if test="${sessionScope.role eq 'customer'}">
                 <div class="card shadow-sm mb-4">
                     <div class="card-header bg-light">
                         <h5 class="mb-0">Leave a Review</h5>
                     </div>
                     <div class="card-body">
                         <form action="AddReviewServlet" method="post">
                             <input type="hidden" name="spotId" value="${spot.spotId}">
                             <div class="mb-3">
                                 <label for="rating" class="form-label">Your Rating</label>
                                 <select name="rating" id="rating" class="form-select" required>
                                     <option value="5" selected>★★★★★ (Excellent)</option>
                                     <option value="4">★★★★☆ (Good)</option>
                                     <option value="3">★★★☆☆ (Average)</option>
                                     <option value="2">★★☆☆☆ (Poor)</option>
                                     <option value="1">★☆☆☆☆ (Terrible)</option>
                                 </select>
                             </div>
                             <div class="mb-3">
                                 <label for="comment" class="form-label">Your Comment</label>
                                 <textarea name="comment" id="comment" rows="3" class="form-control" placeholder="Share your experience..." required></textarea>
                             </div>
                             <button type="submit" class="btn btn-primary">Submit Review</button>
                         </form>
                     </div>
                 </div>
             </c:if>

             <c:choose>
                 <c:when test="${not empty reviews}">
                     <c:forEach var="review" items="${reviews}">
                         <div class="card shadow-sm mb-3">
                             <div class="card-body">
                                 <h6 class="card-title mb-1">${review.userName}</h6>
                                 <p class="card-subtitle mb-2 text-warning">
                                     <c:forEach begin="1" end="${review.rating}">★</c:forEach><c:forEach begin="${review.rating + 1}" end="5">☆</c:forEach>
                                 </p>
                                 <p class="card-text">${review.comment}</p>
                                 <small class="text-muted">Reviewed on: ${review.createdAt}</small>
                             </div>
                         </div>
                     </c:forEach>
                 </c:when>
                 <c:otherwise>
                     <div class="alert alert-light text-center">Be the first to leave a review for this spot!</div>
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