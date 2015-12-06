package database;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import java.util.*;
import exception.*;
import cart.*;
public class BookDBAO {
    private ArrayList books;
    Connection con;
    private boolean conFree = true;
    public BookDBAO() throws Exception {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/BookDB");
            con = ds.getConnection();
        } catch (Exception ex) {
            throw new Exception("Couldn't open connection to database: " +ex.getMessage());
        }
    }
    public void remove() {
        try {con.close(); } catch (SQLException ex) {System.out.println(ex.getMessage());}
    }
    protected synchronized Connection getConnection() {
        while (conFree == false) {
            try { wait(); } catch (InterruptedException e) {}
        }
        conFree = false;
        notify();
        return con;
    }
    protected synchronized void releaseConnection() {
        while (conFree == true) {
            try {wait(); } catch (InterruptedException e) {}
        }
        conFree = true;
        notify();
    }
    public List getBooks() throws BooksNotFoundException {
        books = new ArrayList();
        try {
            getConnection();
            PreparedStatement prepStmt = con.prepareStatement("select * from woodstock");
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(5) > 0) {
                    books.add(new BookDetails(rs.getString(1), rs.getString(2),
                            rs.getFloat(3), rs.getString(4),rs.getInt(5)));
                }
            }
            //prepStmt.close();
        } 
        catch (SQLException ex) { throw new BooksNotFoundException(ex.getMessage()); }
        finally {releaseConnection();}
        Collections.sort(books);
        return books;
    }
    public BookDetails getBookDetails(String bookId)throws BookNotFoundException {
        try {
            getConnection();
            PreparedStatement prepStmt = con.prepareStatement("select * from woodstock where id = ? ");
            prepStmt.setString(1, bookId);
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                BookDetails bd = new BookDetails (rs.getString(1), rs.getString(2),
                            rs.getFloat(3), rs.getString(4),rs.getInt(5));
                        
                //prepStmt.close();
                releaseConnection();
                return bd;
            } else {
                //prepStmt.close();
                releaseConnection();
                throw new BookNotFoundException("Couldn't find book: " +bookId);
            }
        } catch (SQLException ex) {
            releaseConnection();
            throw new BookNotFoundException("Couldn't find book: " + bookId +" " + ex.getMessage());
        }
    }
    public boolean deleteBook(String bookId)throws BookNotFoundException {
        try {
            getConnection();
            PreparedStatement prepStmt = con.prepareStatement("Delete from woodstock where id = ? ");
            prepStmt.setString(1, bookId);
            prepStmt.executeUpdate();
            releaseConnection();
            return true;
        } catch (SQLException ex) {
            releaseConnection();
            throw new BookNotFoundException("Couldn't delete book: " + bookId +" " + ex.getMessage());
        }
    }
    public boolean addBook(String id, String title, 
        float price,String description, int inventory)
            throws NewBookException{
        try {
            getConnection();
            PreparedStatement prepStmt = con.prepareStatement("INSERT INTO woodstock VALUES(?,?,?,?,?)");
            prepStmt.setString(1,id);
            prepStmt.setString(2,title);
            
            prepStmt.setFloat(3,price);
            
            prepStmt.setString(4,description);
            prepStmt.setInt(5,inventory);
            prepStmt.executeUpdate();
            releaseConnection();
            return true;
        } catch (SQLException ex) {
            releaseConnection();
            throw new NewBookException("Couldn't  Add new book due to\n"+ex.getMessage());
        }
    }
    public void buyBooks(ShoppingCart cart) throws OrderException {
        Collection items = cart.getItems();
        Iterator i = items.iterator();
        try {
            getConnection();
            con.setAutoCommit(false);
            while (i.hasNext()) {
                ShoppingCartItem sci = (ShoppingCartItem) i.next();
                BookDetails bd = (BookDetails) sci.getItem();
                String id = bd.getId();
                buyBook(id, sci.getQuantity());
            }
            con.commit();
            con.setAutoCommit(true);
            releaseConnection();
        } catch (Exception ex) {
            try {
                con.rollback();
                throw new OrderException("Transaction failed: " + ex.getMessage());
            } catch (SQLException sqx) {                
                throw new OrderException("Rollback failed: " + sqx.getMessage());
            }
            finally{ releaseConnection();}
        }
    }
    private void buyBook(String bookId, int quantity) throws OrderException {
        try {
            PreparedStatement prepStmt = con.prepareStatement("select * from woodstock where id = ? ");
            prepStmt.setString(1, bookId);
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                int inventory = rs.getInt(7);
                //prepStmt.close();
                if ((inventory - quantity) >= 0) {
                    prepStmt = con.prepareStatement("Update books set inventory = inventory - ? where id = ?");
                    prepStmt.setInt(1, quantity);
                    prepStmt.setString(2, bookId);
                    prepStmt.executeUpdate();
                    //prepStmt.close();
                } else  throw new OrderException("Not enough of " + bookId +" in stock to complete order.");
            }
        } catch (Exception ex) {
            throw new OrderException("Couldn't purchase book: " + bookId + ex.getMessage());
        }
    }
}