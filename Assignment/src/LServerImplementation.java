import CarPark.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static CarPark.EventType.Entered;
import static CarPark.EventType.Paid;

public class LServerImplementation extends LocalServerPOA {

    public ArrayList<ParkingTransaction> logOfParkingTransactions = new ArrayList<ParkingTransaction>();
    public ArrayList<Machine> listOfEntryGates = new ArrayList<Machine>();
    public ArrayList<Machine> listOfExitGates = new ArrayList<Machine>();
    public ArrayList<Machine> listOfPayStations = new ArrayList<Machine>();
    Machine machine;
    HQServer HQRef;
    //initial cost - can be modified through HQ
    double cost = 1.0d;
    //default number of spaces. This is set through a main argument for LocalServer (e.g. -Spaces 75)
    short numberOfSpaces = 200;

    @Override
    public Machine machine() {
        return machine;
    }

    @Override
    public String location() {
        //returns the machine name
        return machine.name;
    }

    @Override
    public ParkingTransaction[] log() {
        //return parking transactions arraylist as an array
        ParkingTransaction[] parkingTransactions = new ParkingTransaction[logOfParkingTransactions.size()];
        logOfParkingTransactions.toArray(parkingTransactions);
        return parkingTransactions;
    }

    @Override
    public Machine[] listOfEntryGates() {
        //return entry gate arraylist as an array
        Machine[] machines = new Machine[listOfEntryGates.size()];
        listOfEntryGates.toArray(machines);
        return machines;
    }

    @Override
    public Machine[] listOfPayStations() {
        //return paystations arraylist as an array
        Machine[] machines = new Machine[listOfPayStations.size()];
        listOfPayStations.toArray(machines);
        return machines;
    }

    @Override
    public Machine[] listOfExitGates() {
        //return exit gates arraylist as an array
        Machine[] machines = new Machine[listOfExitGates.size()];
        listOfExitGates.toArray(machines);
        return machines;
    }


    @Override
    public void registerLocalServer(String machineName) {
        //initialise new machine and set values
        machine = new Machine();
        machine.name = machineName;
        machine.enabled = true;
        //add local server to HQ
        HQRef.register_local_server(machine);
        System.out.println("Added: " + machineName);

    }


    @Override
    public boolean vehicle_in(String reg) {

        if (vehicle_in_car_park(reg)) {
            return false;
        } else {
            //get current date and time
            LocalDateTime currentDateTime = LocalDateTime.now();
            CarPark.Date date = new CarPark.Date();
            Time time = new Time();
            //add current date to custom Date format
            date.day = currentDateTime.getDayOfMonth();
            date.month = currentDateTime.getMonth().getValue();
            date.year = currentDateTime.getYear();
            //add current time to custom Time format
            time.hr = currentDateTime.getHour();
            time.min = currentDateTime.getMinute();
            time.sec = currentDateTime.getSecond();

            //create new Parking Transaction and set values
            ParkingTransaction newTransaction = new ParkingTransaction();
            newTransaction.registration_number = reg;
            newTransaction.entryDate = date;
            newTransaction.entryTime = time;
            newTransaction.event = Entered;
            newTransaction.serverName = machine.name;
            //not set yet
            newTransaction.paystationName = "";
            newTransaction.alert = "";
            newTransaction.hrsStay = 0;
            newTransaction.amountPaid = 0;
            //add new transaction to log
            logOfParkingTransactions.add(newTransaction);
            return true;
        }
    }

