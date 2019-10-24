/*
Camryn Rogers
cpr170030
*/

package drinkrewards;

class Gold extends Customer{
    
    // The Gold class adds a discount member
    public double discount;
    
    // Overloaded constructor
    Gold(String ID, String first, String last, double newAmountSpent)
    {
        super(ID ,first, last, newAmountSpent);
        
        double discount = 0;
                    
        // Find out how much the discount will be
        if (newAmountSpent >= 50 && newAmountSpent < 100)
        {
        discount = 0.05;
        }
        if (newAmountSpent >= 100 && newAmountSpent < 150)
        {
        discount = 0.1;
        }
        if (newAmountSpent >= 150 && newAmountSpent < 200)
        {
        discount = 0.15;
        }
        this.discount = discount;
        
    }
    
    // Accessor
    public double getDiscount() {return this.discount;}
    
    // Mutator
    public void setDiscount(double discount) {this.discount = discount;}
    
}
