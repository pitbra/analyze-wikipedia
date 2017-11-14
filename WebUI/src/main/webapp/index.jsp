<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpage>    
    <jsp:body>

        <script>
            $(function () {
                $.ajax({
                    url: "rest/db",
                    data: null,
                    success: function (data) {
                        data.forEach(function(item, index) {
                            $("#nodes").html("<div class="+item["_type"]+">"+item["_name"]+"</div>");
                        });
                    },
                    dataType: "json"
                });
            });
        </script>

        <div id="nodes"></div>
    </jsp:body>
</t:genericpage>
