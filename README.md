# Bank-Management-System
Bank Management System ( Console-Based ) in Java using MVC architecture. Supports Savings, Current, Salary, and Loan accounts with deposit, withdrawal, overdraft, EMI payment, interest calculation, and full transaction history. Includes end-of-day reports and clean OOP design for learning core Java concepts.

🚀 Features
✅ Account Types Supported

Savings Account

Current Account

Salary Account

Loan Account


🔐 Core Banking Operations
Create new account

Deposit money

Withdraw money

Apply interest (Savings & Loan)

Pay EMI (Loan account)

Transaction logging

Delete account


📊 Reporting

End-of-day detailed report

Per-account transaction report

Summary of accounts, balances, and transactions


🧱 Architecture Used

The application follows MVC architecture:

Model → Handles account data and storage

Controller → Controls operations (deposit, withdraw, interest…)

View → Handles user interaction (menu, input, output)



📂 Project Structure
BankSystem/
│
├── Transaction.java
├── TransactionManager.java
├── Account.java (abstract)
├── SavingsAccount.java
├── CurrentAccount.java
├── SalaryAccount.java
├── LoanAccount.java
│
├── BankModel.java
├── BankController.java
├── BankView.java
│
└── Main.java


All classes are inside a single file as per requirement, but still follow clean MVC separation.

🖥️ How to Run the Project
1. Clone the repository
git clone https://github.com/your-username/BankSystem.git

2. Navigate to folder
cd BankSystem

3. Compile the program
javac Main.java

4. Run the program
java Main

📜 Menu Options
1. Create Account
2. Deposit
3. Withdraw
4. Show Account Details
5. End of Day Report
6. Save Report
0. Exit

🧾 Example Account Creation
Savings Account
Account Type: savings
Account No: 101
Owner Name: Yash
Initial Balance: 5000

Loan Account
Account Type: loan
Account No: 501
Owner Name: Rohit
Loan Amount: 200000

📈 End of Day Report Example

Shows:
Every account created
Current balance
All transactions
Summary of bank operations

🛠️ Technologies Used

Java 8+

OOP Concepts

Collections Framework

Date & Time API (java.time)
