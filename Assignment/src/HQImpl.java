import CarPark.HQServerPOA;
import CarPark.Machine;
import CarPark.VehicleEvent;

import java.util.ArrayList;

public class HQImpl extends HQServerPOA {


    public static ArrayList<Machine> listOfLocalServers;

    public HQImpl()
    {
        listOfLocalServers = new ArrayList<Machine>();
    }


    @Override
    public void raise_alarm(VehicleEvent event) {
    }

    @Override
    public void register_local_server(Machine machine) {
        System.out.println(machine.name);
        listOfLocalServers.add(machine);
    }

    @Override
    public Machine[] returnEntryGates() {
        Machine[] entryGates = new Machine[LServerImpl.listOfEntryGates.size()];
        LServerImpl.listOfEntryGates.toArray(entryGates);
        return entryGates;
    }

    @Override
    public Machine[] returnPayStations() {
        Machine[] payStations = new Machine[LServerImpl.listOfPayStations.size()];
        LServerImpl.listOfPayStations.toArray(payStations);
        return payStations;
    }

    @Override
    public Machine[] returnExitGates() {
        Machine[] exitGates = new Machine[LServerImpl.listOfExitGates.size()];
        LServerImpl.listOfExitGates.toArray(exitGates);
        return exitGates;
    }

    @Override
    public void turn_off_entry_gate() {

        //System.out.println(EntryClient.entryImpl.EntryGateName);
        EntryClient.entryImpl.turn_off();

    }
}
