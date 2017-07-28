package sample.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Таня on 13.02.2017.
 */
public class GroupGameTour{
    private List<GroupGame> groupGames;

    public GroupGameTour(){
        groupGames = new ArrayList<>();
    }

    public GroupGame getGroupGame(int index){
        return groupGames.get(index);
    }
    public int size(){
        return groupGames.size();
    }
    public void addGroupGame(GroupGame groupGame){
        groupGames.add(groupGame);
    }
    public List<GameTour> toGameTour(){
        List<GameTour> gameTours = new ArrayList<>();
        for(GroupGame groupGame : groupGames){
            List<GameTour> groupGameTours = groupGame.getGameTours();
            while (gameTours.size() < groupGameTours.size()){
                gameTours.add(new GameTour());
            }
            for(int i = 0; i < groupGameTours.size(); i++){
                gameTours.get(i).addGames(groupGameTours.get(i));
            }
        }
        return gameTours;
    }
}
