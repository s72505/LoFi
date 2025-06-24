<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Meta and external stylesheets -->
    <meta charset="UTF-8">
    <title>New Food Spot Submission - LoFi</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body  { background:rgba(0,0,0,.5) }
        .card { background:#f3ece2; opacity:.90; }
    </style>
</head>

<body class="vh-100 d-flex flex-column h-100 overflow-auto">

<!-- Background layers: blurred image + dark tint overlay -->
<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed;
            filter:blur(5px); z-index:0;"></div>
<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:rgba(0,0,0,.55); z-index:0;"></div>

<!-- Main wrapper -->
<div class="d-flex flex-column h-100" style="z-index:1; position:relative;">

    <!-- Header with back button, logo, and logout -->
    <header class="container-fluid py-2">
        <div class="d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center">
                <a href="VendorDashboardServlet" class="btn btn-sm btn-outline-light me-2">
                    <i class="fas fa-arrow-left me-1"></i> Back to Dashboard
                </a>
                <img src="img/LoFi.png" alt="LoFi" class="rounded-circle me-2" style="height:48px;">
                <span class="h4 mb-0 text-white">Local Food Finder – New Submission</span>
            </div>
            <div>
                <a href="Logout" class="btn btn-sm btn-outline-light">
                    <i class="fas fa-sign-out-alt me-1"></i> Logout
                </a>
            </div>
        </div>
    </header>

    <!-- Main content area -->
    <main class="container my-4 flex-grow-1 overflow-auto pt-3">

        <!-- Display error message if exists -->
        <c:if test="${not empty err}">
            <div class="alert alert-danger shadow-sm">${err}</div>
        </c:if>

        <!-- Step 1: Spot Details Form -->
        <div class="card shadow-lg mb-4">
            <div class="card-header bg-dark text-white">
                <h4 class="mb-0">Step 1: Food Spot Details</h4>
            </div>
            <div class="card-body p-4">
                <form action="SubmissionServlet" method="post">

                    <!-- Show input form if spot has not been submitted yet -->
                    <c:if test="${empty sessionScope.submissionSpot}">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="restaurantName" class="form-label fw-bold">Restaurant Name</label>
                                <input type="text" id="restaurantName" name="restaurantName" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="address" class="form-label fw-bold">Address</label>
                                <input type="text" id="address" name="address" class="form-control" required>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="googleMapsURL" class="form-label fw-bold">Google Maps URL</label>
                                <input type="url" id="googleMapsURL" name="googleMapsURL" class="form-control">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="photoURL" class="form-label fw-bold">Photo URL</label>
                                <input type="url" id="photoURL" name="photoURL" class="form-control">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="openHours" class="form-label fw-bold">Opening Hour</label>
                                <input type="time" id="openHours" name="openHours" class="form-control">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="closedHours" class="form-label fw-bold">Closing Hour</label>
                                <input type="time" id="closedHours" name="closedHours" class="form-control">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="workingDays" class="form-label fw-bold">Working Days</label>
                                <input type="text" id="workingDays" name="workingDays" class="form-control" placeholder="e.g., Mon - Sat">
                            </div>
                        </div>

                        <div class="form-check mb-3">
                            <input class="form-check-input" type="checkbox" id="halalCertified" name="halalCertified" value="true">
                            <label class="form-check-label fw-bold" for="halalCertified">Halal Certified</label>
                        </div>

                        <button type="submit" name="action" value="set_spot_details" class="btn btn-primary">Save Details & Add Menu</button>
                    </c:if>

                    <!-- If spot is already saved in session -->
                    <c:if test="${not empty sessionScope.submissionSpot}">
                        <h5 class="card-title">${sessionScope.submissionSpot.restaurantName}</h5>
                        <p class="card-text">${sessionScope.submissionSpot.address}</p>
                        <p class="text-success fw-bold"><i class="fas fa-check-circle"></i> Details saved. You can now add menu items below.</p>
                    </c:if>
                </form>
            </div>
        </div>

        <!-- Step 2: Menu Section -->
        <c:if test="${not empty sessionScope.submissionSpot}">
            <div class="card shadow-lg">
                <div class="card-header bg-dark text-white">
                    <h4 class="mb-0">Step 2: Menu Items</h4>
                </div>
                <div class="card-body p-4">

                    <!-- Menu Table -->
                    <h5>Submitted Menu Items</h5>
                    <div class="table-responsive mb-4">
                        <table class="table table-striped table-bordered">
                            <thead class="table-secondary">
                                <tr>
                                    <th>Dish Name</th>
                                    <th>Price</th>
                                    <th>Cuisine</th>
                                    <th class="text-center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Loop through menu items -->
                                <c:forEach var="menu" items="${sessionScope.submissionMenus}">
                                    <tr>
                                        <td>${menu.dish_name}</td>
                                        <td>RM ${String.format("%.2f", menu.price)}</td>
                                        <td>${menu.cuisine_type}</td>
                                        <td class="text-center">
                                            <form action="SubmissionServlet" method="post" class="d-inline">
                                                <input type="hidden" name="menuId" value="${menu.item_id}">
                                                <button type="submit" name="action" value="delete_menu" class="btn btn-danger btn-sm" title="Delete Item">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <!-- Fallback if no menus -->
                                <c:if test="${empty sessionScope.submissionMenus}">
                                    <tr>
                                        <td colspan="4" class="text-center text-muted">No menu items added yet.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <hr>

                    <!-- Add new menu item form -->
                    <h5 class="mt-4">Add a New Menu Item</h5>
                    <form action="SubmissionServlet" method="post">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="dishName" class="form-label fw-bold">Dish Name</label>
                                <input type="text" id="dishName" name="dishName" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="price" class="form-label fw-bold">Price (RM)</label>
                                <input type="number" id="price" name="price" step="0.01" class="form-control" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label fw-bold">Description</label>
                            <textarea id="description" name="description" class="form-control" rows="2"></textarea>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="cuisineType" class="form-label fw-bold">Cuisine Type</label>
                                <select id="cuisineType" name="cuisineType" class="form-select">
                                    <option value="Hot Food">Hot Food</option>
                                    <option value="Noodles and Pasta">Noodles and Pasta</option>
                                    <option value="Rice-Based">Rice-Based</option>
                                    <option value="Snacks">Snacks</option>
                                    <option value="Desserts">Desserts</option>
                                    <option value="Beverages">Beverages</option>
                                    <option value="Fast Food">Fast Food</option>
                                    <option value="Seafood">Seafood</option>
                                    <option value="Breakfast Sets">Breakfast Sets</option>
                                    <option value="Street Food">Street Food</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="imageURL" class="form-label fw-bold">Image URL</label>
                                <input type="url" id="imageURL" name="imageURL" class="form-control">
                            </div>
                        </div>

                        <button type="submit" name="action" value="add_menu" class="btn btn-secondary">
                            <i class="fas fa-plus"></i> Add This Menu Item
                        </button>
                    </form>
                </div>
            </div>

            <!-- Final submission button -->
            <div class="d-grid gap-2 my-4">
                <form action="SubmissionServlet" method="post" onsubmit="return confirm('Are you sure you want to submit this food spot and all its menu items for approval?')">
                    <button type="submit" name="action" value="final_submit" class="btn btn-success btn-lg w-100" <c:if test="${empty sessionScope.submissionMenus}">disabled</c:if>>
                        <i class="fas fa-check-circle me-2"></i>Finalize and Submit for Approval
                    </button>
                </form>
            </div>
        </c:if>
    </main>

    <!-- Footer -->
    <footer class="text-center text-white-50 py-3 mt-auto">
        © 2025 LoFi · <a href="mailto:support@lofi.my" class="text-white-50">support@lofi.my</a>
    </footer>
</div>
</body>
</html>
