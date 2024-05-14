package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Floor implements Serializable {
    //房间数量
    private int roomCount;
    //楼层
    private int floorNumber;
    //楼层类型
    private String floorType;
    //楼层价格
    private double floorPrice;
    //房间数组
    ArrayList<Room> rooms = new ArrayList<>();


//    //通过RoomId获取Room对象
//    public Room getRoomById(int roomId){
//        for(Room room: rooms){
//            if(room.getRoomId() == roomId){
//                return room;
//            }
//        }
//        return null;
//    }



    public double getFloorPrice() {
        return floorPrice;
    }

    public void setFloorPrice(double floorPrice) {
        this.floorPrice = floorPrice;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
        for(int i = 1; i <= roomCount; i++) {
            Room room = new Room();
            room.setRoomId(this.floorNumber*100+i);
            rooms.add(room);
        }
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getFloorType() {
        return floorType;
    }

    public void setFloorType(String floorType) {
        this.floorType = floorType;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }
}
