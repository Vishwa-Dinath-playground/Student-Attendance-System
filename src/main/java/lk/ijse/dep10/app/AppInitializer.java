package lk.ijse.dep10.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                System.out.println("connection is about to close ");
                if (DBConnection.getDbConnection().getConnection() !=null && !DBConnection.getDbConnection().getConnection().isClosed()) {
                    DBConnection.getDbConnection().getConnection().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        generateTables();
        primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/MainScene.fxml")).load()));
        primaryStage.setTitle("Student Attendance");
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    private void generateTables() {
        Connection connection = DBConnection.getDbConnection().getConnection();
        try {
            Statement stm = connection.createStatement();
            InputStream is = getClass().getResourceAsStream("/schema.sql");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuilder dbScript = new StringBuilder();
            while((line=br.readLine())!=null){
                dbScript.append(line).append("\n");
            }
            stm.execute(dbScript.toString());
            System.out.println(dbScript);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to generate Tables").showAndWait();
        }
    }
}
