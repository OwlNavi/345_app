package com.example.cwagt.taskapp345.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cwagt on 15/09/2018.
 *
 * This class is used to validate the input for users trying to add new tasks to the system
 */

public class AddTaskInputValidator {

    //returns a string containing errors in validation, or an emptpy string if it validates correctly
    // params
    // String taskName, the name of the task should be a string of characters
    // String taskDescription, a description of the task, a string of characters
    // String time, the time, in valid HH:MM format
    public static String validateTask(String taskName, String taskDescription, String time){
        String message = "";

        message += validateName(taskName);
        message += validateDescription(taskDescription);
        message += validateTime(time);

        return message;
    }

    //validates a task name
    private static String validateName(String name){
        //Check length
        if(name.length() > 32) return "Task name must be less than 32 characters.\n";

        String patternString = "[a-zA-Z 0-9'?!]+";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(name);

        //fauled validation
        if(!matcher.matches()){
            return "Task Name must contain only valid characters\n";
        }

        //passes
        return "";
    }
    //validates a task name
    private static String validateTime(String time){
        //Check length
        if(time.length() > 5) return "Task time must in the format HH:MM\n";

        String patternString = "[0-2][0-9]:[0-5][0-9]";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(time);

        //fauled validation
        if(!matcher.matches()){
            return "Task time must in the format HH:MM\n";
        }

        //passes
        return "";
    }
    //validates a task name
    private static String validateDescription(String description){
        //Check length
        if(description.length() > 32) return "Task description must be less than 32 characters.\n";

        String patternString = "[a-zA-Z 0-9'?!]+";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(description);

        //fauled validation
        if(!matcher.matches()){
            return "Task description must contain only valid characters\n";
        }

        //passes
        return "";
    }
}
