import CarPark.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LServerImpl extends LocalServerPOA {

    HQImpl hqimp = new HQImpl();
    public static ArrayList<VehicleEvent> logOfVehicleEvents = new ArrayList<VehicleEvent>();
    public static ArrayList<Ticket> listOfTickets = new ArrayList<Ticket>();
    public static ArrayList<Machine> listOfEntryGates = new ArrayList<Machine>();
    public static ArrayList<Machine> listOfExitGates = new ArrayList<Machine>();
    public static ArrayList<Machine> listOfPayStations = new ArrayList<Machine>();

    public LServerImpl()
    {
//        logOfVehicleEvents = new ArrayList<VehicleEvent>();
//        listOfTickets = new ArrayList<Ticket>();
//        listOfEntryGates = new ArrayList<Machine>();
//        listOfExitGates = new ArrayList<Machine>();
//        listOfPayStations = new ArrayList<Machine>();
    }

    @Override
    public String location() {
        return null;
    }

    @Override
    public VehicleEvent[] log() {
        return (VehicleEvent[])logOfVehicleEvents.toArray();
    }

    @Override
    public Machine[] logOfEntryGates() {
        return (Machine[])listOfEntryGates.toArray();
    }

    @Override
    public void registerLocalServer(String machineName, String iorVal) {
        Machine machine = new Machine();
        machine.name = machineName;
        machine.ior = iorVal;
        hqimp.register_local_server(machine);
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

    @Override
    public boolean add_Ticket(Ticket newTicket) {
        try {
            listOfTickets.add(newTicket);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean vehicle_in_car_park(String registration_number) {

        for (int i = 0; i < logOfVehicleEvents.size(); i++)
        {
            VehicleEvent currentEvent = logOfVehicleEvents.get(i);
            if ((currentEvent.registration_number.equals(registration_number)) && ((currentEvent.event == EventType.Entered) || (currentEvent.event == EventType.Paid)))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean vehicle_already_paid(String registration_number) {
        for (int i = 0; i < logOfVehicleEvents.size(); i++)
        {
            VehicleEvent currentEvent = logOfVehicleEvents.get(i);
            if ((currentEvent.registration_number.equals(registration_number)) && (currentEvent.event == EventType.Paid))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public double return_cash_total() {
        double total = 0;
        LocalDate currentDate = LocalDate.now();
        for (int i = 0; i < listOfTickets.size(); i++)
        {
            Ticket ticket = listOfTickets.get(i);
            if (ticket.dateEntered.day == currentDate.getDayOfMonth())
            {
                total += (double)ticket.amountPaid;
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
}
