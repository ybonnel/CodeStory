package fr.ybonnel.codestory.path.jajascript;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.primitives.Ints;

public class Flight implements Comparable<Flight> {

    @JsonProperty("VOL")
    public String name;
    @JsonProperty("DEPART")
    public int startTime;
    @JsonProperty("DUREE")
    public int duration;
    @JsonProperty("PRIX")
    public int price;
    @JsonIgnore
    public int endTime;

    public void calculateEndTime() {
        endTime = startTime + duration;
    }

    public String getName() {
        return name;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public int getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    @Override
    public int compareTo(Flight o) {
        return Ints.compare(startTime, o.startTime);
    }
}
