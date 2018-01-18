package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.com.fuad.database.TodoData;

import java.io.IOException;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Fuad's Bucket List");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        try
        {
            TodoData.getOnlyInstance().saveToDoList();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() throws Exception {
        try
        {
            TodoData.getOnlyInstance().laodToDoList();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
