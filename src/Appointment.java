import java.sql.Date;

public class Appointment {
    private Customer customer;
    private Service service;
    private Date date;
    private String startTime;
    private double totalPrice;

    public Appointment(Customer customer, Service service, Date date, String startTime, double totalPrice ) {
        this.customer = customer;
        this.service = service;
        this.date = date;
        this.startTime = startTime;
        this.setTotalPrice(totalPrice);
    }

    public Customer getCustomer() {
        return customer;
    }
    
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setService(Service service) {
		this.service = service;
	}
	
    public Service getService() {
        return service;
    }
    

    public Date getDate() {
        return date;
    }
    
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setTime(String startTime) {
        this.startTime = startTime;
	}


    public String getTime() {
        return startTime;
    }


	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
    @Override
    public String toString() {
        return 
                " Customer: " + customer.getName() +
                " , Service: " + service.getName() +
                " , Date: " + date +
                " , Start Time: " + startTime +  
                " , Total Price: " + totalPrice ;
                
               
    }
    
}
