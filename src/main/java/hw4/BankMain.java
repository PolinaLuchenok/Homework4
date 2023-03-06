package hw4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BankMain {

	public static void main(String[] args) throws InvalidNumberException {
		
		try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
                System.out.println("No sqlite driver");
                return;
        }
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
        	String path = "jdbc:sqlite:hw4db.db";
        	connection = DriverManager.getConnection(path);
            
        	TableDatabase tableDb = new TableDatabase(connection, statement);
        	tableDb.createUsers();
        	tableDb.createAccounts();
        	tableDb.createTransactions();
        	
        	Scanner scan = new Scanner(System.in);
            System.out.println("Здравствуйте! Приветствуем Вас в нашем онлайн-банке!");
            System.out.println("Введите Ваше имя");
            String name = scan.nextLine();
            
            User user = new User(connection, statement, name);
            Account account = new Account(connection, statement);
            
			int solution;
            do {
                int userId = user.getUserId();
                
            	System.out.println("");
            	System.out.println(name + ", выберите нужное Вам действие и введите соответствующий номер:");
    			System.out.println("Регистрация нового пользователя - 1");
    			System.out.println("Добавление аккаунта пользователю - 2");
    			System.out.println("Пополнение существующего аккаунта - 3");
    			System.out.println("Снятие средств с существующего аккаунта - 4");
    			System.out.println("Выход - 5");
    			solution = scan.nextInt();
    			
    			if(solution == 1) {                                 
    				
    				if(userId != 0) {
    					System.out.println("Пользователь с таким именем уже зарегистрирован");
    				} else {
    					scan = new Scanner(System.in);
        				System.out.println("Введите Ваш адрес (по желанию)");
        				String address = scan.nextLine();
        				
        				if(address.isEmpty()) {
        					address = null;
        				}
        				user.createUser(address);
        				System.out.println("Вы успешно зарегистрированы!");
    				}
    			} else if(solution == 2 || solution == 3 || solution == 4) {
    				
    				if(userId == 0) {
    					System.out.println("Вы не зарегистрированы!");
    				} else {
        				System.out.println("Выберете валюту аккаунта и введите соответствующий номер:");
        				System.out.println("BYN - 1");
            			System.out.println("RUB - 2");
            			System.out.println("USD - 3");
            			System.out.println("EUR - 4");
            			
            			int numberCurrency = scan.nextInt();
            			String currency = account.getCurrencyFromUser(numberCurrency);
            			ResultSet resultSet = account.getAllAboutAccount(userId, currency);
            			
            			int accountId = resultSet.getInt("accountId");
            			int balance = resultSet.getInt("balance");
            			
            			if(solution == 2) {
            				if(accountId != 0) {
                				System.out.println("Аккаунт в указанной валюте у Вас уже открыт!");
                			} else {
                				account.createAccount(userId, currency);
                				System.out.println("Аккаунт успешно добавлен!");
                			}
            			} else {
            				if(accountId == 0) {
                				System.out.println("В указанной валюте аккаунт не добавлен!");
                			} else {
                				System.out.println("Введите сумму для изменения баланса");
                    			double result = scan.nextDouble();
                    			int sum = account.getSumWithLimit(result);
                    			
                    			if(sum > 100000000) {
                        			System.out.println("Максимальная сумма транзакции не может превышать 100 000 000!");
                        		} else {
                        			if(solution == 3) {
                        				int moreBalance = balance+sum;
                        				if(moreBalance > 2000000000) {
                            				System.out.println("Максимальный баланс аккаунта не может превышать 2 000 000 000!");
                            			} else {
                                    		account.increaseBalance(sum, accountId, moreBalance);
                                			System.out.println("Аккаунт успешно пополнен!");
                            			}
                        			} else {
                        				int lessBalance = balance-sum;
                        				if(lessBalance < 0) {
                        					System.out.println("На балансе не хватает средств для снятия указанной суммы!");
                            			} else {
                            				account.decreaseBalance(sum, accountId, lessBalance);
                                			System.out.println("Снятие средств прошло успешно!");
                            			}
                        			}
                    			}
                			}
            			} 
    				}		
    			} else if(solution == 5) {
    				System.out.println("Вы вышли из онлайн-банка!");
    			} else {
    				System.out.println("Вы указали неверный номер!");
    			}
    		} while (solution != 5);
            
            connection.close();
            scan.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } catch (InputMismatchException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                	statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println("Can't close the connection");
            }
        }
	}
}
