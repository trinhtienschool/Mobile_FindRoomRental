package com.trinhtien2212.mobilefindroomrental.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bookmark {
    private String userUid;
//    private List<Room>rooms;
    private Map<String,String>mapRoomIds;
    public Bookmark() {
        mapRoomIds = new HashMap<>();
    }

    public Bookmark(String userId, List<Room> rooms) {
        this.userUid = userId;
//        this.rooms = rooms;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

//    public List<Room> getRooms() {
//        return rooms;
//    }
//
//    public void setRooms(List<Room> rooms) {
//        this.rooms = rooms;
//    }
//    public Map<String, Object> convertBookmarkToMap(){
//        Map<String,Object>map = new HashMap<String, Object>();
//        map.put("userUid",this.userUid);
//        for(int i=0;i<rooms.size();i++){
//            map.put(i+"",rooms.get(i).getRoomID());
//        }
//        return map;
//    }
    public List<String> convertToRoomId(Map<String,Object>map){
        List<String>roomIds = new ArrayList<String>();
        mapRoomIds = new HashMap<String,String>();
        for(String key: map.keySet()){
            mapRoomIds.put(key,(String)map.get(key));
            roomIds.add((String)map.get(key));
        }
        return roomIds;
    }
    //ToDo
    public void addRoom(String roomId){
        mapRoomIds.put(getMax(),roomId);
    }
    public String removeRoom(String roomId){
        for(String key: mapRoomIds.keySet()){
            if(mapRoomIds.get(key).equalsIgnoreCase(roomId)){
                mapRoomIds.remove(key);
                return key;
            }
        }
        return  null;
    }
    private String getMax(){
        int max = -1;
        for(String key: mapRoomIds.keySet()){
            max = Math.max(max,Integer.parseInt(key));
        }
        max = max+1;
        return String.valueOf(max);
    }
    public String getKey(String roomId) {
        Log.e("RoomID",roomId);
        Log.e("KeyMap",mapRoomIds.toString());
        for(String key: mapRoomIds.keySet()){
            if(roomId.equals(mapRoomIds.get(key))){
                mapRoomIds.remove(key);
                return key;
            }


        }
        return null;
    }
}
