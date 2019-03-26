import CarPark.*;
import static CarPark.EventType.Exited;

public class ExitGateImpl extends ExitGatePOA {

    LServerImpl lserver = new LServerImpl();


    @Override
    public String machine_name() {
        return null;
    }

    @Override
    public void registerGate(String machineName, String iorVal) {
        Machine machine = new Machine();
        machine.name = machineName;
        machine.ior = iorVal;
        machine.enabled = true;
        lserver.add_exit_gate(machine);
        System.out.println("Added: " + machineName + " with ior" + iorVal);
    }

    @Override
    public void car_exited(String reg, Date date, Time time) {
        if (lserver.vehicle_in_car_park(reg)) {
            String dateStr = Integer.toString(date.day) + "/" + Integer.toString(date.month) + "/" + Integer.toString(date.year);
            VehicleEvent vehicleEvent = new VehicleEvent();
            vehicleEvent.registration_number = reg;
            vehicleEvent.date = date;
            vehicleEvent.time = time;
            vehicleEvent.event = Exited;
            System.out.println("Car Exited with reg: " + reg + ". Date: " + dateStr + ". Time: " + (time.hr + ":") + (time.min + ":") + time.sec);

            lserver.vehicle_out(vehicleEvent);
        }
        else {
            System.out.println("Vehicle with registration '" + reg + "' is NOT in car park.");
        }
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
}
