/*
Camryn Rogers
cpr170030
*/

package drinkrewards;

public class Platinum extends Customer{
    
    // Platinum adds a bonus bucks member to Customer
    public double bonusbucks;
    
    // Overloaded constructor
    Platinum(String ID, String first, String last, double amountspent, double bonusbucks)
    {
        super(ID, first, last, amountspent);
        this.bonusbucks = bonusbucks;
    }
    
    // Accessor 
    public double getBonusBucks() {return this.bonusbucks;}
    
    // Mutator
    public void setBonusBucks(double bonusbucks) {this.bonusbucks = bonusbucks;}
}
