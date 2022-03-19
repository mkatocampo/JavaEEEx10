/**
 * 
 */
package edu.nbcc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.nbcc.model.Book;

/**
 * @author Maria Ocampo
 * No comments in implementation class
 */
public class BookDAOImpl implements BookDAO {

	//help to connect mysql to java code
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3000/javaee";
	private static final String USER_ID = "dev";
	private static final String PASSWORD = "dev1234";
	
	private static final String DELETE = "DELETE FROM book WHERE id=?";
	private static final String INSERT = "INSERT INTO book(name, price, term) VALUES (?,?,?)";
	private static final String UPDATE = "UPDATE book SET name=?, price=?, term=? WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM book ORDER BY id=?";
	private static final String FIND_BY_ID = "SELECT * FROM book WHERE id=?";
	private static final String FIND_BY_NAME = "SELECT * FROM book WHERE name=?";
	
	/**
	 * get connection
	 * @return
	 */
	private Connection getConnection() {
		try {
			Class.forName(DRIVER_NAME);
			return DriverManager.getConnection(DB_URL,USER_ID,PASSWORD);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * close the connection
	 * @param conn
	 */
	private static void close(Connection conn) {
		if (conn!=null) {
			try {
				conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * close the statement
	 * @param conn
	 */
	private static void close(Statement stmt) {
		if (stmt!=null) {
			try {
				stmt.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	@Override
	public int delete(int id) {
		Connection conn = null;
		PreparedStatement statement = null;
		try{
			conn = getConnection();
			statement = conn.prepareStatement(DELETE);
			statement.setInt(1,id);
			return statement.executeUpdate();
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}finally {
			close(statement);
			close(conn);
		}	
	}

	@Override
	public int insert(Book book) {
		Connection conn = null;
		PreparedStatement statement = null;
		try{
			conn = getConnection();
			statement = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,book.getName());
			statement.setDouble(2,book.getPrice());
			statement.setInt(3,book.getTerm());
			
			int result = statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			
			if(rs.next()) {
				book.setId(rs.getInt(1));
			}
			return result;
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}finally {
			close(statement);
			close(conn);
		}	
	}

	@Override
	public int update(Book book) {
		Connection conn = null;
		PreparedStatement statement = null;
		try{
			conn = getConnection();
			statement = conn.prepareStatement(UPDATE);
			statement.setString(1,book.getName());
			statement.setDouble(2,book.getPrice());
			statement.setInt(3,book.getTerm());
			statement.setInt(4,book.getId());
			
			return statement.executeUpdate();
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}finally {
			close(statement);
			close(conn);
		}	
	}

	@Override
	public List<Book> findAll() {
		Connection conn = null;
		PreparedStatement statement = null;
		List<Book> list = new ArrayList<Book>();
		try{
			conn = getConnection();
			statement = conn.prepareStatement(FIND_ALL);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt("id"));
				book.setName(rs.getString("name"));
				book.setTerm(rs.getInt("term"));
				book.setPrice(rs.getDouble("price"));
				list.add(book);
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}finally {
			close(statement);
			close(conn);
		}	
		return list;
	}

	@Override
	public Book findByName(String name) {
		Connection conn = null;
		PreparedStatement statement = null;
		try{
			conn = getConnection();
			statement = conn.prepareStatement(FIND_BY_NAME);
			statement.setString(1, name);
			ResultSet rs = statement.executeQuery();
			Book book = new Book();
			if (rs.next()) {
				book.setId(rs.getInt("id"));
				book.setName(rs.getString("name"));
				book.setTerm(rs.getInt("term"));
				book.setPrice(rs.getDouble("price"));
			}
			return book;
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}finally {
			close(statement);
			close(conn);
		}
	}

	@Override
	public Book findById(int id) {
		Connection conn = null;
		PreparedStatement statement = null;
		try{
			conn = getConnection();
			statement = conn.prepareStatement(FIND_BY_ID);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			Book book = new Book();
			if (rs.next()) {
				book.setId(rs.getInt("id"));
				book.setName(rs.getString("name"));
				book.setTerm(rs.getInt("term"));
				book.setPrice(rs.getDouble("price"));
			}
			return book;
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}finally {
			close(statement);
			close(conn);
		}
	}

}
