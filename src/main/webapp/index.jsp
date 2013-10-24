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
                    <a class="hiddenanchor" id="toregister"></a>
                    <a class="hiddenanchor" id="tologin"></a>
                    <div id="wrapper">
                        <div id="login" class="animate form">
                            <form method="post" action="home.jsp" autocomplete="on"> 
                                <h1>Log in</h1> 
                                <p> 
                                    <label for="username" class="uname" data-icon="u" >Your username</label>
                                    <input id="username" name="username" required="required" type="text" placeholder="myusername"/>
                                </p>
                                <p> 
                                    <label for="password" class="youpasswd" data-icon="p">Your password</label>
                                    <input id="password" name="password" required="required" type="password" placeholder="eg. X8df!90EO"/> 
                                </p>
                                <p class="keeplogin"> 
									<input type="checkbox" name="loginkeeping" id="loginkeeping" value="loginkeeping"/> 
									<label for="loginkeeping">Keep me logged in</label>
								</p>
                                <p class="login button"> 
                                    <input type="submit" value="Login" /> 
								</p>
                                <p class="change_link">
									Not a member yet ?
									<a href="#toregister" class="to_register">Join us</a>
								</p>
                            </form>
                        </div>
                        <div id="register" class="animate form">
                            <form method="post" action="home.jsp" autocomplete="on"> 
                                <h1>Sign up</h1> 
                                <p> 
                                    <label for="username-register" class="uname" data-icon="u">Your username</label>
                                    <input id="username-register" name="username" required="required" type="text" placeholder="mysuperusername"/>
                                </p>
                                <p> 
                                    <label for="email-register" class="youmail" data-icon="e" >Your email</label>
                                    <input id="email-register" name="email" required="required" type="email" placeholder="mysupermail@mail.com"/> 
                                </p>
                                <p> 
                                    <label for="password-register" class="youpasswd" data-icon="p">Your password</label>
                                    <input id="password-register" name="password" required="required" type="password" placeholder="eg. X8df!90EO"/>
                                </p>
                                <p class="signin button"> 
									<input type="submit" value="Sign up"/> 
								</p>
                                <p class="change_link">  
									Already a member ?
									<a href="#tologin" class="to_register">Go and log in</a>
								</p>
                            </form>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </body>
</html>