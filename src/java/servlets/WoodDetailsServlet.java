package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import exception.*;

public class WoodDetailsServlet extends HttpServlet {
    private BookDBAO bookDB;
    @Override
    public void init() throws ServletException {
        bookDB = (BookDBAO) getServletContext().getAttribute("bookDB");
        if (bookDB == null) throw new UnavailableException("Couldn't get database.");
    }
    @Override
    public void destroy() {bookDB = null;}
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        response.setBufferSize(8192);
        PrintWriter out = response.getWriter();
        String bookId = request.getParameter("Id");
        out.println("<html><head><title> WOOD Details:" +bookId+ "</title></head><body>");
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        if (bookId != null) {
            try {
                BookDetails bd = bookDB.getBookDetails(bookId);
                out.println("<br/>"+
                    "<table border='1'>"+
                    "<tr><th colspan='2'>WOOD Information</th><tr>"+
                    "<tr><td>ID</td><td>"+bookId+"</td></tr>"+
                    "<tr><td>Title</td><td>"+bd.getTitle()+"</td></tr>"+
                    "<tr><td>Price</td><td>$&nbsp;"+bd.getPrice()+"</td></tr></table>");
            // Go back to catalog
            out.println("<p> &nbsp; <p><strong><a href='" +
                response.encodeURL(request.getContextPath() + "/WoodCatalog") +
                "'>Continue Shopping</a>" );                
            } catch (BookNotFoundException ex) {
                out.println("<center><h1>Book Not Found</h1></center>");
            }
            finally{
                out.println("</body></html>");
                out.close();
            }
        }        
    }
    @Override
    public String getServletInfo() {return "Returns information about WOOD";}
}
