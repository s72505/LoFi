<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="com.lofi.dao.DBHelper"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>LoFi – Admin Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <style>
        /* theme-wide colours & card look */
        body        { background:rgba(0,0,0,.5); }
        .card-bg    { background:#f3ece2; opacity:.85; }
    </style>
</head>

<body class="vh-100 d-flex flex-column h-100 overflow-auto">

<!-- --- blurred BG layers (exactly like other pages) -------------------- -->
<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed;
            filter:blur(5px); z-index:0;"></div>
<div class="position-fixed top-0 start-0 w-100 h-100"
     style="background:rgba(0,0,0,.55); z-index:0;"></div>

<!-- ================== wrapper ================== -->
<div class="d-flex flex-column h-100" style="z-index:1; position:relative;">

    <!-- ---------- header (same height as search / favourites) ----------- -->
    <header class="container-fluid py-2">
        <div class="d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center">
                <a href="Home" class="btn btn-sm btn-outline-light me-2">
                    <i class="fas fa-arrow-left me-1"></i> Back to Home
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

    <!-- ---------- main content  ---------------------------------------- -->
    <main class="container my-4 flex-grow-1 overflow-auto pt-3">

        <div class="card shadow-lg card-bg">
            <div class="card-body">
                <h3 class="card-title mb-4 text-center">Pending Food-Spot Requests</h3>

                <div class="table-responsive">
                    <table class="table table-striped align-middle">
                        <thead class="table-dark text-center">
                            <tr>
                                <th scope="col">Req&nbsp;ID</th>
                                <th scope="col">Restaurant</th>
                                <th scope="col">Vendor&nbsp;ID</th>
                                <th scope="col">Vendor&nbsp;Name</th>
                                <th scope="col">Phone</th>
                                <th scope="col">Submitted&nbsp;At</th>
                                <th scope="col">Action</th>
                            </tr>
                        </thead>
                        <tbody class="table-group-divider text-center">
                            <%
                                /* ----- DB query ----- */
                                String sql =
                                    "SELECT f.request_id, " +
                                    "       f.restaurant_name, " +
                                    "       u.user_id, " +
                                    "       u.name, " +
                                    "       u.phone, " +
                                    "       f.submitted_time " +
                                    "  FROM food_spot_approval f " +
                                    "  JOIN users u ON f.user_id = u.user_id " +
                                    " WHERE f.status IS NULL " +
                                    " ORDER BY f.submitted_time DESC";

                                try (Connection con = DBHelper.getConnection();
                                     Statement  st  = con.createStatement();
                                     ResultSet  rs  = st.executeQuery(sql)) {

                                    while (rs.next()) {
                            %>
                                        <tr>
                                            <td><%= rs.getInt("request_id") %></td>
                                            <td class="text-start"><%= rs.getString("restaurant_name") %></td>
                                            <td><%= rs.getInt("user_id") %></td>
                                            <td class="text-start"><%= rs.getString("name") %></td>
                                            <td><%= rs.getString("phone") %></td>
                                            <td><%= rs.getTimestamp("submitted_time") %></td>
                                            <td>
                                                <form action="ReviewRequestServlet" method="get" class="m-0">
                                                    <input type="hidden" name="requestID"
                                                           value="<%= rs.getInt("request_id") %>">
                                                    <button class="btn btn-sm btn-outline-primary">
                                                        <i class="fas fa-eye me-1"></i>Details
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                            <%
                                    } // end while
                                } catch (Exception e) {
                            %>
                                    <tr>
                                        <td colspan="7" class="text-danger fw-semibold">
                                            Oops: <%= e.getMessage() %>
                                        </td>
                                    </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div> <!-- /.table-responsive -->

            </div><!-- /.card-body -->
        </div><!-- /.card -->

    </main>

    <!-- ---------- footer ----------------------------------------------- -->
    <footer class="text-center text-white-50 py-3 mt-auto">
        © 2025 LoFi · Contact:
        <a href="mailto:support@lofi.my" class="text-white-50">support@lofi.my</a>
    </footer>
</div><!-- /.wrapper -->

</body>
</html>
