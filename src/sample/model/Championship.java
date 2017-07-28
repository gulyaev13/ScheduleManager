package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Championship {
    private Group allTeamsGroup;
    private SuperGroup conferences;
    private SuperGroup divisions;
    private SuperGroup touchGroups;
    private Group conferencesCheck;
    private Group divisionsCheck;
    private Group touchGroupsCheck;

    private boolean olympicCalendar = false;
    private boolean russianLanguage = true;
    private int gamesWithAll;
    private int gamesInConference;
    private int gamesInDivisions;

    public Championship(){
        allTeamsGroup = new Group();
        conferences = new SuperGroup();
        conferences.setGamesBetweenGroups(false);
        divisions = new SuperGroup();
        divisions.setGamesBetweenGroups(false);
        touchGroups = new SuperGroup();
        conferencesCheck = new Group();
        divisionsCheck = new Group();
        touchGroupsCheck = new Group();
        /*Team[] bobrovTeams = {new Team("Динамо", "Минск"),
                new Team("Динамо", "Рига"),
                new Team("Йокерит", "Хельсинки"),
                new Team("Медвешчак", "Загреб"),
                new Team("СКА", "Санкт-Петербург"),
                new Team("Слован", "Братислава"),
                new Team("Спартак", "Москва")};
        Team[] tarasovTeams = {new Team("Витязь", "Подольск"),
                new Team("Динамо", "Москва"),
                new Team("Локомотив", "Ярославль"),
                new Team("Северсталь", "Череповец"),
                new Team("Сочи", "Сочи"),
                new Team("Торпедо", "Нижний Новгород"),
                new Team("ЦСКА", "Москва")};
        Team[] harlamovTeams = {new Team("Автомобилист", "Екатеринбург"),
                new Team("Ак Барс", "Казань"),
                new Team("Лада", "Тольятти"),
                new Team("Металлург", "Магнитогорск"),
                new Team("Нефтехимик", "Нижнекамск"),
                new Team("Трактор", "Челябинск"),
                new Team("Югра", "Ханты-Мансийск")};
        Team[] chernushevTeams = {new Team("Авангард", "Омск"),
                new Team("Адмирал", "Владивосток"),
                new Team("Амур", "Хабаровск"),
                new Team("Барыс", "Астана"),
                new Team("Металлург", "Новокузнецк"),
                new Team("Салават Юлаев", "Уфа"),
                new Team("Сибирь", "Новосибирск"),
                new Team("Красная Звезда Куньлунь", "Пекин")
        };
        for (Team t : bobrovTeams) {
            allTeamsGroup.addTeam(t);
        }
        for (Team t : tarasovTeams) {
            allTeamsGroup.addTeam(t);
        }
        for (Team t : harlamovTeams) {
            allTeamsGroup.addTeam(t);
        }
        for (Team t : chernushevTeams) {
            allTeamsGroup.addTeam(t);
        }*/
    }

    public void setGamesWithAll(int gamesWithAll) {
        this.gamesWithAll = gamesWithAll;
    }

    public void setGamesInConference(int gamesInConference) {
        this.gamesInConference = gamesInConference;
    }

    public void setGamesInDivisions(int gamesInDivisions) {
        this.gamesInDivisions = gamesInDivisions;
    }

    public void setOlympicCalendar(boolean olympicCalendar) {
        this.olympicCalendar = olympicCalendar;
    }

    public void setRussianLanguage(boolean russianLanguage) {
        this.russianLanguage = russianLanguage;
    }

    public boolean isOlympicCalendar() {
        return olympicCalendar;
    }
    private List<GameTour> getReverseList(List<GameTour> gameTourList){
        List<GameTour> reverseList = new ArrayList<>();
        for (GameTour gameTour : gameTourList){
            reverseList.add(gameTour.getReverse());
        }
        return reverseList;
    }
    private int getMaxOlympicTeams(){
        int i = 1;
        while (i < allTeamsGroup.size()) i *= 2;
        return i;
    }
    private int power(int a, int b){
        int result = 1;
        for(int i = 0; i < b; i++){
            result *= a;
        }
        return result;
    }
    String getTourString(int tourNumber){
        String tourString = (russianLanguage ? "Тур " : "Tour ") + tourNumber;
        if(olympicCalendar){
            int olympicTour = getMaxOlympicTeams() / power(2, tourNumber);
            if(olympicTour > 1){
                tourString += " - 1/" + olympicTour + (russianLanguage ? " Финала" : " of Final");
            } else {
                tourString += russianLanguage ? " - Финал" : " - Final";
            }
        }
        return tourString;
    }
    public List<GameTour> getGameTours(){
        if(olympicCalendar){
           return getOlympicTours();
        }
        return getRoundTours();
    }

    private List<GameTour> getRoundTours(){
        List<GameTour> championship = new ArrayList<>();
        List<GameTour> allTeamsTours;
        List<GameTour> conferenceTours = conferences.getGameTours();
        List<GameTour> divisionTours = divisions.getGameTours();
        if(allTeamsGroup.size() > touchGroups.teamsCount()){
            allTeamsTours = allTeamsGroup.gamesInGroup();
        } else {
            allTeamsTours = touchGroups.getGameTours();
        }
        List<GameTour> allTeamsToursReverse = getReverseList(allTeamsTours);
        List<GameTour> conferenceToursReverse = getReverseList(conferenceTours);
        List<GameTour> divisionToursReverse = getReverseList(divisionTours);
        boolean flag = true;
        while (gamesWithAll > 0 || gamesInConference > 0|| gamesInDivisions > 0){
            if(gamesInDivisions-- > 0){
                championship.addAll(flag ? divisionTours : divisionToursReverse);
            }
            if(gamesWithAll-- > 0){
                championship.addAll(flag ? allTeamsTours : allTeamsToursReverse);
            }
            if(gamesInConference-- > 0){
                championship.addAll(flag ? conferenceTours : conferenceToursReverse);
            }
            flag = !flag;
        }
        for(GameTour gameTour : championship){
            gameTour.getGames().sort(new Comparator<Game>() {
                @Override
                public int compare(Game game1, Game game2) {
                    int resultGame1, resultGame2, result;
                    resultGame1 = conferences.teamGroupCount(game1.getHome());
                    resultGame2 = conferences.teamGroupCount(game2.getHome());
                    result = resultGame1 - resultGame2;
                    if(resultGame1 != -1 && resultGame2 != -1 && result != 0) return result;
                    resultGame1 = divisions.teamGroupCount(game1.getHome());
                    resultGame2 = divisions.teamGroupCount(game2.getHome());
                    result = resultGame1 - resultGame2;
                    if(resultGame1 != -1 && resultGame2 != -1 && result != 0) return result;
                    resultGame1 = touchGroups.teamGroupCount(game1.getHome());
                    resultGame2 = touchGroups.teamGroupCount(game2.getHome());
                    result = resultGame1 - resultGame2;
                    if(resultGame1 != -1 && resultGame2 != -1 && result != 0) return result;
                    return allTeamsGroup.getTeams().indexOf(game1.getHome()) - allTeamsGroup.getTeams().indexOf(game2.getHome());
                }
            });
        }
        return championship;
    }

    private List<GameTour> getOlympicTours(){
        List<GameTour> championship = new ArrayList<>();
        allTeamsGroup.olympicCheatModeOn();
        Group presentGroup = allTeamsGroup;
        for(int tourNumber = 1; presentGroup.getMaxOlympicTeams() > 1; tourNumber++){
            Group nextGroup = new Group();
            nextGroup.setOlympicCheatMode(true);
            GameTour gameTour = new GameTour();
            for(int i = 0; i < presentGroup.size() / 2; i++){
                gameTour.addGame(new Game(presentGroup.getTeam(2 * i), presentGroup.getTeam(2 * i + 1)));
                Team winnerTeam = gameTour.getGame(i).getWinner();
                if(winnerTeam.equals(new Team())){
                    nextGroup.addTeam(new Team());
                } else if(!winnerTeam.isRealTeam() && winnerTeam.equals(new Team("NewFake", "Team"))){
                    Team winnerFakeTeam = new Team("Победитель " + getTourString(tourNumber), "Игра " + (i + 1));
                    winnerFakeTeam.setNameSeparator("");
                    winnerFakeTeam.setCitySeparator(" ");
                    nextGroup.addTeam(winnerFakeTeam);
                } else {
                    nextGroup.addTeam(winnerTeam);
                }
            }
            championship.add(gameTour);
            nextGroup.setOlympicCheatMode(false);
            presentGroup = nextGroup;
        }
        allTeamsGroup.olympicCheatModeOff();
        return championship;
    }
    public ObservableList<Team> getAllTeams(){
        return FXCollections.observableArrayList(allTeamsGroup.getTeams());
    }
    public ObservableList<Team> getConferenceGroup(int index){
        return FXCollections.observableArrayList(conferences.getGroup(index).getTeams());
    }
    public ObservableList<Team> getDivisionGroup(int index){
        return FXCollections.observableArrayList(divisions.getGroup(index).getTeams());
    }
    public ObservableList<Team> getTouchGroup(int index){
        return FXCollections.observableArrayList(touchGroups.getGroup(index).getTeams());
    }
    public List<Team> getAvailableTeams(int superGroupIndex){
        List<Team> teams = new ArrayList<>(allTeamsGroup.getTeams());
        switch (superGroupIndex){
            case 1:
                teams.removeAll(conferencesCheck.getTeams());
                break;
            case 2:
                teams.removeAll(divisionsCheck.getTeams());
                break;
            case 3:
                teams.removeAll(touchGroupsCheck.getTeams());
                break;
        }
        return teams;
    }

    public boolean addGroup(int superGroupIndex){
        switch (superGroupIndex){
            case 1:
                return addConferenceGroup();
            case 2:
                return addDivisionGroup();
            case 3:
                return addTouchGroup();
            default:
                return false;
        }
    }
    private boolean addConferenceGroup(){
        return conferences.addGroup(new Group());
    }
    private boolean addDivisionGroup(){
        return divisions.addGroup(new Group());
    }
    private boolean addTouchGroup(){
        return touchGroups.addGroup(new Group());
    }

    public boolean addTeam(int superGroupIndex, int groupIndex, Team team){
        switch (superGroupIndex){
            case 0:
                return allTeamsGroup.addTeam(team);
            case 1:
                return addConferenceTeam(groupIndex, team);
                //return conferences.getGroup(groupIndex).addTeam(team);
            case 2:
                return addDivisionTeam(groupIndex, team);
                //return divisions.getGroup(groupIndex).addTeam(team);
            case 3:
                return addTouchGroupsTeam(groupIndex, team);
                //return touchGroups.getGroup(groupIndex).addTeam(team);
        }
        return false;
    }
    private boolean addConferenceTeam(int groupIndex, Team team){
            return conferencesCheck.addTeam(team) && conferences.getGroup(groupIndex).addTeam(team);
    }
    private boolean addDivisionTeam(int groupIndex, Team team){
            return divisionsCheck.addTeam(team) && divisions.getGroup(groupIndex).addTeam(team);
    }
    private boolean addTouchGroupsTeam(int groupIndex, Team team){
        return touchGroupsCheck.addTeam(team) && touchGroups.getGroup(groupIndex).addTeam(team);
    }
    public boolean changeTeam(int superGroupIndex, int groupIndex, int teamIndex, Team newTeam){
        Team team = getTeam(superGroupIndex, groupIndex, teamIndex);
        if(team.equals(newTeam)|| allTeamsGroup.getTeams().contains(newTeam)) return false;
        team.change(newTeam);
        return true;
    }
    public Team getTeam(int superGroupIndex, int groupIndex, int teamIndex){
        switch (superGroupIndex){
            case 0:
                return allTeamsGroup.getTeam(teamIndex);
            case 1:
                return conferences.getGroup(groupIndex).getTeam(teamIndex);
            case 2:
                return divisions.getGroup(groupIndex).getTeam(teamIndex);
            case 3:
                return conferences.getGroup(groupIndex).getTeam(teamIndex);
        }
        return new Team("","");
    }


    public void deleteGroup(int superGroupIndex, int groupIndex){
        switch (superGroupIndex){
            case 1:
                conferences.deleteGroup(groupIndex);
                break;
            case 2:
                divisions.deleteGroup(groupIndex);
                break;
            case 3:
                touchGroups.deleteGroup(groupIndex);
                break;
        }
    }

    public void deleteTeam(int superGroupIndex, int groupIndex, int teamIndex){
        switch (superGroupIndex){
            case 0:
                deleteGeneralTeam(teamIndex);
                break;
            case 1:
                deleteConferenceTeam(groupIndex, teamIndex);
                break;
            case 2:
                deleteDivisionTeam(groupIndex, teamIndex);
                break;
            case 3:
                deleteTouchTeam(groupIndex, teamIndex);
                break;
        }
    }

    private void deleteGeneralTeam(int teamIndex){
        Team team = allTeamsGroup.getTeam(teamIndex);
        allTeamsGroup.deleteTeam(teamIndex);
        conferences.deleteTeam(team);
        divisions.deleteTeam(team);
        touchGroups.deleteTeam(team);
    }
    private void deleteConferenceTeam(int groupIndex, int teamIndex){
        Team team = conferences.getGroup(groupIndex).getTeam(teamIndex);
        conferences.getGroup(groupIndex).deleteTeam(teamIndex);
        conferencesCheck.deleteTeam(team);
    }
    private void deleteDivisionTeam(int groupIndex, int teamIndex){
        Team team = divisions.getGroup(groupIndex).getTeam(teamIndex);
        divisions.getGroup(groupIndex).deleteTeam(teamIndex);
        divisionsCheck.deleteTeam(team);
    }
    private void deleteTouchTeam(int groupIndex, int teamIndex){
        Team team = touchGroups.getGroup(groupIndex).getTeam(teamIndex);
        touchGroups.getGroup(groupIndex).deleteTeam(teamIndex);
        touchGroupsCheck.deleteTeam(team);
    }
}
