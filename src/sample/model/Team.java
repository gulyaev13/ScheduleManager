package sample.model;

import javafx.beans.property.SimpleStringProperty;

public class Team {
    private String name;
    private String city;
    private String nameSeparator;
    private String citySeparator;

    public Team(String name, String city){
        this.name = name;
        this.city = city;
        nameSeparator = "\"";
        citySeparator =  " г.";
    }
    public Team(){
        this.name = "";
        this.city = "";
        nameSeparator = "";
        citySeparator =  " ";
    }
    public boolean isRealTeam(){
        return !citySeparator.trim().isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj.getClass() != this.getClass())
            return false;
        Team team = (Team)obj;
        String str1 = team.name + team.city;
        String str = this.name + this.city;
        return str.equals(str1);
    }

    @Override
    public int hashCode() {
        return (this.name + this.city).intern().hashCode();
    }

    @Override
    public String toString() {
        return nameSeparator + name + nameSeparator + citySeparator + city;
    }

    public SimpleStringProperty getNameProperty(){
        return new SimpleStringProperty(name);
    }

    public SimpleStringProperty getCityProperty(){
        return new SimpleStringProperty(city);
    }

    public String getCity() { return city; }

    public String getName() { return name; }

    public void change(Team team){
        this.name = team.getName();
        this.city = team.getCity();
    }

    public void setNameSeparator(String nameSeparator){
        this.nameSeparator = nameSeparator;
    }
    public void setCitySeparator(String citySeparator){
        this.citySeparator = citySeparator;
    }
}
