# Factory Backend Project

## 📌 Project Overview

The **Factory Backend Project** is a Spring Boot-based backend
application developed to manage factory-related data efficiently.\
The project focuses on building a RESTful service integrated with
MongoDB for data storage and retrieval.\
It demonstrates backend development concepts such as API creation,
database integration, and layered architecture.

------------------------------------------------------------------------

## 🎯 Project Objectives

-   Design a backend system for managing factory records.
-   Implement REST APIs for CRUD and statistics operations.
-   Integrate MongoDB as a NoSQL database.
-   Follow a clean layered architecture (Controller → Service →
    Repository).
-   Ensure smooth communication between client and database.

------------------------------------------------------------------------

## 🛠️ Technologies Used

-   Java 20\
-   Spring Boot 3.5.9\
-   Spring Data MongoDB\
-   MongoDB Database\
-   REST API Architecture\
-   Embedded Apache Tomcat Server

------------------------------------------------------------------------

## 🏗️ Project Architecture

Controller Layer → Handles HTTP requests and responses\
Service Layer → Contains business logic\
Repository Layer → Communicates with MongoDB\
Model Layer → Defines data structure

This separation improves maintainability and scalability.

------------------------------------------------------------------------

## 📂 Core Modules Implemented

### 1. Model Module

-   Defines factory event and statistics data structure.
-   Maps Java objects to MongoDB documents.

### 2. Repository Module

-   Uses Spring Data MongoDB repository interface.
-   Provides built-in CRUD and query operations.

### 3. Service Module

-   Implements business logic for event processing and statistics
    generation.

### 4. Controller Module

-   Exposes REST API endpoints.
-   Handles client requests and sends JSON responses.

------------------------------------------------------------------------

## 🌐 API Functionalities

  Operation             Description
  --------------------- ---------------------------------------------
  Create Events         Store machine event data
  Read Events           Fetch factory event records
  Generate Statistics   Calculate defect rates and machine health
  Top Defect Lines      Identify production lines with high defects

------------------------------------------------------------------------

## 📊 Results and API Output

### 🔹 Top Defect Lines API

**Endpoint**

    GET http://localhost:8080/stats/top-defect-lines?factoryId=F01&from=2026-01-15T00:00:00Z&to=2026-01-16T00:00:00Z&limit=5

**Response**

``` json
[
  {
    "lineId": "L-01",
    "totalDefects": 2,
    "eventCount": 2,
    "defectsPercent": 100.0
  },
  {
    "lineId": "L-02",
    "totalDefects": 0,
    "eventCount": 1,
    "defectsPercent": 0.0
  }
]
```

**Inference:**\
Line **L-01** has the highest defect percentage, indicating a need for
maintenance inspection.

------------------------------------------------------------------------

### 🔹 Event Processing Output

**Response**

``` json
[
  {
    "eventId": "E-1001",
    "eventTime": "2026-01-15T10:12:03.123Z",
    "receivedTime": "2026-01-15T10:12:04.500Z",
    "machineId": "M-001",
    "durationMs": 1200,
    "defectCount": 0,
    "lineId": "L-01",
    "factoryId": "F01"
  },
  {
    "eventId": "E-1002",
    "eventTime": "2026-01-15T10:15:10.000Z",
    "receivedTime": "2026-01-15T10:15:11.000Z",
    "machineId": "M-001",
    "durationMs": 1500,
    "defectCount": 2,
    "lineId": "L-01",
    "factoryId": "F01"
  },
  {
    "eventId": "E-1003",
    "eventTime": "2026-01-15T10:20:00.000Z",
    "receivedTime": "2026-01-15T10:20:01.000Z",
    "machineId": "M-002",
    "durationMs": 1800,
    "defectCount": -1,
    "lineId": "L-02",
    "factoryId": "F01"
  }
]
```

**Processing Summary**

``` json
{
  "accepted": 0,
  "deduped": 3,
  "updated": 0,
  "rejected": 0,
  "rejections": []
}
```

**Inference:**\
All incoming events were successfully processed and deduplicated with no
rejected records.

------------------------------------------------------------------------

### 🔹 Machine Health Statistics

**Endpoint**

    GET http://localhost:8080/stats?machineId=M-001&start=2026-01-15T00:00:00Z&end=2026-01-16T00:00:00Z

**Response**

``` json
{
  "machineId": "M-001",
  "start": "2026-01-15T00:00:00Z",
  "end": "2026-01-16T00:00:00Z",
  "eventsCount": 2,
  "defectsCount": 2,
  "avgDefectRate": 0.08333333333333333,
  "status": "Healthy"
}
```

**Inference:**\
Machine **M-001** is operating in a **Healthy** state with a low average
defect rate.

------------------------------------------------------------------------

## 📈 Project Outcomes

-   Successfully built a working Spring Boot backend.
-   Achieved seamless MongoDB integration.
-   Implemented event processing and statistics APIs.
-   Generated real-time defect and machine health reports.
-   Ensured structured and scalable backend design.

------------------------------------------------------------------------

## 🧠 Learning Outcomes

-   REST API development with Spring Boot.
-   MongoDB query and aggregation handling.
-   Backend system design and layered architecture.
-   Real-world data processing and analytics.

------------------------------------------------------------------------

## 👨‍💻 Developer

**Agent**\
B.Tech Computer Science Student

------------------------------------------------------------------------

## 📜 Note

This project is developed for academic and learning purposes.
