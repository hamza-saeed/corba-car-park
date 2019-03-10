import CarPark.*;

public class EntryGateImpl extends EntryGatePOA {
    @Override
    public String machine_name() {
        return null;
    }

    @Override
    public void registerGate(String machineName) {

    }

    @Override
    public void car_entered(String reg, int date, int time) {
        System.out.println("Car Entered with reg: " + reg);
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
}