    @Override
    public boolean vehicle_out(String reg) {

        //get current date/time
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (!vehicle_in_car_park(reg)) {
            //car is NOT in car park
            return false;
        } else {
            for (int i = 0; i < logOfParkingTransactions.size(); i++) {
                //get the transaction at the index
                ParkingTransaction parkingTransaction = logOfParkingTransactions.get(i);
                //check if the reg is the same as car trying to leave
                if (parkingTransaction.registration_number.equals(reg)) {
                    //if they've entered but not paid
                    if (parkingTransaction.event == Entered) {
                        //give 5 minute grace period
                        LocalDateTime gracePeriod = LocalDateTime.of(parkingTransaction.entryDate.year, parkingTransaction.entryDate.month, parkingTransaction.entryDate.day,
                                parkingTransaction.entryTime.hr, parkingTransaction.entryTime.min, parkingTransaction.entryTime.sec).plusMinutes(5);
                        //if they're trying to leave after grace period raise alarm
                        if (currentDateTime.isAfter(gracePeriod)) {
                            Duration duration = Duration.between(currentDateTime, gracePeriod);
                            logOfParkingTransactions.get(i).alert = "Overdue by " + (duration.toMinutes() * -1) + " mins";
                            //raise alarm but allow them to leave
                            HQRef.raise_alarm(parkingTransaction);
                            logOfParkingTransactions.get(i).event = EventType.Exited;
                            return false;
                        } else {
                            logOfParkingTransactions.get(i).event = EventType.Exited;
                            return true;
                        }
                    }
                    //if they've paid
                    else if (parkingTransaction.event == Paid) {

                        //LocalDateTime of when car entered
                        LocalDateTime entry = LocalDateTime.of(parkingTransaction.entryDate.year, parkingTransaction.entryDate.month,
                                parkingTransaction.entryDate.day, parkingTransaction.entryTime.hr, parkingTransaction.entryTime.min
                                , parkingTransaction.entryTime.sec);

                        //adding number of hours payed for
                        LocalDateTime expiry = entry.plusHours(parkingTransaction.hrsStay);

                        //adding five minutes grace period
                        expiry = expiry.plusMinutes(5);
                        //if
                        if (currentDateTime.isAfter(expiry)) {
                            //raise alarm but allow them to leave
                            Duration duration = Duration.between(expiry, entry);
                            logOfParkingTransactions.get(i).alert = "Overdue by " + duration.toMinutes() + " mins";
                            logOfParkingTransactions.get(i).event = EventType.Exited;
                            HQRef.raise_alarm(logOfParkingTransactions.get(i));
                            return false;
                        } else {
                            logOfParkingTransactions.get(i).event = EventType.Exited;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean vehicle_payment(String reg, String paystationName, short hrsStay, double amountPaid) {
        for (int i = 0; i < logOfParkingTransactions.size(); i++) {
            //get parking transaction for each index i
            ParkingTransaction parkingTransaction = logOfParkingTransactions.get(i);
            //if it's the correct one for the paying car
            if ((parkingTransaction.registration_number.equals(reg)) && (parkingTransaction.event == EventType.Entered)) {
                //add paystation name, hours stay, amount paid for and the event
                logOfParkingTransactions.get(i).paystationName = paystationName;
                logOfParkingTransactions.get(i).hrsStay = hrsStay;
                logOfParkingTransactions.get(i).amountPaid = amountPaid;
                logOfParkingTransactions.get(i).event = EventType.Paid;
                return true;
            }
        }
        //car attempting to pay not found
        return false;
    }


    @Override
    public boolean vehicle_in_car_park(String registration_number) {
        for (int i = 0; i < logOfParkingTransactions.size(); i++) {
            //get parking transaction for index i
            ParkingTransaction currentEvent = logOfParkingTransactions.get(i);
            //if the reg matches and the car has not left, return true
            if ((currentEvent.registration_number.equals(registration_number)) && ((currentEvent.event == EventType.Entered) || (currentEvent.event == EventType.Paid))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean vehicle_already_paid(String registration_number) {
        for (int i = 0; i < logOfParkingTransactions.size(); i++) {
            //get parking transaction for index i
            ParkingTransaction currentEvent = logOfParkingTransactions.get(i);
            //if payment has been made, return true
            if ((currentEvent.registration_number.equals(registration_number)) && (currentEvent.event == EventType.Paid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double return_cash_total() {
        double total = 0;
        //get current date
        LocalDate currentDate = LocalDate.now();
        //loop through all log entries
        for (int i = 0; i < logOfParkingTransactions.size(); i++) {
            ParkingTransaction parkingTransaction = logOfParkingTransactions.get(i);
            //add all payments received today
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
        //add entry gate to array list
        listOfEntryGates.add(machine);
        System.out.println("Added entry gate:" + machine.name);
    }

    @Override
    public void add_exit_gate(Machine machine) {
        //add exit gate to array list
        listOfExitGates.add(machine);
        System.out.println("Added exit gate:" + machine.name);
    }

    @Override
    public void add_pay_station(Machine machine) {
        //add paystation to array list
        listOfPayStations.add(machine);
        System.out.println("Added pay station:" + machine.name);
    }

    @Override
    public void updateEntryGate(String machineName, boolean enabled) {
        //update entrygate arraylist to show new enabled value
        for (int i = 0; i < listOfEntryGates.size(); i++) {
            if (listOfEntryGates.get(i).name.equals(machineName)) {
                listOfEntryGates.get(i).enabled = enabled;
            }
        }
    }

    @Override
    public void updatePayStation(String machineName, boolean enabled) {
        //update entrygate arraylist to show new enabled value
        for (int i = 0; i < listOfPayStations.size(); i++) {
            if (listOfPayStations.get(i).name.equals(machineName)) {
                listOfPayStations.get(i).enabled = enabled;
            }
        }
    }

    @Override
    public void updateExitGate(String machineName, boolean enabled) {
        //update entrygate arraylist to show new enabled value
        for (int i = 0; i < listOfExitGates.size(); i++) {
            if (listOfExitGates.get(i).name.equals(machineName)) {
                listOfExitGates.get(i).enabled = enabled;
            }
        }
    }

    @Override
    public double getCost() {
        //return current cost
        return cost;
    }

    @Override
    public void setCost(double newCost) {
        //set new cost
        cost = newCost;
    }

    @Override
    public short returnNumberofSpaces() {
        //return number of spaces
        return numberOfSpaces;
    }

    @Override
    public short returnAvailableSpaces() {
        //calculate how many spaces are currently available
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
        //get parking transaction for specified reg
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
        //if any of the machines in arraylist have the same name
        //false will be returned
        for (Machine entryGates : listOfEntryGates) {
            if (entryGates.name.equals(name)) {
                unique = false;
            }
        }
        return unique;
    }

    @Override
    public boolean isPayStationNameUnique(String name) {
        boolean unique = true;
        //if any of the machines in arraylist have the same name
        //false will be returned
        for (Machine paystations : listOfPayStations) {
            if (paystations.name.equals(name)) {
                unique = false;
            }
        }
        return unique;
    }

    @Override
    public boolean isExitGateNameUnique(String name) {
        boolean unique = true;
        //if any of the machines in arraylist have the same name
        //false will be returned
        for (Machine exitGates : listOfExitGates) {
            if (exitGates.name.equals(name)) {
                unique = false;
            }
        }
        return unique;
    }
}
