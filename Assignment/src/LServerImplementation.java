import CarPark.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class LServerImplementation extends LocalServerPOA {

    public ArrayList<VehicleEvent> logOfVehicleEvents = new ArrayList<VehicleEvent>();
    public ArrayList<Machine> listOfEntryGates = new ArrayList<Machine>();
    public ArrayList<Machine> listOfExitGates = new ArrayList<Machine>();
    public ArrayList<Machine> listOfPayStations = new ArrayList<Machine>();
    Machine machine;
    HQServer HQRef;


    @Override
    public String location() {
        return null;
    }

    @Override
    public VehicleEvent[] log() {
        VehicleEvent[] vehicleEvents = new VehicleEvent[logOfVehicleEvents.size()];
        logOfVehicleEvents.toArray(vehicleEvents);
        return vehicleEvents;
    }

    @Override
    public Machine[] listOfEntryGates() {

        Machine[] machines = new Machine[listOfEntryGates.size()];
        listOfEntryGates.toArray(machines);
        return machines;
    }

    @Override
    public Machine[] listOfPayStations() {
        Machine[] machines = new Machine[listOfPayStations.size()];
        listOfPayStations.toArray(machines);
        return machines;
    }

    @Override
    public Machine[] listOfExitGates() {
        Machine[] machines = new Machine[listOfExitGates.size()];
        listOfExitGates.toArray(machines);
        return machines;
    }


    @Override
    public void registerLocalServer(String machineName, String iorVal,HQServer hqRef) {
        machine = new Machine();
        machine.name = machineName;
        machine.ior = iorVal;
        machine.enabled = true;
        hqRef.register_local_server(machine);
        HQRef = hqRef;
        System.out.println("Added: " + machineName + " with ior" + iorVal);

    }


    @Override
    public void vehicle_in(VehicleEvent event) {

        logOfVehicleEvents.add(event);
        System.out.println("Size after addition: " + logOfVehicleEvents.size());
    }

    @Override
    public void vehicle_out(VehicleEvent event) {
        logOfVehicleEvents.add(event);
        //TODO: Write Event
    }

    @Override
    public boolean vehicle_paid(VehicleEvent event) {
        logOfVehicleEvents.add(event);
        return true;
    }
//
//    @Override
//    public boolean add_Ticket(Ticket newTicket) {
//        try {
//            listOfTickets.add(newTicket);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    @Override
    public boolean vehicle_in_car_park(String registration_number) {

        for (int i = 0; i < logOfVehicleEvents.size(); i++) {
            VehicleEvent currentEvent = logOfVehicleEvents.get(i);
            if ((currentEvent.registration_number.equals(registration_number)) && ((currentEvent.event == EventType.Entered) || (currentEvent.event == EventType.Paid))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean vehicle_already_paid(String registration_number) {
        for (int i = 0; i < logOfVehicleEvents.size(); i++) {
            VehicleEvent currentEvent = logOfVehicleEvents.get(i);
            if ((currentEvent.registration_number.equals(registration_number)) && (currentEvent.event == EventType.Paid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double return_cash_total() {
        double total = 0;
        LocalDate currentDate = LocalDate.now();

        for (int i=0; i < logOfVehicleEvents.size();i++)
        {
            VehicleEvent vEvent = logOfVehicleEvents.get(i);
            if (vEvent.event == EventType.Paid)
            {
                if (vEvent.date.day == currentDate.getDayOfMonth() &&
                        (vEvent.date.month == currentDate.getMonth().getValue()) &&
                        (vEvent.date.year == currentDate.getYear()))
                {
                    total +=(double) vEvent.amountPaid;
                }
            }
        }

        return total;
    }

    @Override
    public void add_entry_gate(Machine machine) {
        listOfEntryGates.add(machine);
        System.out.println("Added entry gate:" + machine.name);
    }

    @Override
    public void add_exit_gate(Machine machine) {
        listOfExitGates.add(machine);
        System.out.println("Added exit gate:" + machine.name);

    }

    @Override
    public void add_pay_station(Machine machine) {
        listOfPayStations.add(machine);
        System.out.println("Added pay station:" + machine.name);

    }

    @Override
    public void updateEntryGate(String machineName, boolean enabled) {
        for (int i=0; i < listOfEntryGates.size();i++)
        {
            if (listOfEntryGates.get(i).name.equals(machineName))
            {
                listOfEntryGates.get(i).enabled = enabled;
            }
        }
    }

    @Override
    public void updatePayStation(String machineName, boolean enabled) {
        for (int i=0; i < listOfPayStations.size();i++)
        {
            if (listOfPayStations.get(i).name.equals(machineName))
            {
                listOfPayStations.get(i).enabled = enabled;
            }
        }
    }

    @Override
    public void updateExitGate(String machineName, boolean enabled) {
        for (int i=0; i < listOfExitGates.size();i++)
        {
            if (listOfExitGates.get(i).name.equals(machineName))
            {
                listOfExitGates.get(i).enabled = enabled;
            }
        }
    }

}
