package bean;

import java.io.Serializable;
import java.time.LocalDate;

public class Appointment implements Serializable {
    private String Name;
    private String telephone;
    private LocalDate startDate;
    private LocalDate endDate;
    private int roomId;
    private double paidfare;


    @Override
    public String toString() {
        return
                "房间号：" + roomId+
                "\t客户姓名：" + Name  +
                "\t联系电话：" + telephone +
                "\t入住时间：" + startDate +
                "\t退房时间：" + endDate +
                "\t所付房费：" + paidfare;
    }
    //按房间号排序，按预约时间排序
    public int compareByRoomId(Appointment appointment){
        if(this.roomId==appointment.roomId){
            return this.startDate.compareTo(appointment.startDate);
        }else{
            return this.roomId-appointment.roomId;
        }
    }


    public double getPaidfare() {
        return paidfare;
    }

    public void setPaidfare(double paidfare) {
        this.paidfare = paidfare;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
