# Airline Management System — Product Requirements Document
> Low Level Design Edition

---

## 1. System Overview

Design an **Airline Management System** that allows passengers to search, book, and manage
flights while enabling airline staff and administrators to manage flight operations, crew
assignments, and aircraft scheduling. The system must handle concurrent seat bookings,
process payments through multiple gateways, manage refunds, generate boarding passes,
and notify users of relevant events — all while being extensible and scalable for future growth.

---

## 2. Core Actors

| Actor | Description |
|---|---|
| **Passenger** | Searches flights, books seats, makes payments, cancels bookings |
| **Airline Staff** | Manages flight operations, assigned to departments |
| **Admin** | Manages aircraft, crew, routes, flight schedules |
| **System** | Handles seat lock expiry, notifications, payment processing |

---

## 3. Functional Requirements

---

### 3.1 Flight Management

- The system shall maintain **flight information** including a unique flight ID, route
  (source and destination airports), schedule (departure and arrival datetime),
  assigned aircraft, seat map, crew list, and current flight status.
- Each **airport** shall be identified by a unique IATA code, name, and city.
- A **route** shall be defined as a directional pair of source and destination airports.
- A **schedule** shall store departure and arrival timestamps as `LocalDateTime`.
- The system shall support **flight status tracking** with the following states:
    - `SCHEDULED` → `DELAYED` → `BOARDING` → `DEPARTED` → `ARRIVED`
    - `SCHEDULED` → `CANCELLED`
- An **administrator** shall be able to update flight status at any time.
- The system shall support **multiple flights** on the same route on the same day.

---

### 3.2 Aircraft Management

- The system shall maintain **aircraft records** with a unique aircraft ID,
  model name, and total seating capacity.
- An **administrator** shall be able to **assign an aircraft** to a flight.
- Each aircraft shall have a defined set of **seats** organized by seat number
  and seat class (`ECONOMY`, `PREMIUM`, `BUSINESS`, `FIRST`).
- The same aircraft shall not be assignable to two overlapping flights *(future enhancement)*.

---

### 3.3 Crew Management

- The system shall maintain **crew members** identified by a crew ID and role.
- Supported crew roles shall include: `PILOT`, `COPILOT`, `CABIN_CREW`.
- An **administrator** shall be able to **assign a list of crew members** to a flight.
- A valid flight must have at least one `PILOT` and one `COPILOT` assigned
  *(future validation)*.

---

### 3.4 Flight Search

- A **passenger** shall be able to search for available flights by providing:
    - Source airport code
    - Destination airport code
    - Travel date
- Search results shall return a list of matching flights **enriched with pricing
  information** per seat class.
- The pricing response shall include base fare per `SeatClass` for each flight found.
- Search shall be **case-insensitive** for airport codes.

---

### 3.5 Seat Management

- Each flight shall maintain a **seat inventory** tracking the status of every
  seat independently.
- Seat status lifecycle:
    - `AVAILABLE` → `LOCKED` → `BOOKED`
    - `LOCKED` → `AVAILABLE` (on expiry or cancellation)
    - `BOOKED` → `AVAILABLE` (on booking cancellation)
- The system shall support **seat locking** for a configurable duration
  (default: 5 minutes) during the booking initiation phase to prevent double booking.
- A **seat lock** shall record:
    - Which flight and seat it applies to
    - Which booking ID holds the lock
    - When the lock expires
- The system shall provide a **background expiry service** that periodically
  scans and releases expired seat locks, resetting affected seats to `AVAILABLE`.
- Each seat inventory record shall maintain a **version number** for optimistic
  concurrency control.
- The system shall support a **manual seat selection strategy** where passengers
  explicitly choose their seats.
- The `SeatAllocationStrategy` interface shall be extensible to support
  **auto-assignment** strategies in the future.

---

### 3.6 Booking Management

- A **passenger** shall be able to initiate a booking by providing:
    - Flight ID
    - List of selected seats
    - List of passengers
    - Total booking amount
- The booking lifecycle shall follow these states:
    - `INITIATED` → `CONFIRMED` → `CANCELLED`
    - `INITIATED` → `FAILED` (on payment failure)
