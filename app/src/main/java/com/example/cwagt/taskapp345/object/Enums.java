package com.example.cwagt.taskapp345.object;

/**
 * Created by cwagt on 2/05/2018.
 *
 * Contains enumerated types for use in the com.example.cwagt.taskapp345.object.Task class
 */
public class Enums {
    public enum Status {
        COMPLETED, //green
        IN_PROGRESS, //yellow
        INCOMPLETE //white
    }

    public enum Frequency {
        DAILY, WEEKLY, MONTHLY
    }
}