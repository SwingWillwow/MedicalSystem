<%-- 
    Document   : JDBCTestJSP
    Created on : 2017-11-21, 13:50:25
    Author     : qiuyukun

    This is a example of how to connect with mySQL database using connectionPool 
    dataSource
--%>

<%@page import="javax.naming.*,java.io.*,javax.sql.*,java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <%
        InitialContext context = new InitialContext();
        DataSource dataSource = (DataSource)context.lookup("mydb");
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery("select * from `user`");
            if(rset.next()){
    %>
    <table width="100%" border="1">
        <tr align ="left">
            <th>Name</th><th>password</th>
        </tr>
    <%
        do{
    %>
        <tr>
            <td><%=rset.getString("userName")%></td>
            <td><%=rset.getString("password")%></td>
        </tr>
    <%
        }while(rset.next());
    %>
    </table>
    <%        
            }else{
    %>
        No Result from query
    <%        
            }
        }catch(SQLException e){
    %>
    <%=e.getMessage()%>
    <%e.printStackTrace();
    }finally{
        if(rset!=null){
            rset.close();
        }
        if(stmt!=null){stmt.close();}
        if(conn!=null){conn.close();}
        if(context!=null){context.close();}
    }
        
    %>
    
        
        <h1>Hello World!</h1>
    </body>
</html>
