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
}
