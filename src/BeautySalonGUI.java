import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeautySalonGUI extends JFrame {

    private List<Customer> customers = new ArrayList<>();
    private List<Service> services = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private JPanel calendarPanel;
    private Calendar calendar;
    private Map<String, JButton> timeSlotButtons = new HashMap<>();
    private Map<String, Double> membershipDiscounts;
    private Map<String, List<Appointment>> appointmentMap;

    public BeautySalonGUI() 
    {
        setTitle("Beauty Salon Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);


        // Initialize calendar and calendarPanel
        calendarPanel = new JPanel(new GridLayout(0, 8));
        calendar = Calendar.getInstance();
        
        membershipDiscounts = new HashMap<>();
        membershipDiscounts.put("Silver", 0.10); // 10% discount
        membershipDiscounts.put("Gold", 0.20);   // 20% discount
        membershipDiscounts.put("Platinum", 0.30); // 30% discount
        
        appointmentMap = new HashMap<>();

        // Load customers, services, and appointments from files
        loadCustomers();
        loadServices();
        loadAppointments();
        
        // Create control panel for managing customers, services, and appointments
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.WEST);

        // Create calendar panel for displaying week schedule
        createCalendarPanel();
        add(new JScrollPane(calendarPanel), BorderLayout.CENTER);
    

        // Add window listener to save data when closing the application
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveDataOnExit();
                System.exit(0);
            }
        });
    }

    private void saveDataOnExit() {
        saveCustomers();
        saveServices();
        saveAppointments();
    }


    private JPanel createControlPanel()
 {
        JPanel controlPanel = new JPanel(new GridLayout(0, 2));
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Buttons for managing customers
        JButton addCustomerButton = new JButton("Add Customer");
        addCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCustomerDialog();
            }
        });
        controlPanel.add(addCustomerButton);

        JButton editCustomerButton = new JButton("Edit Customer");
        editCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectCustomerDialog();
            }
        });
        controlPanel.add(editCustomerButton);

        JButton deleteCustomerButton = new JButton("Delete Customer");
        deleteCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectForDeleteCustomerDialog();
            }
        });
        controlPanel.add(deleteCustomerButton);

        // Buttons for managing services
        JButton addServiceButton = new JButton("Add Service");
        addServiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addServiceDialog();
            }
        });
        controlPanel.add(addServiceButton);
        JButton editServiceButton = new JButton("Edit Service");
        editServiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectServiceDialog();
            }
        });
        controlPanel.add(editServiceButton);

        JButton deleteServiceButton = new JButton("Delete Service");
        deleteServiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectForDeleteServiceDialog();
            }
        });
        controlPanel.add(deleteServiceButton);

        // Button for viewing appointments
        JButton viewAppointmentsButton = new JButton("View Appointments");
        viewAppointmentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewAppointmentsDialog();
            }
        });
        controlPanel.add(viewAppointmentsButton);

        // Button for viewing customers
        JButton viewCustomersButton = new JButton("View Customers");
        viewCustomersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewCustomersDialog();
            }
        });
        controlPanel.add(viewCustomersButton);

        // Buttons for managing appointments
        JButton editAppointmentButton = new JButton("Edit Appointment");
        editAppointmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editOrDeleteAppointmentDialog("Edit");

            }
        });
        controlPanel.add(editAppointmentButton);

        JButton deleteAppointmentButton = new JButton("Delete Appointment");
        deleteAppointmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editOrDeleteAppointmentDialog("Delete");
            }
        });
        controlPanel.add(deleteAppointmentButton);

        return controlPanel;
    }


    private void addCustomerDialog()
 {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(4, 2, 10, 10));

    JLabel nameLabel = new JLabel("Name:");
    JTextField nameField = new JTextField();
    JLabel phoneLabel = new JLabel("Phone Number:");
    JTextField phoneField = new JTextField();
    JLabel membershipLabel = new JLabel("Membership Type:");
    JComboBox<String> membershipBox = new JComboBox<>(new String[]{"Silver", "Gold", "Platinum"});

    panel.add(nameLabel);
    panel.add(nameField);
    panel.add(phoneLabel);
    panel.add(phoneField);
    panel.add(membershipLabel);
    panel.add(membershipBox);

    int option = JOptionPane.showOptionDialog(this, panel, "Add Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

    if (option == JOptionPane.OK_OPTION) {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String membership = (String) membershipBox.getSelectedItem();
        customers.add(new Customer(name, phone, membership));
    }
}

    private void selectCustomerDialog()
 {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers available to edit.");
            return;
        }

        // Create a panel for customer selection
        JPanel panel = new JPanel(new GridLayout(1, 1, 10, 10));
        JLabel selectLabel = new JLabel("Select Customer:");
        JComboBox<Customer> customerComboBox = new JComboBox<>(customers.toArray(new Customer[0]));

        panel.add(selectLabel);
        panel.add(customerComboBox);

        // Show the JOptionPane as a question dialog for selecting a customer
        int choice = JOptionPane.showOptionDialog(
            this,
            panel,
            "Edit Customer",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            null,
            null
        );

        if (choice == JOptionPane.OK_OPTION) {
            Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
            if (selectedCustomer != null) {
                editCustomerDialog(selectedCustomer);
            }
        }
    }

    private void editCustomerDialog(Customer customer) {
        // Create a panel for editing customer details
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(customer.getName());
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField(customer.getPhoneNumber());
        JLabel membershipLabel = new JLabel("Membership Type:");
        JComboBox<String> membershipBox = new JComboBox<>(new String[]{"Silver", "Gold", "Platinum"});
        membershipBox.setSelectedItem(customer.getMembership());

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(membershipLabel);
        panel.add(membershipBox);

        // Show the JOptionPane as a question dialog for editing customer details
        int option = JOptionPane.showOptionDialog(
            this,
            panel,
            "Edit Customer",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            null,
            null
        );

        if (option == JOptionPane.OK_OPTION) {
            customer.setName(nameField.getText());
            customer.setPhoneNumber(phoneField.getText());
            customer.setMembership((String) membershipBox.getSelectedItem());
            // Update your customer in your data structure or database here
        }
    }

    private void selectForDeleteCustomerDialog() {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers available to delete.");
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        
        JLabel label = new JLabel("Select Customer:");
        panel.add(label);
        JComboBox<Customer> customerComboBox = new JComboBox<>(customers.toArray(new Customer[0]));
        panel.add(customerComboBox);

        int choice = JOptionPane.showConfirmDialog(this, panel, "Select Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.OK_OPTION) {
            Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
            if (selectedCustomer != null) {
                int deleteChoice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Delete Customer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (deleteChoice == JOptionPane.YES_OPTION) {
                	deleteCustomerDialog(selectedCustomer);
                }
            }
        }
    }
    
    private void deleteCustomerDialog(Customer customer) {
    	  customers.remove(customer);
    }

    
    private void addServiceDialog() 
    {
    	// Create a panel for adding a new service
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel nameLabel = new JLabel("Service Name:");
        JTextField nameField = new JTextField();
        JLabel durationLabel = new JLabel("Duration:");
        JComboBox<String> durationBox = new JComboBox<>(new String[]{"30 Minutes", "1 Hour", "1.5 Hours", "2 Hours"});
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(durationLabel);
        panel.add(durationBox);
        panel.add(priceLabel);
        panel.add(priceField);

        // Show the JOptionPane as a question dialog for adding a new service
        int option = JOptionPane.showOptionDialog(
            this,
            panel,
            "Add Service",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            null,
            null
        );

        if (option == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String duration = (String) durationBox.getSelectedItem();
                double price = Double.parseDouble(priceField.getText());
                if(price <= 0.0) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid price.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                services.add(new Service(name, duration, price));
  
        }
    }
    
    private void selectServiceDialog() 
    {
    	  if (services.isEmpty()) {
    	        JOptionPane.showMessageDialog(this, "No services available to edit.");
    	        return;
    	    }

    	    // Create a panel for selecting a service
    	    JPanel selectServicePanel = new JPanel(new GridLayout(1, 1, 10, 10));
    	    JLabel selectServiceLabel = new JLabel("Select Service:");
    	    JComboBox<Service> serviceComboBox = new JComboBox<>(services.toArray(new Service[0]));
    	    selectServicePanel.add(selectServiceLabel);
    	    selectServicePanel.add(serviceComboBox);

    	    // Show the first JOptionPane for selecting a service
    	    int selectOption = JOptionPane.showOptionDialog(
    	        this,
    	        selectServicePanel,
    	        "Select Service",
    	        JOptionPane.OK_CANCEL_OPTION,
    	        JOptionPane.QUESTION_MESSAGE,
    	        null,
    	        null,
    	        null
    	    );
    	    if (selectOption == JOptionPane.OK_OPTION) {
    	        Service selectedService = (Service) serviceComboBox.getSelectedItem();
    	        if (selectedService != null) {
    	        	editServiceDialog( selectedService);  
    	          }
    	        }
    	}

    private void editServiceDialog(Service selectedService) 
    {
        // Create a panel for updating the service details
        JPanel updateServicePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel nameLabel = new JLabel("Name:");
        updateServicePanel.add(nameLabel);
        
        JTextField serviceNameField = new JTextField(selectedService.getName());
        updateServicePanel.add(serviceNameField);
        
        JLabel durationLabel = new JLabel("Duration:");
        updateServicePanel.add(durationLabel);
        
        JComboBox<String> durationBox = new JComboBox<>(new String[]{"30 Minutes", "1 Hour", "1.5 Hours", "2 Hours"});
        durationBox.setSelectedItem(selectedService.getDuration());
        updateServicePanel.add(durationBox);
        
        JLabel priceLabel = new JLabel("Price:");
        updateServicePanel.add(priceLabel);
        
        JTextField priceField = new JTextField(String.valueOf(selectedService.getPrice()));
        updateServicePanel.add(priceField);

        // Show the second JOptionPane for updating service details
        int updateOption = JOptionPane.showOptionDialog(
            this,
            updateServicePanel,
            "Update Service",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            null,
            null
        );

        if (updateOption == JOptionPane.OK_OPTION) {
            String newName = serviceNameField.getText();
            String newDuration = (String) durationBox.getSelectedItem();
            double newPrice;
                newPrice = Double.parseDouble(priceField.getText());
            if(newPrice <= 0.0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid price.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            selectedService.setName(newName);
            selectedService.setDuration(newDuration);
            selectedService.setPrice(newPrice);

            JOptionPane.showMessageDialog(this, "Service updated successfully.");
            //updateAppointmentsForService(selectedService); 
        }
    }

 
    private void selectForDeleteServiceDialog() {
        if (services.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No services available to delete.");
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        
        JLabel label = new JLabel("Select Service:");
        panel.add(label);
        JComboBox<Service> serviceComboBox = new JComboBox<>(services.toArray(new Service[0]));
        panel.add(serviceComboBox);

        int choice = JOptionPane.showConfirmDialog(this, panel, "Select Service", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.OK_OPTION) {
        	Service selectedService = (Service) serviceComboBox.getSelectedItem();
            if (selectedService != null) {
                int deleteChoice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Service?", "Delete Service", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (deleteChoice == JOptionPane.YES_OPTION) {
                	deleteServiceDialog(selectedService);
                }
            }
        }
    }
    
    private void deleteServiceDialog(Service selectedService) {
    	services.remove(selectedService);
    }

    
    private void viewAppointmentsDialog()
    {
        if (appointments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No appointments available to view.", "Appointments", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTextArea textArea = new JTextArea(20, 40); // 20 rows, 40 columns for the text area
        textArea.setEditable(false);
        
        StringBuilder appointmentsText = new StringBuilder();
        for (Appointment appointment : appointments) {
            appointmentsText.append(appointment.toString()).append("\n");
        }
        
        textArea.setText(appointmentsText.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(this, scrollPane, "Appointments", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewCustomersDialog() 
    {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers available.", "Customers", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTextArea textArea = new JTextArea(20, 40); // 20 rows, 40 columns for the text area
        textArea.setEditable(false);
        
        StringBuilder customerDetails = new StringBuilder();
        for (Customer customer : customers) {
            customerDetails.append(customer.toString()).append("\n");
        }
        
        textArea.setText(customerDetails.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(this, scrollPane, "Customers", JOptionPane.INFORMATION_MESSAGE);
    }

    
    // Working on add appointment
    
    // Method  to  add appointment to the map
    private void addAppointmentToMap(Appointment appointment) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(appointment.getDate());
        appointmentMap.putIfAbsent(date, new ArrayList<>());
        appointmentMap.get(date).add(appointment);
    }

    // Method to get discount based on membership
    private double getMembershipDiscount(String membershipType) {
        return membershipDiscounts.getOrDefault(membershipType, 0.0);
    }

    // Generate time slots
    private String[] generateTimeSlotsForWeek() {
        return new String[]{
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
        };
    }

	private String determineAdjustedTime(String date, String time) {
	    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	    Calendar slotStart = Calendar.getInstance();
	    try {
	        slotStart.setTime(timeFormat.parse(time));
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	
	    List<Appointment> dailyAppointments = appointmentMap.get(date);
	    if (dailyAppointments != null) {
	        for (Appointment appointment : dailyAppointments) {
	            Calendar appointmentStart = Calendar.getInstance();
	            try {
	                appointmentStart.setTime(timeFormat.parse(appointment.getTime()));
	            } catch (ParseException e) {
	                e.printStackTrace();
	            }
	
	            if (appointmentStart.equals(slotStart)) {
	                // Adjust time if there is an overlap
	                slotStart.add(Calendar.MINUTE, 30);
	            }
	        }
	    }
	
	    return timeFormat.format(slotStart.getTime());
	}    
	
	private boolean isTimeSlotAvailable(String date, String startTime, String endTime) 
	{
	    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	    Calendar start = Calendar.getInstance();
	    Calendar end = Calendar.getInstance();
	    try {
	        start.setTime(timeFormat.parse(startTime));
	        end.setTime(timeFormat.parse(endTime));
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    List<Appointment> dailyAppointments = appointmentMap.get(date);
	    if (dailyAppointments != null) {
	        for (Appointment appointment : dailyAppointments) {
	            Calendar appointmentStart = Calendar.getInstance();
	            Calendar appointmentEnd = Calendar.getInstance();
	            try {
	                appointmentStart.setTime(timeFormat.parse(appointment.getTime()));
	                double duration = getDurationInTimeSlots(appointment.getService().getDuration());
	                appointmentEnd.setTime(appointmentStart.getTime());
	                appointmentEnd.add(Calendar.MINUTE, (int) (duration * 60));
	            } catch (ParseException e) {
	                e.printStackTrace();
	            }

	            if ((start.before(appointmentEnd) && end.after(appointmentStart)) ||
	                start.equals(appointmentStart) || end.equals(appointmentEnd)) {
	                return false; // Time slot is not available
	            }
	        }
	    }
	    return true; // Time slot is available
	}
	
	private String calculateEndTime(String startTime, String duration) {
		    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		    Calendar cal = Calendar.getInstance();
		    try {
		        cal.setTime(timeFormat.parse(startTime));
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
		
		    double durationInHours = getDurationInTimeSlots(duration);
		    cal.add(Calendar.MINUTE, (int) (durationInHours * 60));
		    return timeFormat.format(cal.getTime());
		}


    // Create calendar panel with time slots and add appointment logic
    private void createCalendarPanel() {
        calendar.add(Calendar.DATE, 1);
        String[] times = generateTimeSlotsForWeek();

        for (int i = 0; i < 7; i++) {
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            String currentDate = dateFormat.format(calendar.getTime());
            JLabel dayLabel = new JLabel(currentDate);
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dayPanel.add(dayLabel, BorderLayout.NORTH);

            JPanel timePanel = new JPanel(new GridLayout(times.length, 1));
            for (String time : times) {
                String dateTime = currentDate + " " + time;
                JButton timeSlot = new JButton(time);
                timeSlot.setBackground(Color.GREEN); // Set initial color to green
                timeSlotButtons.put(dateTime, timeSlot);
                updateTimeSlotColor(timeSlot, currentDate, time);

                timeSlot.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String adjustedTime = determineAdjustedTime(currentDate, time);
                        addAppointmentDialog(currentDate, adjustedTime);

                    }
                });
                timePanel.add(timeSlot);
            }
            dayPanel.add(timePanel, BorderLayout.CENTER);
            calendar.add(Calendar.DATE, 1);
            calendarPanel.add(dayPanel);
        }
    }

    // Get duration in time slots
    private double getDurationInTimeSlots(String duration) {
        switch (duration) {
            case "30 Minutes":
                return 0.5;
            case "1 Hour":
                return 1.0;
            case "1.5 Hours":
                return 1.5;
            case "2 Hours":
                return 2;
            default:
                return -1;
        }
    }

    // Update calendar panel
    private void updateCalendarPanel() {
        for (Map.Entry<String, JButton> entry : timeSlotButtons.entrySet()) {
            String dateTime = entry.getKey();
            JButton timeSlot = entry.getValue();
            String[] parts = dateTime.split(" ");
            updateTimeSlotColor(timeSlot, parts[0], parts[1]);
        }
    }

    // Update time slot color
    private void updateTimeSlotColor(JButton timeSlot, String date, String time) {
        boolean isFullyBooked = false;
        boolean isPartiallyBooked = false;
        double bookedSlots = 0.0;

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Calendar slotStart = Calendar.getInstance();
        try {
            slotStart.setTime(timeFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar slotEnd = (Calendar) slotStart.clone();
        slotEnd.add(Calendar.HOUR_OF_DAY, 1);

        for (Appointment appointment : appointments) {
            String appointmentDate = dateFormat.format(appointment.getDate());
            String appointmentTime = appointment.getTime();
            if (appointmentDate.equals(date)) {
                double durationSlots = getDurationInTimeSlots(appointment.getService().getDuration());
                Calendar appointmentStart = Calendar.getInstance();
                try {
                    appointmentStart.setTime(timeFormat.parse(appointmentTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar appointmentEnd = (Calendar) appointmentStart.clone();
                appointmentEnd.add(Calendar.MINUTE, (int) (durationSlots * 60));

                // Check if appointment overlaps with the slot
                if (appointmentStart.before(slotEnd) && appointmentEnd.after(slotStart)) {
                    // Calculate the overlap between appointment and slot
                    Calendar overlapStart = appointmentStart.after(slotStart) ? appointmentStart : slotStart;
                    Calendar overlapEnd = appointmentEnd.before(slotEnd) ? appointmentEnd : slotEnd;

                    long overlapMinutes = (overlapEnd.getTimeInMillis() - overlapStart.getTimeInMillis()) / (1000 * 60);
                    bookedSlots += overlapMinutes / 60.0;

                    // If the overlap fully covers the slot time, mark it as fully booked
                    if (overlapStart.compareTo(slotStart) <= 0 && overlapEnd.compareTo(slotEnd) >= 0) {
                        isFullyBooked = true;
                    }
                }
            }
        }

        if (isFullyBooked || bookedSlots >= 1.0) {
            timeSlot.setBackground(Color.RED);
            timeSlot.setEnabled(false);
        } else if (bookedSlots > 0.0) {
            timeSlot.setBackground(Color.ORANGE);
            timeSlot.setEnabled(true);
        } else {
            timeSlot.setBackground(Color.GREEN);
            timeSlot.setEnabled(true);
        }
    }
    
 
    // Add an appointment
    private void addAppointmentDialog(String date, String startTime) {

        
        if (customers.isEmpty() || services.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add customers and services before scheduling an appointment.");
            return;
        }

        JPanel appointmentPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JComboBox<Customer> customerComboBox = new JComboBox<>(customers.toArray(new Customer[0]));
        JComboBox<Service> serviceComboBox = new JComboBox<>(services.toArray(new Service[0]));
        JLabel dateLabel = new JLabel(date);
        JLabel timeLabel = new JLabel(startTime);

        appointmentPanel.add(new JLabel("Customer:"));
        appointmentPanel.add(customerComboBox);
        appointmentPanel.add(new JLabel("Service:"));
        appointmentPanel.add(serviceComboBox);
        appointmentPanel.add(new JLabel("Date:"));
        appointmentPanel.add(dateLabel);
        appointmentPanel.add(new JLabel("Start Time:"));
        appointmentPanel.add(timeLabel);

        int result = JOptionPane.showConfirmDialog(
                this,
                appointmentPanel,
                "Add Appointment",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
            Service selectedService = (Service) serviceComboBox.getSelectedItem();
            String selectedMembership = (String) selectedCustomer.getMembership();
            if (selectedCustomer != null && selectedService != null) {
                String endTime = calculateEndTime(startTime, selectedService.getDuration());
                if (isTimeSlotAvailable(date, startTime, endTime)) {
                    double discount = getMembershipDiscount(selectedMembership);
                    double finalPrice = selectedService.getPrice() * (1 - discount);
                    Appointment appointment = new Appointment(
                            selectedCustomer,
                            selectedService,
                            java.sql.Date.valueOf(date),
                            startTime,
                            finalPrice
                    );
                    appointments.add(appointment);
                    addAppointmentToMap(appointment);

                    updateCalendarPanel();

                    JOptionPane.showMessageDialog(this, "Appointment added successfully with a " +
                            (discount * 100) + "% discount! Final price: " + finalPrice);
                } else {
                    JOptionPane.showMessageDialog(this, "The selected time slot is not available for the chosen duration.");
                }
            }
        }
    }
    
    private void editOrDeleteAppointmentDialog(String action) {
        if (appointments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No appointments available.");
            return;
        }

        JComboBox<Appointment> appointmentComboBox = new JComboBox<>(appointments.toArray(new Appointment[0]));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Appointment:"));
        panel.add(appointmentComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, action + " Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Appointment selectedAppointment = (Appointment) appointmentComboBox.getSelectedItem();
            if (selectedAppointment != null) {
                if (action.equals("Edit")) {
                    editAppointmentDialog(selectedAppointment);
                } else if (action.equals("Delete")) {
                    deleteAppointment(selectedAppointment);
                    updateCalendarPanel();
                    JOptionPane.showMessageDialog(this, "Appointment deleted successfully.");
                }
            }
        }
    }

    private void editAppointmentDialog(Appointment appointment) {
        JPanel appointmentPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        JComboBox<Customer> customerComboBox = new JComboBox<>(customers.toArray(new Customer[0]));
        JComboBox<Service> serviceComboBox = new JComboBox<>(services.toArray(new Service[0]));
        JTextField dateField = new JTextField(dateFormat.format(appointment.getDate()));
        JTextField timeField = new JTextField(appointment.getTime());

        customerComboBox.setSelectedItem(appointment.getCustomer());
        serviceComboBox.setSelectedItem(appointment.getService());

        appointmentPanel.add(new JLabel("Customer:"));
        appointmentPanel.add(customerComboBox);
        appointmentPanel.add(new JLabel("Service:"));
        appointmentPanel.add(serviceComboBox);
        appointmentPanel.add(new JLabel("Date:"));
        appointmentPanel.add(dateField);
        appointmentPanel.add(new JLabel("Time:"));
        appointmentPanel.add(timeField);

        int result = JOptionPane.showConfirmDialog(this, appointmentPanel, "Edit Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
            Service selectedService = (Service) serviceComboBox.getSelectedItem();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            if (selectedCustomer != null && selectedService != null && !date.isEmpty() && !time.isEmpty()) {
                appointment.setCustomer(selectedCustomer);
                appointment.setService(selectedService);
                appointment.setDate(java.sql.Date.valueOf(date));
                appointment.setTime(time);

                updateCalendarPanel();
                JOptionPane.showMessageDialog(this, "Appointment edited successfully.");
            }
        }
    }

    private void deleteAppointment(Appointment appointment) {
        appointments.remove(appointment);
        String date = dateFormat.format(appointment.getDate());
        List<Appointment> dailyAppointments = appointmentMap.get(date);
        if (dailyAppointments != null) {
            dailyAppointments.remove(appointment);
            if (dailyAppointments.isEmpty()) {
                appointmentMap.remove(date);
            }
        }
    }

    // Load appointments from file
    private void loadAppointments() {
        try (BufferedReader reader = new BufferedReader(new FileReader("appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String customerName = parts[0];
                String serviceName = parts[1];
                String date = parts[2];
                String time = parts[3];
                double price = Double.parseDouble(parts[4]);

                Customer customer = customers.stream().filter(c -> c.getName().equals(customerName)).findFirst().orElse(null);
                Service service = services.stream().filter(s -> s.getName().equals(serviceName)).findFirst().orElse(null);

                if (customer != null && service != null) {
                    Appointment appointment = new Appointment(customer, service, java.sql.Date.valueOf(date), time, price);
                    appointments.add(appointment);
                    addAppointmentToMap(appointment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Save appointments to a file
    private void saveAppointments() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("appointments.txt"))) {
            for (Appointment appointment : appointments) {
                String line = String.format("%s,%s,%s,%s,%.2f",
                        appointment.getCustomer().getName(),
                        appointment.getService().getName(),
                        dateFormat.format(appointment.getDate()),
                        appointment.getTime(),
                        appointment.getTotalPrice());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load customers from file
	  private void loadCustomers() {
	    // Example loading from a file (you can change this to database or other sources)
	    try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.trim().isEmpty()) {
	                continue; // Skip empty lines
	            }
	            String[] parts = line.split(",");
	            if (parts.length != 3) {
	                System.err.println("Invalid line format: " + line);
	                continue; // Skip invalid lines
	            }
	            String name = parts[0].trim();
	            String phoneNumber = parts[1].trim();
	            String membership = parts[2].trim();
	
	            Customer customer = new Customer(name, phoneNumber, membership);
	            customers.add(customer);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

    // Save customers to file
    private void saveCustomers() {
        // Example saving to a file (you can change this to database or other destinations)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (Customer customer : customers) {
                String line = String.format("%s,%s,%s",
                        customer.getName(),
                        customer.getPhoneNumber(),
                        customer.getMembership());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load services from file
    private void loadServices() {
        try (BufferedReader reader = new BufferedReader(new FileReader("services.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                String duration = parts[2];

                Service service = new Service(name, duration, price);
                services.add(service);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Save services to file
    private void saveServices() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("services.txt"))) {
            for (Service service : services) {
                String line = String.format("%s,%.2f,%s",
                        service.getName(),
                        service.getPrice(),
                        service.getDuration());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BeautySalonGUI().setVisible(true);
            }
        });
    }
}
 
        
        
    