import CarPark.*;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PayStationImplementation extends PayStationPOA {

    public LocalServer lServerRef;
    Machine machine;

    @Override
    public String machine_name() {
        return machine.name;
    }

    @Override
    public void registerPaystation(String machineName) {
        machine = new Machine();
        machine.name = machineName;
        machine.enabled = true;
        lServerRef.add_pay_station(machine);
        System.out.println("Added: " + machineName);
    }

    @Override
    public void toggleEnabled() {
        if (machine.enabled) {
            machine.enabled = false;
            System.out.println("Paystation " + machine_name() + " was turned off");
        } else {
            machine.enabled = true;
            System.out.println("Paystation " + machine_name() + "  was turned on");
        }
        lServerRef.updatePayStation(machine.name, machine.enabled);

    }

    @Override
    public void reset() {
        machine.enabled = false;
        machine.enabled = true;
        System.out.println("Paystation " + machine_name() + "  was reset");

    }

    @Override
    public void pay(String carReg, int duration, double amountPaid) {

        //only continue if ticket has not been paid
        if (lServerRef.vehicle_already_paid(carReg)) {
            JOptionPane.showMessageDialog(null,
                    "Ticket has already been paid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //only continue if vehicle is in carPark
        if (!lServerRef.vehicle_in_car_park(carReg)) {
            JOptionPane.showMessageDialog(null,
                    "Vehicle with registration '" + carReg + "' is NOT in car park.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //register payment
        if (lServerRef.vehicle_payment(carReg, machine_name(), (short) duration, amountPaid)) {
            System.out.println("Car Reg:" + carReg + " paid.");
            JOptionPane.showMessageDialog(null,
                    "Car Reg:" + carReg + " paid.", "Paid", JOptionPane.INFORMATION_MESSAGE);

            //get the entry time and calculate the expiry time
            ParkingTransaction transaction = lServerRef.getParkingTransaction(carReg);
            LocalDateTime entryDateTime = LocalDateTime.of(transaction.entryDate.year, transaction.entryDate.month,
                    transaction.entryDate.day, transaction.entryTime.hr, transaction.entryTime.min, transaction.entryTime.sec);
            LocalDateTime expiry = entryDateTime.plusHours((long) duration);

            //Display a ticket to the user showing car reg, amount payed, time entered and time to leave by.
            JTextArea ticket = new JTextArea();
            ticket.append("Car Reg: " + carReg + "\n");
            ticket.append("Amount Paid: £" + amountPaid + "\n");
            ticket.append("Entered: " + entryDateTime.getDayOfMonth() + "/" + entryDateTime.getMonth().getValue() + "/" + entryDateTime.getYear() + " ");
            ticket.append(entryDateTime.getHour() + ":" + entryDateTime.getMinute() + ":" + entryDateTime.getSecond() + "\n");
            ticket.append("Leave by: " + expiry.getDayOfMonth() + "/" + expiry.getMonth().getValue() + "/" + expiry.getYear() + " ");
            ticket.append(expiry.getHour() + ":" + expiry.getMinute() + ":" + expiry.getSecond());
            JOptionPane.showMessageDialog(null, new JScrollPane(ticket), "Ticket",
                    JOptionPane.INFORMATION_MESSAGE);

        } else {
            //should never happen in theory
            JOptionPane.showMessageDialog(null,
                    "Error locating transaction",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


    }

    @Override
    public double return_cash_total() {
        double total = 0;
        //get current date
        LocalDate currentDate = LocalDate.now();
        //loop through all log entries
        for (int i = 0; i < lServerRef.log().length; i++) {
            ParkingTransaction parkingTransaction = lServerRef.log()[i];
            //add all payments received today.
            if ((parkingTransaction.event == EventType.Paid)) {
                if (parkingTransaction.entryDate.day == currentDate.getDayOfMonth() &&
                        (parkingTransaction.entryDate.month == currentDate.getMonth().getValue()) &&
                        (parkingTransaction.entryDate.year == currentDate.getYear()) && (parkingTransaction.paystationName.equals(machine_name()))) {
                    total += (double) parkingTransaction.amountPaid;
                }
            }
        }
        //return total
        return total;
    }

}
