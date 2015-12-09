package servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class BannerServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        output(request, response);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException { 
        output(request, response);
    }
    private void output(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String path= request.getContextPath();
        String contextPath = request.getContextPath();
        PrintWriter out = response.getWriter();
        out.println(
            "<table border='1' width='100%'>"+
              "<tr><th width='100'><img src='"+response.encodeURL(path +"/images/logo.jpg") +"'/></th>"+
                     "<th><a href=\'" +response.encodeURL(contextPath+ "/") +
                "'>BUBBLEWRAP.COM</a></th>"+
                     "<th width='100'><select id='currency' name='currency' onchange='options[selectedIndex].value&&self.location.reload(true)'>\n" +
                        "  <option value='USD' selected>USD</option>\n" +
                        "  <option value='MYR'>MYR</option>\n" +
                        "  <option value='AUD'>AUD</option>\n" +
                        "  <option value='CNY'>CNY</option>\n" +
                        "  <option value='HKD'>HKD</option>\n" +
                        "  <option value='GBP'>GBP</option>\n" +
                        "  <option value='CAD'>CAD</option>\n" +
                        "  <option value='PHP'>PHP</option>\n" +
                        "  <option value='BDT'>BDT</option>\n" +
                        "  <option value='IDR'>IDR</option>\n" +
                        "  <option value='SGD'>SGD</option>\n" +
                     "</select></th>" +
                     "<th width='50'><a href='"+response.encodeURL(path +"/Admin")+"'>Admin</a></th>"+
                     "<th width='50'><img src='./images/plank.jpg'/></th></tr></table>");
    }
}
