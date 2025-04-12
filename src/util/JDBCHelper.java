/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author doann
 */
public class JDBCHelper {
    private static String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=QLVT;trustServerCertificate=true";
    private static String userName = "sa";
    private static String passWord = "123";

    /*
    Nạp driver
     */
    static {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    * Xây dựng PreparedStatement
    * @param sql là câu lệnh SQL chứa có thể chứa tham số. Nó có thể là một lời gọi thủ tục lưu
    * @param args là danh sách các giá trị được cung cấp cho các tham số trong câu lệnh sql
    * @return PreparedStatement tạo được
    * @throws java.sql.SQLException lỗi sai cú pháp
    */
    public static PreparedStatement getStmt(String sql, Object...args) throws SQLException{
        Connection connection = DriverManager.getConnection(dbURL, userName, passWord);
        PreparedStatement pstmt = null;
        //Loại bỏ các khoảng trắng ở đầu và cuối của chuỗi sql.
        //Kiểm tra xem câu lệnh SQL có bắt đầu bằng dấu { hay không
        //Dấu { thường được sử dụng để gọi stored procedures trong JDBC.
        if(sql.trim().startsWith("{")){  
            pstmt = connection.prepareCall(sql);
            //Nếu không phải stored procedure, connection.prepareStatement(sql) được sử dụng để tạo đối tượng PreparedStatement cho các câu lệnh SQL chuẩn như SELECT, INSERT, UPDATE, hoặc DELETE.
        }
        else{
            pstmt = connection.prepareStatement(sql);
        }
        //Vòng lặp chạy qua danh sách các tham số (args) mà người dùng truyền vào.
        //Gắn từng giá trị trong args vào vị trí tham số (?) trong câu lệnh SQL.
        //Chỉ số của tham số trong SQL bắt đầu từ 1 (không phải 0 như trong Java).
        
        for(int i=0;i<args.length;i++){
            pstmt.setObject(i + 1, args[i]);
        }
        return pstmt;
    }
    /**
     * Thực hiện câu lệnh SQL thao tác (INSERT, UPDATE, DELETE) hoặc thủ tục lưu thao tác dữ liệu
     * @param sql là câu lệnh SQL chứa có thể chứa tham số. Nó có thể là một lời gọi thủ tục lưu
     * @param args là danh sách các giá trị được cung cấp cho các tham số trong câu lệnh sql     * 
     */
    public static void update(String sql, Object...args) {
        try {
            PreparedStatement pstmt = JDBCHelper.getStmt(sql, args);
            try {
                pstmt.executeUpdate();
            } 
            finally{
                pstmt.getConnection().close();
            }
        } 
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Thực hiện câu lệnh SQL truy vấn (SELECT) hoặc thủ tục lưu truy vấn dữ liệu
     * @param sql là câu lệnh SQL chứa có thể chứa tham số. Nó có thể là một lời gọi thủ tục lưu
     * @param args là danh sách các giá trị được cung cấp cho các tham số trong câu lệnh sql
     */  
    
    
    public static ResultSet query(String sql, Object...args) {
        try {
            PreparedStatement pstmt = JDBCHelper.getStmt(sql, args);
            return pstmt.executeQuery();
        } 
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    

   public static ResultSet executeQuery(String sql, Object... args) {
    try {
        PreparedStatement stmt = getStmt(sql, args); // ← dùng đúng tên hàm này
        return stmt.executeQuery();
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
}

    
}
    



