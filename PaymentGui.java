import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class PaymentGui extends JFrame {
    private final Font mainFont = new Font("Arial", Font.BOLD, 16);
    private JTextArea purchaseDetailsArea;
    private JCheckBox deliveryCheckBox;
    private JTextField addressConfirmationField;
    private JLabel totalAmountLabel;
    private double totalAmount;
    private String currentAddress;
    private boolean deliveryFeeAdded = false;  // Flag to check if delivery fee has been added
    private String currentUser;

    public PaymentGui(String currentUserEmail) {
        this.currentUser = currentUserEmail;  // receive value current user
        setTitle("Payment Form");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // Main Panel Setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Title
        JLabel titleLabel = new JLabel("Payment Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 167, 240));
        mainPanel.add(titleLabel);

        // Purchase Details Area (no address)
        purchaseDetailsArea = new JTextArea(10, 30);
        purchaseDetailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(purchaseDetailsArea);
        mainPanel.add(scrollPane);

        // Add total price label
        totalAmountLabel = new JLabel("Total Amount: RM 0.00");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalAmountLabel.setForeground(new Color(34, 167, 240));
        mainPanel.add(totalAmountLabel);

        // Address Panel for Delivery
        JPanel addressPanel = new JPanel();
        addressPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel addressLabel = new JLabel("Confirm Delivery Address: ");
        addressPanel.add(addressLabel);

        addressConfirmationField = new JTextField(20);
        addressConfirmationField.setEnabled(false);  // Initially disabled (disabled for pickup)
        addressPanel.add(addressConfirmationField);

        mainPanel.add(addressPanel);

        // Option Panel for Delivery or Pickup (Tabbed)
        JTabbedPane optionTabs = new JTabbedPane();
        JPanel pickupPanel = createPickupPanel();
        JPanel deliveryPanel = createDeliveryPanel();

        optionTabs.addTab("Pickup", pickupPanel);
        optionTabs.addTab("Delivery", deliveryPanel);

        mainPanel.add(optionTabs);

        // Confirm Payment Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton confirmPaymentButton = new JButton("Confirm Payment");
        confirmPaymentButton.setFont(mainFont);
        confirmPaymentButton.setBackground(new Color(34, 167, 240));
        confirmPaymentButton.setForeground(Color.WHITE);
        confirmPaymentButton.setFocusPainted(false);

        confirmPaymentButton.addActionListener(e -> confirmPayment(currentUserEmail));

        buttonPanel.add(confirmPaymentButton);

        mainPanel.add(buttonPanel);

        // Add main panel to the frame
        add(mainPanel, BorderLayout.CENTER);

        // Load Purchase Details
        loadPurchaseData(currentUserEmail);  // Load data based on the dynamic email
    }

    // Create Pickup Panel
    private JPanel createPickupPanel() {
        JPanel pickupPanel = new JPanel();
        pickupPanel.setLayout(new BoxLayout(pickupPanel, BoxLayout.Y_AXIS));

        JLabel pickupLabel = new JLabel("Choose Pickup Option");
        pickupLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pickupLabel.setForeground(new Color(34, 167, 240));
        pickupPanel.add(pickupLabel);

        JLabel pickupInfo = new JLabel("No delivery charges apply for Pickup.");
        pickupInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        pickupPanel.add(pickupInfo);

        return pickupPanel;
    }

    // Create Delivery Panel
    private JPanel createDeliveryPanel() {
        JPanel deliveryPanel = new JPanel();
        deliveryPanel.setLayout(new BoxLayout(deliveryPanel, BoxLayout.Y_AXIS));

        // Delivery Checkbox
        deliveryCheckBox = new JCheckBox("Delivery (RM 10 extra)");
        deliveryCheckBox.addItemListener(e -> updateTotalAmount());

        JLabel deliveryInfo = new JLabel("Delivery fee is RM10.");
        deliveryInfo.setFont(new Font("Arial", Font.PLAIN, 14));

        deliveryPanel.add(deliveryCheckBox);
        deliveryPanel.add(deliveryInfo);

        return deliveryPanel;
    }

    // Load Purchase Data (excluding address from details)
    private void loadPurchaseData(String currentUserEmail) {
        try (BufferedReader reader = new BufferedReader(new FileReader("UserPurchased.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equalsIgnoreCase(currentUserEmail)) {  // Case insensitive comparison
                    String name = data[1];
                    String address = data[2];
                    currentAddress = address;  // Store the current address for later use
                    StringBuilder purchaseDetails = new StringBuilder();
                    purchaseDetails.append("Name: ").append(name).append("\n");

                    double totalPrice = 0.0;
                    for (int i = 3; i < data.length - 1; i += 2) {
                        String itemName = data[i];
                        int quantity = Integer.parseInt(data[i + 1]);
                        double itemPrice = getItemPrice(itemName);
                        totalPrice += itemPrice * quantity;
                        purchaseDetails.append(itemName).append(" (x").append(quantity).append(") - RM").append(itemPrice * quantity).append("\n");
                    }

                    totalAmount = totalPrice;
                    purchaseDetails.append("\nTotal: RM").append(String.format("%.2f", totalAmount));
                    purchaseDetailsArea.setText(purchaseDetails.toString());
                    totalAmountLabel.setText("Total Amount: RM " + String.format("%.2f", totalAmount));

                    // Pre-fill address field with user's address, but don't display it in purchase details
                    addressConfirmationField.setText(currentAddress);

                    // Enable address field for editing
                    addressConfirmationField.setEnabled(true);

                    return;  // Exit once the user data is found
                }
            }
            JOptionPane.showMessageDialog(this, "User not found in purchase data.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading the purchase data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Get Item Price for items
    private double getItemPrice(String itemName) {
        switch (itemName) {
            case "BBQ Smokehouse Burger": return 12.90;
            case "Classic Cheeseburger": return 9.90;
            case "Grilled Chicken Burger": return 11.90;
            case "Fried Chicken (2 pcs)": return 8.90;
            case "Chicken Wings (6 pcs)": return 12.90;
            case "Chicken Tenders (4 pcs)": return 9.90;
            case "French Fries (Regular)": return 4.50;
            case "Curly Fries": return 5.50;
            case "Sweet Potato Fries": return 6.50;
            case "Soft Drinks": return 3.50;
            case "Iced Tea": return 4.50;
            case "Milkshakes": return 8.90;
            case "Classic Burger Combo": return 14.90;
            case "Chicken Lover's Combo": return 16.90; 

            default: return 0.0;
        }
    }

    // Update Total Amount based on Delivery Option
    private void updateTotalAmount() {
        if (deliveryCheckBox.isSelected()) {
            if (!deliveryFeeAdded) {  // Check if delivery fee has been added
                totalAmount += 10.0;  // Add RM 10 for delivery fee
                deliveryFeeAdded = true;
            }
        } else {
            if (deliveryFeeAdded) {  // Subtract RM 10 if delivery fee was added earlier
                totalAmount -= 10.0;
                deliveryFeeAdded = false;
            }
        }
        totalAmountLabel.setText("Total Amount: RM " + String.format("%.2f", totalAmount));
    }

    // Confirm Payment
    private void confirmPayment(String currentUserEmail) {
        // Check for delivery and add RM 10 if selected
        String enteredAddress = addressConfirmationField.getText();
        if (deliveryCheckBox.isSelected()) {
            if (enteredAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please confirm your address for delivery.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Show the updated total including the delivery fee and confirm the address
            JOptionPane.showMessageDialog(this,
                    "Your total amount is RM" + String.format("%.2f", totalAmount) + " (including RM10 delivery fee).\n" +
                            "Delivery Address: " + enteredAddress,
                    "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);

            // Create the receipt and save it as a text file
            saveReceipt(enteredAddress);
        } else {
            JOptionPane.showMessageDialog(this, "Your total amount is RM" + String.format("%.2f", totalAmount) + ". Proceeding to payment.", "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);
            // Create the receipt and save it as a text file
            saveReceipt("Pickup (No delivery address required)");
        }
    }

    // Save Receipt to a text file
    private void saveReceipt(String deliveryInfo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("receipt.txt"))) {
            writer.write("***** Receipt *****\n");
            writer.write("User: " + currentUser + "\n");
            writer.write("Total Amount: RM" + String.format("%.2f", totalAmount) + "\n");
            writer.write("Delivery Info: " + deliveryInfo + "\n");

            writer.write("\nItems Purchased:\n");
            writer.write(purchaseDetailsArea.getText());
            
            writer.write("\nThank you for your purchase!\n");
            writer.write("***** End of Receipt *****");

            JOptionPane.showMessageDialog(this, "Receipt has been saved as 'receipt.txt'.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving the receipt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
