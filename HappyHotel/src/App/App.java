package App;


import frame.CustomerMenu;
import frame.HotelBuild;

public class App {
    public static void main(String[] args){
        HotelBuild hotelBuild = new HotelBuild();
        hotelBuild.isBuild();
        CustomerMenu customerMenu = new CustomerMenu();
        customerMenu.start();
    }
}
