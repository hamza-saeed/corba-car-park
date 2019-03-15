import CarPark.LocalServerPOA;
import CarPark.Log_of_vehicle_eventsHelper;
import CarPark.Log_of_vehicle_eventsHolder;
import CarPark.VehicleEvent;
import org.omg.CORBA.ORB;

import java.util.ArrayList;

public class LServerImpl extends LocalServerPOA {
    ArrayList<VehicleEvent> logOfVehicleEvents;

    public LServerImpl()
    {
        logOfVehicleEvents = new ArrayList<VehicleEvent>();
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
    public void vehicle_in(VehicleEvent event) {

        logOfVehicleEvents.add(event);
    }

    @Override
    public void vehicle_out(VehicleEvent event) {
        logOfVehicleEvents.add(event);
        //TODO: Write Event
    }

    @Override
    public boolean vehicle_paid(VehicleEvent event) {

        //TODO: Write Event
        return true;
    }

    @Override
    public boolean vehicle_in_car_park(String registration_number) {

        for (int i = 0; i < logOfVehicleEvents.size(); i++)
        {
            if (logOfVehicleEvents.get(i).registration_number == registration_number)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int return_cash_total() {
        return 0;
    }

    @Override
    public void add_entry_gate(String gate_name, String gate_ior) {

    }

    @Override
    public void add_exit_gate(String gate_name, String gate_ior) {

    }

    @Override
    public void add_pay_station(String station_name, String station_ior) {

    }
}
