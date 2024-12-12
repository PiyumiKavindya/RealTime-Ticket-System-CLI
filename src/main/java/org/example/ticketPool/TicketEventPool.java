package org.example.ticketPool;

import java.util.LinkedList;
import java.util.Queue;

public class TicketEventPool {
    // Queue to store tickets
    private final Queue<Integer> tickets;
    private final int maxCapacity;
    private final int totalTickets; // Total tickets allowed in the system

    public TicketEventPool(int maxCapacity, int totalTickets) {
        this.tickets = new LinkedList<>();
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
    }

    // Add a ticket to the pool if the pool is not full and notify consumers that a ticket is available
    // Return true if the ticket is added successfully, false otherwise
    // This method is synchronized to prevent
    public synchronized boolean addTicket(int ticketId) {
        if (tickets.size() >= maxCapacity) {
            return false; // Pool is full
        }
        tickets.add(ticketId);
        notifyAll(); // Notify consumers that a ticket is available
        return true;
    }

    public synchronized Integer removeTicket() {
        while (tickets.isEmpty()) {
            try {
                wait(); // Wait for tickets to be available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return tickets.poll();
    }

    public synchronized int getCurrentSize() {
        return tickets.size();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }
}
