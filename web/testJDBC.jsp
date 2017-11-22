<%-- 
    Document   : testJDBC
    Created on : 2017-11-21, 19:45:21
    Author     : qiuyukun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            java.sql.Connection conn = null;
            java.lang.String strConnString;
            try{
            conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost/mydb","root","root");
            %>
            链接数据库成功!
            <%
            }catch(java.sql.SQLException e){
                out.print(e.toString());
            }finally{
                if(conn!=null) conn.close();
            }
        %>
        <h1>Hello World!</h1>
    </body>
</html>
