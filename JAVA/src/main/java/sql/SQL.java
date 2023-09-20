package sql;

import java.sql.*;

public class SQL {
    private Connection connection;
    private Statement statement;
    private final String urlStr = "jdbc:mysql://localhost:3306/my_db?serverTimezone=Asia/Seoul";
    private final String sqlID = "root";
    private final String sqlPW = "1234";
    public void connect(){
        try{
            connection = DriverManager.getConnection(urlStr, sqlID, sqlPW);
            statement = connection.createStatement();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void select(){
        try{
            ResultSet resultSet = statement.executeQuery("SELECT * FROM student");
            while(resultSet.next()){
                for(int i=0;i<resultSet.getMetaData().getColumnCount();i++){
                    System.out.println(resultSet.getMetaData().getCatalogName(i+1));
                }
            }
        } catch (Exception sqlException){
            sqlException.printStackTrace();
        } finally {
            try{
                if(statement != null) statement.close();
                if(connection != null) connection.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
