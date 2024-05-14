package frame;

import bean.Admin;
import bean.Appointment;
import bean.Floor;
import bean.Hotel;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class HotelBuild {
    Hotel hotel = new Hotel();
    Scanner sc = new Scanner(System.in);
    //记录预约信息
    private ArrayList<Appointment> appointments = new ArrayList<>();

    //每次默认构造都取出data文件中的酒店数据
    public HotelBuild() {
        File file = new File("src/data.txt");
        File file1 = new File("src/appointment.txt");
        if(file1.exists()&&file.exists()){
           this.getUpdateFile();
        }
    }

    //管理员菜单
    public void menu() {
        while (true) {
            this.updateFile(appointments);
            System.out.println("===🎉🎉欢迎进入酒店管理系统🎉🎉===");

            System.out.println("现在是北京时间："+ DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())+"🕒");
            System.out.println("1.重新构建酒店");
            System.out.println("2.展示所有预约信息");
            System.out.println("3.更新预约目录");
            System.out.println("4.一键预订房间");
            System.out.println("5.取消顾客预约");
            System.out.println("6.添加管理员");
            System.out.println("7.删除管理员");
            System.out.println("8.修改房间价格");
            System.out.println("0.退出登录");
            System.out.println("请输入您的选择：");
            String choice = sc.next();
            switch (choice) {
                case "1":
                    this.rebuild();
                    break;
                case "2":
                    this.showappointments();
                    break;
                case "3":
                    this.updateappointments();
                    break;
                case "4":
                    this.reservation();
                    break;
                case "5":
                    this.deleteReservation();
                    break;
                case "6":
                    this.createAdmin();
                    break;
                case "7":
                    this.deleteAdmin();
                    break;
                case "8":
                    this.changePrice();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("输入有误，请重新输入！");
            }
        }
    }

    //添加管理员
    public void createAdmin(){
        try (
                ObjectInputStream ois=new ObjectInputStream(new FileInputStream("src/admin.txt"));
                ){
            ArrayList<Admin> admins = (ArrayList<Admin>) ois.readObject();
            System.out.println("请输入要添加的管理员账号：");
            String username = sc.next();
            System.out.println("请输入该添加的管理员密码：");
            String password =sc.next();
            admins.add(new Admin(username,password));

            try (
                    ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("src/admin.txt"));
                    ){
                oos.writeObject(admins);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("添加成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //删除管理员
    public void deleteAdmin(){
        try (
                ObjectInputStream ois=new ObjectInputStream(new FileInputStream("src/admin.txt"));
        ){
            ArrayList<Admin> admins = (ArrayList<Admin>) ois.readObject();
            for (int i = 0; i < admins.size(); i++) {
                System.out.println(i+1+"、"+admins.get(i));
            }
            System.out.println("请输入要删除的管理员编号：");
            int index = sc.nextInt();
            if(admins.size()==1)
                System.out.println("删除失败！至少保留一个管理员！");
            else if (index>admins.size()||index<1) {
                System.out.println("输入有误，请重新输入！");
            } else {
                admins.remove(index-1);
                try (
                        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("src/admin.txt"));
                        ){
                    oos.writeObject(admins);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println("删除成功！");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //登录管理员账号
    public boolean loginadmin() {
        try (
                ObjectInputStream ois=new ObjectInputStream(new FileInputStream("src/admin.txt"));
                ){
            ArrayList<Admin> admins = (ArrayList<Admin>) ois.readObject();
            int index = 3;
            while (index>0) {
                System.out.println("请输入管理员账号：");
                String username = sc.next();
                System.out.println("请输入管理员密码：");
                String password = sc.next();
                for (Admin admin : admins) {
                    if(admin.getUsername().equals(username)&&admin.getPassword().equals(password)) {
                        System.out.println("登录成功");
                        return true;
                    }
                }
                System.out.println("账号或密码错误，请重新输入！");
                index--;
            }
            System.out.println("输入错误次数过多，请稍后再试！");
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //修改房间价格
    public void changePrice(){
        while (true) {
            System.out.println("当前房型如下：");
            ArrayList<Floor> floors = hotel.getFloors();
            for (int i=0;i<floors.size();i++) {
                System.out.println(i+1+"、"+floors.get(i).getFloorType()+"\t当前价格"+floors.get(i).getFloorPrice());
            }
            System.out.println("请输选择您要修改的房间类型：(输入-1退出)");
            int index = sc.nextInt();
            if(index==-1) break;
            System.out.println("请输入新的价格：");
            double price = sc.nextDouble();
            floors.get(index-1).setFloorPrice(price);
            hotel.setFloors(floors);
            System.out.println("修改成功！");
        }
        this.updateHotelFile(hotel);
    }

    //判断是否存在酒店，不存在则构建
    public void isBuild() {
        File file = new File("src/data.txt");
        File file1 = new File("src/appointment.txt");
        File file2 = new File("src/admin.txt");
        if(!file2.exists()||file2.length()==0){
            //创建一个空的Admin文件
            Admin admin = new Admin("admin","123456");
            ArrayList<Admin> admins = new ArrayList<>();
            admins.add(admin);
            try (
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/admin.txt"))
            ){
                oos.writeObject(admins);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(!file.exists()||file.length()==0){
            System.out.println("当前未构建房间，请先登录管理员账号构建酒店");
            if(this.loginadmin())this.build();
        }
        if(!file1.exists()||file1.length()==0){
            //创建一个空的Appointment文件
            this.deleteappointments();
        }
    }

    //返回构建好的酒店
    public Hotel getHotel() {
        //return hotel;
        try (
                ObjectInputStream ois =new ObjectInputStream(new FileInputStream("src/data.txt"));
                ){
            hotel = (Hotel) ois.readObject();
            return hotel;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //返回所有预约信息
    public ArrayList<Appointment> getAppointments(){
        try (
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/appointment.txt"));
        ){
            ArrayList<Appointment> appointments = (ArrayList<Appointment>) ois.readObject();
            appointments.sort((o1,o2)->o1.compareByRoomId(o2));
            return appointments;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //构建酒店系统
    public void build() {
        System.out.println("欢迎进入酒店构建系统！");

        System.out.println("请输入楼层总数(1楼为酒店大堂)：");

        int floor = 0;
        while (true) {
            floor = sc.nextInt();
            if(floor<2) {
                System.out.println("楼层数不能为1！！请重新输入：");
            } else {
                break;
            }
        }

        Hotel hotel=new Hotel();
        hotel.setFloor(floor);
        ArrayList<Floor> floors = new ArrayList<>();
        for(int i=2;i<=floor;i++){
            System.out.println("当前为第"+i+"层：");

            System.out.println("请输入该层房间类型：");
            String type = sc.next();
            System.out.println("请输入该层房间价格：");
            double price = sc.nextDouble();
            System.out.println("请输入该层房间数量：");
            int number = sc.nextInt();
            Floor f = new Floor();
            f.setFloorNumber(i);
            f.setFloorPrice(price);
            f.setFloorType(type);
            f.setRoomCount(number);
            floors.add(f);
        }
        hotel.setFloors(floors);
        //将构建的酒店数据写入data文件
        try (
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data.txt"))
        ){
            oos.writeObject(hotel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.hotel = hotel;
        System.out.println("酒店构建成功！");
    }

    //重新构建酒店
    private void rebuild() {
        System.out.println("注意：重新构建酒店将删除所有预约信息！");
        System.out.println("是否重新构建酒店？(y/n)");
        String choice = sc.next();
        if(choice.equals("y")) {
            this.deleteappointments();
            this.build();
        }
    }

    //清空预约信息
    private  void deleteappointments() {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/appointment.txt"))
        ){
            oos.writeObject(new ArrayList<Appointment>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //取消预约
    public void deleteReservation() {
        System.out.println("当前的预约信息如下：");
        for (int i=0;i<appointments.size();i++) {
            Appointment appointment = appointments.get(i);
            System.out.print(i+1+"、");
            System.out.println(appointment);
        }
        if (appointments.size() == 0){
            System.out.println("当前无预约信息！");
            return;
        }
        System.out.println("请输入您要取消的预约编号：");
        int index = sc.nextInt();
        appointments.remove(index-1);
        System.out.println("取消成功！");
    }

    //一键预订房间
    private void reservation() {
        System.out.println("请输入顾客的姓名：");
        String name = sc.next();
        System.out.println("请输入顾客的手机号：");
        String phone = sc.next();
        ArrayList<Floor>  floors = null;
        int floor= 0;
        while (true) {
            System.out.println("请选择顾客的房间类型：");
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
            System.out.println("请选择顾客的入住时间：");
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
            System.out.println("请选择顾客的退房时间：");
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
        int roomId = new CustomerMenu().isRoom(startdate,enddate.minusDays(1),floor1);
        if(roomId==-1){
            System.out.println("当前房型该时段已售罄！");
            return;
        }
        double bill = 0;
        //将预约写入预约数组
        Appointment appointment = new Appointment();
        appointment.setName(name);
        appointment.setTelephone(phone);
        appointment.setStartDate(startdate);
        appointment.setEndDate(enddate);
        appointment.setRoomId(roomId);
        appointment.setPaidfare(bill);
        appointments.add(appointment);

        System.out.println("预订成功！该顾客的预约信息如下：");
        System.out.println(appointment);
    }

    //将所有过期的预约信息删除
    private void updateappointments() {
        LocalDate now = LocalDate.now();
        for (int i=0;i<appointments.size();i++){
            Appointment appointment = appointments.get(i);
            if(appointment.getEndDate().isBefore(now)){
                appointments.remove(i);
                i--;
            }
        }
        System.out.println("更新完成！");
        System.out.println("更新后的预约信息如下：");
        this.showappointments();
    }

    //更新酒店文件
    public void updateHotelFile(Hotel hotel) {
        try (
                ObjectOutputStream oosd = new ObjectOutputStream(new FileOutputStream("src/data.txt"));
                ){
            oosd.writeObject(hotel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //更新预约文件
    public void updateFile(ArrayList<Appointment> appointments) {
        try (
                ObjectOutputStream oosa = new ObjectOutputStream(new FileOutputStream("src/appointment.txt"));
        ){
            oosa.writeObject(appointments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //得到更新后文件
    public void getUpdateFile() {
        try (
                ObjectInputStream oisa = new ObjectInputStream(new FileInputStream("src/appointment.txt"));
                ObjectInputStream oisd = new ObjectInputStream(new FileInputStream("src/data.txt"));
        ){
            this.hotel = (Hotel) oisd.readObject();
            this.appointments=(ArrayList<Appointment>) oisa.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //展示所有预约信息
    public void showappointments() {
        appointments.sort((o1,o2)->o1.compareByRoomId(o2));
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }

}
