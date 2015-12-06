<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html xmlns="[http://www.w3.org/1999/xhtml]" xml:lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Login Failure</title>
    </head>
    <body>
        <% 
            getServletContext().getRequestDispatcher("/Banner").include(request, response); 
            request.logout();
        %>    
        <h1>Login Failed!</h1>
        <p>
            Please <a href="/Admin">try again</a>.
        </p>
    </body>
</html>