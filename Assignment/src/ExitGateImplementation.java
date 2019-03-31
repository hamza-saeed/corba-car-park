import CarPark.*;

import javax.swing.*;

public class ExitGateImplementation extends ExitGatePOA {

    public Machine machine = new Machine();
    public LocalServer lServerRef;

    @Override
    public String machine_name() {
        return machine.name;
    }

    @Override
    public void registerGate(String machineName, String ior, LocalServer lserverRef) {
        machine.name = machineName;
        machine.ior = ior;
        machine.enabled = true;
        lserverRef.add_exit_gate(machine);
        lServerRef = lserverRef;
        System.out.println("Added: " + machineName + " with ior" + ior);
    }

    @Override
    public void car_exited(String reg, Date date, Time time) {
        if (lServerRef.vehicle_in_car_park(reg)) {
            String dateStr = Integer.toString(date.day) + "/" + Integer.toString(date.month) + "/" + Integer.toString(date.year);
            VehicleEvent vehicleEvent = new VehicleEvent();
            vehicleEvent.registration_number = reg;
            vehicleEvent.date = date;
            vehicleEvent.time = time;
            vehicleEvent.event = EventType.Exited;
            //only needed for pay events
            vehicleEvent.amountPaid=0;
            vehicleEvent.hrsStay=0;
            vehicleEvent.paystationName="";
            System.out.println("Car Exited with reg: " + reg + ". Date: " + dateStr + ". Time: " + (time.hr + ":") + (time.min + ":") + time.sec);
            lServerRef.vehicle_out(vehicleEvent);
            JOptionPane.showMessageDialog(null,
                    "Exit successful",
                    "Exit", JOptionPane.INFORMATION_MESSAGE);


        }
        else {
            JOptionPane.showMessageDialog(null,
                    "Vehicle with registration '" + reg + "' is NOT in car park.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void toggleEnabled() {
        if (machine.enabled)
        {
            machine.enabled = false;
            System.out.println("EXIT GATE " + machine_name() + " WAS TURNED OFF");
        }
        else
        {
            machine.enabled = true;
            System.out.println("EXIT GATE " + machine_name() + "  WAS TURNED ON");
        }
        lServerRef.updateExitGate(machine.name,machine.enabled);

    }

    @Override
    public void reset() {
        machine.enabled = false;
        machine.enabled = true;
        System.out.println("EXIT GATE " + machine_name() + "  WAS RESET");
    }

}
