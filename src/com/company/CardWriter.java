package com.company;
import java.util.*;

public class CardWriter {

    User userBuffer;
    boolean stored = false;

    public enum payloadOption{
        openDoor,lockDoor,modifyWhiteList,
        fakeAttendance
    }

    public void setUserBuffer(User user){
        userBuffer = user;
        stored = true;
    }

    public void changeData(User user){
        if(!stored){
            return;
        }
    }

    public void pushPayload(payloadOption option){
        if(!stored){
            return;
        }
    }

    public void getOutput(){
        //set Image
    }




}
