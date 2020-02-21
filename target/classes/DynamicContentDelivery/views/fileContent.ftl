<html>
    <head>
        <title>ContentDelivery</title>
        <script>
            setTimeout(()=>{ location.reload(true); },6000);
        </script>
    </head>
    <body style="background-color : #F5F5F5">
        <h3 style="margin-left: 35%">
            <i>
                <b>File ${fileName?capitalize} Contents :</b>
            </i>
        </h3>
        <div style="padding : 10 10 10 10; color : white; background-color: #2b2727">
            <#list lines as x>
            <h5>${x}</h5>
        </#list>
    </div>
    <h4>Listening for Changes .....</h4>
</body>
        </html>