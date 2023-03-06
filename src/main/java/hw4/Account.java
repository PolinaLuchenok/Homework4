package hw4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
	private Connection connection;
	private PreparedStatement statement;
	
	public Account(Connection connection, PreparedStatement statement) {
		super();
		this.connection = connection;
		this.statement = statement;
	}
	
	public String getCurrencyFromUser(int numberCurrency) throws InvalidNumberException {
		String currency = null;
		switch (numberCurrency){
        case 1:
        	currency = "BYN";
            break;
        case 2:
        	currency = "RUB";
            break;
        case 3:
        	currency = "USD";
            break;
        case 4:
        	currency = "EUR";
            break;
        default:
        	throw new InvalidNumberException("Вы ввели неверный номер!");
		}
		return currency;
	}
	
	public ResultSet getAllAboutAccount(int userId, String currency) throws SQLException {
		String sql = "SELECT * FROM Accounts WHERE userId=? AND currency=?";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, userId);
		statement.setString(2, currency);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}
	
	public void createAccount(int userId, String currency) throws SQLException {
		String sql = "INSERT INTO Accounts (userId, balance, currency) VALUES(?, 0, ?)";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, userId);
		statement.setString(2, currency);
		statement.executeUpdate();
	}
	
	public int getSumWithLimit(double result) {
		return (int)(result*1000);
	}
	
	public void increaseBalance(int sum, int accountId, int moreBalance) throws SQLException {
		connection.setAutoCommit(false);
		String sql1 = "UPDATE Accounts SET balance=? WHERE accountId = ?";
		statement = connection.prepareStatement(sql1);
		statement.setInt(1, moreBalance);
		statement.setInt(2, accountId);
		statement.executeUpdate();
		
		String sql2 = "INSERT INTO Transactions (accountId, amount) VALUES(?, ?)";
		statement = connection.prepareStatement(sql2);
		statement.setInt(1, accountId);
		statement.setInt(2, sum);
		statement.executeUpdate();
		connection.commit();
	}
	
	public void decreaseBalance(int sum, int accountId, int lessBalance) throws SQLException {
		connection.setAutoCommit(false);
		String sql1 = "UPDATE Accounts SET balance=? WHERE accountId = ?";
		statement = connection.prepareStatement(sql1);
		statement.setInt(1, lessBalance);
		statement.setInt(2, accountId);
		statement.executeUpdate();
		
		String sql2 = "INSERT INTO Transactions (accountId, amount) VALUES(?, -?)";
		statement = connection.prepareStatement(sql2);
		statement.setInt(1, accountId);
		statement.setInt(2, sum);
		statement.executeUpdate();
		connection.commit();
	}
}
