# ⚡ PowerTrack — Electricity Management & Customer Service Platform

PowerTrack is a backend-focused management system built for electricity distribution companies and their customers. It digitizes and automates core utility operations — meter & feeder management, meter readings, billing & payments, complaint handling, customer support chat, installment plans, load-shedding scheduling, and system notifications — through a centralized, role-based platform.

---

## 📋 Table of Contents
- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Core Features](#-core-features)
- [Feature Details](#-feature-details)
- [Getting Started](#-getting-started)
- [Demo Accounts](#-demo-accounts)
- [Project Timeline](#-project-timeline)
- 
---

## 🔍 Overview

PowerTrack provides a centralized backend for managing users, meters, feeders, billing, payments, complaints, load-shedding, and communication across three primary roles:

- **Customers** — register, view meters, track bills, pay dues, raise complaints, and check real-time load-shedding schedules through a personalized dashboard.
- **Field Staff** — submit meter readings, manage assigned installation tasks, and update progress directly from the system.
- **Customer Support (CSR)** — resolve complaints efficiently through an integrated real-time chat system connecting them with customers.
- **Admins** — configure tariffs, manage approval workflows, monitor revenue and outstanding bills, handle installment requests, manage load-shedding plans, and view complete system analytics.

Built with a structured, domain-driven design and secure session-based authentication, PowerTrack aims to streamline electricity distribution operations while improving customer satisfaction.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3 |
| Persistence | JPA / Hibernate |
| Database | MySQL |
| Boilerplate | Lombok |
| Build Tool | Maven |
| Frontend | Next.js |
| Auth | Server-side sessions & cookie management |
| API Testing | Postman |

---

## ✨ Core Features

1. Meter Management
2. Meter Reading Submission
3. Billing & Tariff Management
4. Payments & Outstanding Management
5. Installment Plans
6. Complaint Management
7. Real-Time Chat System
8. Load-Shedding Schedule Management
9. Notifications & Alerts
10. Feeder & Area Management

---

## 📖 Feature Details

<details>
<summary><strong>1. Meter Management</strong></summary>

Manages the complete lifecycle of electricity meters, including installation requests submitted by customers. Admins approve or reject requests, after which meters are assigned to a feeder and activated. The system tracks active/inactive meters for proper infrastructure monitoring.
</details>

<details>
<summary><strong>2. Meter Reading Submission</strong></summary>

Field staff submit periodic readings for their assigned meters. Readings are validated, stored, and directly feed into consumption history and automatic bill generation — reducing manual errors.
</details>

<details>
<summary><strong>3. Billing & Tariff Management</strong></summary>

Bills are auto-generated monthly based on reading history and admin-configured tariff slabs, supporting multiple tariff categories and consumption ranges. Each bill records units consumed, charges, taxes, and due dates.
</details>

<details>
<summary><strong>4. Payments & Outstanding Management</strong></summary>

Customers pay bills through an integrated payment module. Bill status and outstanding balances update automatically on payment, while the system tracks overdue amounts for admin visibility.
</details>

<details>
<summary><strong>5. Installment Plans</strong></summary>

Customers unable to pay in full can request an installment plan with a reason and preferred terms. Admins review and approve/reject requests, with approved plans tracked in-system.
</details>

<details>
<summary><strong>6. Complaint Management</strong></summary>

Customers file complaints related to billing, technical issues, outages, or service quality. Complaints are prioritized and routed to CSR/admin, who update status, add resolution notes, and escalate critical issues.
</details>

<details>
<summary><strong>7. Real-Time Chat System</strong></summary>

Built-in conversational support connecting customers directly with CSR agents — supporting text messages, attachments, read/unread indicators, timestamps, and full conversation history.
</details>

<details>
<summary><strong>8. Load-Shedding Schedule Management</strong></summary>

Admins create and update load-shedding schedules per area/feeder. Customers view today's and upcoming weekly schedules from their dashboard, enabling better daily planning.
</details>

<details>
<summary><strong>9. Notifications & Alerts</strong></summary>

Admins broadcast outage updates, payment reminders, system alerts, and policy changes. Users see these in-dashboard with prominent unread counts.
</details>

<details>
<summary><strong>10. Feeder & Area Management</strong></summary>

Maintains structured data on feeders, associated areas, and connected consumers, enabling accurate distribution mapping and effective load/outage management.
</details>

---

## 🚀 Getting Started

### Prerequisites
- Java JDK 21
- Maven (`mvn` available) or IDE support (IntelliJ / Eclipse)
- MySQL server running, with a database named `powertrack_db`
- Node.js (stable version)
- IntelliJ IDEA (recommended) or terminal

### Backend Setup
```bash
# 1. Navigate to backend folder
cd powertrack-backend

# 2. Create the database and run the provided SQL schema

# 3. Open in your IDE and let Maven load dependencies

# 4. Configure application.properties with your DB credentials

# 5. Build & run
mvn clean package
mvn spring-boot:run
```

### Frontend Setup
```bash
cd powertrack-frontend
npm install
npm run dev
```

---

## 🔑 Demo Accounts

> ⚠️ These are demo credentials for local testing/showcase purposes only — replace before any real deployment.

| Role | Email | Password |
|---|---|---|
| Admin | admin@example.com | password123 |
| Support | support@powertrack.com | password123 |
| Field Staff | field@powertrack.com | password123 |

---

## 🗓️ Project Timeline

| Phase | Dates |
|---|---|
| Planning | 21–22 Nov 2025 |
| Database Design | 23 Nov 2025 |
| Backend Development | 24–29 Nov 2025 |
| Frontend Development | 29–30 Nov 2025 |
| Testing | 1 Dec 2025 |

---

