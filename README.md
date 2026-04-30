# PDM Lifecycle Manager

A lightweight Product Data Management system built with Java and Spring Boot, inspired by enterprise PLM platforms like PTC Windchill PDMLink. This project demonstrates and executes core PLM concepts including configurable lifecycle workflows, object attribute management, and bill of materials hierarchy.

---

## Features

- **Part & Assembly Management** — Create and manage parts with types: `COMPONENT`, `ASSEMBLY`, `DOCUMENT`
- **Lifecycle State Machine** — Parts move through `DRAFT → IN_REVIEW → RELEASED → OBSOLETE` with enforced transition rules
- **Workflow Validation** — Invalid transitions are rejected with descriptive error messages (e.g. a part cannot be released without a part number)
- **BOM Hierarchy** — Parent/child relationships between parts model real bill of materials structures
- **Audit Trail** — Every state change and attribute update is logged with timestamp
- **REST API** — Full CRUD and lifecycle transition endpoints

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Persistence | Spring Data JPA + Hibernate |
| Database | H2 (in-memory, dev) |
| Build | Maven |
| Boilerplate reduction | Lombok |

---

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+

### Run locally

```bash
git clone https://github.com/yourusername/pdm-lifecycle-manager.git
cd pdm-lifecycle-manager
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

To browse the database directly, open `http://localhost:8080/h2-console` and connect with:
- JDBC URL: `jdbc:h2:mem:pdmdb`
- Username: `SA`
- Password: *(leave blank)*

---

## API Reference

### Parts

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/parts` | Create a new part |
| `GET` | `/api/parts` | Get all parts |
| `GET` | `/api/parts/{id}` | Get a part by ID |
| `GET` | `/api/parts/{id}/children` | Get BOM children of a part |
| `PUT` | `/api/parts/{id}/transition?targetState=` | Transition lifecycle state |

### Example: Create a part

```bash
curl -X POST http://localhost:8080/api/parts \
  -H "Content-Type: application/json" \
  -d '{"name": "Main Assembly", "partNumber": "ASM-001", "type": "ASSEMBLY"}'
```

Response:
```json
{
  "id": 1,
  "name": "Main Assembly",
  "partNumber": "ASM-001",
  "type": "ASSEMBLY",
  "state": "DRAFT",
  "createdAt": "2026-04-30T02:12:27.664",
  "updatedAt": "2026-04-30T02:12:27.664"
}
```

### Example: Transition lifecycle state

```bash
curl -X PUT "http://localhost:8080/api/parts/1/transition?targetState=IN_REVIEW"
```

### Example: Invalid transition (rejected)

```bash
curl -X PUT "http://localhost:8080/api/parts/1/transition?targetState=OBSOLETE"
# Response: "Cannot transition from IN_REVIEW to OBSOLETE"
```

---

## Lifecycle State Machine

```
DRAFT ──► IN_REVIEW ──► RELEASED ──► OBSOLETE
             │
             └──► DRAFT (send back for rework)
```

Transition rules are enforced by `LifecycleService`. Each transition can carry pre-conditions (e.g. a part number is required before a part can be released) and post-transition hooks for notifications or automated field updates — mirroring how PDMLink workflow customization works in practice.

---

## Project Structure

```
src/main/java/com/yourname/pdm_lifecycle_manager/
├── domain/
│   ├── PartObject.java               # Core JPA entity
│   ├── lifecyclestate.java           # Lifecycle state enum
│   └── partType.java                 # Part type enum
├── service/
│   └── LifecycleService.java         # Workflow engine + business logic
├── controller/
│   └── PartController.java           # REST API endpoints
└── repository/
    └── PartObjectRepository.java     # Spring Data JPA interface
```

---

## PLM Concepts Demonstrated

This project maps directly to real PDMLink customization work:

| This Project | PDMLink Equivalent |
|---|---|
| `LifecycleService` transition rules | Workflow state gate customization |
| `PartObject` entity with typed attributes | Windchill business object model |
| BOM parent/child relationships | Part structure / where-used |
| Transition validation (part number required) | Workflow pre-condition checks |
| Audit trail | Windchill change history |

---
