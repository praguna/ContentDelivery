<html>
    <head>
        <title>ContentDelivery</title>
        <script>
            function loadData(){
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function(){
                if(this.readyState==4 && this.status==200){
                        var response  = JSON.parse(this.responseText)
                        var res = "";
                        response.content.forEach(value => {
                                var h = document.createElement("H5")
                                var t = document.createTextNode(value.toString());
                                h.appendChild(t);
                                document.getElementsByName("data")[0].appendChild(h);
                        });
                    }
                }
                var id = document.getElementsByName("data")[0].id;
                xhttp.open("POST", "reload", true);
                xhttp.setRequestHeader("Content-type","application/json")
                xhttp.send(id);
            }
            setInterval(()=>{
                loadData();
            },6000);
        </script>
    </head>
    <div style="background-color : #F5F5F5">

        <h3 style="margin-left: 35%">
            <i>
                <b>File ${fileName?capitalize} Contents :</b>
            </i>
        </h3>
        <div style="padding : 10 10 10 10; color : white; background-color: #2b2727"
             id=<#if id??>${id}<#else>not_set</#if> name="data">
            <#list lines as x>
            <h5>${x}</h5>
        </#list>
    </div>
    <h4>Listening for Changes .....</h4>
</body>
</html>