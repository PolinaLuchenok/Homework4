package hw4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	private Connection connection;
	private PreparedStatement statement;
	private String name;
	
	public User(Connection connection, PreparedStatement statement, String name) {
		super();
		this.connection = connection;
		this.statement = statement;
		this.name = name;
	}
	
	public int getUserId() throws SQLException {
		String sql = "SELECT userId FROM Users WHERE name = ?";
		statement = connection.prepareStatement(sql);
		statement.setString(1, name);
		ResultSet resultset = statement.executeQuery();
		return resultset.getInt("userId");
	}
	
	public void createUser(String address) throws SQLException {
		String sql = "INSERT INTO Users (name, address) VALUES(?, ?)";
		statement = connection.prepareStatement(sql);
		statement.setString(1, name);
		statement.setString(2, address);
		statement.executeUpdate();
	}
}
