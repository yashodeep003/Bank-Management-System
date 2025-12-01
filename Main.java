

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/* ------------------ TRANSACTION CLASS  ------------------ */

class Transaction {

	int accountNo;
	double amount;
	String type;
	String mode;
	String status;
	String dateTime;

	Transaction(int accountNo, double amount, String type, String mode, String status) {
		this.accountNo = accountNo;
		this.amount = amount;
		this.type = type;
		this.mode = mode;
		this.status = status;
		this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public String toString() {
		return "Transaction [accountNo=" + accountNo + ", amount=" + amount + ", type=" + type + ", mode=" + mode
				+ ", status=" + status + ", dateTime=" + dateTime + "]";
	}

	void print() {
		System.out.println(toString());
	}
}

/* ------------------ TRANSACTION MANAGER ------------------ */

class TransactionManager {
	List<Transaction> list = new ArrayList<>();

	void add(Transaction t) {
		list.add(t);
	}

	void printAll() {
		if (list.isEmpty())
			System.out.println("No transactions.");
		else
			for (Transaction t : list)
				t.print();
	}

	int getTransactionCount() {
		return list.size();
	}
}

/* ------------------ BASE ACCOUNT ------------------ */

abstract class Account {

	int accountNumber;
	String ownerName;
	double balance;

	TransactionManager tm = new TransactionManager();

	Account(int ac, String owner, double bal) {
		this.accountNumber = ac;
		this.ownerName = owner;
		this.balance = bal;
	}

	void deposit(double amount) {
		if (amount <= 0) {
			System.out.println("Invalid amount");
			return;
		}
		balance += amount;
		tm.add(new Transaction(accountNumber, amount, "Deposit", "Manual", "Success"));
	}

	void withdraw(double amount) {
		if (amount <= 0) {
			System.out.println("Invalid amount");
			return;
		}

		if (balance >= amount) {
			balance -= amount;
			tm.add(new Transaction(accountNumber, amount, "Withdraw", "Manual", "Success"));
		} else {
			System.out.println("Insufficient balance");
			tm.add(new Transaction(accountNumber, amount, "Withdraw", "Manual", "Failed"));
		}
	}

	void printTransactions() {
		tm.printAll();
	}

	TransactionManager getTransactionManager() {
		return tm;
	}

	abstract String getType();

	abstract void printDetails();
}

/* ------------------ SAVINGS ACCOUNT ------------------ */

class SavingsAccount extends Account {

	double minBalance;
	double interestRate;

	SavingsAccount(int ac, String owner, double bal, double minBal, double rate) {
		super(ac, owner, bal);
		this.minBalance = minBal;
		this.interestRate = rate;
	}

	String getType() {
		return "Savings";
	}

	void printDetails() {
		System.out.println("\n[SAVINGS ACCOUNT]");
		System.out.println("Account No : " + accountNumber);
		System.out.println("Owner      : " + ownerName);
		System.out.println("Balance    : " + balance);
		System.out.println("Min Bal    : " + minBalance + " | Interest: " + interestRate + "%");
	}

	void applyInterest() {
		double interest = balance * interestRate / 100.0;
		deposit(interest);
		System.out.println("Interest applied: " + interest);
	}
}

/* ------------------ CURRENT ACCOUNT ------------------ */

class CurrentAccount extends Account {

	double overdraftLimit;

	CurrentAccount(int ac, String owner, double bal, double limit) {
		super(ac, owner, bal);
		this.overdraftLimit = limit;
	}

	String getType() {
		return "Current";
	}

	void printDetails() {
		System.out.println("\n[CURRENT ACCOUNT]");
		System.out.println("Account No : " + accountNumber);
		System.out.println("Owner      : " + ownerName);
		System.out.println("Balance    : " + balance + " | Overdraft: " + overdraftLimit);
	}

	void withdraw(double amount) {
		if (amount <= 0) {
			System.out.println("Invalid amount");
			return;
		}

		if (balance + overdraftLimit >= amount) {
			balance -= amount;
			tm.add(new Transaction(accountNumber, amount, "Withdraw", "Manual", "Success"));
		} else {
			System.out.println("Overdraft limit exceeded");
			tm.add(new Transaction(accountNumber, amount, "Withdraw", "Manual", "Failed- Overdraft"));
			System.out.println("Cannot withdraw= Overdraft Limit Exceeded");
		}
	}
}

/* ------------------ SALARY ACCOUNT ------------------ */

class SalaryAccount extends Account {

