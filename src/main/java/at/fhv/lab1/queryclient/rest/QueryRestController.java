package at.fhv.lab1.queryclient.rest;

import at.fhv.lab1.eventbus.events.CancelBookingEvent;
import at.fhv.lab1.eventbus.events.CreateCustomerEvent;
import at.fhv.lab1.eventbus.events.CreateRoomEvent;
import at.fhv.lab1.eventbus.events.RoomBookedEvent;
import at.fhv.lab1.queryclient.database.BookingsProjectedDB;
import at.fhv.lab1.queryclient.database.CustomersProjectedDB;
import at.fhv.lab1.queryclient.database.FreeRoomsProjectedDB;
import at.fhv.lab1.queryclient.domain.BookingsProjected;
import at.fhv.lab1.queryclient.domain.CustomersProjected;
import at.fhv.lab1.queryclient.domain.FreeRoomsProjected;
import at.fhv.lab1.queryclient.domain.Timeframe;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryRestController {


    public QueryRestController() {
    }

    @PostMapping(value = "/eventRoomBookedAdded", consumes = "application/json")
    public boolean addRoomBookedEvent(@RequestBody RoomBookedEvent event) {

        System.out.println("Event received: " + event);

        BookingsProjected bookingsProjected = new BookingsProjected();

        bookingsProjected.setBookingId(event.getBooking().getId());
        bookingsProjected.setCapacity(event.getRoom().getCapacity());
        bookingsProjected.setCustomerFirstname(event.getCustomer().getFirstname());
        bookingsProjected.setCustomerSurname(event.getCustomer().getSurname());
        bookingsProjected.setStartDate(event.getStartDate());
        bookingsProjected.setEndDate(event.getEndDate());
        bookingsProjected.setRoomNr(event.getRoom().getRoomNr());
        bookingsProjected.setFloor(event.getRoom().getFloor());

        BookingsProjectedDB.addBooking(bookingsProjected);


        //handle timeFrame setter

        FreeRoomsProjected test = FreeRoomsProjectedDB.getRoomById(event.getRoom().getId());
        Timeframe timeframe = new Timeframe(event.getStartDate(), event.getEndDate());
        System.out.println("JAAAAAAAAAAAAAAA" + test + timeframe);

        test.addTimeFrame(timeframe);


        for (BookingsProjected bp : BookingsProjectedDB.getBookings()) {
            System.out.println("TEST: " + bp);
        }



        return true;
    }

    @PostMapping(value = "/eventCancelBooking", consumes = "application/json")
    public boolean cancelBookingEvent(@RequestBody CancelBookingEvent event) {
        // TODO: process event through projection

        //TODO: remove from projected DB

        System.out.println("Event received: " + event);

        return true;
    }


    @PostMapping(value = "/eventCustomerAdded", consumes = "application/json")
    public boolean addCustomerEvent(@RequestBody CreateCustomerEvent event) {
        System.out.println("Event received: " + event);


        CustomersProjected customersProjected = new CustomersProjected();

        customersProjected.setFirstname(event.getFirstname());
        customersProjected.setSurname(event.getSurname());
        customersProjected.setBirthdate(event.getBirthdate());
        customersProjected.setEmail(event.getEmail());
        customersProjected.setAddress(event.getAddress());

        CustomersProjectedDB.addCustomer(customersProjected);

        //System.out.println("PROJECTED: " + customersProjected);


        for (CustomersProjected bp : CustomersProjectedDB.getCustomers()) {
            System.out.println("TEST: " + bp);
        }





        return true;
    }



    @PostMapping(value = "/roomcreateevent", consumes = "application/json")
    public boolean createRoomEvent(@RequestBody CreateRoomEvent event) {
        // TODO: process event through projection

        FreeRoomsProjected freeRoomsProjected = new FreeRoomsProjected();

        freeRoomsProjected.setRoomId(event.getRoomId());
        freeRoomsProjected.setRoomNr(event.getRoomNr());
        freeRoomsProjected.setFloor(event.getFloor());
        freeRoomsProjected.setCapacity(event.getCapacity());

        FreeRoomsProjectedDB.addFreeRoom(freeRoomsProjected);

        for(FreeRoomsProjected frp : FreeRoomsProjectedDB.getFreeRooms()) {
            System.out.println("FREE ROOMS DB: " + frp);
        }


        System.out.println("Event received: " + event);

        return true;
    }

}
