package org.example;

import org.example.ticket.Customer;
import org.example.ticket.Vendor;
import org.example.logger.Logger;
import org.example.systemConfiguration.SystemEventConfig;
import org.example.ticketPool.TicketEventPool;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class TicketingSystemCLI {
    private enum SystemState { STOPPED, RUNNING }
    // Initial state is STOPPED
    private static SystemState currentState = SystemState.STOPPED;
    private static SystemEventConfig config = new SystemEventConfig();
    private static TicketEventPool ticketEventPool;
    final private static List<Thread> vendorThreads = new ArrayList<>();
    final private static List<Thread> customerThreads = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Welcome to the Event Ticketing System!");
        promptForInput();
        sysControl();
    }

    private static void promptForInput() {
        Scanner configScanner = new Scanner(System.in);

        System.out.print("Enter Total Tickets: ");
        config.setTotalTickets(config.getValidatedInput(configScanner, "Total Tickets"));

        System.out.print("Enter Ticket Release Rate: ");
        config.setTicketReleaseRate(config.getValidatedInput(configScanner, "Ticket Release Rate"));

        System.out.print("Enter Customer Retrieval Rate: ");
        config.setCustomerRetrievalRate(config.getValidatedInput(configScanner, "Customer Retrieval Rate"));

        System.out.print("Enter Maximum Ticket Capacity: ");
        config.setMaxTicketCapacity(config.getValidatedInput(configScanner, "Maximum Ticket Capacity"));

        System.out.print("Enter Vendor count: ");
        config.setVendor(config.getValidatedInput(configScanner, "vendor count"));

        System.out.print("Enter customer count: ");
        config.setCustomer(config.getValidatedInput(configScanner, "customer count"));

        ticketEventPool = new TicketEventPool(config.getMaxTicketCapacity(), config.getTotalTickets());
        config.saveToFile("src/main/resources/configuration.json");
        System.out.println("System configured successfully!");

    }

    private static void sysControl() {
        Scanner controlScanner = new Scanner(System.in);
        System.out.println(
                "=== System Controls Menu ===\n" +
                        "  [1] Load system configurations\n" +
                        "  [2] Start trading operations\n" +
                        "  [4] Stop trading operations\n" +
                        "  [5] Shut down the system\n" +
                        "============================="
        );


        while (true) {
            try {
                System.out.print("Enter command: ");
                int command = controlScanner.nextInt();

                switch (command) {
                    case 1:
                        loadConfig();
                        break;
                    case 2:
                        if (currentState == SystemState.RUNNING) {
                            System.out.println("System is already running.");
                        } else {
                            System.out.println("Starting buying and selling...");
                            startThreads();
                            currentState = SystemState.RUNNING;
                        }
                        break;
                    case 4:
                        if (currentState == SystemState.STOPPED) {
                            System.out.println("System is already stopped.");
                        } else {
                            System.out.println("Stopping buying and selling...");
                            stopThreads();
                            currentState = SystemState.STOPPED;
                        }
                        break;
                    case 5:
                        System.out.println("Stopping the system...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid command. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid command. Please enter a valid integer.");
                controlScanner.nextLine(); // Clear invalid input
            }
        }
    }

    // Start the vendor and customer threads
    private static void startThreads() {
        for (int i = 0; i < config.getVendor(); i++) {
            Thread vendorThread = new Thread(new Vendor(i+1, ticketEventPool, config.getTicketReleaseRate(), config.getTotalTickets()));
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        for (int i = 0; i < config.getCustomer(); i++) {
            Thread customerThread = new Thread(new Customer(ticketEventPool, config.getCustomerRetrievalRate()));
            customerThreads.add(customerThread);
            customerThread.start();
        }

        Logger.log("Ticket operations started with threads.");
    }




    private static void stopThreads() {
        try {
            for (Thread thread : vendorThreads) {
                if (thread.isAlive()) {
                    thread.interrupt();
                    thread.join(); // Wait for the thread to terminate
                }
            }
            for (Thread thread : customerThreads) {
                if (thread.isAlive()) {
                    thread.interrupt();
                    thread.join(); // Wait for the thread to terminate
                }
            }
            vendorThreads.clear();
            customerThreads.clear();
            System.out.println("Threads stopped.");
        } catch (Exception e) {
            System.out.println("Error stopping threads: " + e.getMessage());
        }
    }

    private static void loadConfig() {
        config = SystemEventConfig.loadFromFile("src/main/resources/configuration.json");
        if (config != null) {
            System.out.println("Configuration loaded successfully:");
            System.out.println("Total Tickets: " + config.getTotalTickets());
            System.out.println("Ticket Release Rate: " + config.getTicketReleaseRate());
            System.out.println("Customer Retrieval Rate: " + config.getCustomerRetrievalRate());
            System.out.println("Max Ticket Capacity: " + config.getMaxTicketCapacity());
            System.out.println("vendor count: " + config.getVendor());
            System.out.println("Customer count: " + config.getCustomer());
        }
    }


}

// # github repo urls :
//
// CLI - https://github.com/PiyumiKavindya/RealTime-Ticket-System-CLI.git
//
//BE - https://github.com/PiyumiKavindya/Real-Time-Event-Ticket-System.git
//
//FE - https://github.com/PiyumiKavindya/Real-Time-Ticket-System-frontend.git

