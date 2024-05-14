package bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Hotel implements Serializable {
    //一共几层楼，就是就是几种房间类型
    private int floor;
    //房型数组
    private ArrayList<Floor> floors = new ArrayList<>();
//    //预约数组
//    private ArrayList<Appointment> appointments = new ArrayList<>();



    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public ArrayList<Floor> getFloors() {
        return floors;
    }

    public void setFloors(ArrayList<Floor> floors) {
        this.floors = floors;
    }
}
