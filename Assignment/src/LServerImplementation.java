import CarPark.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LServerImplementation extends LocalServerPOA {

    public ArrayList<ParkingTransaction> logOfParkingTransactions = new ArrayList<ParkingTransaction>();
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
    public ParkingTransaction[] log() {
        ParkingTransaction[] parkingTransactions = new ParkingTransaction[logOfParkingTransactions.size()];
        logOfParkingTransactions.toArray(parkingTransactions);
        return parkingTransactions;
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
    public void vehicle_in(ParkingTransaction transaction) {

        logOfParkingTransactions.add(transaction);
        System.out.println("Size after addition: " + logOfParkingTransactions.size());
    }

    @Override
    public void vehicle_out(String reg) {

        for (int i =0; i < logOfParkingTransactions.size();i++) {
            ParkingTransaction parkingTransaction = logOfParkingTransactions.get(i);

            if (parkingTransaction.registration_number.equals(reg) && (parkingTransaction.event == EventType.Entered))
            {
                System.out.println("unpaid");
            }
            else if (parkingTransaction.registration_number.equals(reg) && (parkingTransaction.event == EventType.Exited))
            {
                //They aren't even here?!
            }
            else if (parkingTransaction.registration_number.equals(reg) && (parkingTransaction.event == EventType.Paid))
            {
                LocalDateTime dateTime = LocalDateTime.now();

                //LocalDateTime of when car entered
                LocalDateTime entry = LocalDateTime.of(parkingTransaction.entryDate.year,parkingTransaction.entryDate.month,
                        parkingTransaction.entryDate.day,parkingTransaction.entryTime.hr,parkingTransaction.entryTime.min
                ,parkingTransaction.entryTime.sec);

                //adding number of hours payed for
                LocalDateTime expiry = entry.plusHours(parkingTransaction.hrsStay);

                //adding five minutes grace period
                expiry = expiry.plusMinutes(5);

                if (dateTime.isAfter(expiry))
                {
                    //raise alarm. they've been here too long.
                    System.out.println("alarm triggered");

                }
                else
                {
                    logOfParkingTransactions.get(i).event = EventType.Exited;
                    System.out.println("Successfully exited");
                }
            }

        }
    }

    @Override
    public boolean vehicle_payment(String reg, String paystationName, short hrsStay, double amountPaid) {
        for (int i =0; i < logOfParkingTransactions.size();i++)
        {
            ParkingTransaction parkingTransaction = logOfParkingTransactions.get(i);
            if ((parkingTransaction.registration_number.equals(reg)) && (parkingTransaction.event == EventType.Entered))
            {
                logOfParkingTransactions.get(i).paystationName = paystationName;
                logOfParkingTransactions.get(i).hrsStay = hrsStay;
                logOfParkingTransactions.get(i).amountPaid = amountPaid;
                logOfParkingTransactions.get(i).event = EventType.Paid;
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean vehicle_in_car_park(String registration_number) {

        for (int i = 0; i < logOfParkingTransactions.size(); i++) {
            ParkingTransaction currentEvent = logOfParkingTransactions.get(i);
            if ((currentEvent.registration_number.equals(registration_number)) && ((currentEvent.event == EventType.Entered) || (currentEvent.event == EventType.Paid))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean vehicle_already_paid(String registration_number) {
        for (int i = 0; i < logOfParkingTransactions.size(); i++) {
            ParkingTransaction currentEvent = logOfParkingTransactions.get(i);
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

        for (int i = 0; i < logOfParkingTransactions.size(); i++)
        {
            ParkingTransaction parkingTransaction = logOfParkingTransactions.get(i);
            if (parkingTransaction.event == EventType.Paid)
            {
                if (parkingTransaction.entryDate.day == currentDate.getDayOfMonth() &&
                        (parkingTransaction.entryDate.month == currentDate.getMonth().getValue()) &&
                        (parkingTransaction.entryDate.year == currentDate.getYear()))
                {
                    total +=(double) parkingTransaction.amountPaid;
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
