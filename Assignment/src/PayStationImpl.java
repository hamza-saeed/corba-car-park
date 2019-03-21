import CarPark.*;

public class PayStationImpl extends PayStationPOA {

    LServerImpl lServer = new LServerImpl();

    @Override
    public String machine_name() {
        return null;
    }

    @Override
    public void registerPaystation(String machineName) {
        Machine machine = new Machine();
        machine.name = machineName;
        machine.ior = "";
        lServer.add_pay_station(machine);
    }

    @Override
    public void turn_on() {

    }

    @Override
    public void turn_off() {

    }

    @Override
    public void reset() {

    }

    @Override
    public boolean checkVehicleInCarPark(String carReg) {
        return lServer.vehicle_in_car_park(carReg);
    }

    @Override
    public boolean createTicket(Ticket newTicket) {
        if (lServer.add_Ticket(newTicket))
        {
            System.out.println("Added Ticket");
            return true;
        }
        else
        {
            System.out.println("Could not add ticket.");
            return false;
        }
    }

    @Override
    public boolean pay(String carReg, Date payDate, Time payTime, int duration) {
        VehicleEvent payEvent = new VehicleEvent();
        payEvent.registration_number = carReg;
        payEvent.time=payTime;
        payEvent.date=payDate;
        payEvent.event = EventType.Paid;
        if (!lServer.vehicle_already_paid(carReg)) {
            if (lServer.vehicle_in_car_park(carReg)) {
                //TODO: Check if Reg is in car park/duration etc.
                if (lServer.vehicle_paid(payEvent)) {
                    System.out.println("Car Reg:" + carReg + " paid.");
                    return true;
                } else {
                    System.out.println("Payment failed");
                    return false;
                }
            } else {
                System.out.println("Vehicle is NOT in car park");
            return false;
            }

        }
        else
        {
            System.out.println("Ticket has already been paid.");
            return false;

        }
    }



    @Override
    public int return_cash_total() {
        return 0;
    }
}
