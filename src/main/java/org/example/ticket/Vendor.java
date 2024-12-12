package org.example.ticket;


import org.example.logger.Logger;
import org.example.ticketPool.TicketEventPool;

import java.util.Random;

// Vendor class that represents a vendor adding tickets to the ticket pool at a specified rate until the total limit is reached
// The vendor adds tickets to the ticket pool at a specified rate until the total limit is reached
// The vendor stops adding tickets when the total limit is reached
// The vendor implements the Runnable interface to be executed by a thread

public class Vendor implements Runnable {
    private final TicketEventPool ticketEventPool;
    private final int ticketReleaseRate;
    private final int totalTickets;
    private final int id;
    static int ticketsAddedSoFar = 0; // Shared count among vendors

    public Vendor(int id, TicketEventPool ticketEventPool, int ticketReleaseRate, int totalTickets) {
        this.id = id;
        this.ticketEventPool = ticketEventPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTickets = totalTickets;
    }

    @Override
    public void run() {
        Random random = new Random();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(ticketReleaseRate * 1000L); // Release tickets at a specified rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve interrupted status
                //System.out.println("Vendor thread interrupted.");
                Logger.log("Vendor [" + id + "] thread interrupted.");
                return;
            }

            synchronized (ticketEventPool) {
                if (ticketsAddedSoFar >= totalTickets) {
                    //System.out.println("Vendor has added the total limit of tickets. Stopping...");
                    Logger.log("Vendor [" + id + "] has added the total limit of tickets. Stopping...");
                    return;
                }

                if (ticketEventPool.getCurrentSize() < ticketEventPool.getMaxCapacity()) {
                    int ticketId = random.nextInt(1000) + 1; // Generate random ticket ID
                    ticketEventPool.addTicket(ticketId);
                    ticketsAddedSoFar++;
                    //System.out.println("Vendor added Ticket ID: " + ticketId + " | Total Tickets Added: " + ticketsAddedSoFar);
                    Logger.log("Vendor [" + id + "] added Ticket ID: " + ticketId + " | Total Tickets Added: " + ticketsAddedSoFar);
                } else {
                    //System.out.println("Vendor waiting: Ticket Pool is full.");
                    Logger.log("Vendor [" + id + "] waiting: Ticket Pool is full.");
                }
            }
        }

        //System.out.println("Vendor finished adding tickets.");
        Logger.log("Vendor finished adding tickets.");
    }
}