	String employerName;
	double salaryLimit;

	SalaryAccount(int ac, String owner, double bal, String emp, double limit) {
		super(ac, owner, bal);
		this.employerName = emp;
		this.salaryLimit = limit;
	}

	String getType() {
		return "Salary";
	}

	void printDetails() {
		System.out.println("\n[SALARY ACCOUNT]");
		System.out.println("Account No : " + accountNumber);
		System.out.println("Owner      : " + ownerName);
		System.out.println("Balance    : " + balance);
		System.out.println("Employer   : " + employerName + " | Limit: " + salaryLimit);
	}
}

/* ------------------ LOAN ACCOUNT ------------------ */

class LoanAccount extends Account {

	double loanAmount;
	double interestRate;
	double emi;
	String dueDate;

	LoanAccount(int ac, String owner, double loanAmt, double rate, double emi, String due) {
		super(ac, owner, -loanAmt);
		this.loanAmount = loanAmt;
		this.interestRate = rate;
		this.emi = emi;
		this.dueDate = due;
	}

	String getType() {
		return "Loan";
	}

	void printDetails() {
		System.out.println("\n[LOAN ACCOUNT]");
		System.out.println("Account No       : " + accountNumber);
		System.out.println("Owner            : " + ownerName);
		System.out.println("Outstanding Loan : " + (-balance));
		System.out.println("Interest         : " + interestRate + "% | EMI: " + emi + " | Due: " + dueDate);
	}

	void applyInterest() {
		double interest = (-balance) * interestRate / 100.0;
		balance -= interest;
		tm.add(new Transaction(accountNumber, interest, "Interest", "System", "Success"));
		System.out.println("Loan Interest charged: " + interest);
	}

	void payEmi(double amount) {
		if (amount <= 0) {
			System.out.println("Invalid EMI");
			return;
		}
		balance += amount;
		tm.add(new Transaction(accountNumber, amount, "EMI", "Manual", "Success"));
		System.out.println("EMI Paid: " + amount);
	}
}

/* ------------------ MODEL ------------------ */

class BankModel {

	List<Account> accounts = new ArrayList<>();

	void add(Account a) {
		accounts.add(a);
	}

	Account get(int ac) {
		for (Account a : accounts)
			if (a.accountNumber == ac)
				return a;
		return null;
	}

	Account[] getAll() {
		return accounts.toArray(new Account[0]);
	}
}//BankModel class ends here

/* ------------------ CONTROLLER ------------------ */

class BankController {

	BankModel model = new BankModel();

	void addAccount(Account a) {
		if (model.get(a.accountNumber) != null) {
			System.out.println("Account already exists");
			return;
		}
		model.add(a);
		System.out.println("✔ Account created successfully!");
	}

	Account find(int ac) {
		return model.get(ac);
	}

	void deposit(int ac, double amt) {
		Account a = find(ac);
		if (a == null) {
			System.out.println("Account not found");
			return;
		}
		a.deposit(amt);
		System.out.println("✔ Transaction successful");

	}

	void withdraw(int ac, double amt) {
		Account a = find(ac);
		if (a == null) {
			System.out.println("Account not found");
			return;
		}
		a.withdraw(amt);
		System.out.println("✔ Transaction processed");

	}

	void deleteAccount(int acNo) {
		Account a = find(acNo);
		if (a == null) {
			System.out.println("Account not found");
			return;
		}
		model.accounts.remove(a);
		System.out.println("✔ Account deleted successfully!");
	}

	// ---------------- Interest / EMI ----------------//
	void applyInterestToSavings(int acNo) {
		Account a = find(acNo);
		if (a instanceof SavingsAccount)
			((SavingsAccount) a).applyInterest();
		else
			System.out.println("Not a savings account.");
	}

	void applyInterestToLoan(int acNo) {
		Account a = find(acNo);
		if (a instanceof LoanAccount)
			((LoanAccount) a).applyInterest();
		else
			System.out.println("Not a loan account.");
	}

	void payEmi(int acNo, double amt) {
		Account a = find(acNo);
		if (a instanceof LoanAccount)
			((LoanAccount) a).payEmi(amt);
		else
			System.out.println("Not a loan account.");
	}

