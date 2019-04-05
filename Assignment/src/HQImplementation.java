import CarPark.HQServerPOA;
import CarPark.Machine;
import CarPark.ParkingTransaction;

import java.util.ArrayList;

public class HQImplementation extends HQServerPOA {

    public ArrayList<Machine> listOfLocalServers = new ArrayList<Machine>();
    public ArrayList<ParkingTransaction> listOfAlerts = new ArrayList<ParkingTransaction>();



    @Override
    public void raise_alarm(ParkingTransaction transaction) {
        //add transaction to the list of alerts
        listOfAlerts.add(transaction);
    }

    @Override
    public void register_local_server(Machine machine) {
        //add local server to list of local servers
        listOfLocalServers.add(machine);
    }

    @Override
    public boolean isLocalServerNameUnique(String name) {
        boolean unique = true;
        //if any of the machines in arraylist have the same name
        //false will be returned
        for (Machine lServers : listOfLocalServers)
        {
            if (lServers.name.equals(name))
            {
                unique = false;
            }
        }
        return unique;
    }

    @Override
    public ParkingTransaction[] listOfAlerts() {
        //return alerts arraylist as array
        ParkingTransaction[] parkingTransactions = new ParkingTransaction[listOfAlerts.size()];
        listOfAlerts.toArray(parkingTransactions);
        return parkingTransactions;
    }

    @Override
    public Machine[] listOfLocalServers() {
        Machine[] machine = new Machine[listOfLocalServers.size()];
        listOfLocalServers.toArray(machine);
        return machine;
    }


}
