# Multi-Level Parking Lot System

## Problem Statement

Design and implement a multi-level parking lot system that can manage parking for different types of vehicles such as bikes, cars, and trucks.

The system should support parking and unparking vehicles, allocating appropriate parking spots, generating tickets on entry, calculating parking fees on exit, and handling multiple entry/exit gates concurrently.

The solution should be designed with extensibility, maintainability, and object-oriented design principles in mind.

---

# Functional Requirements

## Vehicle Management

The parking lot should support multiple vehicle types.

Examples:

* Bike
* Car
* Truck

Each vehicle has:

* License plate number
* Vehicle type
* Vehicle size

The design should allow adding new vehicle types in the future with minimal code changes.

---

## Parking Spot Management

The parking lot contains multiple floors.

Each floor contains multiple parking spots.

Each parking spot has:

* Unique identifier
* Spot number
* Spot size
* Current status

Supported spot sizes:

* Small
* Medium
* Large

A parking spot can accommodate a vehicle of equal or smaller size.

Examples:

| Spot Size | Allowed Vehicles |
| --------- | ---------------- |
| Small     | Bike             |
| Medium    | Bike, Car        |
| Large     | Bike, Car, Truck |

A parking spot can be either:

* Available
* Occupied

---

## Parking Lot Structure

The parking lot consists of:

* Multiple floors
* Multiple entry gates
* Multiple exit gates

All gates should operate on the same parking lot data and always see the latest parking availability.

---

## Vehicle Entry

When a vehicle enters the parking lot:

1. The system should find a suitable parking spot.
2. The spot should be marked as occupied.
3. A parking ticket should be generated.
4. The ticket should be returned to the customer.

The ticket should contain:

* Ticket ID
* Vehicle details
* Assigned parking spot
* Entry timestamp

---

## Vehicle Exit

When a vehicle exits the parking lot:

1. The ticket should be validated.
2. Parking duration should be calculated.
3. Parking fee should be calculated.
4. The parking spot should be released.
5. The ticket should be closed.

Assume payment is always successful.

---

## Spot Allocation Strategy

The parking lot should support pluggable parking spot allocation algorithms.

Examples:

* Nearest available spot
* Best fit spot

The allocation strategy should be configurable and swappable at runtime without modifying existing code.

---

## Fee Calculation Strategy

The parking lot should support pluggable fee calculation algorithms.

Examples:

* Flat hourly rate
* Tiered pricing

The fee calculation strategy should be configurable and swappable at runtime without modifying existing code.

---

# Non-Functional Requirements

## Extensibility

The design should support:

* New vehicle types
* New parking spot types
* New fee calculation strategies
* New allocation strategies

without modifying existing business logic.

---

## Maintainability

The design should follow:

* SOLID principles
* Clean object-oriented design
* Separation of concerns

---

## Concurrency

The parking lot should safely handle concurrent requests from:

* Multiple entry gates
* Multiple exit gates

The design should prevent:

* Double booking of a parking spot
* Inconsistent parking availability
* Race conditions during parking and unparking operations

---

## Performance

Parking spot lookup should be reasonably efficient.

The initial implementation may use in-memory data structures.

---

# Assumptions

* Payment always succeeds.
* Data is stored in memory.
* Parking spot availability is maintained in real time.
* A vehicle can have only one active parking ticket at a time.
* Vehicle license plate numbers are unique.
* The parking lot operates continuously and does not have opening/closing hours.

---

# Out of Scope

The following features are not required for the initial implementation:

* Reservation of parking spots
* Electric vehicle charging
* Lost ticket handling
* Dynamic pricing based on demand
* Vehicle tracking using cameras
* User accounts or authentication
* Database persistence
* Distributed deployment

---

# Expected Design

The solution should demonstrate:

* Object-oriented design
* Proper class relationships
* Design patterns where appropriate
* Thread-safe handling of parking operations
* Clean separation between domain models and business logic

Focus on producing a maintainable and extensible low-level design rather than a UI or API implementation.
    