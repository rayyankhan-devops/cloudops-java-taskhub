# DevOps & Task Hub - Spring Boot 3.5 & Java 17 Application

An enhanced Spring Boot web application featuring a DevOps task management board, real-time JVM system telemetry, and an interactive REST API console.

---

## 📋 Prerequisites

- **Java 17+** (installed at `/usr/local/opt/openjdk@17` or configured in your `PATH`)
- **Maven 3.8+** (or use the included `./mvnw` wrapper)

---

## 🛠️ Step 1: Set JAVA_HOME (macOS / Linux)

Ensure your shell points to OpenJDK 17:

```bash
export JAVA_HOME=/usr/local/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"
```

Verify your Java version:
```bash
java -version
```
*(Should output `openjdk version "17.x.x"`)*

---

## 🔨 Step 2: Build the Application

Navigate to the project directory:
```bash
cd "basic java app"
```

### Build & Package the Executable JAR
```bash
mvn clean package
```
*(This compiles the code, executes all tests, and creates `target/demo-0.0.1-SNAPSHOT.jar`)*

### Build without running tests (Fast Build)
```bash
mvn clean package -DskipTests
```

---

## 🧪 Step 3: Run Automated Tests

Run the full JUnit 5 & SpringBootTest suite:
```bash
mvn test
```

---

## 🚀 Step 4: Run the Application

You can run the application in two ways:

### Option A: Run directly using Maven (Recommended for development)
```bash
mvn spring-boot:run
```

### Option B: Run using the packaged JAR (Recommended for production/deployment)
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Custom Port (Optional)
By default, the server runs on port **8080**. To change the port on the fly:
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=9090
```

---

## 🌐 Accessing the Application

Once started, open your web browser:
- **Web Dashboard**: [http://localhost:8080](http://localhost:8080)
- **Health Check**: [http://localhost:8080/api/health](http://localhost:8080/api/health)
- **System Metrics**: [http://localhost:8080/api/system/metrics](http://localhost:8080/api/system/metrics)
- **Tasks API**: [http://localhost:8080/api/tasks](http://localhost:8080/api/tasks)

---

## 📡 REST API Quick Reference

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/tasks` | Get all tasks (supports `?status=...&priority=...&search=...`) |
| `GET` | `/api/tasks/{id}` | Get task by ID |
| `POST` | `/api/tasks` | Create a new task |
| `PUT` | `/api/tasks/{id}` | Update existing task |
| `PATCH`| `/api/tasks/{id}/status` | Update task status |
| `DELETE`| `/api/tasks/{id}` | Delete task |
| `GET` | `/api/tasks/stats` | Get KPI task metrics |
| `GET` | `/api/health` | Application health status |
| `GET` | `/api/system/metrics` | Real-time JVM memory & system telemetry |
