package com.nt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


public class RegistrationServlet extends HttpServlet {
	private static final String  STUDENT_INSERT_QUERY="INSERT INTO STUDENT VALUES(?,?,?,?)";
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	   PrintWriter pw=null;
	   int sno=0;
	   String name=null,addrs=null;
	   float avg=0.0f;
	   Connection con=null;
	   PreparedStatement ps=null;
	   int result=0;
	   try {
		//general settings
	   pw=res.getWriter();
	   res.setContentType("text/html");
	   //read form data
	   sno=Integer.parseInt(req.getParameter("sno"));
	   name=req.getParameter("sname");
	   addrs=req.getParameter("sadd");
	   avg=Float.parseFloat(req.getParameter("avg"));
	   
	   //get Pooled jdbc con object
		   con=getPooledConnection();
		  //create Jdbc PreparedStatement object
		   ps=con.prepareStatement(STUDENT_INSERT_QUERY);
		   //set form data as the query param values
		   ps.setInt(1,sno);
		   ps.setString(2,name);
		   ps.setString(3,addrs);
		   ps.setFloat(4,avg);
		   //execute the Query
		   result=ps.executeUpdate();
		   //process theResult
		   if(result==0)
			   pw.println("<h1 style='color:red'> registation failed </h1>");
		   else
			   pw.println("<h1 style='color:green'> registation succeded </h1>");
		   //add hyperlink
		   pw.println("<br><a href='register.html'> home </a>");
		   
	   }//try
	   catch(SQLException se) {
		   se.printStackTrace();
		   pw.println("<h1 style='color:red'> registation failed </h1>");
	   }
	   catch(Exception e) {
		   e.printStackTrace();
		   pw.println("<h1 style='color:red'> Internal problem </h1>");
	   }
	   finally {
		   //close jdbc objs
		   try {
			   if(ps!=null)
				   ps.close();
		   }
		   catch(SQLException se) {
			   se.printStackTrace();
		   }
		   
		   try {
			   if(con!=null)
				   con.close();
		   }
		   catch(SQLException se) {
			   se.printStackTrace();
		   }
		   try {
			   if(pw!=null)
				   pw.close();
		   }
		   catch(Exception e) {
			   e.printStackTrace();
		   }
	   }//finally
	}//doGet(-,-)
	
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	    doGet(req,res);
	}
	
	private Connection  getPooledConnection()throws Exception{
		ServletConfig cg=null;
		String jndi=null;
		InitialContext ic=null;
		DataSource ds=null;
		Connection con=null;
		//get ServletConfig obj  and get jndi name
		cg=getServletConfig();
		jndi=cg.getInitParameter("jndi");
		//create InitialContext object
		ic=new InitialContext();
		//get Datasource obj from jndi registry
		ds=(DataSource) ic.lookup(jndi);
		//get Pooled jdbc con
		con=ds.getConnection();
		return con;
	}//method

}//class
