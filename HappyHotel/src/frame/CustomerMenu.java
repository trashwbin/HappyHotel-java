package frame;

import bean.Appointment;
import bean.Floor;
import bean.Hotel;
import bean.Room;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerMenu {
    //记录构建后的酒店
    private Hotel hotel = new Hotel();
    //记录预约信息
    private ArrayList<Appointment> appointments = new ArrayList<>();

    HotelBuild hotelBuild = new HotelBuild();
    Scanner sc = new Scanner(System.in);

    public CustomerMenu() {
        this.hotel=new HotelBuild().getHotel();
        this.appointments = new HotelBuild().getAppointments();
    }

    public void start() {
        while (true) {
            new HotelBuild().updateFile(appointments);
            System.out.println("===欢迎光临HappyHotel🎉🎉🎉===");
            System.out.println("现在是北京时间："+ DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())+"🕒");
            System.out.println("1.查看房间");
            System.out.println("2.预订房间");
            System.out.println("3.查看订单");
            System.out.println("4.管理员登录");
            System.out.println("0.退出程序");
            System.out.println("请输入您的选择：");
            String choice = sc.next();
            switch (choice) {
                case "1":
                    this.showRoom();
                    break;
                case "2":
                    this.reservation();
                    break;
                case "3":
                    this.checkReservation();
                    break;
                case "4":
                    if(hotelBuild.loginadmin()) {
                        hotelBuild = new HotelBuild();
                        hotelBuild.menu();
                        this.hotel = hotelBuild.getHotel();
                        this.appointments = hotelBuild.getAppointments();
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("输入有误，请重新输入！");
            }
        }
    }
    //展示有的房型和价格
    private void showRoom() {
        System.out.println("当前酒店有以下房型：");
        for (int i = 0; i < hotel.getFloors().size(); i++) {
            Floor floor = hotel.getFloors().get(i);
            System.out.println(i + 1 + "、" + floor.getFloorType() + " 价格：" + floor.getFloorPrice() + "元/天");
        }
    }

    //预订房间
    private void reservation() {
        System.out.println("请输入您的姓名：");
        String name = sc.next();
        System.out.println("请输入您的手机号：");
        String phone = sc.next();
        ArrayList<Floor>  floors = null;
        int floor= 0;
        while (true) {
            System.out.println("请选择您的房间类型：");
            floors = new ArrayList<>(hotel.getFloors());
            for(int i=0;i<floors.size();i++){
                System.out.println(i+1+"."+floors.get(i).getFloorType());
            }
            floor = sc.nextInt();
            if(floor>floors.size()||floor<1){
                System.out.println("输入有误，请重新输入！");
            }else{
                break;
            }

        }

        LocalDate date = LocalDate.now();
        //入住时间
        int checkin = 0;
        while (true) {
            System.out.println("请选择您的入住时间：");
            for(int i=0;i<7;i++){
                if(i==6){
                    System.out.println(i+1+"、"+ date.plusDays(i)+"(暂不办理入住)");
                } else{
                System.out.println(i+1+"、"+ date.plusDays(i));
                }
            }
            System.out.println("请输入您的选择：");
            checkin = sc.nextInt();
            if(checkin<1||checkin>6){
                System.out.println("输入有误，请重新输入！");
            } else {
                break;
            }
        }
        LocalDate startdate = date.plusDays(checkin-1);
        //退房时间
        int checkout = 0;
        while (true) {
            System.out.println("请选择您的退房时间：");
            for(int i=checkin;i<7;i++){
                System.out.println(i-checkin+1+"、"+ date.plusDays(i));
            }
            System.out.println("请输入您的选择：");
            checkout = sc.nextInt();
            if(checkout<1||checkout>7-checkin){
                System.out.println("输入有误，请重新输入！");
            } else {
                break;
            }
        }
        LocalDate enddate = startdate.plusDays(checkout);

        //判断是否有房间
        Floor floor1 = floors.get(floor-1);
        int roomId = this.isRoom(startdate,enddate.minusDays(1),floor1);
        if(roomId==-1){
            System.out.println("当前房型该时段已售罄！");
            return;
        }
        double bill = floor1.getFloorPrice()*checkout;

        System.out.println("您的所需要支付的金额为："+bill+"元");
        System.out.println("是否确认预订？(y/n)");
        if (sc.next().equals("y")){
            //将预约写入预约数组
            Appointment appointment = new Appointment();
            appointment.setName(name);
            appointment.setTelephone(phone);
            appointment.setStartDate(startdate);
            appointment.setEndDate(enddate);
            appointment.setRoomId(roomId);
            appointment.setPaidfare(bill);
            appointments.add(appointment);

            System.out.println("预订成功！您的预约信息如下：");
            System.out.println(appointment);
        } else {
            System.out.println("取消预订！");
        }
    }

    //查看订单
    public void checkReservation() {
        System.out.println("请输入您的姓名：");
        String name = sc.next();
        System.out.println("请输入您的手机号：");
        String phone = sc.next();
        ArrayList<Integer> myapindex = new ArrayList<>();
        ArrayList<Appointment> myAppointments = new ArrayList<>();
        for (int i=0;i<appointments.size();i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getName().equals(name) && appointment.getTelephone().equals(phone)){
                myAppointments.add(appointment);
                myapindex.add(i);
            }
        }
        if (myAppointments.size() == 0){
            System.out.println("您没有预约信息！");
            return;
        } else {
            System.out.println("您的预约信息如下：");
            for (int i=0;i<myAppointments.size();i++) {
                Appointment appointment = myAppointments.get(i);
                System.out.print(i+1+"、");
                System.out.println(appointment);
            }
        }
        System.out.println("是否需要取消预约？(y/n)");
        if (sc.next().equals("y")){
            System.out.println("请输入您要取消的预约编号：");
            int index = sc.nextInt();
            //把Integer转化为Int
            appointments.remove((int)myapindex.get(index-1));
            System.out.println("取消成功！");
        }
    }




    //判断是否有空房
    public int isRoom(LocalDate startdate, LocalDate enddate,Floor floor){
        for(Room room: floor.getRooms()){
            boolean flag = true;
            ArrayList<Appointment> thisRoomAppointments = this.getAppointmenByRoomId(room.getRoomId());
            if(thisRoomAppointments!=null) {
                for (int j = 0; j < thisRoomAppointments.size(); j++) {
                    Appointment appointment = thisRoomAppointments.get(j);
                    LocalDate asdate = appointment.getStartDate();//预约的起始时间
                    LocalDate aedate = appointment.getEndDate().minusDays(1);//退房当天可以让别人入住
                    //如果起始时间在已有预约时间内 或者结束时间在已有预约时间内 或者起始时间小于已有预约时间且结束时间大于已有预约时间
                    if ((startdate.isAfter(asdate) && startdate.isBefore(aedate))
                            || (enddate.isAfter(asdate) && enddate.isBefore(aedate))
                            || (startdate.isBefore(asdate) && enddate.isAfter(aedate))
                            || (startdate.equals(asdate))
                            || (enddate.equals(aedate))
                    ) {
                        flag = false;
                        break;
                    }
                }
            }
            if(flag)
                return room.getRoomId();
        }
        return -1;
    }


    //返回该房间号的预约
    public ArrayList<Appointment> getAppointmenByRoomId(int roomId){
        ArrayList<Appointment> thisRoomAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getRoomId() == roomId){
                thisRoomAppointments.add(appointment);
            }
        }
        if(thisRoomAppointments.size() == 0)
            return null;
        return thisRoomAppointments;
    }
}
