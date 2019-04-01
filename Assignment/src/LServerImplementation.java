import CarPark.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static CarPark.EventType.Entered;

public class LServerImplementation extends LocalServerPOA {

    public ArrayList<ParkingTransaction> logOfParkingTransactions = new ArrayList<ParkingTransaction>();
    public ArrayList<Machine> listOfEntryGates = new ArrayList<Machine>();
    public ArrayList<Machine> listOfExitGates = new ArrayList<Machine>();
    public ArrayList<Machine> listOfPayStations = new ArrayList<Machine>();
    double cost = 1.0d;
    short numberOfSpaces = 200;
    Machine machine;
    HQServer HQRef;


    @Override
    public String location() {
        return machine.name;
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
    public void registerLocalServer(String machineName) {
        machine = new Machine();
        machine.name = machineName;
        machine.enabled = true;
        HQRef.register_local_server(machine);
        System.out.println("Added: " + machineName);

    }


    @Override
    public boolean vehicle_in(String reg) {

        if (vehicle_in_car_park(reg)) {
            return false;
        } else {
            //date and time
            LocalDateTime currentDateTime = LocalDateTime.now();
            CarPark.Date date = new CarPark.Date();
            Time time = new Time();
            date.day = currentDateTime.getDayOfMonth();
            date.month = currentDateTime.getMonth().getValue();
            date.year = currentDateTime.getYear();
            time.hr = currentDateTime.getHour();
            time.min = currentDateTime.getMinute();
            time.sec = currentDateTime.getSecond();

            //create new Parking Transaction
            ParkingTransaction newTransaction = new ParkingTransaction();
            newTransaction.registration_number = reg;
            newTransaction.entryDate = date;
            newTransaction.entryTime = time;
            newTransaction.event = Entered;
            //not set yet
            newTransaction.paystationName = "";
            newTransaction.alert = "";
            newTransaction.hrsStay = 0;
            newTransaction.amountPaid = 0;
            logOfParkingTransactions.add(newTransaction);
            return true;
        }
    }

    @Override
    public boolean vehicle_out(String reg) {

        //check to ensure car is in carpark
        if (vehicle_in_car_park(reg)) {

            //find the correct transaction
            for (int i = 0; i < logOfParkingTransactions.size(); i++) {
                ParkingTransaction parkingTransaction = logOfParkingTransactions.get(i);
                if (parkingTransaction.registration_number.equals(reg) && (parkingTransaction.event == EventType.Entered)) {

                    //give 5 minute grace period
                    LocalDateTime gracePeriod = LocalDateTime.of(parkingTransaction.entryDate.year, parkingTransaction.entryDate.month, parkingTransaction.entryDate.day,
                            parkingTransaction.entryTime.hr, parkingTransaction.entryTime.min, parkingTransaction.entryTime.sec).plusMinutes(5);
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    //if they're trying to leave after grace period raise alarm
                    if (currentDateTime.isAfter(gracePeriod)) {
                        Duration duration = Duration.between(currentDateTime, gracePeriod);
                        logOfParkingTransactions.get(i).alert = "Overdue by " + duration.toMinutes() + " mins";
                        //raise alarm but allow them to leave
                        //TODO: pass overdue time.
                        HQRef.raise_alarm(parkingTransaction);
                        logOfParkingTransactions.get(i).event = EventType.Exited;
                        return true;
                    } else {
                        //they can leave
                        logOfParkingTransactions.get(i).event = EventType.Exited;
                        return true;
                    }
                } else if (parkingTransaction.registration_number.equals(reg) && (parkingTransaction.event == EventType.Paid)) {
                    LocalDateTime dateTime = LocalDateTime.now();

                    //LocalDateTime of when car entered
                    LocalDateTime entry = LocalDateTime.of(parkingTransaction.entryDate.year, parkingTransaction.entryDate.month,
                            parkingTransaction.entryDate.day, parkingTransaction.entryTime.hr, parkingTransaction.entryTime.min
                            , parkingTransaction.entryTime.sec);

                    //adding number of hours payed for
                    LocalDateTime expiry = entry.plusHours(parkingTransaction.hrsStay);

                    //adding five minutes grace period
                    expiry = expiry.plusMinutes(5);
                    //if
                    if (dateTime.isAfter(expiry)) {
                        //raise alarm but allow them to leave
                        Duration duration = Duration.between(expiry, entry);
                        logOfParkingTransactions.get(i).alert = "Overdue by " + duration.toMinutes() + " mins";
                        logOfParkingTransactions.get(i).event = EventType.Exited;
                        HQRef.raise_alarm(logOfParkingTransactions.get(i));
                        return true;
                    } else {
                        logOfParkingTransactions.get(i).event = EventType.Exited;
                        return true;
                    }
                }
            }
        } else {
            //car is NOT in entry gate.
            return false;
        }
        return false;
    }

    @Override
    public boolean vehicle_payment(String reg, String paystationName, short hrsStay, double amountPaid) {
        for (int i = 0; i < logOfParkingTransactions.size(); i++) {
            ParkingTransaction parkingTransaction = logOfParkingTransactions.get(i);
            if ((parkingTransaction.registration_number.equals(reg)) && (parkingTransaction.event == EventType.Entered)) {
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

        for (int i = 0; i < logOfParkingTransactions.size(); i++) {
            ParkingTransaction parkingTransaction = logOfParkingTransactions.get(i);
            if (parkingTransaction.event != Entered) {
                if (parkingTransaction.entryDate.day == currentDate.getDayOfMonth() &&
                        (parkingTransaction.entryDate.month == currentDate.getMonth().getValue()) &&
                        (parkingTransaction.entryDate.year == currentDate.getYear())) {
                    total += (double) parkingTransaction.amountPaid;
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
        for (int i = 0; i < listOfEntryGates.size(); i++) {
            if (listOfEntryGates.get(i).name.equals(machineName)) {
                listOfEntryGates.get(i).enabled = enabled;
            }
        }
    }

    @Override
    public void updatePayStation(String machineName, boolean enabled) {
        for (int i = 0; i < listOfPayStations.size(); i++) {
            if (listOfPayStations.get(i).name.equals(machineName)) {
                listOfPayStations.get(i).enabled = enabled;
            }
        }
    }

    @Override
    public void updateExitGate(String machineName, boolean enabled) {
        for (int i = 0; i < listOfExitGates.size(); i++) {
            if (listOfExitGates.get(i).name.equals(machineName)) {
                listOfExitGates.get(i).enabled = enabled;
            }
        }
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public void setCost(double newCost) {
        cost = newCost;
    }

    @Override
    public short returnNumberofSpaces() {
        return numberOfSpaces;
    }

    @Override
    public short returnAvailableSpaces() {
        short count = numberOfSpaces;
        for (ParkingTransaction parkingTrans : logOfParkingTransactions) {
            if (parkingTrans.event != EventType.Exited) {
                count--;
            }
        }
        return count;
    }

    @Override
    public ParkingTransaction getParkingTransaction(String reg) {
        for (int i = 0; i < logOfParkingTransactions.size(); i++) {
            ParkingTransaction parkingTransaction = logOfParkingTransactions.get(i);
            if ((parkingTransaction.registration_number.equals(reg)) && (parkingTransaction.event != EventType.Exited)) {
                return parkingTransaction;
            }
        }
        return new ParkingTransaction();
    }

    @Override
    public boolean isEntryNameUnique(String name) {
        boolean unique = true;
        for (Machine entryGates : listOfEntryGates)
        {
            if (entryGates.name.equals(name))
            {
                unique = false;
            }
        }
        return unique;
    }

    @Override
    public boolean isPayStationNameUnique(String name) {
        boolean unique = true;
        for (Machine paystations : listOfPayStations)
        {
            if (paystations.name.equals(name))
            {
                unique = false;
            }
        }
        return unique;
    }

    @Override
    public boolean isExitGateNameUnique(String name) {
        boolean unique = true;
        for (Machine exitGates : listOfExitGates)
        {
            if (exitGates.name.equals(name))
            {
                unique = false;
            }
        }
        return unique;
    }
}
