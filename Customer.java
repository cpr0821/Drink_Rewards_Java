/*
Camryn Rogers
cpr170030
*/

package drinkrewards;

class Customer {
     // The Customer class includes the first and last names, guest IDs and amount spent of each customer
    private String first;
    private String last;
    private String ID;
    private double amountspent;
    
    // Overloaded constructor
    Customer ( String ID, String first, String last, double amountspent)
    {
        this.ID = ID;
        this.first = first;
        this.last = last;
        this.amountspent = amountspent;
    }
    
    // Accessors
    public String getID() {return ID;}
    public String getFirst() {return first;}
    public String getLast() {return last;}
    public double getAmountSpent() {return amountspent;}
    
    // Mutators
    public void setID(String ID) {this.ID = ID;}
    public void setFirst(String first) {this.first = first;}
    public void setLast(String last) {this.last = last;}
    public void setAmountSpent(double amountspent) {this.amountspent = amountspent;}
    
}
