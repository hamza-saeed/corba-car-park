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
    public void registerPaystation(String machineName, String ior, LocalServer lserverRef) {
        machine = new Machine();
        machine.name = machineName;
        machine.ior = ior;
        machine.enabled = true;
        lserverRef.add_pay_station(machine);
        lServerRef = lserverRef;
        System.out.println("Added: " + machineName + " with ior" + ior);
    }

    @Override
    public void toggleEnabled() {
        if (machine.enabled)
        {
            machine.enabled = false;
            System.out.println("PAYSTATION " + machine_name() + " WAS TURNED OFF");
        }
        else
        {
            machine.enabled = true;
            System.out.println("PAYSTATION " + machine_name() + "  WAS TURNED ON");
        }
        lServerRef.updatePayStation(machine.name,machine.enabled);

    }

    @Override
    public void reset() {
        machine.enabled = false;
        machine.enabled = true;
        System.out.println("PAYSTATION " + machine_name() + "  WAS RESET");

    }

    @Override
    public boolean checkVehicleInCarPark(String carReg) {
        return lServerRef.vehicle_in_car_park(carReg);
    }

//    @Override
//    public boolean createTicket(Ticket newTicket) {
//        if (lServerRef.add_Ticket(newTicket))
//        {
//            System.out.println("Added Ticket");
//            return true;
//        }
//        else
//        {
//            System.out.println("Could not add ticket.");
//            return false;
//        }
//    }

    @Override
    public boolean pay(String carReg, Date payDate, Time payTime, int duration, double amountPaid) {
        VehicleEvent payEvent = new VehicleEvent();
        payEvent.registration_number = carReg;
        payEvent.time=payTime;
        payEvent.date=payDate;
        payEvent.event = EventType.Paid;
        payEvent.amountPaid=amountPaid;
        payEvent.paystationName=machine_name();
        payEvent.hrsStay = (short)duration;
        if (!lServerRef.vehicle_already_paid(carReg)) {
            if (lServerRef.vehicle_in_car_park(carReg)) {
                //TODO: Check if Reg is in car park/duration etc.
                if (lServerRef.vehicle_paid(payEvent)) {
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
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "Ticket has already been paid.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }    }

    @Override
    public double return_cash_total() {
        double total = 0;
        LocalDate currentDate = LocalDate.now();

        for (int i=0; i < lServerRef.log().length;i++)
        {
            VehicleEvent vEvent = lServerRef.log()[i];
            if (vEvent.event == EventType.Paid)
            {
                if (vEvent.date.day == currentDate.getDayOfMonth() &&
                        (vEvent.date.month == currentDate.getMonth().getValue()) &&
                        (vEvent.date.year == currentDate.getYear()) && (vEvent.paystationName.equals(machine_name())))
                {
                    total +=(double) vEvent.amountPaid;
                }
            }
        }

        return total;
    }

}