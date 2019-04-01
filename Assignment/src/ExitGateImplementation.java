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

        if (lServerRef.vehicle_out(reg))
        {
            JOptionPane.showMessageDialog(null,
                    "Exit successful","Exit", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "Vehicle with registration '" + reg + "' is NOT in car park.","Error", JOptionPane.ERROR_MESSAGE);
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
