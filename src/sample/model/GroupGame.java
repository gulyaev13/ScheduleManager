package sample.model;

import java.util.List;

/**
 * Created by Таня on 13.02.2017.
 */
public class GroupGame{
    private Group home;
    private Group guest;

    public GroupGame(Group home, Group guest){
        this.home = home;
        this.guest = guest;
    }
    public Group getHomeGroup(){ return home; }
    public Group getGuestGroup(){ return guest; }
    public List<GameTour> getGameTours(){
        if(home.size() == 0) return guest.gamesInGroup();
        if(guest.size() == 0) return home.gamesInGroup();
        return home.gamesWithGroup(guest);
    }
}
