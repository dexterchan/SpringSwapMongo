package hello;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;


public class Customer {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private Date lastUpdate;
    
    public Customer() {}

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastUpdate=Calendar.getInstance().getTime(); 
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, firstName='%s', lastName='%s', lastUpdate=%s]",
                id, firstName, lastName,lastUpdate.toString());
    }

}