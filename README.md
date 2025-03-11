# 📁 File-Backup-System  

**File-Backup-System** is a web-based **file management and backup** system that allows users to **securely upload, store, and retrieve** their files. It is built using **Spring Boot** and **MongoDB**, with user authentication and authorization features for enhanced security.  

## 🎯 Project Goal  

The goal of this project is to provide **a secure and efficient file backup solution** for users, ensuring that only authorized individuals can access the files. The system supports **file upload, download, user authentication, and access control** features.  

## 🚀 Features  

✅ **File Upload & Download** – Users can upload and retrieve files securely.  
✅ **User Authentication & Authorization** – Secure login and role-based access control (Spring Security).  
✅ **MongoDB for Storage** – File metadata is stored in a NoSQL database.  
✅ **RESTful API** – Well-structured API endpoints for smooth integration.  
✅ **Spring Boot for Easy Deployment** – Quickly deployable with Maven.  

## 🛠 Technologies Used  

| Component     | Technology Used |
|--------------|----------------|
| **Backend**  | Java (Spring Boot) |
| **Database** | MongoDB |
| **Security** | Spring Security, Spring Security Crypto |
| **Additional** | Spring AOP, Spring Boot DevTools |

## 📦 Installation & Usage  

### 1️⃣ Clone the Project  
```bash
git clone https://github.com/bengubal/File-Backup-System.git
cd File-Backup-System

### 2️⃣ Install Dependencies
```bash
mvn clean install
3️⃣ Configure MongoDB Connection
Edit your application.properties or application.yml to add the MongoDB connection details:

properties
spring.data.mongodb.uri=mongodb://localhost:27017/file_backup_db

4️⃣ Start the Application

mvn spring-boot:run
5️⃣ Access the Web Interface or API
Once the server is running, access it via:
http://localhost:8080
