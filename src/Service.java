public class Service {

    private String name;
    private String duration;
    private double price;

    public Service(String name, String duration, double price) {
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }
   

	public void setDuration(String duration) {
		
		this.duration=duration;
	}

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return  name + " ( " + duration + " ) "+ "- $" + price ;
    }
}
