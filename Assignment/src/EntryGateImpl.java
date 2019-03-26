import CarPark.*;

import static CarPark.EventType.Entered;

public class EntryGateImpl extends EntryGatePOA {

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
        lserver.add_entry_gate(machine);
        System.out.println("Added: " + machineName + " with ior" + iorVal);

    }

    @Override
    public void car_entered(String reg, Date date, Time time) {
        if (!lserver.vehicle_in_car_park(reg)) {
            String dateStr = Integer.toString(date.day) + "/" + Integer.toString(date.month) + "/" + Integer.toString(date.year);
            VehicleEvent vehicleEvent = new VehicleEvent();
            vehicleEvent.registration_number = reg;
            vehicleEvent.date = date;
            vehicleEvent.time = time;
            vehicleEvent.event = Entered;
            System.out.println("Car Entered with reg: " + reg + ". Date: " + dateStr + ". Time: " + (time.hr + ":") + (time.min + ":") + time.sec);

            lserver.vehicle_in(vehicleEvent);
        }
        else {
            System.out.println("Vehicle with registration '" + reg + "' is already in car park.");
        }
    }

    @Override
    public void turn_on() {
        System.out.println("ENTRY GATE WAS TURNED ON");
    }

    @Override
    public void turn_off() {
        System.out.println("ENTRY GATE WAS TURNED OFF");
    }

    @Override
    public void reset() {
        turn_off();
        turn_on();
    }
}

