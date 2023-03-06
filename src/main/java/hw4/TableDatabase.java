package hw4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableDatabase {
	private Connection connection;
	private PreparedStatement statement;
	
	public TableDatabase(Connection connection, PreparedStatement statement) {
		super();
		this.connection = connection;
		this.statement = statement;
	}
	
	public void createUsers() throws SQLException {
		String sqlUsers = "CREATE TABLE IF NOT EXISTS Users (userId INTEGER PRIMARY KEY AUTOINCREMENT, name varchar(50) NOT NULL, address varchar(255))"; 
    	statement = connection.prepareStatement(sqlUsers);
    	statement.executeUpdate();
	}
	
	public void createAccounts() throws SQLException {
		String sqlAccounts = "CREATE TABLE IF NOT EXISTS Accounts (accountId INTEGER PRIMARY KEY AUTOINCREMENT, userId Integer NOT NULL, balance INTEGER(15) NOT NULL, currency varchar(10) NOT NULL, FOREIGN KEY(userId) REFERENCES Users(userIdd))"; 
    	statement = connection.prepareStatement(sqlAccounts);
    	statement.executeUpdate();
	}
	
	public void createTransactions() throws SQLException {
		String sqlTransactions = "CREATE TABLE IF NOT EXISTS Transactions (transactionIdId INTEGER PRIMARY KEY AUTOINCREMENT, accountId Integer NOT NULL, amount INTEGER(15) NOT NULL, FOREIGN KEY(accountId) REFERENCES Accounts(accountId))"; 
    	statement = connection.prepareStatement(sqlTransactions);
    	statement.executeUpdate();
	}
}
