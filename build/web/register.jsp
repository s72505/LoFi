<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <!-- Meta tags for character encoding and responsive scaling -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Register - LoFi</title>

        <!-- Bootstrap and Font Awesome for layout and icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

        <!-- Custom styles for form sizing across screen sizes -->
        <style>
            .reg-card {
                max-width: 100%;
            }
            
            @media (min-width: 992px) {
                .reg-control{
                    font-size: .9rem;
                    padding: .5rem .75rem;
                }
                .reg-card {
                    max-width:920px;
                }
            }

            @media (max-width: 991.98px){
                .reg-control{
                    font-size: 1.1rem;
                    padding: .75rem 1rem; 
                }
            }
        </style>
    </head>

    <body class="vh-100" style="background:rgba(0,0,0,.5)">

        <!-- Blurred background image layer -->
        <div class="position-fixed top-0 start-0 w-100 h-100"
             style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed;
             filter:blur(5px); z-index:0;"></div>

        <!-- Dark transparent overlay -->
        <div class="position-fixed top-0 start-0 w-100 h-100"
             style="background:rgba(0,0,0,.55); z-index:0;"></div>

        <!-- Main content container -->
        <div class="container py-4 d-flex align-items-center justify-content-center" style="z-index:1; position:relative; min-height: 100vh;">
            <!-- Registration card -->
            <div class="card shadow-lg reg-card w-100" style="border-radius:1rem; background-color: #f3ece2;">
                <div class="row g-0">

                    <!-- Left side image (desktop only) -->
                    <div class="col-lg-4 d-none d-lg-block p-0">
                        <img src="img/loginPage.png" class="w-100 h-100"
                             style="object-fit:cover; border-radius:1rem 0 0 1rem;" alt="register">
                    </div>

                    <!-- Registration form -->
                    <div class="col-12 col-lg-8">
                        <div class="card-body p-3 p-lg-4 text-center text-md-start w-100">

                            <!-- Logo and branding -->
                            <div class="d-flex align-items-center mb-2">
                                <img src="img/LoFi.png" alt="LoFi Logo" class="rounded-circle me-2" style="height: 80px;"/>
                                <span class="h3 fw-bold" style="margin-bottom: 0"> · Local Food Finder</span>
                            </div>

                            <!-- Error message if email is taken -->
                            <c:if test="${err == 'E-mail already exists.'}">
                                <div class="alert alert-danger">${err}</div>
                            </c:if>

                            <!-- Success message after registration -->
                            <c:if test="${param.ok == '1'}">
                                <div class="alert alert-success">Registration successful! Please log in.</div>
                            </c:if>

                            <!-- Form begins -->
                            <form action="Register" method="post" autocomplete="off">
                                <h5 class="fw-normal mb-2">Create an account</h5>

                                <!-- Full name input -->
                                <div class="form-outline mb-3">
                                    <input type="text" name="name" class="form-control reg-control form-control-lg"
                                           placeholder="Full name" required>
                                    <label class="form-label fw-semibold">Full Name</label>
                                </div>

                                <!-- Email input -->
                                <div class="form-outline mb-3">
                                    <input type="email" name="email" class="form-control reg-control form-control-lg"
                                           placeholder="Enter email" required>
                                    <label class="form-label fw-semibold">Email address</label>
                                </div>

                                <!-- Phone input -->
                                <div class="form-outline mb-3">
                                    <input type="tel" name="phone" class="form-control reg-control form-control-lg"
                                           placeholder="Phone number" required>
                                    <label class="form-label fw-semibold">Phone</label>
                                </div>

                                <!-- Password input -->
                                <div class="form-outline mb-3">
                                    <input type="password" name="password" id="pwd"
                                           class="form-control reg-control form-control-lg" placeholder="Password"
                                           required minlength="6">
                                    <label class="form-label fw-semibold">Password</label>
                                </div>

                                <!-- Confirm password input -->
                                <div class="form-outline mb-3">
                                    <input type="password" id="confirmPwd"
                                           class="form-control reg-control form-control-lg" placeholder="Confirm password"
                                           required minlength="6">
                                    <label class="form-label fw-semibold">Confirm Password</label>
                                    <div id="pwdHelp" class="form-text text-danger d-none">Passwords don’t match.</div>
                                </div>

                                <!-- Role dropdown -->
                                <div class="form-outline mb-4">
                                    <select name="role" class="form-select reg-control form-select-lg" required>
                                        <option value="" disabled selected class="text-muted" style="color:#6c757d;">
                                            Choose role…
                                        </option>
                                        <option value="customer">Customer</option>
                                        <option value="vendor">Food Vendor</option>
                                    </select>
                                    <label class="form-label fw-semibold">Register as</label>
                                </div>

                                <!-- Submit button -->
                                <button id="submitBtn" class="btn btn-dark btn-lg w-100 mb-3">Register</button>

                                <!-- Link to login -->
                                <p class="mb-0 text-muted">Already have an account?
                                    <a href="login.jsp" class="fw-semibold">Log in here</a>.
                                </p>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- JS: Password match validation -->
        <script>
            const pwd=document.getElementById('pwd');
            const cp=document.getElementById('confirmPwd');
            const msg=document.getElementById('pwdHelp');
            const btn=document.getElementById('submitBtn');
            
            function chk(){
                const ok=pwd.value===cp.value;
                msg.classList.toggle('d-none',ok); // hide/show warning
                btn.disabled=!ok; // disable submit if mismatch
            }

            // Listen for input on both password fields
            pwd.addEventListener('input',chk); 
            cp.addEventListener('input',chk);
        </script>

    </body>
</html>
