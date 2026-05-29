package com.airline;

import com.airline.enums.CrewRole;
import com.airline.enums.SeatClass;
import com.airline.enums.SeatStatus;
import com.airline.model.*;
import com.airline.repository.*;
import com.airline.repository.impl.*;
import com.airline.service.*;
import com.airline.service.impl.*;
import com.airline.strategy.refund.FlexibleRefundPolicy;
import com.airline.strategy.seat.ManualSeatAllocationStrategy;
import com.airline.strategy.seat.SeatAllocationStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Airport airport1 = new Airport("DEL", "INDRA GANDHI", "Delhi");
        Airport airport2 = new Airport("BLR", "KEMPOGOWDA", "Bangalore");
        Airport airport3 = new Airport("PNQ", "PUNE INTERNATIONAL", "Pune");

        Route route1 = new Route(airport1, airport2);
        Route route2 = new Route(airport1, airport3);

        Schedule schedule1 = new Schedule(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3));
        Schedule schedule2 = new Schedule(LocalDateTime.now().plusDays(1).plusHours(5), LocalDateTime.now().plusDays(1).plusHours(8));

        Aircraft aircraft1 = new Aircraft("A1", "Boeing 737", 3);
        Aircraft aircraft2 = new Aircraft("A2", "Boeing 737", 3);

        Seat seat1 = new Seat("A1", SeatClass.BUSINESS);
        Seat seat2 = new Seat("A2", SeatClass.PREMIUM);
        Seat seat3 = new Seat("A3", SeatClass.ECONOMY);


        Crew crew1 = new Crew("CR1", CrewRole.PILOT);
        Crew crew2 = new Crew("CR2", CrewRole.COPILOT);
        Crew crew3 = new Crew("CR3", CrewRole.CABIN_CREW);
        List<Crew> crews = new ArrayList<>();
        crews.add(crew1);
        crews.add(crew2);
        crews.add(crew3);

        List<Flight> flights = List.of(new Flight("FL-101", route1, schedule1, aircraft1, Map.of("A1", seat1, "A2", seat2, "A3", seat3), crews),
                new Flight("FL-102", route2, schedule2, aircraft2, Map.of("A1", seat1, "A2", seat2, "A3", seat3), crews));

        FlightRepository flightRepository =
                new InMemoryFlightRepository();
        flights.forEach(flightRepository::save);

        SeatInventoryRepository seatInventoryRepository =
                new InMemorySeatInventoryRepository();

        for (Flight flight : flights) {
            for (Seat seat : flight.getSeats().values()) {
                SeatInventory inventory = new SeatInventory(
                        flight.getFlightId() + "-" + seat.getSeatNumber(),
                        flight.getFlightId(),
                        seat.getSeatNumber(),
                        seat.getSeatClass(),
                        SeatStatus.AVAILABLE,
                        null,
                        0
                );

                seatInventoryRepository.save(inventory);
            }
        }

        FlightPrice flightPrice1 = new FlightPrice("1", "FL-101", SeatClass.BUSINESS);
        flightPrice1.setBaseFare(10000);
        FlightPrice flightPrice2 = new FlightPrice("2", "FL-101", SeatClass.PREMIUM);
        flightPrice2.setBaseFare(5000);
        FlightPrice flightPrice3 = new FlightPrice("3", "FL-101", SeatClass.ECONOMY);
        flightPrice3.setBaseFare(2000);
        FlightPrice flightPrice4 = new FlightPrice("4", "FL-102", SeatClass.BUSINESS);
        flightPrice4.setBaseFare(10000);
        FlightPrice flightPrice5 = new FlightPrice("5", "FL-102", SeatClass.PREMIUM);
        flightPrice5.setBaseFare(10000);
        FlightPrice flightPrice6 = new FlightPrice("6", "FL-102", SeatClass.ECONOMY);
        flightPrice6.setBaseFare(10000);

        List<FlightPrice> flightPrices = List.of(flightPrice1, flightPrice2, flightPrice3, flightPrice4, flightPrice5, flightPrice6);

        FlightPriceRepository flightPriceRepository = new FlightPriceRepositoryImpl();
        flightPrices.forEach(flightPriceRepository::save);

        FlightSearchService flightSearchService =
                new FlightSearchServiceImpl(flightRepository, flightPriceRepository);
        System.out.println("=== SEARCH FLIGHT BETWEEN DELHI TO BANGALORE===");
        List<FlightWithFlightPrice> flightWithFlightPrices = flightSearchService.searchFlights(
                "DEL",
                "BLR",
                flights.get(0).getSchedule().getDeparture().toLocalDate()
        );
        flightWithFlightPrices.forEach(System.out::println);

        Passenger passenger = new Passenger("P1", "Nishant", "");
        PassengerRepository passengerRepository = new InMemoryPassengerRepository();
        passengerRepository.save(passenger);


        BookingRepository bookingRepository =
                new InMemoryBookingRepository();
        PaymentRepository paymentRepository =
                new InMemoryPaymentRepository();
        PaymentService paymentService =
                new PaymentServiceImpl(paymentRepository);
        RefundService refundService =
                new RefundServiceImpl(paymentService, new FlexibleRefundPolicy());
        SeatAllocationStrategy seatAllocationStrategy = new ManualSeatAllocationStrategy(seatInventoryRepository);

        BookingService bookingService =
                new BookingServiceImpl(
                        flightRepository,
                        bookingRepository,
                        refundService,
                        paymentRepository,
                        seatAllocationStrategy,
                        seatInventoryRepository
                );


        System.out.println("\n=== INITIATE BOOKING FLIGHT ===");

        Booking booking = bookingService.initiateBooking(
                flightWithFlightPrices.get(0).getFlight().getFlightId(),
                flightWithFlightPrices.get(0).getPrice().get(0).getBaseFare(),
                List.of(flightWithFlightPrices.get(0).getFlight().getSeats().get("A1")),
                List.of(passenger)
        );

        System.out.println("Process Payment");
        Payment payment = paymentService.processPayment(
                booking.getBookingAmount(),
                "UPI"
        );
        System.out.println("Payment processed :: " + payment);

        System.out.println("Confirm Booking");
        Booking booking2 = bookingService.completeBooking(booking.getBookingId(), payment.getPaymentId());
        System.out.println("Booking completed :: " + booking2);
    }
}