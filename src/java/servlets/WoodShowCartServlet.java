package servlets;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import database.*;
import cart.*;
import exception.*;

public class WoodShowCartServlet extends HttpServlet {
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
        HttpSession session = request.getSession(true);
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        response.setContentType("text/html");
        response.setBufferSize(8192);
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Shopping Cart</title></head>");
        getServletContext().getRequestDispatcher("/Banner").include(request, response);
        String bookId = request.getParameter("Remove");
        BookDetails bd;
        
        if (bookId != null) {
            try {
                bd = bookDB.getBookDetails(bookId);
                cart.remove(bookId);
                out.println("<font color='red' size='+2'>You just removed <strong>" +
                    bd.getTitle() + "</strong> <br> &nbsp; <br></font>");
            } catch (BookNotFoundException ex) {
                response.reset();
                throw new ServletException(ex);
            }
        } else if (request.getParameter("Clear") != null) {
            cart.clear();
            out.println("<font color='red' size='+2'><strong>You just cleared your shopping cart!</strong> <br>&nbsp; <br> </font>");
        }

        // Print a summary of the shopping cart
        int num = cart.getNumberOfItems();
        if (num > 0) {
            out.println("<font size='+2'>Your shopping cart contains " + num +
                ((num == 1) ? " item":" items") + "</font><br>&nbsp;");

            // Return the Shopping Cart
            out.println("<table summary='layout'><tr>" +
                "<th align='left'>Quantity</th>" + 
                "<th align='left'>Title</th>" + 
                "<th align='left'>Price</th></tr>");

            Iterator i = cart.getItems().iterator();
            while (i.hasNext()) {
                ShoppingCartItem item = (ShoppingCartItem) i.next();
                bd = (BookDetails) item.getItem();
                out.println("<tr>" +
                    "<td align='right' bgcolor='white'>" + item.getQuantity() + "</td>" + 
                    "<td bgcolor='#ffffaa'><strong><a href='" +
                        response.encodeURL(contextPath+"/BookDetails?Id=" + bd.getId()) + "'>" +
                        bd.getTitle() + "</a></strong></td>" +
                    "<td bgcolor='#ffffaa' align='right'>$&nbsp;" + bd.getPrice() +"</td>" + 
                    "<td bgcolor='#ffffaa'><strong><a href='" + response.encodeURL(contextPath+
                        "/BookShowCart?Remove=" + bd.getId()) + "'>Remove Item</a></strong></td></tr>");
            }

            // Print the total at the bottom of the table
            out.println("<tr><td colspan='5' bgcolor='white'><br></td></tr>" + 
                    "<tr><td colspan='2' align='right' bgcolor='white'>Subtotal</td>" +
                           "<td bgcolor='#ffffaa' align='right'>$&nbsp;" + cart.getTotal() + "</td>" + 
                           "</td><td><br></td></tr></table>");

            // Where to go and what to do next
            out.println("<p> &nbsp; <p><strong><a href='" +
                response.encodeURL(contextPath+ "/BookCatalog") +
                "'>Continue Shopping</a> &nbsp; &nbsp; &nbsp;<a href='" +
                response.encodeURL(contextPath+ "/BookCashier") +
                "'>Check Out</a> &nbsp; &nbsp; &nbsp;" + "<a href='" +
                response.encodeURL(contextPath+"/BookShowCart?Clear=clear") + 
                "'>Clear Cart</a></strong>");
        } else {// Shopping cart is empty!
            out.println("<font size='+2'>Your cart is empty.</font><br> &nbsp; <br><center><a href='" +
                response.encodeURL(contextPath+ "/BookCatalog") +"'>Back to the Catalog</a></center>");
        }
        out.println("</body> </html>");
        out.close();
    }
    @Override
    public String getServletInfo() {
        return "Returns information about the books that the user is in the process of ordering.";
    }
}
