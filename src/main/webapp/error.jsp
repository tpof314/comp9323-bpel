<html lang="en" class="no-js">
    <head>
        <meta charset="utf-8" />
        <title>Welcome to E-Enterprise Project</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <meta name="description" content="Login and Registration Form with HTML5 and CSS3" />
        <meta name="keywords" content="html5, css3, form, switch, animation, :target, pseudo-class" />
        <meta name="author" content="Codrops" />
        <link rel="stylesheet" type="text/css" href="css/index.css" />
        <link rel="stylesheet" type="text/css" href="css/index-style.css" />
		<link rel="stylesheet" type="text/css" href="css/animate-custom.css" />
    </head>
    <body>
        <div class="container">
            <header>
                <h1><img src="images/unsw-logo.png" height="30px" align="middle">&nbsp; University of New South Wales <span>E-Enterprise Project</span></h1>
                <h2>Welcome to Online BEPL Integrated Development Environment</h2>
            </header>
            <section>
            	<div id="container_login_and_register">
	                <div id="wrapper">
	                	<div id="login" class="animate form">
	                		<h1>Error</h1>
	                		<br/><br/><br/><br/>
	                		<p style="font: bold 18px/16px Arial, Helvetica, sans-serif; color: red; text-align: center">Invaild username or wrong password !</p>
	                		<br/><br/><br/><br/>
	                		<p class="login button">
	                			<input type="submit" value="Login again" onclick="window.location.href='index.jsp#tologin'"/>
	                		</p>
	                		<p class="change_link">
	                			Not a member yet ?
	                			<a href="index.jsp#toregister" class="to_register">Join us</a>
	                		</p>
	                	</div>
	                </div>
	            </div>
            </section>
        </div>
    </body>
</html>