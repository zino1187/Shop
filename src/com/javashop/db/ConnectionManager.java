/*
 * 데이터베이스 접속과 관련된 중복된 업무를 이 클래스로 정의하여
 * 재사용 해보자!!
 * */
package com.javashop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:XE";
	String user="adams";
	String password="1234";
	
	//접속 객체를 반환하는 메서드 
	public Connection getConnection() {
		Connection con=null;
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	//사용된 커넥션 끊기!!!
	public void disconnect(Connection con) {
		if(con !=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}










