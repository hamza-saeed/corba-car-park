import CarPark.HQServerPOA;
import CarPark.Machine;
import CarPark.VehicleEvent;

public class HQImpl extends HQServerPOA {
    @Override
    public void raise_alarm(VehicleEvent event) {

    }

    @Override
    public void register_local_server(Machine machine) {
        System.out.println(machine.name);
    }
}