	void dailyReport() {
		System.out.println("\n--- END OF DAY REPORT ---");
		Account[] allAccounts = model.getAll();

		double totalBalance = 0;
		int totalTransactions = 0;

		for (Account a : allAccounts) {
			a.printDetails();
			System.out.println("Transactions:");
			a.printTransactions();
			totalBalance += a.balance;
			totalTransactions += a.getTransactionManager().getTransactionCount();
			System.out.println("--------------------------");
		}

		System.out.println("\n=== SUMMARY ===");
		System.out.println("Total Accounts: " + allAccounts.length);
		System.out.println("Total Balance: " + totalBalance);
		System.out.println("Total Transactions: " + totalTransactions);
	}

	void saveReport() {
		System.out.println("END OF DAY REPORT\n==============================\n");
		for (Account a : model.getAll()) {
			System.out.println(a.getType() + " Account: " + a.accountNumber);
			System.out.println("Owner: " + a.ownerName);
			System.out.println("Balance: " + a.balance);
			System.out.println("Transactions:");
			a.printTransactions();
			System.out.println("-----------------------------\n");
		}
		System.out.println("✔ Report displayed on console");
	}

}//Bank Controller class ends here

/* ------------------ VIEW ------------------ */

class BankView {

	Scanner sc = new Scanner(System.in);
	BankController c = new BankController();

	void start() {
		while (true) {
			System.out.println("\n=== BANK MENU ===");
			System.out.println("1. Create Account");
			System.out.println("2. Deposit");
			System.out.println("3. Withdraw");
			System.out.println("4. Show Account");
			System.out.println("5. End of Day Report");
			System.out.println("6. Save Report");
			System.out.println("0. Exit");

			System.out.print("Choice: ");
			int ch = readInt();

			switch (ch) {
			case 1:
				createAccount();
				break;
			case 2:
				deposit();
				break;
			case 3:
				withdraw();
				break;
			case 4:
				show();
				break;
			case 5:
				c.dailyReport();
				break;
			case 6:
				c.saveReport();
				break;
			case 0:
				return;
			default:
				System.out.println("Invalid Choice");
			}
		}
	}

	int readInt() {
		try {
			return Integer.parseInt(sc.nextLine());
		} catch (Exception e) {
			return -1;
		}
	}

	double readDouble() {
		try {
			return Double.parseDouble(sc.nextLine());
		} catch (Exception e) {
			return 0;
		}
	}// Bank View class ends here

	/* ---------- CREATE ACCOUNT WITH TYPE INPUT ----------- */

	void createAccount() {
		System.out.println("\nEnter Account Type (savings/salary/current/loan): ");
		String type = sc.nextLine().toLowerCase();

		System.out.print("Account No: ");
		int ac = readInt();

		System.out.print("Owner Name: ");
		String name = sc.nextLine();

		switch (type) {

		case "savings":
			System.out.print("Initial Balance: ");
			double bal = readDouble();
			c.addAccount(new SavingsAccount(ac, name, bal, 1000, 5.5));
			break;

		case "current":
			System.out.print("Initial Balance: ");
			double bal2 = readDouble();
			c.addAccount(new CurrentAccount(ac, name, bal2, 10000));
			break;

		case "salary":
			System.out.print("Initial Balance: ");
			double bal3 = readDouble();
			c.addAccount(new SalaryAccount(ac, name, bal3, "Company", 50000));
			break;

		case "loan":
			System.out.print("Loan Amount: ");
			double loanAmt = readDouble();
			c.addAccount(new LoanAccount(ac, name, loanAmt, 10, 2500, "10th Every Month"));
			break;

		default:
			System.out.println("Invalid Account Type");
		}
	}

	void deposit() {
		System.out.print("Account No: ");
		int ac = readInt();
		System.out.print("Amount: ");
		double amt = readDouble();
		c.deposit(ac, amt);
	}

	void withdraw() {
		System.out.print("Account No: ");
		int ac = readInt();
		System.out.print("Amount: ");
		double amt = readDouble();
		c.withdraw(ac, amt);
	}

	void show() {
		System.out.print("Account No: ");
		int ac = readInt();
		Account a = c.find(ac);

		if (a == null) {
			System.out.println("Account Not Found");
			return;
		}

		a.printDetails();
		System.out.println("\nTransactions:");
		a.printTransactions();
	}

}
/* ------------------ MAIN CLASS ------------------ */

class Main {
	public static void main(String[] args) {
		new BankView().start();
	} //Main ends here
}// TestApp Class ends Here
