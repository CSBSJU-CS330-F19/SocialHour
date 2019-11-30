package com.example.services;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDateTime;
import com.example.DataTypes.Group;

public class GenerateMeetingTimes {
    private static DBConnection dbc;

    public static ArrayList<LocalDateTime> generateMeetingTime(String groupId, int month, int day, int year, int sTime, int eTime){


        //Generate array that holds the times that meetings can start at
        ArrayList<Integer> times = new ArrayList<>();
        for(int k = sTime; k <= eTime; k+=100){
            times.add(k);
            if(k % 100 == 0 && k != eTime){
                    times.add(k + 30);
            }
            else{
                k = k - 30;
            }
        }

        //parallel arrays for each event of start and end times
        ArrayList<LocalDateTime> startTimes = new ArrayList<>();
        ArrayList<LocalDateTime> endTimes = new ArrayList<>();

        dbc = DBConnection.getInstance();
        //get all the users in the groupId and a list with all their LocalDateTimes
        ArrayList<String> users = dbc.getAllMembersOfGroup(groupId);
        for(String user: users){
            ArrayList<LocalDateTime> userStart = dbc.getEventStartTimes(user);
            ArrayList<LocalDateTime> userEnd = dbc.getEventEndTimes(user);
            for(LocalDateTime start: userStart){
                if(start.getDayOfMonth() == day && start.getYear() == year && start.getMonthValue() == month){
                    startTimes.add(start);
                }
            }
            for(LocalDateTime end: userEnd){
                if(end.getDayOfMonth() == day && end.getYear() == year && end.getMonthValue() == month){
                    endTimes.add(end);
                }
            }

        }

        //that they are busy on that day
        //LocalDateTime start1 = LocalDateTime.of(2019,11,27,12,00,0);
        //LocalDateTime end1 = LocalDateTime.of(2019,11,27,13,30,00);
        //LocalDateTime start2 = LocalDateTime.of(2019,11,27,13,30,00);
        //LocalDateTime end2 = LocalDateTime.of(2019,11,27,14,00,00);
        //LocalDateTime start3 = LocalDateTime.of(2019,11,27,12,30,00);
        //LocalDateTime end3 = LocalDateTime.of(2019,11,27,14,00,00);


        for(int i = 0; i < startTimes.size(); i++){
            int startTime = (startTimes.get(i).getHour() * 100) + startTimes.get(i).getMinute();
            int endTime = (endTimes.get(i).getHour() * 100) + endTimes.get(i).getMinute();
            for(int t = 0; t < times.size(); t++){
                if(startTime <= times.get(t) && times.get(t) < endTime){
                    times.remove(t);
                    t--;
                }
            }
        }

        ArrayList<LocalDateTime> ret = new ArrayList<>();

        for(int i: times){
            LocalDateTime meetingTime = LocalDateTime.of(year, day, month, i/100, i%100,00);
            ret.add(meetingTime);
        }


        return ret;
    }
}
