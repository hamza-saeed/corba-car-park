import CarPark.*;
import static CarPark.EventType.Exited;

public class ExitGateImpl extends ExitGatePOA {

    public static String ExitGateName;
    public static Machine machine;

    @Override
    public String machine_name() {
        return ExitGateName;
    }

    @Override
    public void registerGate(String machineName, String iorVal) {
        machine.name = machineName;
        machine.ior = iorVal;
        machine.enabled = true;
        LServer.lserver.add_exit_gate(machine);
        System.out.println("Added: " + machineName + " with ior" + iorVal);
    }

    @Override
    public void car_exited(String reg, Date date, Time time) {
        if (LServer.lserver.vehicle_in_car_park(reg)) {
            String dateStr = Integer.toString(date.day) + "/" + Integer.toString(date.month) + "/" + Integer.toString(date.year);
            VehicleEvent vehicleEvent = new VehicleEvent();
            vehicleEvent.registration_number = reg;
            vehicleEvent.date = date;
            vehicleEvent.time = time;
            vehicleEvent.event = Exited;
            System.out.println("Car Exited with reg: " + reg + ". Date: " + dateStr + ". Time: " + (time.hr + ":") + (time.min + ":") + time.sec);

            LServer.lserver.vehicle_out(vehicleEvent);
        }
        else {
            System.out.println("Vehicle with registration '" + reg + "' is NOT in car park.");
        }
    }

    @Override
    public void turn_on() {
        machine.enabled = true;
    }

    @Override
    public void turn_off() {
        machine.enabled = false;
    }

    @Override
    public void reset() {

    }
}
