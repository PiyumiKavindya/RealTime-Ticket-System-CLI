package org.example.ticket;



// Customer class that represents a customer buying tickets from the ticket pool at a specified rate
// The customer retrieves tickets from the ticket pool at a specified rate and buys them
// The customer stops buying tickets when all tickets have been bought
// The customer stops buying tickets when the vendor stops adding tickets
// The customer implements the Runnable interface to be executed by a thread
// OOP feature: Encapsulation - The customer class encapsulates the ticket pool and the customer retrieval rate
// OOP feature: Inheritance - The customer class inherits from the Runnable interface
// OOP feature: Polymorphism - The customer class overrides the run method from the Runnable interface
// OOP feature: Abstraction - The customer class abstracts the behavior of a customer buying tickets

import org.example.logger.Logger;
import org.example.ticketPool.TicketEventPool;

public class Customer implements Runnable {
    private final TicketEventPool ticketEventPool;
    private final int customerRetrievalRate;

    public Customer(TicketEventPool ticketEventPool, int customerRetrievalRate) {
        this.ticketEventPool = ticketEventPool;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(customerRetrievalRate * 1000L); // Retrieve tickets at a specified rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve interrupted status
                //System.out.println("Customer thread interrupted.");
                Logger.log("Customer thread interrupted.");
                return;
            }

            synchronized (ticketEventPool) {
                if (ticketEventPool.getCurrentSize() > 0) {
                    Integer ticketId = ticketEventPool.removeTicket();
                    //System.out.println("Customer bought Ticket ID: " + ticketId + " | Tickets Remaining: " + ticketPool.getCurrentSize());
                    Logger.log("Customer bought Ticket ID: " + ticketId + " | Tickets Remaining: " + ticketEventPool.getCurrentSize());
                } else if (Vendor.ticketsAddedSoFar == ticketEventPool.getTotalTickets()) {
                    //System.out.println("All tickets have been bought. Stopping customers...");
                    Logger.log("All tickets have been bought. Stopping customers...");
                    return;
                }
            }
        }
    }
}
