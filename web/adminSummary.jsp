<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- 
    Access control guard:
    - Redirects users who are not admins to the login page with an error message.
-->
<c:if test="${sessionScope.role ne 'admin'}">
    <c:redirect url="login.jsp?err=loginRequired"/>
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <!-- 
        Page metadata and stylesheets
        - Responsive design setup
        - Bootstrap and Font Awesome
        - Custom styling for background and card elements
    -->
    <meta charset="UTF-8">
    <title>LoFi – Admin Review</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body { background:rgba(0,0,0,.5) }
        .card{background:#f3ece2;opacity:.85}
        .img-thumb{max-width:260px;border-radius:10px}
        .blur-bg{
            background:url('img/loginBackground.jpg') center/cover no-repeat fixed;
            filter:blur(5px); z-index:0;
        }
    </style>
</head>

<body class="vh-100 d-flex flex-column h-100 overflow-auto">

<!-- 
    Layered background effect:
    - First div applies blurred image
    - Second adds dark overlay
-->
<div class="position-fixed top-0 start-0 w-100 h-100 blur-bg"></div>
<div class="position-fixed top-0 start-0 w-100 h-100" style="background:rgba(0,0,0,.55);z-index:0;"></div>

<!-- 
    Wrapper for the whole page layout
    - Positioned above background
-->
<div class="d-flex flex-column h-100" style="z-index:1;position:relative;">

    <!-- 
        Header bar with navigation and branding
        - Back to dashboard link
        - LoFi logo
        - Logout button
    -->
    <header class="container-fluid py-2">
        <div class="d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center">
                <a href="adminDashboard.jsp" class="btn btn-sm btn-outline-light me-2">
                    <i class="fas fa-arrow-left me-1"></i> Back to Admin Dashboard
                </a>
                <img src="img/LoFi.png" alt="LoFi logo" class="rounded-circle me-2" style="height:48px;">
                <span class="h4 mb-0 text-white">Local Food Finder – Admin</span>
            </div>
            <form action="Logout" method="get" class="m-0">
                <button class="btn btn-sm btn-outline-light">
                    <i class="fas fa-sign-out-alt me-1"></i> Logout
                </button>
            </form>
        </div>
    </header>

    <!-- 
        Main content block:
        - Contains card showing full submission details
    -->
    <main class="container my-4 flex-grow-1 overflow-auto pt-3">

        <!-- Review card container -->
        <div class="card shadow-lg mx-auto" style="max-width:900px">
            <div class="card-body">

                <h3 class="card-title mb-4 text-center">Submission #${sessionScope.requestID}</h3>

                <!-- 
                    Section: Food Spot Details
                    - Displays image, restaurant name, address, hours, etc.
                -->
                <h5 class="fw-semibold mb-3"><i class="fas fa-store me-1"></i> Food Spot Details</h5>

                <div class="row g-4">
                    <!-- Left: Photo of restaurant -->
                    <div class="col-12 col-md-4 text-center">
                        <img src="${sessionScope.photoURL}"
                             alt="${sessionScope.restaurantName}"
                             class="img-fluid img-thumb shadow-sm">
                    </div>

                    <!-- Right: Textual details -->
                    <div class="col-12 col-md-8">
                        <dl class="row mb-0">
                            <dt class="col-sm-4">Restaurant:</dt>
                            <dd class="col-sm-8">${sessionScope.restaurantName}</dd>

                            <dt class="col-sm-4">Address:</dt>
                            <dd class="col-sm-8">${sessionScope.address}</dd>

                            <dt class="col-sm-4">Google Maps:</dt>
                            <dd class="col-sm-8">
                                <a href="${sessionScope.googleMapsURL}" target="_blank">
                                    View location
                                </a>
                            </dd>

                            <dt class="col-sm-4">Open / Close:</dt>
                            <dd class="col-sm-8">
                                ${sessionScope.openHours} – ${sessionScope.closedHours}
                            </dd>

                            <dt class="col-sm-4">Working Days:</dt>
                            <dd class="col-sm-8">${sessionScope.workingDays}</dd>

                            <dt class="col-sm-4">Halal:</dt>
                            <dd class="col-sm-8">
                                <!-- Conditional logic to display halal status -->
                                <c:choose>
                                    <c:when test="${sessionScope.halalCertified eq true}">Yes</c:when>
                                    <c:when test="${sessionScope.halalCertified eq false}">No</c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </dd>
                        </dl>
                    </div>
                </div>

                <hr class="my-4">

                <!-- 
                    Section: Menu items submitted
                    - If empty, shows placeholder message
                    - Otherwise, loops and displays each item
                -->
                <h5 class="fw-semibold mb-3"><i class="fas fa-utensils me-1"></i> Submitted Menu Items</h5>

                <c:if test="${empty sessionScope.menus}">
                    <p class="text-muted fst-italic">No menu items were submitted.</p>
                </c:if>

                <c:forEach var="item" items="${sessionScope.menus}">
                    <div class="border rounded p-3 mb-3">
                        <div class="row g-3 align-items-center">
                            <!-- Left: Dish image -->
                            <div class="col-12 col-lg-3 text-center">
                                <img src="${item.image_url}"
                                     alt="${item.dish_name}"
                                     class="img-fluid"
                                     style="max-height:140px;object-fit:cover;border-radius:6px">
                            </div>
                            <!-- Right: Dish info -->
                            <div class="col-12 col-lg-9">
                                <h6 class="mb-1 fw-semibold">
                                    ${item.dish_name}
                                    <span class="text-warning ms-2">RM ${item.price}</span>
                                </h6>
                                <p class="mb-1">${item.description}</p>
                                <small class="text-muted">Cuisine: ${item.cuisine_type}</small>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <hr class="my-4">

                <!-- 
                    Admin decision form
                    - Two dropdowns: one for food spot, one for menu
                    - Optional rejection reason
                -->
                <h5 class="fw-semibold mb-3">Admin Decision</h5>

                <form action="ApprovalDecisionServlet" method="post">
                    <input type="hidden" name="requestID" value="${sessionScope.requestID}">

                    <div class="row mb-3">
                        <label class="col-md-4 col-form-label fw-semibold">
                            Approve food-spot?
                        </label>
                        <div class="col-md-8">
                            <select name="approveSpot" class="form-select" required>
                                <option value="true">Yes</option>
                                <option value="false">No</option>
                            </select>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-md-4 col-form-label fw-semibold">
                            Approve menu items?
                        </label>
                        <div class="col-md-8">
                            <select name="approveMenu" class="form-select" required>
                                <option value="true">Yes</option>
                                <option value="false">No</option>
                            </select>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Reason if rejected</label>
                        <textarea name="rejectionReason" class="form-control"
                                  rows="3" placeholder="Optional – shown to vendor"></textarea>
                    </div>

                    <div class="text-end">
                        <a href="adminDashboard.jsp" class="btn btn-outline-secondary me-2">
                            <i class="fas fa-arrow-left me-1"></i> Back
                        </a>
                        <button type="submit" class="btn btn-dark">
                            <i class="fas fa-check me-1"></i> Confirm Decision
                        </button>
                    </div>
                </form>

            </div><!-- card-body -->
        </div><!-- card -->
    </main>

    <!-- 
        Footer with contact email
    -->
    <footer class="text-center text-white-50 py-3 mt-auto">
        © 2025 LoFi · <a href="mailto:support@lofi.my" class="text-white-50">support@lofi.my</a>
    </footer>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
