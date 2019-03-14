import CarPark.Date;
import CarPark.PayStationPOA;
import CarPark.Time;
import CarPark.VehicleEvent;

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
    public boolean pay(String carReg, Date payDate, Time payTime, int duration) {
        LServerImpl lserverimp = new LServerImpl();
        VehicleEvent paidEvent = new VehicleEvent();
        paidEvent.registration_number = carReg;
        paidEvent.time=payTime;
        paidEvent.date=payDate;
        //TODO: Check if Reg is in car park/duration etc.
        if (lserverimp.vehicle_paid(paidEvent))
        {
            System.out.println("Car Reg:" + carReg + " paid.");
            return true;
        }
        else
        {
            return false;
        }

    }


    @Override
    public int return_cash_total() {
        return 0;
    }
}