- On booking initiation:
    - All selected seats shall be **locked atomically**
    - If any seat lock fails, all previously locked seats in the same request
      shall be **rolled back**
    - A booking record shall be created with status `INITIATED`
- On booking confirmation:
    - Payment status must be `SUCCESS` before confirmation is allowed
    - All locked seats shall transition to `BOOKED`
    - Booking status shall be updated to `CONFIRMED`
    - A **confirmation notification** shall be sent to the passenger
- On booking cancellation:
    - All booked/locked seats shall be released back to `AVAILABLE`
    - A **refund** shall be processed based on the cancellation policy
    - A **cancellation notification** shall be sent to the passenger
- Booking shall store a **passenger-to-seat mapping**.
- Each booking shall reference its associated **payment record**.

---

### 3.7 Payment Management

- The system shall support **multiple payment gateways**, selectable at runtime
  by provider name.
- Supported payment providers shall include: `CARD`, `UPI`
  (extensible via factory).
- Each payment shall track:
    - A unique payment ID
    - Amount
    - Status: `PENDING` → `SUCCESS` / `FAILED` / `REFUNDED`
- Payment status transitions shall be done via explicit methods
  (`markSuccess()`, `markFailed()`, `markRefunded()`) rather than direct
  field mutation.
- All payment records shall be **persisted regardless of success or failure**
  for audit purposes.
- On payment failure, the associated booking shall transition to `FAILED`
  and seats shall be released.

---

### 3.8 Refund Management

- The system shall process refunds upon **booking cancellation**.
- Refund amount shall be calculated based on a **configurable refund policy**
  using hours before departure:
    - **≥ 48 hours** before departure → **90% refund**
    - **≥ 24 hours** before departure → **50% refund**
    - **< 24 hours** before departure → **No refund**
- The refund policy shall implement the `RefundPolicy` interface,
  making it **swappable**
  (e.g., `StandardRefundPolicy`, `FlexibleRefundPolicy`, `NoRefundPolicy`).
- The system shall check for a valid payment record before processing a refund.

---

### 3.9 Passenger & Baggage Management

- Each **passenger** shall be identified by a unique passenger ID,
  full name, and passport number.
- Passport number shall be a **mandatory non-empty field**.
- The system shall store **baggage information** per passenger per flight,
  including item count and total weight.
- Baggage records shall be queryable by flight ID or by
  flight ID + passenger ID combination.

---

### 3.10 Boarding Pass Generation

- The system shall **automatically generate boarding passes** upon successful
  booking confirmation.
- Each boarding pass shall contain:
    - Unique boarding pass ID
    - Booking ID reference
    - Passenger ID
    - Flight ID
    - Assigned seat number
    - Gate information
    - Boarding time (45 minutes before departure)
- Boarding passes shall be **retrievable** by passenger ID and flight ID.
- One boarding pass shall be generated **per passenger per booking**.

---

### 3.11 Notification Management

- The system shall send notifications to passengers on the following events:

| Event | Trigger |
|---|---|
| `BOOKING_CONFIRMED` | Successful booking completion |
| `BOOKING_CANCELLED` | Booking cancellation |
| `PAYMENT_SUCCESS` | Successful payment |
| `PAYMENT_FAILED` | Payment failure |
| `FLIGHT_DELAYED` | Flight status changed to delayed |
| `FLIGHT_CANCELLED` | Flight status changed to cancelled |
| `BOARDING_PASS_GENERATED` | Boarding pass creation |

- Each notification shall record:
    - Notification ID
    - Recipient ID
    - Notification type
    - Message
    - Creation timestamp
- Notifications shall be **queryable per recipient**.
- The `NotificationService` interface shall be extensible to support
  **email, SMS, or push** delivery channels in the future.

---

### 3.12 Flight Pricing

- Each flight shall have **per-class pricing** defined by seat class
  (`ECONOMY`, `PREMIUM`, `BUSINESS`, `FIRST`).
- Pricing shall be stored and queryable by flight ID and seat class combination.
- Pricing records shall be returned **alongside flight search results**.

---

### 3.13 User Management

- The system shall support three user types: `PASSENGER`, `STAFF`, `ADMIN`.
- Each user shall have a unique username and an assigned role.
- `AirlineStaff` shall additionally carry a department identifier.
- Role-based access control (RBAC) shall be enforced at the service layer
  *(future enhancement)*.

