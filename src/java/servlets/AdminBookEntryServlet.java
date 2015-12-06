package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
//import cart.*;
//import exception.*;

public class AdminBookEntryServlet extends HttpServlet {
    private BookDBAO bookDB;
    @Override
    public void init() throws ServletException 
    {
        bookDB = (BookDBAO) getServletContext().getAttribute("bookDB");
        if (bookDB == null) throw new UnavailableException("Couldn't get database.");
    }
    @Override
    public void destroy() {bookDB = null;}
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        doGet(request,response);
    }
    private String getParamWithoutNull(HttpServletRequest request,String param){
         String s=request.getParameter(param);
         return (s==null)?"":s;
    }
    private static String showMsgInErrIcon(String msg){
        return msg.equals("")?"":"<img src='images/error.jpg' alt='"+msg+"'/>";
    }
    private static String validateId(String s){
       return showMsgInErrIcon((s.length()==0)?"No missing ID":((s.length()>4)?"ID too long":""));
    }
    private static String validateTitle(String s){
       return showMsgInErrIcon((s.length()==0)?"No missing Title":((s.length()>64)?"Title too long":""));
    }   
    private static String validatePrice(String s){
       if(s.length()==0) s = "No missing Price";
       else{
           try{
               float f = Float.parseFloat(s);
               s = (f<0)?"Price cannot be negative":"";
           }
           catch(Exception e){
               s = "Invalid Price format";
           }           
       }
       return showMsgInErrIcon(s);
    }      
    private static String validateDescription(String s){
       return showMsgInErrIcon((s.length()>128)?"Description more than 128":"");
    }
    private static String validateInventory(String s){
       if(s.length()==0) s = "No missing Inventory";
       else{
           try{
               int i = Integer.parseInt(s);
               s = (i<0)?"Inventory cannot be less 0":"";
           }
           catch(Exception e){
               s = "Invalid Inventory format";
           }           
       }
       return showMsgInErrIcon(s);
    }   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        String contextPath=request.getContextPath();
        String sMode=getParamWithoutNull(request,"Mode");
        String sOperation=getParamWithoutNull(request,"Operation");
        
        if(sOperation.equals("Cancel"))
        {
           response.sendRedirect(contextPath+(sMode.equals("New")?"/Admin":"/AdminUpdate"));
           return;
        }
        String sId=getParamWithoutNull(request,"Id");
        String sTitle=getParamWithoutNull(request,"Title");
        String sPrice=getParamWithoutNull(request,"Price");
        String sDescription=getParamWithoutNull(request,"Description");
        String sInventory=getParamWithoutNull(request,"Inventory");

        String sIdErr="";
        String sTitleErr="";
        String sPriceErr="";
        String sDescriptionErr="";
        String sInventoryErr="";
        String sHeader=sMode.equals("New")?"New Wood":"Update Wood";
        String sButton=sMode.equals("New")?"Add":"Update";
 
        if(sOperation.equals("")){
          if(sMode.equals("Update")){
            BookDetails bd=bookDB.getBookDetails(request.getParameter("ID"));
            sId=bd.getId();
            sTitle=bd.getTitle();
            sPrice=""+bd.getPrice();
            sDescription=bd.getDescription();
            sInventory=""+bd.getInventory();
          }
        }
        else{
          if(sMode.equals("Update")) sId = getParamWithoutNull(request,"ID");
          sIdErr=validateId(sId);
          sTitleErr=validateTitle(sTitle);
          sPriceErr=validatePrice(sPrice);
          sDescriptionErr=validateDescription(sDescription);
          sInventoryErr=validateInventory(sInventory);
          if((sIdErr+sTitleErr+sPriceErr+sDescriptionErr+sInventoryErr).equals("")){
            if(sMode.equals("New")){
              if(sOperation.equals("Add")){
                bookDB.addBook(sId,sTitle,Float.parseFloat(sPrice),sDescription,Integer.parseInt(sInventory));
                response.sendRedirect(contextPath+"/Admin");
                return;
              }
            }
            else{
              if(sMode.equals("Update")){
                if(sOperation.equals("Update")){
                  bookDB.deleteBook(sId);
                  bookDB.addBook(sId,sTitle,Float.parseFloat(sPrice),sDescription,Integer.parseInt(sInventory));
                  response.sendRedirect(contextPath+"/AdminUpdate");
                  return;
                }             
              }
            }
          }
        }
        response.setContentType("text/html");
        response.setBufferSize(8192);
        PrintWriter out = response.getWriter();
     
        out.println("<html><head><title>"+((sId==null)?"New":"Update")+" Book</title></head><body>");
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        out.println(
            "<html>"+
            "  <head>"+
            "    <title>"+sHeader+"</title>"+
            "  </head>"+
            "  <body>"+
            "  <form method='get'>"+
            "    <table border='1' bgcolor='Beige'>"+
            "       <tr><th><img src='images/logo.jpg'/></th><th colspan=3' bgcolor='Beige'><h2>"+sHeader+" Entry</h2></th></tr>"+
            "       <tr><td rowspan='8' bgcolor='beige'>&nbsp;</td><td title='Not more than 4 characters'>ID</td><td bgcolor='cadetblue'>&nbsp;</td><td><input type='text' name='Id' value='"+sId+"' size='4'"+(sMode.equals("Update")?" disabled='disabled'":"")+"'/>"+sIdErr+"</td></tr>"+
            "       <tr><td  title='Not more that 64 characters'>TITLE</td><td bgcolor='cadetblue'>&nbsp;</td><td><input type='text' name='Title' value='"+sTitle+"' size='64' />"+sTitleErr+"</td></tr>"+
            "       <tr><td>PRICE</td><td bgcolor='cadetblue'>&nbsp;</td><td><input type='text' name='Price' value='"+sPrice+"' />"+sPriceErr+"</td></tr>"+
            "       <tr><td colspan='2' title='Not more that 128 characters'>DESC.</td><td><textarea name='Description' rows='3' cols='40'>"+sDescription+"</textarea>"+sDescriptionErr+"</td></tr>"+
            "       <tr><td>INVENTORY</td><td bgcolor='cadetblue'>&nbsp;</td><td><input type='text' name='Inventory' value='"+sInventory+"' size='5' />"+sInventoryErr+"</td></tr>"+
            "       <tr><th colspan='3'><input type='submit' name='Operation' value='"+sButton+"'/><input type='submit' name='Operation' value='Cancel'/><input type='hidden' name='Mode' value='"+sMode+"'/><input type='hidden' name='ID' value='"+sId+"'/></th></tr>"+
            "    </table>"+
            "</form>"+
            "</body></html>"
        );
        out.close();
    }
    @Override
    public String getServletInfo() {return "Admin Function to handle book entry.";}
}
