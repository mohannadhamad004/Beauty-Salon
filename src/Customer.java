public class Customer {

    private String name;
    private String phoneNumber;
    private String membershipType;

    public Customer(String name, String phoneNumber, String membership) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.membershipType = membership;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMembership() {
        return membershipType;
    }

    public void setMembership(String membership) {
        this.membershipType = membership;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", phoneNumber: " + phoneNumber + ", membership: " + membershipType ;
    }
}