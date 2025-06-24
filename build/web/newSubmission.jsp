<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>New Food Spot Submission - LoFi</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
</head>
<body>

    <div class="container my-5">
        <h1 class="mb-4">Food Spot Submission</h1>

        <div class="card shadow-sm mb-4">
            <div class="card-header">
                <h4 class="mb-0">Step 1: Food Spot Details</h4>
            </div>
            <div class="card-body">
                <form action="SubmissionServlet" method="post">
                    <c:if test="${empty sessionScope.submissionSpot}">
                        <%-- Show this form only if food spot details haven't been submitted yet --%>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="restaurantName" class="form-label">Restaurant Name</label>
                                <input type="text" id="restaurantName" name="restaurantName" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="address" class="form-label">Address</label>
                                <input type="text" id="address" name="address" class="form-control" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="googleMapsURL" class="form-label">Google Maps URL</label>
                                <input type="url" id="googleMapsURL" name="googleMapsURL" class="form-control">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="photoURL" class="form-label">Photo URL</label>
                                <input type="url" id="photoURL" name="photoURL" class="form-control">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="openHours" class="form-label">Opening Hour</label>
                                <input type="time" id="openHours" name="openHours" class="form-control">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="closedHours" class="form-label">Closing Hour</label>
                                <input type="time" id="closedHours" name="closedHours" class="form-control">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="workingDays" class="form-label">Working Days</label>
                                <input type="text" id="workingDays" name="workingDays" class="form-control" placeholder="e.g., Mon - Sat">
                            </div>
                        </div>
                        <div class="form-check mb-3">
                            <input class="form-check-input" type="checkbox" id="halalCertified" name="halalCertified" value="true">
                            <label class="form-check-label" for="halalCertified">Halal Certified</label>
                        </div>
                        <button type="submit" name="action" value="set_spot_details" class="btn btn-primary">Save Details & Add Menu</button>
                    </c:if>

                    <c:if test="${not empty sessionScope.submissionSpot}">
                        <%-- Display the saved details once submitted --%>
                        <h5 class="card-title">${sessionScope.submissionSpot.restaurantName}</h5>
                        <p class="card-text">${sessionScope.submissionSpot.address}</p>
                        <p class="text-muted">Details saved. Now add menu items below.</p>
                    </c:if>
                </form>
            </div>
        </div>

        <c:if test="${not empty sessionScope.submissionSpot}">
            <div class="card shadow-sm">
                <div class="card-header">
                    <h4 class="mb-0">Step 2: Menu Items</h4>
                </div>
                <div class="card-body">
                    <h5>Submitted Menu Items</h5>
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Dish Name</th>
                                <th>Price</th>
                                <th>Cuisine</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="menu" items="${sessionScope.submissionMenus}">
                                <tr>
                                    <td>${menu.dish_name}</td>
                                    <td>RM ${String.format("%.2f", menu.price)}</td>
                                    <td>${menu.cuisine_type}</td>
                                    <td>
                                        <form action="SubmissionServlet" method="post" class="d-inline">
                                            <input type="hidden" name="menuId" value="${menu.item_id}">
                                            <button type="submit" name="action" value="delete_menu" class="btn btn-danger btn-sm">Delete</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty sessionScope.submissionMenus}">
                                <tr>
                                    <td colspan="4" class="text-center text-muted">No menu items added yet.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>

                    <hr class="my-4">

                    <h5>Add a New Menu Item</h5>
                    <form action="SubmissionServlet" method="post">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="dishName" class="form-label">Dish Name</label>
                                <input type="text" id="dishName" name="dishName" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="price" class="form-label">Price (RM)</label>
                                <input type="number" id="price" name="price" step="0.01" class="form-control" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea id="description" name="description" class="form-control"></textarea>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="cuisineType" class="form-label">Cuisine Type</label>
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
                                <label for="imageURL" class="form-label">Image URL</label>
                                <input type="url" id="imageURL" name="imageURL" class="form-control">
                            </div>
                        </div>
                        <button type="submit" name="action" value="add_menu" class="btn btn-secondary">Add This Menu Item</button>
                    </form>
                </div>
            </div>

            <div class="d-grid gap-2 mt-4">
                 <form action="SubmissionServlet" method="post" onsubmit="return confirm('Are you sure you want to submit this food spot and all its menu items for approval?')">
                    <button type="submit" name="action" value="final_submit" class="btn btn-success btn-lg w-100" <c:if test="${empty sessionScope.submissionMenus}">disabled</c:if>>
                        Finalize and Submit for Approval
                    </button>
                </form>
            </div>
        </c:if>
    </div>
</body>
</html>