<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>    
    <jsp:body>


        <div id="nodesDiv" style="position: absolute!important; overflow: scroll;">
            <canvas id="nodes" style="position:absoulte; left: 0px; top:56px;">
                <menu type="context" id="canvas_context">
                    <menuitem label="Alles doof" />
                </menu>
            </canvas>
        </div>

        <script>

            var sys;

            $(function () {
                init();
            });

            function init() {
                htmlCanvas = $("#nodes");
                htmlCanvas[0].width = window.innerWidth;
                htmlCanvas[0].height = (window.innerHeight - 2 * 56);

                var canvasOffset = $("#nodes").offset();
                offsetX = canvasOffset.left;
                offsetY = canvasOffset.top;

                var div = $("#nodesdiv");
                div.width = window.innerWidth;
                div.height = window.innerHeight;

                sys = arbor.ParticleSystem(1000, 400, 1);
                sys.parameters({gravity: true});
                sys.renderer = Renderer("#nodes");

                $("#error").show();
            }

            function handleMouseMove(e) {
                var mouseX = parseInt(e.clientX - offsetX);
                var mouseY = parseInt(e.clientY - offsetY);

                var point = new arbor.Point(mouseX, mouseY);
                var nearest = sys.nearest(point);
                if (nearest.distance < 50) {
                    $("#tooltip").show();
                    $("#tooltip").html(nearest.node.data.FullText);
                    $("#tooltip").css({'left': mouseX + 20, 'top': mouseY});
                } else {
                    $("#tooltip").hide();
                }
            }

            function handleDoubleClick(e) {
                var mouseX = parseInt(e.clientX - offsetX);
                var mouseY = parseInt(e.clientY - offsetY);

                var point = new arbor.Point(mouseX, mouseY);
                var nearest = sys.nearest(point);

                if (nearest.distance < 50) {
                    $("#info_dialogue_title").html(nearest.node.data.FullText);
                    expandInfo();
                }
            }

            function handleClick(e) {
                alert(e);
            }

            function compressInfo() {
                $("#info_dialogue_show").show();
                $("#info_dialogue").hide();
            }

            function expandInfo() {
                $("#info_dialogue_show").hide();
                $("#info_dialogue").show();
            }

            function showLegend() {
                if ($('#checkLegend').is(':visible')) {
                    $('#legend').hide();
                    $('#checkLegend').hide();
                } else {
                    $('#legend').show();
                    $('#checkLegend').show();
                }
            }
        </script>

        <div id="error" style="display: none">Bitte suchen Sie nach einem Artikel, vorher wird aufgrund der Größe des Graphen keiner angezeigt.</div>
        <div id="tooltip" class="wa_tooltip" style="position: relative; z-index: 10000000"></div>
        <div id="info_dialogue" class="card wa_infodialogue" style="position: fixed; top: 56px; bottom: 56px; right: 0; width: 400px; display:none;">
                <h5 id="info_dialogue_title" class="card-header">Artikel</h2>
                <button onclick="compressInfo()" class="btn btn-outline-secondary" style="position: fixed; top: 60px; right: 4px;" title="Compress the Info-Dialogue"><span class="fa fa-compress"></span></button>
            <div>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.</div>
        </div>
        <div id="info_dialogue_show" class="wa_infodialogue" style="position: fixed; top:60px; right:4px; display:none;">
            <button onclick="expandInfo()" class="btn btn-outline-secondary" title="Expand the Info-Dialogue"><span class="fa fa-expand"></span></button>
        </div>

        <div class="card wa_legend" id="legend" style="position:fixed; bottom: 56px; left:0px; width: 15%; display: none">
            <h5 class="card-header">Legend</h5>
            <div class="card-body">
                <h6 class="card-subtitle mb-2 text-muted">Nodes</h6>
                Article: ||||||<br />
                SubArticle: ||||||<br />
                Externe: ||||||<br />
                SubExterne: ||||||<br />
                <br />
                <h6 class="card-subtitle mb-2 text-muted">Edges</h6>
                Has: ||||||<br />
                LinkTo: ||||||<br />
            </div>
        </div>
    </jsp:body>
</t:genericpage>