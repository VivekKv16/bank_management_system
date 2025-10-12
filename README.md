# Bank Management System - XBank

Welcome to **XBank**, a web-based banking management system built using **Java**, **JDBC**, **Servlet**, **MySQL** and **Web Technologies (Bootstrap)**.  
This project demonstrates the core banking functionalities such as account creation, login validation, deposit, withdrawal, fund transfer, balance check, and transaction history management.

---

## 🚀 Features

- **User Registration & Login** – Create a new account or log in with valid credentials.  
- **Deposit & Withdraw** – Securely handle money transactions.  
- **Balance Enquiry** – Check account balance instantly.  
- **Fund Transfer** – Transfer money between accounts.  
- **Transaction History** – View all previous transactions.  
- **Responsive UI** – Developed using **HTML**, **CSS**, and **Bootstrap** for a modern and user-friendly experience.

---

## 🛠️ Technologies Used

- **Frontend:** HTML, CSS, Bootstrap  
- **Backend:** Java (Servlets, JDBC)  
- **Database:** MySQL  
- **APIs & Libraries:**  
  - `mysql-connector.jar` (JDBC Driver for MySQL)  
  - `servlet-api.jar` (for Servlet configuration and communication)

---

## ⚙️ Installation & Setup

To run this project locally:

1. **Clone the repository**
   ```bash
   git clone https://github.com/VivekKv16/bank_management_system.git
   
2. **Open the project** in an IDE such as **Eclipse** or **IntelliJ IDEA**.

3. **Add the required JAR files** to your project build path:
   - `mysql-connector.jar`
   - `servlet-api.jar`
    
4. **Set up the MySQL database:**
   - Create a new database named `xbank`.
   - Import the provided SQL file (if available) or manually create tables for `users`, `transactions`, etc.
    
5. **Update database credentials** in your JDBC connection file (e.g., `DBConnection.java`):
   ```java
   String url = "jdbc:mysql://localhost:3306/xbank";
   String username = "root";
   String password = "your_password";

## 🧩 Future Enhancements

- Add admin panel for user management.  
- Include email/SMS notifications for transactions.  
- Enhance UI with animations and better responsiveness.  
- Implement role-based authentication and audit logs.
