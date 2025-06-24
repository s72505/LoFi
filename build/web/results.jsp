<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${selectedCuisine} – Results · LoFi</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<!-- simple top bar -->
<nav class="navbar navbar-expand-lg bg-dark navbar-dark">
  <div class="container">
      <a class="navbar-brand" href="home.jsp">
          <img src="img/logo.png" style="height:32px" class="rounded-circle me-2">LoFi
      </a>
      <form action="Logout" method="post" class="ms-auto">
          <button class="btn btn-sm btn-outline-light"><i class="fas fa-sign-out-alt me-1"></i>Logout</button>
      </form>
  </div>
</nav>

<div class="container py-4">

    <a href="search.jsp" class="btn btn-sm btn-secondary mb-3">&larr; Back to cuisines</a>
    <h2 class="mb-4">${selectedCuisine}</h2>

    <c:choose>
        <c:when test="${empty spots}">
            <div class="alert alert-warning">Sorry, no spots found for this category.</div>
        </c:when>

        <c:otherwise>
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <c:forEach var="s" items="${spots}">
                    <div class="col">
                        <div class="card h-100 shadow-sm">
                            <img src="{s.photoUrl}" class="card-img-top" style="object-fit:cover;height:160px">
                            <div class="card-body">
                                <h5 class="card-title">${s.name}</h5>
                                <p class="card-text text-muted" style="font-size:.9rem">${s.address}</p>
                                <span class="badge bg-${s.halalFlag ? 'success' : 'secondary'}">
                                    ${s.halalFlag ? 'Halal' : 'Non-Halal'}
                                </span>
                            </div>
                            <div class="card-footer bg-transparent border-0">
                                <a href="${s.googleMapsUrl}" target="_blank"
                                   class="btn btn-sm btn-outline-secondary me-2">
                                    Maps
                                </a>
                                <a href="MenuServlet?id=${s.spotId}"
                                   class="btn btn-sm btn-dark">
                                    View Menu
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
