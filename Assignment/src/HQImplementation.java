import CarPark.HQServerPOA;
import CarPark.Machine;
import CarPark.ParkingTransaction;

import java.util.ArrayList;

public class HQImplementation extends HQServerPOA {

    public ArrayList<Machine> listOfLocalServers = new ArrayList<Machine>();
    public ArrayList<ParkingTransaction> listOfAlerts = new ArrayList<ParkingTransaction>();



    @Override
    public void raise_alarm(ParkingTransaction transaction) {
        listOfAlerts.add(transaction);
    }

    @Override
    public void register_local_server(Machine machine) {
        System.out.println(machine.name);
        listOfLocalServers.add(machine);
    }

    @Override
    public boolean isLocalServerNameUnique(String name) {
        boolean unique = true;
        for (Machine lServers : listOfLocalServers)
        {
            if (lServers.name.equals(name))
            {
                unique = false;
            }
        }
        return unique;
    }


}
