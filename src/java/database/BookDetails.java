package database;

public class BookDetails implements Comparable {
    private String id=null;         //id            varchar(4)
    private String title=null;      //title         varchar(50)
    private float price=0.0F;       //price         float
    private String description=null;//description   varchar(128)
    private int inventory=0;        //inventory     int
    
    public BookDetails() {}
    
    public BookDetails(String id, String title,float price,String description, int inventory) 
    {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.inventory = inventory;
    }
    
    public String getId() {return id;}
    public String getTitle() {return title;}
    public float getPrice() {return price;}
    public String getDescription() {return description;}
    public int getInventory() {return inventory;}

    public void setId(String id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setPrice(float price) {this.price = price; }
    public void setDescription(String description) {this.description = description; }
    public void setInventory(int inventory) {this.inventory = inventory;}
    
    public int compareTo(Object o) {
        return title.compareTo(((BookDetails) o).title);
    }
}
