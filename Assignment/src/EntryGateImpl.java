import CarPark.*;

import static CarPark.EventType.Entered;

public class EntryGateImpl extends EntryGatePOA {

    public static String EntryGateName;
    public static Machine machine;

    @Override
    public String machine_name() {
        return EntryGateName;
    }

    @Override
    public void registerGate(String machineName, String iorVal) {
        //create new machine and set values
        machine = new Machine();
        machine.name = machineName;
        machine.ior = iorVal;
        //enabled by default
        machine.enabled = true;
        //add the entry gate to the local server
        LServer.lserver.add_entry_gate(machine);
        System.out.println("Registered New Gate with name: " + machineName);
    }

    @Override
    public void car_entered(String reg, Date date, Time time) {

        //if the vehicle is not already in the car park

        if (!LServer.lserver.vehicle_in_car_park(reg)) {
            //put date in string format
            String dateStr = (date.day) + "/" + (date.month) + "/" + (date.year);
            //add information to vehicle event
            VehicleEvent vehicleEvent = new VehicleEvent();
            vehicleEvent.registration_number = reg;
            vehicleEvent.date = date;
            vehicleEvent.time = time;
            vehicleEvent.event = Entered;
            System.out.println("Car Entered with reg: " + reg + ". Date: " + dateStr + ". Time: " + (time.hr + ":") + (time.min + ":") + time.sec);

            LServer.lserver.vehicle_in(vehicleEvent);
        }
        else {
            System.out.println("Vehicle with registration '" + reg + "' is already in car park.");
        }
    }

    @Override
    public void turn_on() {
        machine.enabled = true;
        System.out.println("ENTRY GATE " + machine_name() + "  WAS TURNED ON");
    }

    @Override
    public void turn_off() {
        machine.enabled = false;
            System.out.println("ENTRY GATE " + machine_name() + " WAS TURNED OFF");
    }

    @Override
    public void reset() {
        turn_off();
        turn_on();
        System.out.println("ENTRY GATE " + machine_name() + " WAS TURNED RESET");
    }
}

