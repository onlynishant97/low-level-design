# System Design Interview: Amazon Locker

## Overview

Amazon Locker is a self-service package delivery and pickup network. Packages are shipped to a locker station instead of a home address. The customer receives a pickup code and collects their package from a physical locker compartment within a defined window.

Your task is to design the backend systems that power this network at scale.

---

## Functional Requirements

### Package Deposit (Delivery Agent)
- When a package arrives at a locker station, the system must assign an appropriate compartment based on package size (Small / Medium / Large).
- The system must generate a unique, time-limited pickup code and send it to the customer via email/SMS.
- A package that is not picked up within **3 days** must be automatically marked as expired and its compartment released.

### Package Pickup (Customer)
- The customer enters their pickup code at the locker kiosk.
- The system validates the code (existence, expiry, already-used check).
- On success: the compartment opens, the package is marked as picked up, and the compartment is freed.
- On failure: the system returns an appropriate error — invalid code, already used, or expired.

### Locker Management
- The system must support **multiple locker locations** across cities.
- Each location has multiple locker units; each unit has compartments of varying sizes.
- Real-time availability of compartments must be queryable.

### Notifications
- Customers must be notified when:
  - Their package has been assigned to a locker (with pickup code and expiry time).
  - Their pickup code is expiring soon (e.g., reminder 24 hours before expiry).
  - Their package has expired and been removed.

---

## Non-Functional Requirements

- **Scale**: Design for 10,000 locker locations globally, with an average of 50 compartments per location.
- **Throughput**: Support up to **50,000 package deposits and pickups per minute** at peak (holiday season).
- **Availability**: The pickup flow must have **99.99% uptime** — a customer must never be unable to retrieve their package due to a system outage.
- **Latency**: Pickup code validation must respond in **< 200ms** (p99).
- **Consistency**: A compartment must never be double-assigned. Concurrent deposit requests to the same locker must be handled safely.
- **Durability**: No pickup code or package assignment must be lost, even during a partial system failure.

---

## Out of Scope

The following are explicitly **not** part of this design:

- Payment processing or refunds
- Delivery routing and logistics (how packages reach the locker)
- Package returns or re-routing of expired packages
- Admin dashboards or reporting
- Hardware/firmware of the physical locker itself
- Authentication and identity management (assume a token is already issued to the customer)

---

## Capacity Estimation

Use these numbers as a starting point. You are expected to walk through your own estimates.

| Metric | Value |
|---|---|
| Locker locations | 10,000 |
| Avg compartments per location | 50 |
| Total compartments | 500,000 |
| Peak deposits/pickups | 50,000 / minute |
| Avg package stay duration | 1–3 days |
| Pickup code length | 6–8 alphanumeric characters |
| Notification events/day | ~5 million |

---

## API Design (Suggested Starting Point)

You are free to evolve or change these. Justify your choices.

```
POST   /v1/packages/deposit
POST   /v1/packages/pickup
GET    /v1/lockers/{locationId}/availability
GET    /v1/packages/{packageId}/status
```

---

## Areas to Cover

Your design should address all of the following. Time allocation is a suggestion.

### 1. High-Level Architecture *(~5 min)*
- Major services and their responsibilities.
- How services communicate (sync vs async).
- Entry points (API Gateway, load balancer).

### 2. Data Modeling *(~8 min)*
- Schema for: Package, Compartment, Locker, LockerLocation, AccessToken.
- Which database(s) would you use and why (SQL vs NoSQL)?
- How would you handle compartment availability queries efficiently?

### 3. Compartment Allocation *(~8 min)*
- How do you prevent two packages from being assigned to the same compartment?
- What locking strategy would you use? Discuss tradeoffs: optimistic vs pessimistic locking, distributed locks (Redis, Zookeeper).
- What happens if the allocation service crashes mid-assignment?

### 4. Pickup Code Validation *(~5 min)*
- Where is the code stored and how is it looked up?
- How do you ensure a code cannot be used twice, even under concurrent requests from multiple kiosk terminals?
- Caching strategy: what do you cache, where, and with what TTL?

### 5. Expiry & Cleanup *(~5 min)*
- How does the system detect and process expired packages at scale?
- Design the background job: scheduler type, frequency, failure handling, idempotency.
- How do you avoid processing the same expired package twice across multiple scheduler instances?

### 6. Notification Service *(~5 min)*
- How are notifications triggered (event-driven vs polling)?
- How do you handle notification failures and retries without sending duplicates?
- What message queue / event streaming system would you use?

### 7. Availability & Fault Tolerance *(~4 min)*
- What happens if the Locker Service goes down during a pickup?
- How do you ensure the kiosk can still open a compartment in offline/degraded mode?
- What is your disaster recovery strategy for the assignment database?

---

## Evaluation Criteria

| Area | What We Look For |
|---|---|
| Problem decomposition | Can the candidate break the system into clean, independent services? |
| Data modeling | Appropriate schema, right database choices, indexing strategy |
| Concurrency handling | Awareness of race conditions, distributed locking tradeoffs |
| Scalability | Horizontal scaling, caching, async processing |
| Reliability | Failure scenarios identified, retry logic, idempotency |
| Communication | Clarity of thought, ability to justify tradeoffs |

---

## Tips for the Candidate

- Start with clarifying questions — confirm scale assumptions before diving in.
- Draw the architecture first, then zoom into the components the interviewer asks about.
- Call out tradeoffs explicitly. There is no single right answer; reasoning matters more than conclusions.
- Manage your time — do not spend more than 10 minutes on any single component unless asked to go deeper.
- It is acceptable to say "I would investigate X further" — acknowledge unknowns rather than glossing over them.

---

*Total time: 45–60 minutes*
