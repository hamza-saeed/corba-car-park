import CarPark.Date;
import CarPark.PayStationPOA;
import CarPark.Time;
import CarPark.VehicleEvent;

public class PayStationImpl extends PayStationPOA {

    LServerImpl lServer = new LServerImpl();

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
        return lServer.vehicle_in_car_park(carReg);
    }

    @Override
    public boolean pay(String carReg, Date payDate, Time payTime, int duration) {
        VehicleEvent paidEvent = new VehicleEvent();
        paidEvent.registration_number = carReg;
        paidEvent.time=payTime;
        paidEvent.date=payDate;

        if (lServer.vehicle_in_car_park(carReg)) {

            //TODO: Check if Reg is in car park/duration etc.
            if (lServer.vehicle_paid(paidEvent)) {
                System.out.println("Car Reg:" + carReg + " paid.");
                return true;
            } else {
                System.out.println("Payment failed");
                return false;
            }
        }
        else
        {
            System.out.println("Car NOT in car park");
            return false;
        }
    }


    @Override
    public int return_cash_total() {
        return 0;
    }
}
