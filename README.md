# 🛡️ Sentinel Ledger: Enterprise Banking & Fraud Monitor

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Tech Stack](https://img.shields.io/badge/stack-Java%20|%20React%20|%20Python-blue)
![License](https://img.shields.io/badge/license-MIT-green)

**Sentinel** is an enterprise-grade financial ledger system designed to handle high-volume banking transactions, double-entry bookkeeping, and real-time fraud monitoring. It features a "God Mode" Admin Console, lifestyle auditing tools, and a dynamic goal-tracking system.

## 📂 System Architecture

The project follows a Monorepo architecture organized by domain:

| Module | Stack | Description |
| :--- | :--- | :--- |
| **`core-engine`** | Java 17, Spring Boot | The heart of the ledger. Handles atomic transactions and account logic. |
| **`web-service`** | Java 17, Spring Security | REST API gateway managing authentication and external integrations. |
| **`sentinel-dashboard`** | React, Vite, Recharts | The Frontend Admin Console for visualizing flows and auditing logs. |
| **`sentinel-monitor`** | Python 3.11, FastAPI | Real-time health monitoring service and fraud detection alerts. |
| **`database`** | PostgreSQL 15 | Persistent storage volume with automated schema migration. |

---

## 🚀 Quick Start: The "Launch Sequence"
.

### 1. The Single-Command Launch (Docker)
This starts the Database, Java Core, Python Sentinel, and React Dashboard simultaneously.

```bash
# Clean previous volumes (Optional: wipes data)
sudo docker compose down -v

# Launch the entire stack
sudo docker compose up --build
```

The Admin Console ("God Mode")
    Access the hidden Admin Console via the lock icon in the sidebar.
    Security PIN: 072479

