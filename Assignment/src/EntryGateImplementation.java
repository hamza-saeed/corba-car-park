import CarPark.*;

import javax.swing.*;

import static CarPark.EventType.Entered;

public class EntryGateImplementation extends EntryGatePOA {

    public Machine machine;
    public LocalServer lserverRef;

    @Override
    public String machine_name() {
        return machine.name;
    }

    @Override
    public void registerGate(String machineName, String ior, LocalServer LserverREF) {
        machine = new Machine();
        //set values
        machine.name = machineName;
        machine.ior = ior;
        //enabled by default
        machine.enabled = true;
        //add the entry gate to the local server
        LserverREF.add_entry_gate(machine);
        lserverRef = LserverREF;
        System.out.println("Registered New Gate with name: " + machineName);
    }

    @Override
    public void car_entered(String reg, Date date, Time time) {

        //if the vehicle is not already in the car park

        if (!lserverRef.vehicle_in_car_park(reg)) {
            //put date in string format
            String dateStr = (date.day) + "/" + (date.month) + "/" + (date.year);
            //add information to vehicle event
            VehicleEvent vehicleEvent = new VehicleEvent();
            vehicleEvent.registration_number = reg;
            vehicleEvent.date = date;
            vehicleEvent.time = time;
            vehicleEvent.event = Entered;
            //only needed for pay events
            vehicleEvent.paystationName="";
            vehicleEvent.hrsStay=0;
            vehicleEvent.amountPaid=0;
            System.out.println("Car Entered with reg: " + reg + ". Date: " + dateStr + ". Time: " + (time.hr + ":") + (time.min + ":") + time.sec);
            lserverRef.vehicle_in(vehicleEvent);
            JOptionPane.showMessageDialog(null,
                    "Entry Successful",
                    "Enter", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            //TODO:  REPLACE ALL PRINTS WITH MESSAGEBOXES
            JOptionPane.showMessageDialog(null,
                    "Vehicle with registration '" + reg + "' is already in car park.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void toggleEnabled() {
        if (machine.enabled)
        {
            machine.enabled = false;
            System.out.println("ENTRY GATE " + machine_name() + " WAS TURNED OFF");
        }
        else
        {
            machine.enabled = true;
            System.out.println("ENTRY GATE " + machine_name() + "  WAS TURNED ON");
        }
        lserverRef.updateEntryGate(machine.name,machine.enabled);

    }

    @Override
    public void reset() {
        machine.enabled = false;
        machine.enabled = true;
        System.out.println("ENTRY GATE " + machine_name() + " WAS RESET");
    }

}

