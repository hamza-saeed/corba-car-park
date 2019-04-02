import CarPark.*;

import javax.swing.*;

public class ExitGateImplementation extends ExitGatePOA {

    public Machine machine;
    public LocalServer lServerRef;

    @Override
    public String machine_name() {
        return machine.name;
    }

    @Override
    public void registerGate(String machineName) {
        //create new machine and set values
        machine = new Machine();
        machine.name = machineName;
        machine.enabled = true;
        //add exit gate to localserver
        lServerRef.add_exit_gate(machine);
        System.out.println("Added: " + machineName);
    }

    @Override
    public void car_exited(String reg) {

        //if method returns true, successful, if not, car is already in car park.
        if (lServerRef.vehicle_out(reg))
        {
            JOptionPane.showMessageDialog(null,
                    "Exit successful","Exit", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "Vehicle with registration '" + reg + "' is not in car park.","Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void toggleEnabled() {

        //if on, turn off. if off, turn on.
        machine.enabled = !machine.enabled;
        System.out.println("Exit gate was turned " + (machine.enabled ? "on" : "off"));

        //Updates in the server record
        lServerRef.updateExitGate(machine.name,machine.enabled);
    }

    @Override
    public void reset() {
        //turn off and on
        machine.enabled = false;
        machine.enabled = true;
        System.out.println("Exit gate " + machine_name() + " was reset.");
    }

}
