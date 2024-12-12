package org.example.systemConfiguration;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SystemEventConfig {
    private int totalTickets;
    private int ticketReleaseRate; // How many tickets will be released to the pool at a second
    private int customerRetrievalRate; // How many tickets will a customer buy at a second
    private int maxTicketCapacity;
    private int vendor;
    private int customer;

    // Constructor
    public SystemEventConfig(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity, int vendor, int customer) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.vendor = vendor;
        this.customer = customer;
    }

    public SystemEventConfig() {}

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getVendor() {
        return vendor;
    }

    public void setVendor(int vendor) {
        this.vendor = vendor;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {

        this.maxTicketCapacity = maxTicketCapacity;
    }

    // Validate the input and return the value
    // If the input is invalid, print an error message and ask for the input again
    // The input must be a positive integer
    public int getValidatedInput(Scanner scanner, String inputName) {
        while (true) {
            try {
                int value = scanner.nextInt();
                if (value <= 0) {
                    throw new IllegalArgumentException(inputName + " cannot be negative or zero.");
                }
                return value;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                scanner.nextLine(); // Clear invalid input
                System.out.print("Re-enter " + inputName + ": ");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear invalid input
                System.out.print("Re-enter " + inputName + ": ");
            }
        }
    }


    // Save the configuration to a JSON file
    public void saveToFile(String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(this, writer);
            System.out.println("Configuration saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save configuration: " + e.getMessage());
        }
    }

    // Load configuration from a JSON file
    public static SystemEventConfig loadFromFile(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, SystemEventConfig.class);
        } catch (IOException e) {
            System.out.println("Failed to load configuration: " + e.getMessage());
            return null;
        }
    }
}