---

## 4. Non-Functional Requirements

---

### 4.1 Concurrency & Thread Safety

- The system shall handle **concurrent seat booking requests** without
  allowing double booking.
- Seat locking shall use **per-seat granular locking** rather than global
  locks to maximize throughput.
- Lock acquisition shall be **atomic** using `ConcurrentHashMap.compute()`
  to eliminate race conditions.
- All repository implementations shall use `ConcurrentHashMap` for
  thread-safe in-memory storage.
- Seat inventory shall use **optimistic locking via a version field** to
  detect and reject stale updates.
- Partial booking failures shall trigger **atomic rollback** of all seat
  locks acquired in the same request.
- Expired seat locks shall be **automatically cleaned up** by a scheduled
  background service running at a 1-minute interval.

---

### 4.2 Data Consistency

- Seat status changes shall follow a strict **state machine** —
  invalid transitions shall throw exceptions.
- Booking state transitions shall be **validated** before execution
  (e.g., cannot confirm a cancelled booking).
- Payment records shall be saved for **all outcomes** (success and failure)
  for auditability.
- All in-memory stores shall return **defensive copies** of collections
  to prevent external mutation.

---

### 4.3 Extensibility

- Payment gateways shall be **pluggable** via the `PaymentGateway`
  interface and `PaymentGatewayFactory`.
- Seat allocation strategies shall be **swappable** via the
  `SeatAllocationStrategy` interface.
- Refund policies shall be **interchangeable** via the `RefundPolicy`
  interface.
- All data access shall be behind **repository interfaces** to allow
  swapping in-memory stores with database implementations.
- Notification delivery mechanisms shall be **abstracted** behind
  `NotificationService`.

---

### 4.4 Scalability

- Repository interfaces shall be designed to support **pagination** on
  list operations in future implementations.
- The factory pattern shall support **registry-based gateway registration**
  in future to avoid modifying the factory class for new providers.
- The background seat lock expiry service shall be designed as a
  **single-threaded scheduler**, upgradeable to a distributed scheduler.

---

### 4.5 Testability

- All services shall use **constructor-based dependency injection** for
  easy unit testing.
- Business logic shall be separated from persistence via the
  repository pattern.
- Strategies shall be independently **unit-testable** without requiring
  full service context.

---

## 5. Design Patterns Required

| Pattern | Purpose |
|---|---|
| **Strategy** | Seat allocation, refund policy, payment gateway |
| **Factory** | Payment gateway instantiation |
| **Repository** | Data access abstraction |
| **Dependency Injection** | Constructor injection across all services |
| **State Machine** | Booking, payment, seat, and flight status transitions |

---

## 6. Domain Model Summary

| Entity | Key Attributes |
|---|---|
| `Flight` | flightId, route, schedule, aircraft, seats, crew, status |
| `Booking` | bookingId, flight, passengers, seats, amount, payment, status |
| `Payment` | paymentId, amount, status |
| `SeatInventory` | flightId, seatNumber, seatClass, status, bookingId, version |
| `SeatLock` | flightId, seatNumber, bookingId, expiresAt |
| `Passenger` | passengerId, name, passport |
| `Baggage` | count, weight, passengerId, flightId |
| `BoardingPass` | boardingPassId, bookingId, passengerId, flightId, seat, gate, boardingTime |
| `Notification` | notificationId, recipientId, type, message, createdAt |
| `FlightPrice` | flightId, seatClass, baseFare |
| `Aircraft` | aircraftId, model, capacity |
| `Crew` | crewId, role |
| `Route` | source, destination |
| `Schedule` | departure, arrival |
| `Airport` | code, name, city |

---

## 7. Out of Scope (Future Enhancements)

- Role-based access control (RBAC) enforcement at API level
- Crew conflict detection (same crew assigned to overlapping flights)
- Aircraft conflict detection (same aircraft on overlapping schedules)
- Dynamic pricing and surge pricing engine
- Loyalty points and frequent flyer program
- Multi-leg / connecting flight support
- Real-time flight tracking integration
- Email / SMS / Push notification delivery channels
- Distributed lock management (e.g., Redis-based)
- Persistent database-backed repositories
- Pagination on list operations
- Seat upgrade and change flow
- Check-in management