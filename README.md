# ⚡ CloudOps Java TaskHub

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot 3.5.3](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Multi--Stage-blue.svg)](https://www.docker.com/)
[![Image Size](https://img.shields.io/badge/Docker%20Image-~70MB-blueviolet.svg)](https://hub.docker.com/r/rayyan12311/cloudops-java-taskhub)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-Ready-326CE5.svg)](https://kubernetes.io/)

A full-stack, cloud-native **DevOps Task & System Command Center** built with **Spring Boot 3.5** and **Java 17**. Features an interactive Web SPA dashboard, real-time JVM telemetry, built-in REST API explorer, an ultra-lightweight multi-stage Docker container (~70 MB), Docker Compose setup, and production-ready Kubernetes manifests.

---

## 🌟 Key Features

- 📋 **Interactive Task Board**:
  - Full CRUD operations with priority tags (`Critical`, `High`, `Medium`, `Low`) and status workflows (`To Do` ➔ `In Progress` ➔ `Done`).
  - Real-time client-side search and multi-criteria filtering.
  - Dark Mode and Light Mode theme toggle with local state persistence.
- 📊 **Real-Time System Telemetry & Health**:
  - Live JVM heap memory usage meter and allocation stats.
  - Active thread counts, CPU cores, uptime tracker, and host environment specs.
- 🚀 **Interactive In-Browser REST API Explorer**:
  - Test endpoints (`GET`, `POST`, `PUT`, `DELETE`) directly from the browser with 1-click sample payloads.
- 🐳 **Ultra-Lightweight Multi-Stage Docker Build**:
  - Uses `jlink` to build a custom-stripped JRE (~35 MB), running on an Alpine 3.20 hardened non-root base (`~70 MB` total image size).
- ☸️ **Kubernetes & Cloud Native Ready**:
  - Complete K8s manifests including `Namespace`, `Deployment` (with CPU/Memory limits, Liveness & Readiness probes), and `Service`.

---

## 🏗️ Tech Stack & Architecture

| Layer | Technology |
| :--- | :--- |
| **Backend Framework** | Spring Boot 3.5.3 (Spring Web, Spring Test) |
| **Language / Runtime** | Java 17 (OpenJDK 17) |
| **Frontend UI** | HTML5, Modern CSS3 (Glassmorphism), Vanilla JS (Zero CDN dependencies) |
| **Data Layer** | Thread-safe Concurrent In-Memory Store with Atomic IDs & Seed Data |
| **Containerization** | Multi-Stage Dockerfile (`maven:3.9.9-alpine` + `jlink` + `alpine:3.20`) |
| **Orchestration** | Docker Compose & Kubernetes (`k8s/`) |

---

## 🚀 Quick Start & Local Development

### 1. Prerequisites
- **Java 17+** (OpenJDK 17)
- **Maven 3.8+** (or use included `./mvnw`)

### 2. Set Java 17 Environment
```bash
export JAVA_HOME=/usr/local/opt/openjdk@17
export PATH="$JAVA_HOME/bin:$PATH"
java -version
```

### 3. Build & Run
```bash
# Package the application JAR & execute automated test suite
mvn clean package

# Run with Maven
mvn spring-boot:run

# Or run using the standalone executable JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### 4. Run Automated Tests
```bash
mvn test
```

---

## 🐳 Docker Containerization

The project uses a custom multi-stage Docker build with `jlink` to strip unnecessary JVM modules and produce an ultra-compact **~70 MB** production image.

### Build the Docker Image
```bash
docker build --no-cache -t rayyan12311/cloudops-java-taskhub:v1.0.0 .
```

### Run the Container
```bash
docker run -d -p 8080:8080 --name taskhub rayyan12311/cloudops-java-taskhub:v1.0.0
```

### View Logs & Manage
```bash
docker logs -f taskhub
docker stop taskhub && docker rm taskhub
```

---

## 🐙 Docker Compose

Run the entire service with a single command:

```bash
# Start container in background
docker compose up -d

# View status & logs
docker compose ps
docker compose logs -f

# Stop and remove
docker compose down
```

---

## ☸️ Kubernetes Deployment

The [`k8s/`](./k8s) directory provides ready-to-apply manifests:

```bash
# 1. Create the dedicated namespace
kubectl apply -f k8s/namespace.yml

# 2. Deploy the application (2 replicas, resource limits, health probes)
kubectl apply -f k8s/deployment.yml

# 3. Create the Cluster Service
kubectl apply -f k8s/servicxe.yml

# 4. Verify deployment status
kubectl get all -n cloudops
```

### Port Forward for Local Access
```bash
kubectl port-forward svc/cloudops-dep 8080:8080 -n cloudops
```

---

## 📡 REST API Reference

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/tasks` | Get all tasks (supports `?status=...&priority=...&search=...&tag=...`) |
| `GET` | `/api/tasks/{id}` | Get task details by ID |
| `POST` | `/api/tasks` | Create a new task |
| `PUT` | `/api/tasks/{id}` | Update existing task details |
| `PATCH`| `/api/tasks/{id}/status` | Update task status (`TODO`, `IN_PROGRESS`, `DONE`) |
| `DELETE`| `/api/tasks/{id}` | Delete task by ID |
| `GET` | `/api/tasks/stats` | KPI task metrics and tag distribution |
| `GET` | `/api/health` | Application health check probe endpoint |
| `GET` | `/api/system/metrics` | Real-time JVM memory, threads, cores, and OS telemetry |

### Sample cURL Commands

```bash
# Health Check Probe
curl http://localhost:8080/api/health

# System Telemetry Diagnostics
curl http://localhost:8080/api/system/metrics

# Create a Task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Configure Prometheus Scraping",
    "description": "Scrape JVM Actuator metrics and build Grafana alert rules",
    "priority": "HIGH",
    "status": "TODO",
    "assignee": "SRE Lead",
    "tags": ["monitoring", "prometheus", "grafana"]
  }'
```

---

## 🌐 Web Dashboard Access

Once running, access the web command center at:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## 📄 License
This project is licensed under the [MIT License](LICENSE).
