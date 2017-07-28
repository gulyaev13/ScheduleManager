package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.AlertMessage;
import sample.model.Championship;
import sample.model.PDFCreator;

import java.io.File;

public class TimeTableSettingsController {
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextArea championshipNameArea;
    @FXML
    private TextField gamesWithAllField;
    @FXML
    private TextField gamesInConferenceField;
    @FXML
    private TextField gamesInDivisionsField;
    @FXML
    private TextField filePathField;
    @FXML
    private MenuButton menuButton;

    private boolean okClicked = false;
    private PDFCreator pdfCreator;
    private Championship championship;
    private Stage dialogStage;

    public TimeTableSettingsController(){}

    @FXML
    private void initialize(){
        filePathField.setEditable(false);
    }

    void setPdfCreator(PDFCreator pdfCreator) {
        this.pdfCreator = pdfCreator;
    }

    void setChampionship(Championship championship){
        this.championship = championship;
    }

    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    boolean isOkClicked(){
        return okClicked;
    }

    @FXML
    private void setRoundSystem(){
        menuButton.setText("Круговая система");
        championship.setOlympicCalendar(false);
        gamesWithAllField.setDisable(false);
        gamesInConferenceField.setDisable(false);
        gamesInDivisionsField.setDisable(false);
    }
    @FXML
    private void setOlympicSystem(){
        menuButton.setText("Олимпийская система");
        championship.setOlympicCalendar(true);
        gamesWithAllField.setDisable(true);
        gamesInConferenceField.setDisable(true);
        gamesInDivisionsField.setDisable(true);
    }
    @FXML
    private void okButtonPressed(){
        if(isInputValid()){
            okClicked = true;
            dialogStage.close();
        }
    }
    @FXML
    private void cancelButtonPressed(){
        okClicked = false;
        dialogStage.close();
    }
    @FXML
    private void browseButtonPressed(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл \"PDF\"", "*.pdf"));
        if(filePathField.getText().isEmpty()){
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        } else {
            File oldChooseFile = new File(filePathField.getText());
            fileChooser.setInitialDirectory(oldChooseFile.getParentFile());
            fileChooser.setInitialFileName(oldChooseFile.getName());
        }
        fileChooser.setTitle("Сохранить как");
        File file = fileChooser.showSaveDialog(dialogStage);
        if(file != null){
            filePathField.setText(file.getAbsolutePath());
        }
    }
    private boolean isInputValid(){
        String message = "";
        if(!championship.isOlympicCalendar()){
            if(gamesWithAllField.getText().trim().isEmpty() || gamesInConferenceField.getText().trim().isEmpty() ||
                    gamesInDivisionsField.getText().trim().isEmpty() || filePathField.getText().trim().isEmpty() ||
                    championshipNameArea.getText().trim().isEmpty()){
                message += "Не все поля были заполнены!";
            } else if(menuButton.getText().equals("Выберите систему")){
                message += "Система турнира не выбрана!";
            } else {
                try{
                    int result = Integer.parseUnsignedInt(gamesWithAllField.getText());
                    championship.setGamesWithAll(result);
                    if(result == 0) message += "Поле 1: Введено не целое положительное число!\n";
                    if(result > 10) message += "Поле 1: Количество игр не может быть больше 10\n";
                } catch (NumberFormatException e){
                    message += "Поле 1: Введено не целое положительное число!\n";
                }
                try{
                    int result = Integer.parseUnsignedInt(gamesInConferenceField.getText());
                    championship.setGamesInConference(result);
                    if(result == 0) message += "Поле 2: Введено не целое положительное число!\n";
                    if(result > 10) message += "Поле 2: Количество игр не может быть больше 10\n";
                } catch (NumberFormatException e){
                    message += "Поле 2: Введено не целое положительное число!\n";
                }
                try{
                    int result = Integer.parseUnsignedInt(gamesInDivisionsField.getText());
                    championship.setGamesInDivisions(result);
                    if(result == 0) message += "Поле 2: Введено не целое положительное число!\n";
                    if(result > 10) message += "Поле 2: Количество игр не может быть больше 10\n";
                } catch (NumberFormatException e){
                    message += "Поле 3: Введено не целое положительное число!\n";
                }
            }
        } else {
            if(filePathField.getText().trim().isEmpty() || championshipNameArea.getText().trim().isEmpty()){
                message += "Не все поля были заполнены!";
            } else if(menuButton.getText().equals("Выберите систему")){
                message += "Система турнира не выбрана!";
            }
        }

        File file = new File(filePathField.getText());
        File sameFile = new File(filePathField.getText());
        if(file.exists() && !file.renameTo(sameFile)){
            message += "Неправильный путь к файлу или он открыт в другой программе!";
        }
        if(message.isEmpty()){
            pdfCreator.setFilePath(filePathField.getText());
            pdfCreator.setChampionshipName(championshipNameArea.getText());
            return true;
        }
        AlertMessage.showAlertMessage(message);
        return false;
    }
}
