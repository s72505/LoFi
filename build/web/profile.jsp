<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${empty sessionScope.userId}">
    <c:redirect url="login.jsp"/>
</c:if>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>LoFi – My Profile</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <style>
            body  { 
                background:rgba(0,0,0,.5) 
            }
            .card { 
                background:#f3ece2;opacity:.85; 
            }
        </style>
    </head>
        <!-- ===== alerts from ChangePasswordServlet ===== -->
        <c:if test="${param.pwdErr == 'wrong'}">
            <div class="alert alert-danger alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3"
                 style="z-index:2000;" role="alert">
                Current password is incorrect.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <!-- reopen the modal automatically -->
            <script>
                window.addEventListener('DOMContentLoaded', () => {
                    new bootstrap.Modal(document.getElementById('pwdModal')).show();
                });
            </script>
        </c:if>
        <c:if test="${param.pwdErr == 'same'}">
            <div class="alert alert-danger alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3"
                 style="z-index:2000;" role="alert">
                Your new password must be different from the current one.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <script>
                window.addEventListener('DOMContentLoaded', () => {
                    new bootstrap.Modal(document.getElementById('pwdModal')).show();
                });
            </script>
        </c:if>


        <c:if test="${param.pwdOk == '1'}">
            <div class="alert alert-success alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3"
                 style="z-index:2000;" role="alert">
                Password updated successfully.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

    <body class="vh-100 d-flex flex-column h-100 overflow-auto">

        <!-- blurred BG -->
        <div class="position-fixed top-0 start-0 w-100 h-100"
             style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed; 
             filter:blur(5px);z-index:0;"></div>
        <div class="position-fixed top-0 start-0 w-100 h-100"
             style="background:rgba(0,0,0,.55);z-index:0;"></div>

        <div class="d-flex flex-column h-100" style="z-index:1;position:relative;">

            <!-- ===== header ===== -->
            <header class="container-fluid py-2">
                <div class="d-flex justify-content-between align-items-center">
                    <div class="d-flex align-items-center">'
                        <a href="Home" class="btn btn-sm btn-outline-light me-3">
                           <i class="fas fa-arrow-left"></i> Back to Home
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

            <!-- ===== profile summary ===== -->
            <main class="container my-4 flex-grow-1 overflow-auto pt-3">
                <div class="row justify-content-center">
                    <div class="col-12 col-md-8 col-lg-6">
                        <div class="card shadow-lg">

                            <div class="card-body">
                                <h4 class="card-title mb-4 text-center">My Profile</h4>

                                <dl class="row mb-0">
                                    <dt class="col-sm-4">Name:</dt>
                                    <dd class="col-sm-8">${sessionScope.name}</dd>

                                    <dt class="col-sm-4">E-mail:</dt>
                                    <dd class="col-sm-8">${sessionScope.email}</dd>

                                    <dt class="col-sm-4">Phone:</dt>
                                    <dd class="col-sm-8">${sessionScope.phone}</dd>

                                    <dt class="col-sm-4">Role:</dt>
                                    <dd class="col-sm-8 text-capitalize">${sessionScope.role}</dd>
                                </dl>

                                <hr>

                                <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                                    <!-- triggers -->
                                    <button class="btn btn-dark me-md-2"
                                            data-bs-toggle="modal" data-bs-target="#editModal">
                                        <i class="fas fa-user-pen me-1"></i> Update Profile
                                    </button>

                                    <button class="btn btn-outline-dark"
                                            data-bs-toggle="modal" data-bs-target="#pwdModal">
                                        <i class="fas fa-key me-1"></i> Change Password
                                    </button>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </main>

            <!-- ===== footer ===== -->
            <footer class="text-center text-white-50 py-3 mt-auto">
                © 2025 LoFi · <a href="mailto:support@lofi.my" class="text-white-50">support@lofi.my</a>
            </footer>
        </div>

        <!-- ===========================================================
             MODAL : UPDATE PROFILE
             =========================================================== -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <form action="ProfileUpdateServlet" method="post" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Update Profile</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Full Name</label>
                            <input type="text" name="name" class="form-control"
                                   value="${sessionScope.name}" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Phone</label>
                            <input type="tel" name="phone" class="form-control"
                                   value="${sessionScope.phone}" required>
                        </div>

                        <!-- e-mail shown but read-only (cannot be changed) -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold">E-mail</label>
                            <input type="email" class="form-control" value="${sessionScope.email}" disabled>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-dark">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- ===========================================================
             MODAL : CHANGE PASSWORD
             =========================================================== -->
        <div class="modal fade" id="pwdModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <form action="ChangePasswordServlet" method="post" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Change Password</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Current Password</label>
                            <input type="password" name="currentPwd" class="form-control" required minlength="6">
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">New Password</label>
                            <input type="password" name="newPwd" id="newPwd" class="form-control" required minlength="6">
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Confirm New Password</label>
                            <input type="password" name="confirmPwd" id="confirmPwd" class="form-control" required minlength="6">
                            <div id="pwdHelp" class="form-text text-danger d-none">Passwords don’t match.</div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" id="pwdSubmit" class="btn btn-dark" disabled>Update Password</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- ===== Bootstrap JS (required for modals) ===== -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"></script>

        <!-- simple client-side password-match check -->
        <script>
        const newPwd  = document.getElementById('newPwd');
        const confirm = document.getElementById('confirmPwd');
        const help    = document.getElementById('pwdHelp');
        const submit  = document.getElementById('pwdSubmit');

        function checkMatch(){
            const ok = newPwd.value && newPwd.value === confirm.value;
            help.classList.toggle('d-none', ok);
            submit.disabled = !ok;
        }

        [newPwd, confirm].forEach(el => el.addEventListener('input', checkMatch));
        </script>
    </body>
</html>
