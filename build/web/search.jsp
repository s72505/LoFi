<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- HTML5 document starts -->
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Basic page metadata and responsive design -->
    <meta charset="UTF-8">
    <title>LoFi – Choose Cuisine</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap & Font Awesome -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

    <!-- Custom styles for this page -->
    <style>
        .cuisine-icon{
            width:56px; height:56px;
            object-fit:contain;
            filter:drop-shadow(0 0 2px rgba(0,0,0,.4));
        }
        .text-brand { color:#ffc107; }
        .card { background:#f3ece2; opacity:.85; }
        .card:hover {
            background:#f3ece2; opacity:1;
            transform:scale(1.02);
            transition:ease-in-out .2s;
        }
    </style>
</head>

<body class="vh-100 d-flex flex-column h-100 overflow-auto" style="background:#33455a">

    <!-- ===== Blurred background image and overlay ===== -->
    <div class="position-fixed top-0 start-0 w-100 h-100"
         style="background:url('img/loginBackground.jpg') center/cover no-repeat fixed;
                filter:blur(5px); z-index:0;"></div>
    <div class="position-fixed top-0 start-0 w-100 h-100"
         style="background:rgba(0,0,0,.55); z-index:0;"></div>

    <!-- ===== Page content wrapper ===== -->
    <div class="d-flex flex-column h-100" style="z-index:1; position:relative;">

        <!-- ===== Header navigation bar ===== -->
        <header class="container-fluid py-2">
            <div class="d-flex justify-content-between align-items-center">
                <!-- Back button and branding -->
                <div class="d-flex align-items-center">
                    <a href="Home" class="btn btn-sm btn-outline-light me-3">
                        <i class="fas fa-arrow-left"></i> Back to Home
                    </a>
                    <img src="img/LoFi.png" alt="LoFi" class="rounded-circle me-2" style="height:48px;">
                    <span class="h4 mb-0 text-white">Local Food Finder</span>
                </div>

                <!-- Logout button -->
                <form action="Logout" method="get" class="m-0">
                    <button class="btn btn-sm btn-outline-light">
                        <i class="fa fa-sign-out-alt me-1"></i> Logout
                    </button>
                </form>
            </div>
        </header>

        <!-- ===== Main section with cuisine options ===== -->
        <main class="container my-auto flex-grow-1 overflow-auto pt-3">
            <h2 class="text-center text-white mb-4">What are you craving?</h2>

            <!-- Cuisine category grid -->
            <div class="row row-cols-1 row-cols-md-3 g-4">
                <%
                  // Java array to store cuisine labels and corresponding image file names
                  String[][] preset = {
                    {"Hot Food","hotFood"},
                    {"Noodles and Pasta","noodleAndPasta"},
                    {"Rice-Based","riceBased"},
                    {"Snacks","snacks"},
                    {"Desserts","desserts"},
                    {"Beverages","beverages"},
                    {"Fast Food","fastFood"},
                    {"Seafood","seafood"},
                    {"Breakfast Sets","breakfastSets"},
                    {"Street Food","streetFood"}
                  };

                  // Loop through the array to generate each cuisine card
                  for (String[] c : preset) {
                %>
                    <div class="col">
                        <!-- Card with cuisine link to SearchServlet -->
                        <a href="SearchServlet?cuisine=<%=java.net.URLEncoder.encode(c[0],"UTF-8")%>"
                           class="text-decoration-none">
                            <div class="card h-100 text-center shadow-lg">
                                <div class="card-body d-flex flex-column align-items-center justify-content-center">
                                    <img src="img/<%=c[1]%>.svg" alt="<%=c[0]%> icon"
                                         class="cuisine-icon text-brand mb-3">
                                    <h5 class="card-title text-dark"><%=c[0]%></h5>
                                </div>
                            </div>
                        </a>
                    </div>
                <%
                  } // End for loop
                %>
            </div>
        </main>

        <!-- ===== Footer ===== -->
        <footer class="text-center text-white-50 py-3 mt-auto">
            © 2025 LoFi · Contact:
            <a href="mailto:support@lofi.my" class="text-white-50">support@lofi.my</a>
        </footer>
    </div>
</body>
</html>
