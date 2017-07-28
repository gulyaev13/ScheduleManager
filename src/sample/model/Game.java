package sample.model;

public class Game {
    private Team home;
    private Team guest;

    public Game(Team home, Team guest){
        this.home = home;
        this.guest = guest;
    }

    public Team getHome(){ return home; }
    public Team getGuest(){ return guest; }
    public Game getReverse(){ return new Game(guest, home); }
    public void reverse(){
        Team var = home;
        home = guest;
        guest = var;
    }
    public Team getWinner(){
        Team newFakeTeam = new Team("NewFake", "Team");
        newFakeTeam.setCitySeparator( "");
        if(home.equals(new Team()) && guest.equals(new Team())) return new Team();//E-E
        if(home.equals(new Team())) return guest;
        if(guest.equals(new Team())) return home;
        return newFakeTeam;
    }
    @Override
    public String toString() {
        if(home.equals(new Team("","")) || guest.equals(new Team("","")))
            return "";
        return home + " - " + guest + "\r\n";
    }
}
