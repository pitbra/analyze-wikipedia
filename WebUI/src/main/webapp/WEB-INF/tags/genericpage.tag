<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<html>
    <head>
        <title>Analyze Wikipedia</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" />
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="./styles/style.css" />

        <script src="https://code.jquery.com/jquery-3.2.1.min.js" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>

        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous"></script>

        <script src="./js/arbor.js"></script>
        <script src="./js/renderer.js"></script>
        <script src="./js/graphics.js"></script>


        <script>
            $(document).ready(function () {
                $('#inputSearch').on('keyup', function () {
                    var text = $("#inputSearch").val();

                    if (text.length === 3) {
                        $.ajax({
                            url: "rest/db/articles/" + text,
                            data: null,
                            success: function (data) {
                                $("#inputSearch").autocomplete({
                                    source: data
                                });
                            },
                            dataType: "json"
                        });
                    }
                });
            })

            function search() {
                var search = $("#inputSearch").val();

                $.ajax({
                    url: "rest/db/all/" + search,
                    data: null,
                    success: function (data) {
                        $("#error").hide();
                        clearCanvas();
                        sys.graft(data);
                        sys.stop();
                    },
                    dataType: "json"
                });

                $("#nodes").mousemove(function (e) {
                    handleMouseMove(e);
                });
                $("#nodes").dblclick(function (e) {
                    handleDoubleClick(e);
                });
                $("#nodes").addEventListener('nodeClicked  ', function (e) {
                    handleClick(e);
                });
            }

            function useSubArticles() {
                if ($('#checkSubarticles').is(':visible')) {
                    $.ajax({
                        url: "rest/db/false",
                        data: null,
                        success: function (data) {
                            sys.graft(data);
                        },
                        dataType: "json"
                    });
                    $('#checkSubarticles ').hide();
                } else {
                    $.ajax({
                        url: "rest/db/true",
                        data: null,
                        success: function (data) {
                            sys.graft(data);
                        },
                        dataType: "json"
                    });
                    $('#checkSubarticles ').show();
                }
            }

            function clearCanvas() {
                sys.eachNode(function (node, pt) {
                    //alert(node.data.FullText);
                    sys.pruneNode(node);
                });
            }
        </script>
    </head>
    <body>
        <header id="header">
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
                <a class="navbar-brand" href="#">Analyze Wikipedia</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav mr-auto">
                        <li class="nav-item active">
                            <a class="nav-link" href="index.jsp">Home <span class="sr-only">(current)</span></a>
                        </li>
                        <!--li class="nav-item">
                            <a class="nav-link" href="#">Link</a>
                        </li-->
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Options
                            </a>
                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <!--<a class="dropdown-item" href="javascript:void(0)" onclick="javascript:useSubArticles()"><span id="checkSubarticles" class="fa fa-check" style="display: none"></span>Use </a>-->
                                <a class="dropdown-item" href="javascript:void(0)" onclick="javascript:showLegend()"><span id="checkLegend" class="fa fa-check" style="display: none"></span>Show legend</a>
                            </div>
                        </li>
                        <!--li class="nav-item">
                            <a class="nav-link disabled" href="#">Disabled</a>
                        </li-->
                    </ul>
                    <form class="form-inline my-2 my-lg-0" action="javascript:void(0);">
                        <input class="form-control mr-sm-2" id="inputSearch" type="search" placeholder="Search" aria-label="Search">
                        <button class="btn btn-outline-success my-2 my-sm-0" onclick="search()">Search</button>
                    </form>
                </div>
            </nav>
        </header>
        <div id="body">
            <jsp:doBody/>
        </div>
        <footer class="footer">
            <!--<p id="copyright">Copyright 1927, Future Bits When There Be Bits Inc.</p>-->
        </footer>
    </body>
</html>