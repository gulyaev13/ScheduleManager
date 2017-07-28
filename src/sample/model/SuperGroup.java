package sample.model;

import java.util.ArrayList;
import java.util.List;

public class SuperGroup{
    private List<Group> groups;
    private boolean gamesBetweenGroups = true;

    public SuperGroup(){ groups = new ArrayList<>(); }

    public SuperGroup(List<Group> groups){
        groups = new ArrayList<>();
        this.groups.addAll(groups);
    }

    public void deleteGroup(Group group){ groups.remove(group); }
    public void deleteGroup(int index){ groups.remove(index); }
    public boolean addGroup(Group group){
        if(!this.groups.contains(group)) {
            groups.add(group);
            return true;
        } else {
            return false;
        }
    }
    public void deleteTeam(Team team){
        for(Group group : groups){
            group.deleteTeam(team);
        }
    }
    public Group getGroup(int index){ return groups.get(index); }
    public List<Group> getGroups(){ return groups; }
    public int size(){ return groups.size(); }
    public int teamsCount(){
        int i = 0;
        for (Group group : groups){
            i += group.size();
        }
        return i;
    }
    public int teamGroupCount(Team team){
        int result = -1;
        for(int i = 0; i < groups.size(); i++){
            if(groups.get(i).getTeams().contains(team))
                result = i;
        }
        return result;
    }
    public void setGamesBetweenGroups(boolean flag){
        gamesBetweenGroups = flag;
    }

    public List<GroupGameTour> getGroupGameTours(){
        List<GroupGameTour> groupGameTours = new ArrayList<>();
        boolean addEmptyGroup = false;
        if(gamesBetweenGroups){
            if(this.size() == 1){
                groupGameTours.add(new GroupGameTour());
                groupGameTours.get(0).addGroupGame(new GroupGame(groups.get(0), new Group()));
                return groupGameTours;
            }
            if(this.size() == 2) {
                while (groupGameTours.size() < 2){
                    groupGameTours.add(new GroupGameTour());
                }
                groupGameTours.get(0).addGroupGame(new GroupGame(groups.get(0), groups.get(1)));
                groupGameTours.get(1).addGroupGame(new GroupGame(new Group(), groups.get(1)));
                groupGameTours.get(1).addGroupGame(new GroupGame(groups.get(0), new Group()));
                return groupGameTours;
            }
            if(this.size() % 2 != 0){
                this.addGroup(new Group());
                addEmptyGroup = true;
            }
            groupGameTours.add(new GroupGameTour());
            for(int i = 0; i < this.size() / 2; i++) {
                groupGameTours.get(0).addGroupGame(new GroupGame(groups.get(i), groups.get(groups.size() - 1 - i)));
            }
            for(int i = 1; i < this.size() - 1; i++){
                groupGameTours.add(new GroupGameTour());
                if(i % 2 != 0){
                    groupGameTours.get(i).addGroupGame(
                            new GroupGame(groupGameTours.get(i - 1).getGroupGame(0).getGuestGroup(),
                                    groupGameTours.get(i - 1).getGroupGame(groupGameTours.get(i - 1).size() - 1).getGuestGroup())
                    );
                    for (int j = groupGameTours.get(0).size() - 1; j > 1; j--){
                        groupGameTours.get(i).addGroupGame(
                                new GroupGame(groupGameTours.get(i - 1).getGroupGame(j - 1).getGuestGroup(),
                                        groupGameTours.get(i - 1).getGroupGame(j).getHomeGroup())
                        );
                    }
                    groupGameTours.get(i).addGroupGame(
                            new GroupGame(groupGameTours.get(i - 1).getGroupGame(0).getHomeGroup(),
                                    groupGameTours.get(i - 1).getGroupGame(1).getHomeGroup())
                    );
                } else {
                    groupGameTours.get(i).addGroupGame(
                            new GroupGame(groupGameTours.get(i - 1).getGroupGame(groupGameTours.get(i - 1).size() - 1).getGuestGroup(),
                                    groupGameTours.get(i - 1).getGroupGame(0).getHomeGroup())
                    );
                    for (int j = groupGameTours.get(0).size() - 1; j > 0; j--){
                        groupGameTours.get(i).addGroupGame(
                                new GroupGame(groupGameTours.get(i - 1).getGroupGame(j - 1).getGuestGroup(),
                                        groupGameTours.get(i - 1).getGroupGame(j).getHomeGroup())
                        );
                    }
                }
            }
        }
        if(!addEmptyGroup){
            groupGameTours.add(new GroupGameTour());
            for(int i = 0; i < this.size(); i++){
                groupGameTours.get(groupGameTours.size() - 1).addGroupGame(
                        new GroupGame(groups.get(i), new Group())
                );
            }
        }
        this.deleteGroup(new Group());
        return groupGameTours;
    }
    public List<GameTour> getGameTours(){
        List<GroupGameTour> groupGameTours = getGroupGameTours();
        List<GameTour> gameTours = new ArrayList<>();
        for(GroupGameTour groupGameTour : groupGameTours){
            gameTours.addAll(groupGameTour.toGameTour());
        }
        return gameTours;
    }
}
