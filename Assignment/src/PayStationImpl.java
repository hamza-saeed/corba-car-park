import CarPark.Date;
import CarPark.PayStationPOA;
import CarPark.Time;

public class PayStationImpl extends PayStationPOA {
    @Override
    public String machine_name() {
        return null;
    }

    @Override
    public void turn_on() {

    }

    @Override
    public void turn_off() {

    }

    @Override
    public void reset() {

    }

    @Override
    public boolean checkVehicleInCarPark(String carReg) {
        return false;
    }

    @Override
    public String pay(String carReg, Date payDate, Time payTime, int duration) {
        LServerImpl lserverimp = new LServerImpl();
        //lserverimp.vehicle_paid;
        System.out.println("Car Reg:" + carReg + " paid.");
        return "";
    }


    @Override
    public int return_cash_total() {
        return 0;
    }
}
