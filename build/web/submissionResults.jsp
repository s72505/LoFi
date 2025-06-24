<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Submission Received - LoFi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5 text-center">
        <div class="alert alert-success" role="alert">
            <h4 class="alert-heading">Thank You!</h4>
            <p>${successMessage}</p>
            <hr>
            <p class="mb-0">You will be notified once your submission has been reviewed.</p>
        </div>
        <a href="vendorDashboard.jsp" class="btn btn-primary mt-3">Back to Dashboard</a>
    </div>
</body>
</html>