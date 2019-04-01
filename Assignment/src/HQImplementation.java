import CarPark.HQServerPOA;
import CarPark.Machine;
import CarPark.ParkingTransaction;

import java.util.ArrayList;

public class HQImplementation extends HQServerPOA {

    public ArrayList<Machine> listOfLocalServers = new ArrayList<Machine>();

    @Override
    public void raise_alarm(ParkingTransaction transaction) {

    }

    @Override
    public void register_local_server(Machine machine) {
        System.out.println(machine.name);
        listOfLocalServers.add(machine);
    }


}
