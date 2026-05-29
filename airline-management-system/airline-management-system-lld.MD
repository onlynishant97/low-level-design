# Airline Management System — LLD Interview Question

---

## Problem Statement

Design an **Airline Management System** that allows users to search,
book, and manage flights.

---

## Core Requirements

### 1. Flight Search
- Users should be able to **search for flights** based on source airport,
  destination airport, and travel date.
- Search results should include **pricing information** per seat class.

### 2. Booking Management
- Users should be able to **book a flight** by selecting seats and passengers.
- The system should handle the complete booking lifecycle —
  **initiation, confirmation, and cancellation**.
- On cancellation, the system should **process a refund** based on how far
  in advance the cancellation is made.

### 3. Payment
- The system should support **multiple payment methods** (e.g., UPI, Card).
- The system should handle both **successful and failed payments** gracefully.

### 4. Seat Management
- The system should allow users to **select specific seats** on a flight.
- The system should **prevent double booking** of the same seat.
- Seats selected during booking initiation should be **temporarily held**
  for a short duration to allow payment to complete.

### 5. Flight Operations *(Admin/Staff)*
- The system should support **assigning aircraft and crew** to flights.
- The system should be able to **track and update flight status**
  (e.g., Scheduled, Delayed, Cancelled).

### 6. User Types
- The system should support different types of users —
  **Passenger, Airline Staff, and Admin** —
  with different roles and responsibilities.

### 7. Notifications
- The system should **notify passengers** on key events such as booking
  confirmation, cancellation, and flight status changes.

---

## Non-Functional Requirements

- The system should **handle concurrent seat booking requests** without
  allowing double booking.
- The system should be **extensible** — adding a new payment method or
  refund policy should require minimal changes.
- The system should be **scalable** and designed with clean separation
  of concerns.

---

## What We're Looking For

| Area | Expectation |
|---|---|
| **Class design** | Are entities well-defined with clear responsibilities? |
| **Relationships** | Are associations, compositions, and aggregations used correctly? |
| **Design patterns** | Do they naturally arrive at Strategy, Factory, or Repository patterns? |
| **Concurrency** | How do they prevent two users from booking the same seat simultaneously? |
| **Extensibility** | Is the code open for extension without modification? |
| **State machines** | Do they model booking, payment, and seat status as proper state transitions? |

---

## Hints to Give if Stuck

| If stuck on... | Hint |
|---|---|
| Concurrency | *"Two users are trying to book the same seat at the same time — how would you handle that?"* |
| Payment extensibility | *"A new payment method needs to be added — how would your design accommodate that?"* |
| Refund logic | *"The refund amount depends on how early the cancellation is made — how would you model that?"* |
| Seat holding | *"What happens if a user starts booking but never completes payment?"* |
| Status tracking | *"How would you model the different states a booking or flight can be in?"* |

---

## Time Expectation

| Phase | Time |
|---|---|
| Clarifying questions & scope | 5 mins |
| Class identification & relationships | 10 mins |
| Core class implementation | 25 mins |
| Concurrency & edge cases | 10 mins |
| Review & discussion | 10 mins |
| **Total** | **~60 mins** |