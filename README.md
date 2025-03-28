Steps to Run the Assignment:

     1 Clone the Repo 
     2 Set the Java version to 8 and maven version to 3
     3 Debug/run the application
     4 The apis (Post - logs and Get) can be tested via Postman.

System Architecture
✅ 1. API Layer (LogController)
Handles HTTP requests from clients (POST /logs and GET /logs).

Routes requests to the LogService for processing.

Ensures validation and appropriate response for success or errors.

✅ 2. Service Layer (LogService)
Contains the core business logic for managing logs.

Stores logs in a thread-safe in-memory storage (ConcurrentHashMap and ConcurrentSkipListMap).

Supports concurrent write and read operations for high throughput.

✅ 3. In-Memory Storage (Thread-Safe Maps)
ConcurrentHashMap stores logs by service name.

ConcurrentSkipListMap maintains logs in time-ordered sequence for each service.

Enables fast retrieval of logs using subMap() for time-range queries.

✅ 4. Scheduled Task (LogCleanupTask)
A @Scheduled task runs periodically to clean logs older than 1 hour.

Uses a fixed-rate interval of 60 seconds (@Scheduled(fixedRate = 60000)).

Ensures that old logs are removed without blocking incoming requests.

✅ 5. Optional Database (PostgreSQL/MongoDB)
Optionally persists logs for long-term storage.

Can be configured to provide durability and query support beyond in-memory storage.

Supports fallback querying from the database if needed.

✅ 6. Concurrency and Thread Safety
Supports multiple concurrent requests via thread-safe data structures.

API requests are handled independently by the embedded server's thread pool.

Ensures consistency with atomic operations (computeIfAbsent()) and concurrent iterators

<img width="593" alt="image" src="https://github.com/user-attachments/assets/728ac2c7-b6a5-47e9-abd3-55d51ed75aae" />

