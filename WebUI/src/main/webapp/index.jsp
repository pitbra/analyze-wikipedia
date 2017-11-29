<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>    
    <jsp:body>


        <div>
            <canvas id="nodes" style="position:absoulte; left: 0px; top:56px;">bla</canvas>
        </div>

        <script>

            var sys;

            $(function () {
                htmlCanvas = $("#nodes");
                htmlCanvas[0].width = window.innerWidth;
                htmlCanvas[0].height = window.innerHeight - 2 * 56;

                sys = arbor.ParticleSystem(1000, 400, 1);
                sys.parameters({gravity: true});
                sys.renderer = Renderer("#nodes");
                
                //var data = {nodes:{animals:{'color':'red','shape':'dot','label':'Animals'},dog:{'color':'green','shape':'dot','label':'dog'},cat:{'color':'blue','shape':'dot','label':'cat'}},edges:{animals:{dog:{},cat:{}}}};
                //sys.graft(data);
                
                //var active = sys.addNode('active', {'color': 'red', 'label': 'Active'});
                
                $.ajax({
                        url: "rest/db/false",
                        data: null,
                        success: function (data) {
                            sys.graft(data);
                            /*data.forEach(function(item, index) {
                                var node = sys.addNode(item["_type"], {'color':'green', 'label': item["_name"]});
                                sys.addEdge(active, node);
                            });*/
                       },
                        dataType: "json"
                });
                    
                //htmlCanvas[0].scale(20,20);
            });
            /*$(function () {
             var
             // Obtain a reference to the canvas element
             // using its id.
             htmlCanvas = $("#nodes");
             
             // Obtain a graphics context on the
             // canvas element for drawing.
             context = htmlCanvas[0].getContext('2d');
             
             // Start listening to resize events and
             // draw canvas.
             initialize();
             
             function initialize() {
             // Register an event listener to
             // call the resizeCanvas() function each time 
             // the window is resized.
             window.addEventListener('resize', resizeCanvas, false);
             
             // Draw canvas border for the first time.
             resizeCanvas();
             }
             
             // Display custom canvas.
             // In this case it's a blue, 5 pixel border that 
             // resizes along with the browser window.					
             function redraw() {
             context.strokeStyle = 'blue';
             context.lineWidth = '5';
             context.strokeRect(0, 0, window.innerWidth, window.innerHeight);
             }
             
             // Runs each time the DOM window resize event fires.
             // Resets the canvas dimensions to match window,
             // then draws the new borders accordingly.
             function resizeCanvas() {
             redraw();
             }
             
             })*/
        </script>
    </jsp:body>
</t:genericpage>
