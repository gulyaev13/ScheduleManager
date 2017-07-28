package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.AlertMessage;
import sample.model.Championship;
import sample.model.PDFCreator;

import java.io.IOException;

public class MainController {
    @FXML
    private Button createCalendarButton;

    private Championship championship;
    private Stage primaryStage;
    private PDFCreator pdfCreator;

    public void setPdfCreator(PDFCreator pdfCreator) {
        this.pdfCreator = pdfCreator;
    }

    public void setChampionship(Championship championship){
        this.championship = championship;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public MainController(){}

    @FXML
    public void initialize(){    }

    @FXML
    private void createCalendarButtonPressed() throws Exception {
        if(championship.getAllTeams().size() > 1){
            if(showCalendarCreateWindow()){
                pdfCreator.setAllTeams(championship.getAllTeams());
                try{
                    pdfCreator.createPDF(championship.getGameTours());
                    Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                    dialog.setTitle("Информация");
                    dialog.setHeaderText("Файл с расписанием создан");
                    dialog.setContentText("Путь к файлу:\n" + pdfCreator.getFilePath());
                    ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(
                            new Image("/sample/resources/images/icon.png"));
                    dialog.showAndWait();
                } catch (Exception e){
                    AlertMessage.showAlertMessage("Неправильный путь к файлу или он открыт в другой программе!");
                }

            }
        } else {
            AlertMessage.showAlertMessage("Недостаточное количество команд!");
        }
    }

    @FXML
    private void settingsItemPressed(){
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Настройки");
        dialog.setHeaderText("Зачем они?");
        dialog.setContentText("Всё и так хорошо работает!");
        dialog.setGraphic(null);
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(
                new Image("/sample/resources/images/icon.png"));
        dialog.showAndWait();
    }
    @FXML
    private void aboutItemPressed(){
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("О программе");
        dialog.setHeaderText("Менеджер расписания\nАвтор: Илья Гуляев");
        dialog.setContentText("Программа предназначена для создания расписания турниров " +
                "по круговой и олимпийской системам.\n\nРасписание сохраняется в файл PDF.\n\n" +
                "Добавление связок позволяет группировать команды, которые " +
                "вместе играют выездные матчи против другой связки.\nВсе команды должны быть сгруппированы в связки, " +
                "иначе будет составлено расписание без группировки команд.");
        dialog.setGraphic(null);
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(
                new Image("/sample/resources/images/icon.png"));
        dialog.showAndWait();
    }

    private boolean showCalendarCreateWindow(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/TimeTableSettings.fxml"));
            AnchorPane anchorPane = loader.load();
            TimeTableSettingsController controller = loader.getController();
            controller.setChampionship(championship);
            Stage dialogStage = new Stage();
            controller.setDialogStage(dialogStage);
            controller.setPdfCreator(pdfCreator);
            dialogStage.setTitle("Создание расписания");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setResizable(false);
            dialogStage.getIcons().add(new Image("/sample/resources/images/icon.png"));

            Scene scene = new Scene(anchorPane);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
