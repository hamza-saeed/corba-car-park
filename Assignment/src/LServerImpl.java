import CarPark.LocalServerPOA;
import CarPark.Log_of_vehicle_eventsHelper;
import CarPark.Log_of_vehicle_eventsHolder;
import CarPark.VehicleEvent;
import org.omg.CORBA.ORB;

public class LServerImpl extends LocalServerPOA {
    Log_of_vehicle_eventsHolder log = new Log_of_vehicle_eventsHolder();

    public LServerImpl()
    {

    }


    @Override
    public String location() {
        return null;
    }

    @Override
    public VehicleEvent[] log() {
        return new VehicleEvent[0];
    }

    @Override
    public void vehicle_in(VehicleEvent event) {
        VehicleEvent[] events = new VehicleEvent[1];
        events[0] = event;
    }

    @Override
    public void vehicle_out(VehicleEvent event) {

    }

    @Override
    public boolean vehicle_in_car_park(String registration_number) {
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
