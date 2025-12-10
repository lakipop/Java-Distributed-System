# 🌐 Java Distributed Systems

> **Distributed Computing in Java** - RPC, RMI, Socket Programming, and Real-World Distributed Applications

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.java.com/)
[![Level](https://img.shields.io/badge/Level-Expert-purple.svg)]()
[![Systems](https://img.shields.io/badge/Systems-12%20Packages-blue.svg)]()

---

## 🔗 Part of Java Learning Path

**This is Repository 5 of 5** - The final repository in the complete Java learning curriculum.

| # | Repository | Level | Focus | Your Progress |
|---|------------|-------|-------|---------------|
| 1 | **[Java-Foundation](https://github.com/lakipop/Java-Foundation)** | 🟢 Beginner | Basics, OOP, Exceptions | Completed ✓ |
| 2 | **[Java-Intermediate](https://github.com/lakipop/Java-Intermediate)** | 🟡 Intermediate | GUI, JDBC, Collections | Completed ✓ |
| 3 | **[Java-Advanced](https://github.com/lakipop/Java-Advanced)** | 🔴 Advanced | Multithreading, Spring Boot | Completed ✓ |
| 4 | **[Java-Design-Patterns](https://github.com/lakipop/Java-Design-Patterns)** | 🟣 Expert | 23 GoF Patterns | ← Previous |
| **5** | **[Java-Distributed-System](https://github.com/lakipop/Java-Distributed-System)** ⭐ | 🌐 Expert | RPC, RMI, Sockets | **← YOU ARE HERE** |

**🎉 Congratulations!** This is the final repository - you've completed the entire Java learning path!

---

## 📋 Overview

Complete guide to building distributed systems in Java. Covers all major communication paradigms including **TCP/UDP Sockets**, **Remote Method Invocation (RMI)**, **Remote Procedure Calls (RPC)**, and real-world applications like chat systems and sensor networks.

**Prerequisites:** Java-Advanced (Multithreading, Networking basics)  
**Duration:** 3-4 weeks  
**Skill Level:** 🌐 Expert

---

## 🏗️ Repository Structure

```
src/
├── 📡 Socket Programming
│   ├── TCPClientServerCommunication/   # Basic TCP communication
│   ├── TCPRealWorld/                   # TCP Price Server
│   ├── TCPChat/                        # Multi-client chat ⭐
│   ├── UDPRealWorld/                   # UDP Price Server
│   └── UDPChat/                        # Connectionless chat
│
├── 🔌 RMI (Remote Method Invocation)
│   ├── RMIHelloWorld/                  # Basic RMI example
│   ├── RMICalculator/                  # Calculator service
│   ├── RMIChat/                        # RMI-based chat
│   ├── RMIBankService/                 # Bank operations ⭐
│   └── RMIStudentManagement/           # Student CRUD ⭐
│
├── 📞 RPC (Remote Procedure Call)
│   └── RPCWeatherService/              # Weather data service ⭐
│
└── 🌡️ Real-World Application
    └── TemperatureHumiditySensor/      # Sensor monitoring system ⭐⭐
```

---

## 📚 Content Index

### 🔌 Socket Programming (TCP/UDP)

#### TCP - Transmission Control Protocol
| Package | Description | Key Concepts |
|---------|-------------|--------------|
| `TCPClientServerCommunication` | Basic client-server | Socket, ServerSocket, Streams |
| `TCPRealWorld` | Price lookup server | HashMap, BufferedReader |
| `TCPChat` ⭐ | Multi-client chat | Threading, Broadcast, ClientHandler |

#### UDP - User Datagram Protocol
| Package | Description | Key Concepts |
|---------|-------------|--------------|
| `UDPRealWorld` | Price lookup (connectionless) | DatagramSocket, DatagramPacket |
| `UDPChat` | Lightweight chat | InetSocketAddress, Broadcast |

---

### 🔗 RMI - Remote Method Invocation

| Package | Description | Key Concepts |
|---------|-------------|--------------|
| `RMIHelloWorld` | Basic RMI example | Remote interface, Registry |
| `RMICalculator` | Calculator service | add, sub, mul, div, mod operations |
| `RMIChat` | Simple chat service | Remote messaging |
| `RMIBankService` ⭐ | Bank operations | Deposit, Withdraw, Transfer, Balance |
| `RMIStudentManagement` ⭐ | Student CRUD | Add, Update, Delete, Search, Statistics |

---

### 📞 RPC - Remote Procedure Call

| Package | Description | Key Concepts |
|---------|-------------|--------------|
| `RPCWeatherService` ⭐ | Weather data service | RPCRequest, RPCResponse, Serialization |

**RPC Components:**
- `RPCRequest.java` - Method name + parameters
- `RPCResponse.java` - Result + error handling
- `WeatherData.java` - Serializable data transfer object

---

### 🌡️ Temperature-Humidity Sensor System ⭐⭐

> **Highlight Project** - Complete distributed sensor monitoring network

#### Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      BASE STATION                           │
│  ┌─────────────────────────────────────────────────────┐   │
│  │           Dynamic HashMaps                           │   │
│  │  • temperatureMap<SensorID, Temperature>             │   │
│  │  • humidityMap<SensorID, Humidity>                   │   │
│  │  • statusMap<SensorID, Status>                       │   │
│  └─────────────────────────────────────────────────────┘   │
│                           ▲                                 │
│                           │ TCP Connection                  │
│           ┌───────────────┼───────────────┐                │
│           │               │               │                │
│      SENSOR-001      SENSOR-002      SENSOR-00N            │
│      (Sub-Branch)    (Sub-Branch)    (Sub-Branch)          │
└─────────────────────────────────────────────────────────────┘
```

#### Files

| File | Description |
|------|-------------|
| `BaseStation.java` | Central hub with ConcurrentHashMaps |
| `SensorBranch.java` | Remote sensor node (sub-branch) |
| `SensorData.java` | Serializable sensor reading |
| `StatusCalculator.java` | NORMAL/WARNING/CRITICAL logic |

#### Status Thresholds

| Status | Temperature | Humidity | Action |
|--------|-------------|----------|--------|
| ✅ **NORMAL** | 15°C - 35°C | 30% - 70% | All OK |
| ⚡ **WARNING** | < 15°C or > 35°C | < 30% or > 70% | Monitor closely |
| ⚠️ **CRITICAL** | < 0°C or > 45°C | < 10% or > 90% | Immediate action |

---

## 🚀 Quick Start

### Temperature-Humidity Sensor Demo

```bash
# Compile
cd d:\Projects\JAVA-Full-Course\Java-Distributed-System
javac -d out src/TemperatureHumiditySensor/*.java

# Terminal 1: Start Base Station
java -cp out TemperatureHumiditySensor.BaseStation

# Terminal 2: Start Sensor 1
java -cp out TemperatureHumiditySensor.SensorBranch
# Enter: SENSOR-001

# Terminal 3: Start Sensor 2
java -cp out TemperatureHumiditySensor.SensorBranch
# Enter: SENSOR-002
```

### TCP Chat Demo

```bash
# Compile
javac -d out src/TCPChat/*.java

# Terminal 1: Start Server
java -cp out TCPChat.TCPChatServer

# Terminal 2 & 3: Start Clients
java -cp out TCPChat.TCPChatClient
```

### RMI Bank Service Demo

```bash
# Compile
javac -d out src/RMIBankService/*.java

# Terminal 1: Start Server
java -cp out RMIBankService.BankServer

# Terminal 2: Start Client
java -cp out RMIBankService.BankClient
```

---

## 🔌 Port Configuration

| Service | Port | Protocol |
|---------|------|----------|
| RMI Services (existing) | 1099 | TCP |
| RMI Bank Service | 1100 | TCP |
| RMI Student Management | 1101 | TCP |
| RPC Weather Service | 5001 | TCP |
| TCP Chat | 5002 | TCP |
| UDP Chat | 5003 | UDP |
| Temperature-Humidity Sensor | 5004 | TCP |

---

## 📊 Content Summary

| Category | Packages | Examples | Difficulty |
|----------|----------|----------|------------|
| **TCP Sockets** | 3 | 6 files | 🟡🟡 |
| **UDP Sockets** | 2 | 4 files | 🟡🟡 |
| **RMI Services** | 5 | 20 files | 🔴🔴 |
| **RPC Pattern** | 1 | 5 files | 🔴🔴🔴 |
| **Sensor System** | 1 | 4 files | 🔴🔴🔴 |

**Total:** 12 packages, 35+ Java files

---

## 🎓 What You'll Learn

By completing this repository, you will:

- ✅ **Master TCP Socket Programming** - Client-server architecture, streams
- ✅ **Understand UDP Communication** - Connectionless, datagram-based messaging
- ✅ **Implement RMI Services** - Remote interfaces, registry, distributed objects
- ✅ **Apply RPC Pattern** - Request/response serialization, method invocation
- ✅ **Build Real-World Systems** - Chat applications, sensor networks
- ✅ **Use Dynamic Data Structures** - ConcurrentHashMap for thread-safe storage
- ✅ **Handle Distributed State** - Status calculation, data synchronization

---

## 🛠️ Requirements

| Software | Version | Purpose |
|----------|---------|---------|
| JDK | 8+ | Java Development Kit |
| IDE | Any | IntelliJ IDEA / Eclipse / VS Code |

No external dependencies required - pure Java implementations!

---

## 💡 Key Concepts Covered

### Communication Paradigms

| Paradigm | Java Classes | Use Case |
|----------|--------------|----------|
| **TCP** | Socket, ServerSocket | Reliable, ordered delivery |
| **UDP** | DatagramSocket, DatagramPacket | Fast, lightweight messaging |
| **RMI** | Remote, UnicastRemoteObject | Distributed object access |
| **RPC** | ObjectInputStream/OutputStream | Remote method execution |

### Design Patterns Used

- **Client-Server** - All socket and RMI examples
- **Observer** - Chat broadcast messaging
- **Repository** - Data storage with HashMaps
- **Factory** - RMI object creation
- **Strategy** - Status calculation in sensor system

---

## 🎯 Project Highlights

### 1. Multi-Client TCP Chat
- Thread-per-client model
- Broadcast messaging
- Join/leave notifications
- Client handler abstraction

### 2. RMI Bank Service
- Account creation with unique IDs
- Deposit, withdraw, transfer
- Balance inquiry
- Thread-safe with synchronized methods

### 3. Sensor Monitoring System
- Dynamic sensor registration
- Real-time status calculation
- ConcurrentHashMap for thread safety
- Condition simulation for testing

---

## 🔗 Explore Full Learning Path

**🎉 You've completed the entire Java course!**

| # | Repository | What You Learned |
|---|------------|------------------|
| 1 | [Java-Foundation](https://github.com/lakipop/Java-Foundation) | Basics, OOP, Exceptions |
| 2 | [Java-Intermediate](https://github.com/lakipop/Java-Intermediate) | GUI, JDBC, Collections |
| 3 | [Java-Advanced](https://github.com/lakipop/Java-Advanced) | Multithreading, Spring Boot |
| 4 | [Java-Design-Patterns](https://github.com/lakipop/Java-Design-Patterns) | 23 GoF Patterns |
| 5 | **Java-Distributed-System** ⭐ | RPC, RMI, Sockets |

---

## 📖 Further Reading

- [Java RMI Documentation](https://docs.oracle.com/javase/tutorial/rmi/)
- [Java Socket Programming](https://docs.oracle.com/javase/tutorial/networking/sockets/)
- [Distributed Systems Concepts](https://www.distributed-systems.net/)

---

## 📜 License

Educational use only. Part of the complete Java learning curriculum.

---

## 🌟 Tips for Success

1. **Start with basic TCP** - Understand socket fundamentals first
2. **Run multiple terminals** - Distributed systems need multiple processes
3. **Test with different scenarios** - Normal, warning, and critical conditions
4. **Read the comments** - Each file has detailed section explanations
5. **Experiment** - Modify thresholds, add new sensors, extend functionality

---

**Congratulations on completing the Java Learning Path! 🎓🚀**

---

*Part of the Complete Java Course Collection by lakipop*
