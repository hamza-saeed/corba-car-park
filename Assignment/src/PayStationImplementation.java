import CarPark.*;

import javax.swing.*;
import java.time.LocalDate;

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
    public boolean pay(String carReg, Date payDate, Time payTime, int duration, double amountPaid) {

        if (!lServerRef.vehicle_already_paid(carReg)) {

            if (lServerRef.vehicle_in_car_park(carReg)) {
                //TODO: Check if Reg is in car park/duration etc.
                if (lServerRef.vehicle_payment(carReg, machine_name(), (short) duration, amountPaid)) {
                    System.out.println("Car Reg:" + carReg + " paid.");
                    JOptionPane.showMessageDialog(null,
                            "Car Reg:" + carReg + " paid.",
                            "Error", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    System.out.println("Payment failed");
                    JOptionPane.showMessageDialog(null,
                            "Payment failed",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Vehicle with registration '" + carReg + "' is NOT in car park.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Ticket has already been paid.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public double return_cash_total() {
        double total = 0;
        LocalDate currentDate = LocalDate.now();

        for (int i = 0; i < lServerRef.log().length; i++) {
            ParkingTransaction parkingTransaction = lServerRef.log()[i];
            if ((parkingTransaction.event == EventType.Paid)) {
                if (parkingTransaction.entryDate.day == currentDate.getDayOfMonth() &&
                        (parkingTransaction.entryDate.month == currentDate.getMonth().getValue()) &&
                        (parkingTransaction.entryDate.year == currentDate.getYear()) && (parkingTransaction.paystationName.equals(machine_name()))) {
                    total += (double) parkingTransaction.amountPaid;
                }
            }
        }

        return total;
    }

}
