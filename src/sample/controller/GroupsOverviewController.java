package sample.controller;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import sample.model.*;
import javafx.fxml.FXML;

import java.util.List;
import java.util.Optional;

public class GroupsOverviewController {
    @FXML
    private TableView<Team> teamsTable;
    @FXML
    private TableColumn<Team, String> nameColomn;
    @FXML
    private TableColumn<Team, String> cityColomn;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private Label headerLabel;
    @FXML
    private Button addGroupButton;
    @FXML
    private Button deleteGroupButton;
    @FXML
    private Button addTeamButton;
    @FXML
    private Button changeTeamButton;
    @FXML
    private Button deleteTeamButton;

    private TreeItem<String> allTeams;

    private TreeItem<String> conferenceTeams;

    private TreeItem<String> divisionTeams;

    private TreeItem<String> groupTeams;

    private Championship championship;
    public GroupsOverviewController(){

    }
    @FXML
    private void initialize(){
        treeView.setRoot(fillTreeView());
        headerLabel.setText("");
        nameColomn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());
        cityColomn.setCellValueFactory(cellData->cellData.getValue().getCityProperty());
    }
    @FXML
    private void treeClicked(){
        int superGroupKey = getSuperGroupChoose();
        Pair<Integer, Integer> pair = getGroupChoose();
        if(pair.getKey() < -1) {
            addGroupButton.setVisible(false);
            deleteGroupButton.setVisible(false);
            showTeamButtons(false);
            teamsTable.setItems(FXCollections.observableArrayList());
            headerLabel.setText("");
            return;
        }
        if(superGroupKey == 0){
            addGroupButton.setVisible(false);
            deleteGroupButton.setVisible(false);
            teamsTable.setItems(championship.getAllTeams());
            showTeamButtons(true);
            headerLabel.setText("Полный список команд");
            return;
        } else {
            addGroupButton.setVisible(true);
            deleteGroupButton.setVisible(true);
        }
        switch (superGroupKey){
            case 1:
                addGroupButton.setText("Добавить конференцию");
                break;
            case 2:
                addGroupButton.setText("Добавить дивизион");
                break;
            case 3:
                addGroupButton.setText("Добавить связку");
                break;
        }
        headerLabel.setText((String)((TreeItem)treeView.getSelectionModel().getSelectedItem()).getValue());
        showTeamButtons(true);
        switch (pair.getKey()){
            case 1:
                teamsTable.setItems(championship.getConferenceGroup(pair.getValue()));
                break;
            case 2:
                teamsTable.setItems(championship.getDivisionGroup(pair.getValue()));
                break;
            case 3:
                teamsTable.setItems(championship.getTouchGroup(pair.getValue()));
                break;
            default:
                teamsTable.setItems(FXCollections.observableArrayList());
                headerLabel.setText("");
                deleteGroupButton.setVisible(false);
                showTeamButtons(false);
        }
    }
    @FXML
    private void addGroupButtonPressed(){
        String title = "";
        String message = "";
        TextInputDialog dialog = new TextInputDialog();
        int superGroupKey = getSuperGroupChoose();
        switch (superGroupKey){
            case 1:
                title = "конференции";
                break;
            case 2:
                title = "дивизиона";
                break;
            case 3:
                title = "связки";
                break;
        }
        dialog.setTitle("Добавление " + title);
        dialog.setHeaderText("");
        dialog.setContentText("Название:");
        dialog.setGraphic(null);
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(
                new Image("/sample/resources/images/icon.png"));
        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().clear();
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            if(!result.get().trim().isEmpty()){
                if(!isTreeItemValid(superGroupKey, result.get())){
                    message += getGroupName(superGroupKey) + " с таким именем уже существует!";
                } else if(!championship.addGroup(superGroupKey)){
                    switch (superGroupKey){
                        case 1:
                            message += "Нельзя добавть пустую конференцию!\nСначала заполните предыдущие конференции.";
                            break;
                        case 2:
                            message += "Нельзя добавть пустой дивизион!\nСначала заполните предыдущие дивизионы.";
                            break;
                        case 3:
                            message += "Нельзя добавть пустую связку!\nСначала заполните предыдущие связки.";
                            break;
                    }
                } else{
                    addTreeItem(superGroupKey, result.get());
                }
            } else {
                message += "Не все поля были заполнены!";
            }
            if(!message.isEmpty()){
                AlertMessage.showAlertMessage(message);
            }
        }
    }
    @FXML
    private void deleteGroupButtonPressed(){
        TreeItem treeItem = treeView.getSelectionModel().getSelectedItem();
        Pair<Integer, Integer> pair = getGroupChoose();
        championship.deleteGroup(pair.getKey(), pair.getValue());
        treeItem.getParent().getChildren().remove(treeItem);
        treeClicked();
    }
    @FXML
    private void addTeamButtonPressed(){
        String message = "";
        Team team;
        Pair <Integer, Integer> groupChoose = getGroupChoose();
        if(groupChoose.getKey() > 0){
            team = chooseTeam();
        } else {
            team = createTeam();
        }

        if(team == null) return;
        if(team.equals(new Team(""," "))){
            message += "Нет свободных команд!";
        } else if(team.equals(new Team("",""))){
            message += "Не все поля были заполнены!";
        } else {
            boolean addResult = championship.addTeam(groupChoose.getKey(), groupChoose.getValue(), team);
            if(addResult){
                treeClicked();
            } else {
                message += "Такая команда уже существует!";
            }
        }
        if(!message.equals("")){
            AlertMessage.showAlertMessage(message);
        }
    }
    @FXML
    private void changeTeamButtonPressed(){
        String message = "";
        int teamIndex = teamsTable.getSelectionModel().getSelectedIndex();
        if(teamIndex >= 0){
            Pair <Integer, Integer> groupChoose = getGroupChoose();
            Team team = createTeam(championship.getTeam(groupChoose.getKey(),groupChoose.getValue(), teamIndex));
            if(team == null) return;
            if(team.equals(new Team("",""))){
                message += "Не все поля были заполнены!";
            } else {
                boolean changeResult = championship.changeTeam(groupChoose.getKey(), groupChoose.getValue(), teamIndex, team);
                if(changeResult){
                    treeClicked();
                    teamsTable.refresh();
                } else {
                    message += "Такая команда уже существует!";
                }
            }
        } else {
            message += "Не выбрана команда!";
        }
        if(!message.equals("")){
            AlertMessage.showAlertMessage(message);
        }
    }
    @FXML
    private void deleteTeamButtonPressed(){
        int teamIndex = teamsTable.getSelectionModel().getSelectedIndex();
        if(teamIndex >= 0){
            Pair<Integer, Integer> pair = getGroupChoose();
            championship.deleteTeam(pair.getKey(), pair.getValue(), teamIndex);
            treeClicked();
        } else{
            AlertMessage.showAlertMessage("Команда для удаления не выбрана!");
        }
    }

    public void setChampionship(Championship championship){
        this.championship = championship;
    }

    private TreeItem<String> fillTreeView(){
        TreeItem<String> rootItem = new TreeItem<>("Команды");
        allTeams = new TreeItem<>("Все команды");
        conferenceTeams = new TreeItem<>("Конференции");
        divisionTeams = new TreeItem<>("Дивизионы");
        groupTeams = new TreeItem<>("Связки");
        rootItem.getChildren().add(allTeams);
        rootItem.getChildren().add(conferenceTeams);
        rootItem.getChildren().add(divisionTeams);
        rootItem.getChildren().add(groupTeams);
        return rootItem;
    }

    private Pair<Integer, Integer> getGroupChoose(){
        int treeIndex  = treeView.getSelectionModel().getSelectedIndex();
        int allTeamsRow = treeView.getRow(allTeams);
        int conferenceRow = treeView.getRow(conferenceTeams);
        int divisionRow = treeView.getRow(divisionTeams);
        int groupRow = treeView.getRow(groupTeams);
        if(treeIndex < 1){
            return new Pair<>(-2, -2);
        } else if(treeIndex == allTeamsRow){
            return new Pair<>(0, 0);
        } else if(treeIndex > conferenceRow && treeIndex < divisionRow){
            return new Pair<>(1, treeIndex - conferenceRow - 1);
        } else if(treeIndex > divisionRow && treeIndex < groupRow){
            return new Pair<>(2, treeIndex - divisionRow - 1);
        } else if(treeIndex > groupRow){
            return new Pair<>(3, treeIndex - groupRow - 1);
        }
        return new Pair<>(-1, -1);
    }

    private int getSuperGroupChoose(){
        int treeIndex  = treeView.getSelectionModel().getSelectedIndex();
        int allTeamsRow = treeView.getRow(allTeams);
        int conferenceRow = treeView.getRow(conferenceTeams);
        int divisionRow = treeView.getRow(divisionTeams);
        int groupRow = treeView.getRow(groupTeams);
        if(treeIndex == allTeamsRow){
            return 0;
        } else if(treeIndex >= conferenceRow && treeIndex < divisionRow){
            return 1;
        } else if(treeIndex >= divisionRow && treeIndex < groupRow){
            return 2;
        } else if(treeIndex >= groupRow){
            return 3;
        }
        return -1;
    }

    private void showTeamButtons(boolean state){
        addTeamButton.setVisible(state);
        changeTeamButton.setVisible(state);
        deleteTeamButton.setVisible(state);
    }
    private boolean isTreeItemValid(int superGroupIndex, String groupName){
        List<TreeItem<String>> treeItemList;
        switch (superGroupIndex){
            case 1:
                treeItemList = conferenceTeams.getChildren();
                break;
            case 2:
                treeItemList = divisionTeams.getChildren();
                break;
            case 3:
                treeItemList = groupTeams.getChildren();
                break;
            default:
                return false;
        }
        for(TreeItem<String> treeItem : treeItemList){
            if(treeItem.getValue().equals(groupName)){
                return false;
            }
        }
        return true;
    }
    private void addTreeItem(int superGroupIndex, String groupName){
        TreeItem<String> item = new TreeItem<>(groupName);
        switch (superGroupIndex){
            case 1:
                conferenceTeams.getChildren().add(item);
                break;
            case 2:
                divisionTeams.getChildren().add(item);
                break;
            case 3:
                groupTeams.getChildren().add(item);
                break;
        }
    }
    private String getGroupName(int superGroupIndex){
        switch (superGroupIndex){
            case 1:
                return "Конференция";
            case 2:
                return "Дивизион";
            case 3:
                return "Связка";
        }
        return "";
    }

    private Team createTeam(){ return createTeam(null); }
    private Team createTeam(Team team){
        String title;
        String buttonText;
        String nameText = "";
        String cityText = "";

        if(team == null){
            title = "Добавление команды";
            buttonText = "Добавить";
        } else {
            title = "Изменение команды";
            buttonText = "Изменить";
            nameText = team.getName();
            cityText = team.getCity();
        }
        Dialog<Pair<String,String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        ButtonType addButtonType = new ButtonType(buttonText, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);
        ((Stage)dialog.getDialogPane().getScene().getWindow()).getIcons().add(
                new Image("/sample/resources/images/icon.png"));
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField name = new TextField();
        TextField city = new TextField();
        name.setText(nameText);
        city.setText(cityText);

        grid.add(new Label("Название"), 0,0);
        grid.add(name, 1, 0);
        grid.add(new Label("Город"), 0,1);
        grid.add(city, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(name.getText(), city.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if(!result.isPresent()){
            return null;
        }
        Pair<String, String> pair = result.get();
        if(!pair.getKey().trim().equals("") && !pair.getValue().trim().equals("")){
            return new Team(pair.getKey(), pair.getValue());
        } else {
            return new Team("","");
        }
    }
    private Team chooseTeam(){
        int superGroupChoose = getSuperGroupChoose();
        List<Team> teams = championship.getAvailableTeams(superGroupChoose);
        if(teams.size() < 1) return new Team("", " ");
        ChoiceDialog<Team> dialog = new ChoiceDialog<>(null, teams);
        dialog.setTitle("Выбор команды");
        dialog.setHeaderText(null);
        dialog.setContentText("Выберите команду:");
        Optional<Team> result = dialog.showAndWait();
        if(result.isPresent()){
            return result.get();
        }
        return null;
    }
}