<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <!-- 
        Metadata and responsive setup 
        - Bootstrap and Font Awesome included for styling 
    -->
    <meta charset="UTF-8">
    <title>LoFi – Local Food-Finder</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
</head>

<body class="vh-100 overflow-hidden" style="background:rgba(0,0,0,.5)">

    <!-- 
        Background layers 
        - Blurred food image and semi-transparent dark overlay
    -->
    <div class="position-fixed top-0 start-0 w-100 h-100"
         style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed;
                filter:blur(5px); z-index:0;"></div>
    <div class="position-fixed top-0 start-0 w-100 h-100"
         style="background:rgba(0,0,0,.55); z-index:0;"></div>

    <!-- 
        Container for centered login card 
    -->
    <div class="container h-100 d-flex align-items-center justify-content-center" style="z-index:1; position:relative;">

        <!-- 
            Main login card 
            - Rounded corners, light background
        -->
        <div class="card shadow-lg" style="border-radius:1rem; max-width:920px; background-color:#f3ece2;">
            <div class="row g-0">

                <!-- 
                    Left column: Login illustration (only visible on medium+ screens)
                -->
                <div class="col-md-3 d-none d-md-block">
                    <img src="img/loginPage.png" 
                         class="w-100 h-100"
                         style="object-fit:cover; border-radius:1rem 0 0 1rem;"
                         alt="Login illustration">
                </div>

                <!-- 
                    Right column: Login form 
                -->
                <div class="col-md-9 d-flex align-items-center">
                    <div class="card-body p-4 p-lg-4 text-center text-md-start">

                        <!-- 
                            Branding header with logo 
                        -->
                        <div class="d-flex align-items-center mb-3">
                            <img src="img/LoFi.png"
                                 alt="LoFi logo"
                                 class="rounded-circle me-2"
                                 style="height:80px;">
                            <span class="h3 fw-bold mb-0"> · Local Food Finder</span>
                        </div>

                        <!-- 
                            Conditional messages:
                            - Error, success, or login-required notices
                        -->
                        <c:if test="${not empty err}">
                            <div class="alert alert-danger">${err}</div>
                        </c:if>
                        <c:if test="${param.msg == 'registered'}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                Registration successful! You can log in now.
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        <c:if test="${param.err == 'loginRequired'}">
                            <div class="alert alert-warning">Please log in first.</div>
                        </c:if>

                        <!-- 
                            Login form (POST to Login servlet) 
                        -->
                        <form action="<%=request.getContextPath()%>/Login" method="post" autocomplete="off">
                            <h5 class="fw-normal mb-4">Sign into your account</h5>

                            <!-- Email input -->
                            <div class="form-outline mb-3">
                                <input type="email"
                                       name="email"
                                       id="email"
                                       class="form-control form-control-lg"
                                       placeholder="Enter email"
                                       autocomplete="username"
                                       required>
                                <label class="form-label fw-semibold" for="email">Email address</label>
                            </div>

                            <!-- Password input -->
                            <div class="form-outline mb-3">
                                <input type="password"
                                       name="password"
                                       id="pwd"
                                       class="form-control form-control-lg"
                                       placeholder="Enter password"
                                       autocomplete="current-password"
                                       required minlength="6">
                                <label class="form-label fw-semibold" for="pwd">Password</label>
                            </div>

                            <!-- Role dropdown -->
                            <div class="form-outline mb-4">
                                <select name="role" id="role" class="form-select form-select-lg" required>
                                    <option value="" disabled selected class="text-muted">Choose role…</option>
                                    <option value="customer">Customer</option>
                                    <option value="vendor">Food Vendor</option>
                                    <option value="admin">Admin</option>
                                </select>
                                <label class="form-label fw-semibold" for="role">Login as</label>
                            </div>

                            <!-- Submit button -->
                            <button class="btn btn-dark btn-lg w-100 mb-3" type="submit">Login</button>

                            <!-- Registration link -->
                            <p class="mb-2">
                                Don't have an account?
                                <a href="register.jsp" class="fw-semibold">Register here</a>
                            </p>
                        </form>

                    </div><!-- /card-body -->
                </div><!-- /form column -->
            </div><!-- /row -->
        </div><!-- /card -->
    </div><!-- /container -->
</body>
</html>
