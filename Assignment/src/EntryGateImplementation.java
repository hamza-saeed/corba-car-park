import CarPark.*;

import javax.swing.*;

import java.sql.SQLOutput;

import static CarPark.EventType.Entered;

public class EntryGateImplementation extends EntryGatePOA {

    public Machine machine;
    public LocalServer lserverRef;

    @Override
    public Machine machine() {
        return machine;
    }

    @Override
    public String machine_name() {
        return machine.name;
    }

    @Override
    public void registerGate(String machineName) {
        //initialise new machine and set values
        machine = new Machine();
        machine.name = machineName;
        //enabled by default
        machine.enabled = true;
        //add the entry gate to the local server
        lserverRef.add_entry_gate(machine);
        System.out.println("Registered New Gate with name: " + machineName);
    }

    @Override
    public void car_entered(String reg) {
        //if method returns true, successful, if not, car is already in car park.
        if (lserverRef.vehicle_in(reg))
        {
            JOptionPane.showMessageDialog(null, "Entry Successful");

        }
        else
        {
            JOptionPane.showMessageDialog(null, "Vehicle with registration '" + reg + "' is already in car park.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void toggleEnabled() {
        //if on, turn off. if off, turn on.
        machine.enabled = !machine.enabled;
        System.out.println("Entry gate was turned " + (machine.enabled ? "on" : "off"));

        //Updates in the server record
        lserverRef.updateEntryGate(machine.name, machine.enabled);

    }

    @Override
    public void reset() {
        //turn off and on
        machine.enabled = false;
        machine.enabled = true;
        System.out.println("Entry gate " + machine_name() + " was reset.");
    }

}

