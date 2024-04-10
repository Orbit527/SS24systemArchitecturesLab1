package at.fhv.lab1.commandclient.commandHandler;

import at.fhv.lab1.commandclient.EventPublisher;
import at.fhv.lab1.commandclient.commands.CreateCustomerCommand;
import at.fhv.lab1.commandclient.commands.RoomBookedCommand;
import at.fhv.lab1.commandclient.database.BookingDB;
import at.fhv.lab1.commandclient.database.CustomerDB;
import at.fhv.lab1.commandclient.domain.Booking;
import at.fhv.lab1.commandclient.domain.Customer;
import at.fhv.lab1.eventbus.events.RoomBookedEvent;

import java.util.Objects;

public class CommandHandler {

    private final EventPublisher eventPublisher;
    //private RoomDB roomDB;


    public CommandHandler() {
        eventPublisher = new EventPublisher();
        //roomDB = new RoomDB();
    }

    public String handleRoomBookedCommand(RoomBookedCommand r) {

        //TODO: Validate before creating event

        //TODO: check booking, if customer and room id exists

        //check that endDate is after startDate
        if (r.getEndDate().isBefore(r.getStartDate())) {
            System.out.println("NOT POSSIBLE");
            return "Enddate is before Startdate";
        }

        //check for overlapping date
        for (Booking b : BookingDB.getBookings()) {
            //Booking has to be on the same room
            if (b.getRoom().getId() == r.getRoom().getId()) {
                if(b.getStartDate().isBefore(r.getEndDate()) && b.getEndDate().isAfter(r.getStartDate())) {
                    System.out.println("NOT POSSIBLE!");
                    return "There is a booking already in this timeframe";
                }
            }
        }


        /*
        List<Customer> customerWithId1 = CustomerDB.getCustomers()
                .stream()
                .filter(c -> c.getId() == 1)
                .collect(Collectors.toList());

        for (Customer c : customerWithId1 ) {
            System.out.println("CUSTOMER WITH ID 1: " + c);
        }
         */


        RoomBookedEvent roomBookedEvent = new RoomBookedEvent();

        roomBookedEvent.setCustomer(r.getCustomer());
        roomBookedEvent.setRoom(r.getRoom());
        roomBookedEvent.setBooking(r.getBooking()); //TODO: add real parameters
        roomBookedEvent.setStartDate(r.getStartDate());
        roomBookedEvent.setEndDate(r.getEndDate());

        System.out.println("BookRoomEvent: " + eventPublisher.publishEvent(roomBookedEvent));

        //TODO: sent true or false, if something fails or not
        return "0";
    }

    public boolean handleCreateCustomerCommand(CreateCustomerCommand c) {


        //Customer Email exists validation
        for(Customer cust : CustomerDB.getCustomers()) {
            if (Objects.equals(cust.getEmail(), c.getEmail())) {
                System.out.println("Customer with that email already exists!");
                return false;
            }
        }

        //TODO: further Validation

        //TODO: Create Event and send to EventBus

        return true;

    }

}
