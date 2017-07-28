package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.controller.GroupsOverviewController;
import sample.controller.MainController;
import sample.model.*;

import java.io.IOException;

public class ScheduleManager extends Application {
    private Stage primaryStage;
    private GridPane rootLayout;
    private Championship championship;
    private PDFCreator pdfCreator;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Менеджер расписания");
        championship = new Championship();
        pdfCreator = new PDFCreator();
        pdfCreator.setChampionship(championship);
        initRootLayout();
        showGroupsOverview();
    }
    public static void main(String[] args) {
        launch(args);
    }

    private void initRootLayout(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/RootLayout.fxml"));
            rootLayout = loader.load();
            MainController mainController = loader.getController();
            mainController.setChampionship(championship);
            mainController.setPrimaryStage(primaryStage);
            mainController.setPdfCreator(pdfCreator);
            Scene scene = new Scene(rootLayout);
            primaryStage.setMinWidth(676);//676 690
            primaryStage.setMinHeight(509);//504
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image("/sample/resources/images/icon.png"));
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void showGroupsOverview(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/GroupsOverview.fxml"));
            GridPane gridPane = loader.load();
            rootLayout.add(gridPane,0,1);
            GroupsOverviewController controller = loader.getController();
            controller.setChampionship(this.championship);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
