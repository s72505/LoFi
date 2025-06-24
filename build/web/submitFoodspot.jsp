<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Submit New Food Spot - LoFi</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            border-radius: 1rem;
        }
    </style>
</head>
<body>

    <%-- Include the standard header/navbar of your application --%>
    <jsp:include page="header.jsp" />

    <div class="container mt-5 mb-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-lg">
                    <div class="card-body p-5">
                        <h2 class="card-title text-center fw-bold mb-4">Food Spot Registration</h2>
                        
                        <form action="SubmitFoodSpotServlet" method="post">
                            <fieldset>
                                <legend>Food Spot Details</legend>
                                
                                <div class="mb-3">
                                    <label for="restaurantName" class="form-label">Restaurant Name</label>
                                    <input type="text" id="restaurantName" name="restaurantName" class="form-control" placeholder="e.g.: Mee Sup Cik Jah" value="${sessionScope.restaurantName}" required>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="address" class="form-label">Address</label>
                                    <input type="text" id="address" name="address" class="form-control" placeholder="e.g.: Jalan Perdana, Kuala Nerus" value="${sessionScope.address}" required>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="googleMapsURL" class="form-label">Google Maps URL</label>
                                    <input type="url" id="googleMapsURL" name="googleMapsURL" class="form-control" placeholder="https://goo.gl/maps/..." value="${sessionScope.googleMapsURL}">
                                </div>
                                
                                <div class="mb-3">
                                    <label for="photoURL" class="form-label">Photo URL</label>
                                    <input type="url" id="photoURL" name="photoURL" class="form-control" placeholder="https://example.com/image.jpg" value="${sessionScope.photoURL}">
                                </div>
                                
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="openHours" class="form-label">Opening Hour</label>
                                        <input type="time" id="openHours" name="openHours" class="form-control" value="${sessionScope.openHours}">
                                    </div>
                                    <div class="col-md-6">
                                        <label for="closedHours" class="form-label">Closing Hour</label>
                                        <input type="time" id="closedHours" name="closedHours" class="form-control" value="${sessionScope.closedHours}">
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="workingDays" class="form-label">Working Days</label>
                                    <input type="text" id="workingDays" name="workingDays" class="form-control" placeholder="e.g.: Mon - Sat" value="${sessionScope.workingDays}">
                                </div>
                                
                                <div class="form-check mb-4">
                                    <input class="form-check-input" type="checkbox" id="halalCertified" name="halalCertified" value="true" ${sessionScope.halalCertified ? 'checked' : ''}>
                                    <label class="form-check-label" for="halalCertified">
                                        Halal Certified
                                    </label>
                                </div>
                            </fieldset>

                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary btn-lg">Submit for Approval</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>