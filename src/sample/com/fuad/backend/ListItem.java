package sample.com.fuad.backend;

import java.time.LocalDate;


public class ListItem {
    private String shortDescription;
    private  String longDescription;
    private LocalDate deadLine;

    public ListItem(String shortDescription, String longDescription, LocalDate deadLine) {
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.deadLine = deadLine;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    @Override
    public String toString() {
        return shortDescription;
    }
}
