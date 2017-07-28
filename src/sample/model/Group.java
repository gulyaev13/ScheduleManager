package sample.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private List<Team> teams;
    private boolean olympicCheatMode = false;

    public Group(){
        teams = new ArrayList<>();
    }

    public Group(Team[] teams){
        this.teams = new ArrayList<>();
        for(Team t:teams) this.teams.add(t);
    }
    public Group(List<Team> teams){
        this.teams = new ArrayList<>(teams);
    }

    public void olympicCheatModeOn() {
        while (size() < getMaxOlympicTeams()){
            teams.add(new Team());
        }
    }
    public void setOlympicCheatMode(boolean olympicCheatMode){
        this.olympicCheatMode = olympicCheatMode;
    }
    public void olympicCheatModeOff(){
        while(teams.contains(new Team())){
            teams.remove(new Team());
        }
    }
    public int getMaxOlympicTeams(){
        int i = 1;
        while (i < size()) i *= 2;
        return i;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj.getClass() != this.getClass())
            return false;
        Group group = (Group)obj;
        if (this.size() != group.size())
            return false;
        for(int i = 0; i < this.size(); i++){
            if(!teams.get(i).equals(group.getTeam(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void deleteTeam(Team team){ teams.remove(team); }
    public void deleteTeam(int index){ teams.remove(index); }
    public boolean addTeam(Team team){
        if(olympicCheatMode){
            teams.add(team);
            return true;
        } else {
            if(!this.teams.contains(team)) {
                teams.add(team);
                return true;
            } else {
                return false;
            }
        }
    }
    public Team getTeam(int index){ return teams.get(index); }
    public List<Team> getTeams(){ return teams; }
    public int size(){ return teams.size(); }
    public List<GameTour> gamesWithGroup(Group teams){
        List<GameTour> gameTours = new ArrayList<>();
        Group group1, group2;
        boolean swapFlag = false;
        if(this.size() >= teams.size()){
            group1 = this;
            group2 = teams;
        } else {
            group2 = this;
            group1 = teams;
            swapFlag = true;
        }
        for(int i = 0; i < group1.size(); i++){
            gameTours.add(i, new GameTour());
            for(int j = 0; j < group1.size(); j++){
                int index = (i + j) % group1.size();
                Team team2 = index < group2.size() ? group2.getTeam(index) : null;
                if (team2 != null){
                    Team team1 = group1.getTeam(j);
                    Game game = swapFlag ? new Game(team2, team1) : new Game(team1, team2);
                    gameTours.get(i).addGame(game);
                }
            }
        }
        return gameTours;
    }

    public List<GameTour> gamesInGroup(){
        List<GameTour> gameTours = new ArrayList<>();
        if(this.size() == 2) {
            gameTours.add(new GameTour());
            gameTours.get(0).addGame(new Game(teams.get(0), teams.get(1)));
            return gameTours;
        }
        if(this.size() % 2 != 0)
            this.addTeam(new Team("", ""));
        gameTours.add(new GameTour());
        for(int i = 0; i < this.size() / 2; i++) {
            gameTours.get(0).addGame(new Game(teams.get(i), teams.get(teams.size() - 1 - i)));
        }
        for(int i = 1; i < this.size() - 1; i++){
            gameTours.add(new GameTour());
            if(i % 2 != 0){
                gameTours.get(i).addGame(
                        new Game(gameTours.get(i - 1).getGame(0).getGuest(),
                                gameTours.get(i - 1).getGame(gameTours.get(i - 1).size() - 1).getGuest())
                );
                for (int j = gameTours.get(0).size() - 1; j > 1; j--){
                    gameTours.get(i).addGame(
                            new Game(gameTours.get(i - 1).getGame(j - 1).getGuest(),
                                    gameTours.get(i - 1).getGame(j).getHome())
                    );
                }
                gameTours.get(i).addGame(
                        new Game(gameTours.get(i - 1).getGame(0).getHome(),
                                gameTours.get(i - 1).getGame(1).getHome())
                );
            } else {
                gameTours.get(i).addGame(
                        new Game(gameTours.get(i - 1).getGame(gameTours.get(i - 1).size() - 1).getGuest(),
                                gameTours.get(i - 1).getGame(0).getHome())
                );
                for (int j = gameTours.get(0).size() - 1; j > 0; j--){
                    gameTours.get(i).addGame(
                            new Game(gameTours.get(i - 1).getGame(j - 1).getGuest(),
                                    gameTours.get(i - 1).getGame(j).getHome())
                    );
                }
            }
        }
        this.deleteTeam(new Team("",""));
        return gameTours;
    }
}
