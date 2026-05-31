# 🚗 Car Rental System — High-Level Design (HLD) Assignment

## Overview

You are required to design a **backend system for a multi-location car rental platform**.
This document outlines the problem statement, functional requirements, non-functional
requirements, and evaluation criteria to guide your High-Level Design (HLD).

---

## Problem Statement

Design a scalable and extensible backend system for a car rental company that operates
across **multiple locations**. The system should allow customers to browse vehicles,
make reservations, rent and return vehicles, and receive accurate billing — all while
maintaining **data consistency** and **vehicle availability** across the platform.

---

## Functional Requirements

### 🔍 Vehicle Browsing & Inventory
- Customers should be able to **browse available vehicles** at a specific rental location.
- Each vehicle must belong to a **category**: Economy, Sedan, SUV, or Luxury.
- Each vehicle must have associated **pricing information**.
- The system must maintain **accurate, real-time vehicle availability** across all locations.

### 📅 Reservations
- A customer should be able to **reserve a vehicle for one or more full days**.
- The system must **prevent double-booking** of the same vehicle.
- Customers should be able to **update or cancel** an existing reservation.
- Reservation updates should re-validate availability before confirming changes.

### 🚘 Rental Lifecycle
- Once a reservation begins, the system should **track the trip lifecycle**:
    - Reservation Created → Rental Started → Vehicle Returned
- The system should record the **start and end state** of each rental.

### 💰 Billing & Pricing
- The final bill should be calculated based on:
    - **Daily rental charges** (based on vehicle category and pricing tier)
    - **Free distance allowance per day** (if applicable for the vehicle)
    - **Additional distance-based charges** for usage beyond the free limit
- The billing engine should be **extensible** to support:
    - Promotional discounts
    - Alternate pricing strategies (e.g., hourly, surge pricing)

### 🔧 Vehicle Status Management
- Vehicles should have clearly defined statuses, such as:
    - `AVAILABLE`, `RESERVED`, `RENTED`, `UNDER_MAINTENANCE`
- If a vehicle is **returned damaged**, it must be marked as `UNDER_MAINTENANCE`
  until cleared.
- Vehicles under maintenance should **not be available** for new reservations.

### 💳 Payment Handling
- The system should support **payment processing** for completed rentals.
- The design should be extensible to support **multiple payment methods**
  (e.g., credit card, UPI, wallet).

---

## Non-Functional Requirements

| Requirement        | Details                                                                 |
|--------------------|-------------------------------------------------------------------------|
| **Consistency**    | No two customers should be able to reserve the same vehicle for overlapping dates |
| **Thread Safety**  | The system must handle **concurrent reservation requests** safely       |
| **Extensibility**  | Easy to add new vehicle types, pricing models, discounts, payment methods |
| **Maintainability**| Clean, modular, and well-structured object-oriented design              |
| **In-Memory**      | Use in-memory data structures; no external database required            |

---

## Scope & Constraints

- ✅ Assume an **in-memory implementation** (no database or external services required)
- ✅ Focus on **object-oriented design** with clear class responsibilities
- ✅ The system should be designed for **future extensibility**
- ❌ No UI/frontend design is required
- ❌ No authentication or user management system is required
- ❌ No real payment gateway integration is needed

---