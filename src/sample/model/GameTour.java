package sample.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Таня on 13.02.2017.
 */
public class GameTour{
    private List<Game> games;

    public GameTour(){
        games = new ArrayList<>();
    }

    public Game getGame(int index){
        return games.get(index);
    }
    public List<Game> getGames(){ return games; }
    public int size(){
        return games.size();
    }
    public void addGame(Game game){
        games.add(game);
    }
    public void addGames(GameTour gameTour){
        games.addAll(gameTour.getGames());
    }
    public GameTour getReverse(){
        GameTour gameTour = new GameTour();
        for(Game game : games){
            gameTour.addGame(game.getReverse());
        }
        return gameTour;
    }
    @Override
    public String toString() {
        String str = "";
        for(Game game : games){
            str += game;
        }
        return str;
    }
}
