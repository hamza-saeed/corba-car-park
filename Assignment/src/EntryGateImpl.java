import CarPark.*;

public class EntryGateImpl extends EntryGatePOA {
    @Override
    public String machine_name() {
        return null;
    }

    @Override
    public void registerGate(String machineName) {

    }

    @Override
    public void car_entered(String reg, Date date, Time time) {
        String dateStr = Integer.toString(date.day) + "/" + Integer.toString(date.month) + "/" + Integer.toString(date.year);
        VehicleEvent vehicleEvent = new VehicleEvent();
        vehicleEvent.registration_number = reg;
        vehicleEvent.date = date;
        vehicleEvent.time = time;
        System.out.println("Car Entered with reg: " + reg + ". Date: " + dateStr + ". Time: " + (time.hr + ":") + (time.min + ":") + time.sec);

        LServerImpl lserver = new LServerImpl();
        lserver.vehicle_in(vehicleEvent);
        //_orb().create_any();

    }

    @Override
    public void turn_on() {

    }

    @Override
    public void turn_off() {

    }

    @Override
    public void reset() {
        turn_off();
        turn_on();
    }
}

