/*
Camryn Rogers
cpr170030
 */
package drinkrewards;

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException
    {
        // Define variables
        int numOfReg, numOfPref;
        boolean emptypref = false;
        String ID, size, type;
        double sqinchprice = 0, cost = 0, newAmountSpent = 0;
        int quantity = 0;
        

        File orderFile = new File("ordertest.txt");
        Scanner inorder = new Scanner(orderFile);
        
        // Find the number of regular and preferred customers in the file
        numOfReg = numOfReg();
        numOfPref = numOfPref();
        
        // Create and fill customer array.

        Customer[] regular = new Customer[numOfReg];
        regular = createRegularArray(numOfReg);
        
        // Declare varible for preferred array
        Customer[] preferred = null;
        
        // If the number of preferred customers is 0 (empty of non-existant file), then do not create an array
        if (numOfPref == 0)
        {
            // No array created, make a flag for that
            emptypref = true;
        }
        else
        {
            preferred = new Customer[numOfPref];
            preferred = createPreferredArray(numOfPref);            
        }
        
        // Now that both arrays have been established, processing orders can begin.
        if (orderFile.exists())
        {
            // Start processing orders
            while (inorder.hasNext())
            {
                String order = inorder.nextLine();
                
                // Create array of strings to split up line 
                String arrOfOrder[] = order.split(" ", 5);
                ID = arrOfOrder[0];             
                size = arrOfOrder[1];
                type = arrOfOrder[2];
                sqinchprice = Double.parseDouble(arrOfOrder[3]);
                quantity = Integer.parseInt(arrOfOrder[4]);
                
                // Check if ID matches one in either array, if so remember that person (works)
                int indexOfCust = -1;
                String regOrPref = "Neither";
                
                for (int i = 0; i < regular.length - 1; i++)
                {
                    if(ID.equals(regular[i].getID()))
                    {
                        indexOfCust = i;
                        regOrPref = "reg";
                    }
                }

                if(indexOfCust == -1)
                {
                    for (int j = 0; j < preferred.length - 1; j++)
                    {
                        if(ID.equals(preferred[j].getID()))
                        {
                            indexOfCust = j;
                            regOrPref = "pref";
                        }
                    }
                }
                
                if(indexOfCust == -1)
                {
                    System.out.println("No matching ID");
                    // Do not process order
                }
                else if (!(size.equalsIgnoreCase("s") || size.equalsIgnoreCase("m") || size.equalsIgnoreCase("l")))
                {
                    System.out.println("Incorrect size");
                    // do not process order
                }
                else if (!(type.equals("soda") || type.equals("tea") || type.equals("punch")))
                {
                    System.out.println("Incorrect drink type");
                    // do not process order                    
                }
                else
                {
                    // The order is processed correctly
                    cost = processOrder(size, type, sqinchprice, quantity, regOrPref, indexOfCust, preferred, regular);

                }                
                
                if (regOrPref.equals("reg"))
                {
                    newAmountSpent = regular[indexOfCust].getAmountSpent() + cost;
                }
                else if (regOrPref.equals("pref"))
                {
                    newAmountSpent = preferred[indexOfCust].getAmountSpent() + cost;
                }
 
                // If a person is regular and spends at least $50, promote to gold and give new discount
                if(regOrPref.equals("reg") && (newAmountSpent >= 50))
                {
                    
                    // Find new length of preferred (old length + 1)
                    int newLengthPref = preferred.length;

                    Customer[] tempPreferred = new Customer[newLengthPref];

                    // Fill in the new preferred array with old preferred plus the new customer from the regular array
                    for (int i = 0; i < newLengthPref -1; i++)
                    {
                        tempPreferred[i] = preferred[i];
                    }
                    
                    if (newAmountSpent < 200)
                    {
                        // Make them a Gold object
                        Gold g = new Gold(regular[indexOfCust].getID(), regular[indexOfCust].getFirst(), regular[indexOfCust].getLast(), newAmountSpent);
                        tempPreferred[newLengthPref-1] = g;    
                    }
                    else
                    {
                        // Make them a platinum object
                        int bonusbucks = (((int) cost - 200 ) / 5);
                        Platinum p = new Platinum(regular[indexOfCust].getID(), regular[indexOfCust].getFirst(), regular[indexOfCust].getLast(), newAmountSpent, bonusbucks);
                        tempPreferred[newLengthPref -1] = p;   
                    }
                    
                    
                    // Resize the preferred array and fill it in with the proper objects
                    preferred = new Customer[newLengthPref];
                    for (int i = 0; i < newLengthPref; i++)
                    {
                        preferred[i] = tempPreferred[i];
                    }
    
                    // Find new length of regular (old length - 1)
                    /*int newLengthReg = regular.length - 2;
                    
                    regular = new Customer[newLengthReg];
                    
*/
                    // Give discount and set it in object
                    double discount = ((Gold)preferred[newLengthPref -1]).getDiscount();
                    newAmountSpent = preferred[newLengthPref - 1].getAmountSpent() - (cost * discount);
                    preferred[newLengthPref - 1].setAmountSpent(newAmountSpent);
                    System.out.println("newAmountSpent" + newAmountSpent);

                }
                
                if(regOrPref.equals("pref"))
                {
                    if(preferred[indexOfCust].getAmountSpent() >= 50 && preferred[indexOfCust].getAmountSpent() < 100 && newAmountSpent >= 100 && newAmountSpent < 150)
                    {
                        // Take off an additional 5% (5% was already taken off)
                        newAmountSpent = (cost * 0.95) + preferred[indexOfCust].getAmountSpent();
                        preferred[indexOfCust].setAmountSpent(newAmountSpent);
                    }
                    else if(preferred[indexOfCust].getAmountSpent() >= 50 && preferred[indexOfCust].getAmountSpent() < 100 && newAmountSpent >= 150 && newAmountSpent < 200)
                    {
                        // Take off an additional 10% (5% was already taken off)
                        newAmountSpent = (cost * 0.9) + preferred[indexOfCust].getAmountSpent();
                        preferred[indexOfCust].setAmountSpent(newAmountSpent);
                    }
                    else if (preferred[indexOfCust].getAmountSpent() >= 100 && preferred[indexOfCust].getAmountSpent() < 150 && newAmountSpent >= 150 && newAmountSpent < 200)
                    {
                        // Take off an additional 5% (5% was already taken off)
                        newAmountSpent = (cost * 0.95) + preferred[indexOfCust].getAmountSpent();
                        preferred[indexOfCust].setAmountSpent(newAmountSpent);
                    }
                    else if(preferred[indexOfCust].getAmountSpent() < 200 && newAmountSpent >= 200)
                    {
                        // Make platinum, update bonus bucks 
                        int bonusBucks = (int) (newAmountSpent - 200) / 5;
                        Platinum p = new Platinum(preferred[indexOfCust].getID(), preferred[indexOfCust].getFirst(), preferred[indexOfCust].getLast(), newAmountSpent, bonusBucks);
                        preferred[indexOfCust] = p;    
                    }
                    else if(preferred[indexOfCust].getAmountSpent() >= 200 && newAmountSpent < 200)
                    {
                        // Update bonus bucks
                        newAmountSpent = preferred[indexOfCust].getAmountSpent() + (cost - ((Platinum)preferred[indexOfCust]).getBonusBucks());
                        ((Platinum)preferred[indexOfCust]).setBonusBucks((int)cost / 5);
                        
                    }
                    preferred[indexOfCust].setAmountSpent(newAmountSpent);
                   

                }
            }   
            
        }

        else
        {
            System.out.println("The order file does not exist. The program will now end.");
            return;
        }
        
        // Write to respective files
        File reg = new File ("customer.dat");
        PrintWriter writer = new PrintWriter(new FileWriter(reg));
        
        for (int i = 0; i < regular.length - 1; i++)
        {
            writer.println(regular[i].getID() + " " + regular[i].getFirst() + " " + regular[i].getLast() + " " + regular[i].getAmountSpent());
        }
        writer.close();
        
        File pref = new File("preferred.dat");
        PrintWriter writer2 = new PrintWriter(new FileWriter(pref));
        
        for (int i = 0; i < preferred.length - 1; i++)
        {
            writer2.print(preferred[i].getID() + " " + preferred[i].getFirst() + " " + preferred[i].getLast() + " " + preferred[i].getAmountSpent());
            if(preferred[i] instanceof Gold)
            {
                writer2.print(((((Gold)preferred[i]).getDiscount())*100) + "%" + "\n");
            }
            else
            {
                writer2.print(((Platinum)preferred[i]).getBonusBucks() + "\n");
            }
            writer2.close();
        }
        
        
    }

    
    public static int numOfReg() throws FileNotFoundException
    {
        // Open file and create a scanner object to read from the file
        File customerFile = new File("customer.dat");
        Scanner incustomer = new Scanner(customerFile);
        
        if (customerFile.exists())
        {
            if(customerFile.canRead())
            {
                // Get number of customers so that we can create the array
                int i = 1;                      // Starts at 1 so that if there is no endline at the end of the file, the number of customers is still correct
                String line = "";
                
                while(incustomer.hasNext())
                {
                    line = incustomer.nextLine();
                    i++;
                }
                return i;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return 0;
        }
        
    }
    
    public static int numOfPref() throws FileNotFoundException
    {
        // Open file and create a scanner object to read from the file
        File preferredFile = new File("preferredtest.txt");
        Scanner inpreferred = new Scanner(preferredFile);
        
        if (preferredFile.exists())
        {
            if(preferredFile.canRead())
            {
                // Get number of customers so that we can create the array
                int i = 1;                      // Starts at 1 so that if there is no endline at the end of the file, the number of customers is still correct
                String line = "";
                
                while(inpreferred.hasNext())
                {
                    line = inpreferred.nextLine();
                    i++;
                }
                return i;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return 0;
        }
        
    }

    
    public static Customer[] createRegularArray(int numOfReg) throws FileNotFoundException
    {
        // Open file and create a scanner object to read from the file
        File customerFile = new File("customer.dat");
        Scanner incustomer = new Scanner(customerFile);

        // Define variables
        String first, last, ID, newline;
        double amountspent;
       
        // Create array for regular customers
        Customer[] regular = new Customer[numOfReg];               
                
        // Put the customers in the regular customer array
        int j = 0;
        while (incustomer.hasNext())
        {
            // put customer in one string
            String customer = incustomer.nextLine();
            
            // Use split() to get different peices of info
            String[] arrOfCust = customer.split(" ",4);
            ID = arrOfCust[0];
            first = arrOfCust[1];
            last = arrOfCust[2];
            amountspent = Double.parseDouble(arrOfCust[3]);
                          
            Customer c = new Customer(ID, first, last, amountspent);
                    
            regular[j] = c;
            j++;
        }
        return regular;    
    }
    
    public static Customer[] createPreferredArray(int numOfPref) throws FileNotFoundException
    {
        // Open file and create a scanner object to read from the file
        File preferredFile = new File("preferred.dat");
        Scanner inpreferred = new Scanner(preferredFile);

        // Define variables
        String first, last, ID, newline, x, type;
        double amountspent, discount = 0;
        int bonusbucks = 0;
       
        // Create array for regular customers
        Customer[] preferred = new Customer[numOfPref];               
                
        // Put the customers in the regular customer array
        int j = 0;
        while (inpreferred.hasNext())
        {
            String prefString = inpreferred.nextLine();            
            
            // Use split() to get different peices of info
            String[] arrOfPref = prefString.split(" ",5);
           
                ID = arrOfPref[0];
                first = arrOfPref[1];
                last = arrOfPref[2];
                amountspent = Double.parseDouble(arrOfPref[3]);

                if(amountspent < 200)
                {
                    String discountString = arrOfPref[4];
                    String arrForDiscount[] = discountString.split("%");
                    discount = Double.parseDouble(arrForDiscount[0]); 

                    type = "gold";
                }
                else
                {
                    bonusbucks = Integer.parseInt(arrOfPref[4]); 
                    type = "preferred";
                }

                if(type == "gold")
                {
                    Gold g = new Gold(ID, first, last, amountspent);
                    preferred[j] = g;
                }
                else
                {
                    Platinum p = new Platinum(ID, first, last, amountspent, bonusbucks);
                    preferred[j] = p;
                }
                j++;   
            
        }
        return preferred;    
    }
    
public static double processOrder(String size, String type, double sqinchprice, int quantity, String regOrPref, int indexOfCust, Customer[] preferred, Customer[] regular)    
{
    double price = 0, area = 0, ounces = 0, ppounce = 0;    
    final double PI = 3.141592;
    
    if(size.equalsIgnoreCase("s"))
    {
        area = (2*PI*2*4.5);
        ounces = 12;
    }
    
    if(size.equalsIgnoreCase("m"))
    {
        area = (2*PI*2.25*5.75);
        ounces = 20;
    }
    
    if(size.equalsIgnoreCase("l"))
    {
        area = (2*PI*2.75*7);
        ounces = 32;
    }
    
    if(type.equalsIgnoreCase("soda"))
    {
        ppounce = 0.2;
    }
    
    if(type.equalsIgnoreCase("tea"))
    {
        ppounce = 0.12;
    }
    
    if(type.equalsIgnoreCase("punch"))
    {
        ppounce = 0.15;
    }

    double total = ((ounces * ppounce) + (sqinchprice * area)) * quantity;
    
    if (regOrPref.equals("reg"))
    {
        return total;
    }
    else if (regOrPref.equals("pref"))
    {
        if (preferred[indexOfCust] instanceof Gold)
        {
            total *= (1 - ((Gold)preferred[indexOfCust]).getDiscount());
        }
        else if(preferred[indexOfCust] instanceof Platinum)
        {
            total -= ((Platinum)preferred[indexOfCust]).getBonusBucks();
        }
    }
    return total;
    
}


}
