<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Top Trumps Game</title>
        <style>
            *{
                margin:0;
                padding:0;
            }
            .section {
                background-color:rgb(146, 198, 228);
                color:white;
                font-size: 10px;
                /* float: left; */
                margin: 10px;
                padding: 15px;
                width: 250px;
                height: 80px;
                border: 1px solid white;
                border-radius: 8px;
                display: inline-block;
            }
            .button {
                background-color:rgb(82, 149, 189);
                border: none;
                color: white;
                width: 200px;
                height: 40px;
                text-align: center;
                text-decoration: none;
                font-size: 15px;
                border-radius: 8px;
                margin: 10px auto;
            }
            .bg {
                background-repeat: no-repeat;
                background-image: linear-gradient(hsl(191, 27%, 71%), #fcfcfd);
                height: 100px;
            }

        </style>
    </head>
    
    <body class="bg">
        <center>
                <h1 style="background-color:rgb(75, 136, 177); font-size: 20px; color:white; text-align:center; height: 30px; width: 800px;">Top Trumps Game</h1>
                
    
            <div class="section">
                <form action="/toptrumps/newGame" method="get"><button class="button" type="submit">New Game</button></form>
                <p style="font-size:15px;">Start a new Top Trumps Game </p>
            </div>
            
            <div class="section">
                <form action="/toptrumps/stats" method="get"><button class="button" type="submit">Statistics</button></form>
                <p style="font-size:15px;">Get Statisics from past Games </p>
            </div>
        </center>

<!-- This is a comment -->

    </body>
</html>