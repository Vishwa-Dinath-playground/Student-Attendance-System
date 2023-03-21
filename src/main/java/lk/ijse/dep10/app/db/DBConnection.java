package lk.ijse.dep10.app.db;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;

    private final Connection connection;

    private DBConnection(){
        File file = new File("application.properties");
        Properties properties = new Properties();
        try {
            FileReader fr = new FileReader(file);
            properties.load(fr);
            fr.close();
            String host = properties.getProperty("mysql.host");
            String port = properties.getProperty("mysql.port");
            String database = properties.getProperty("mysql.database");
            String username = properties.getProperty("mysql.username");
            String password = properties.getProperty("mysql.password");

            String str = "jdbc:mysql://"+host+":"+port+"/"+database+"?createDatabaseIfNotExist=true&allowMultiQueries=true";
            connection = DriverManager.getConnection(str, username, password);

        }catch (FileNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Configuration File does not exist").showAndWait();
            throw new RuntimeException(e);
        }catch (IOException e){
            new Alert(Alert.AlertType.ERROR, "Failed to read configurations");
            throw new RuntimeException(e);
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR, "Failed to obtain the database connection, Please try again. If the problem persist please contact the Technical team").showAndWait();
            throw new RuntimeException(e);
        }
    }

    public static DBConnection getDbConnection() {
        return (dbConnection==null)?dbConnection=new DBConnection():dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
