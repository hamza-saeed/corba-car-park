package CarPark;


/**
* CarPark/LocalServerOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Friday, 22 March 2019 11:46:24 o'clock GMT
*/

public interface LocalServerOperations 
{
  String location ();
  CarPark.VehicleEvent[] log ();
  void registerLocalServer (String machineName, String ior);
  void vehicle_in (CarPark.VehicleEvent event);
  void vehicle_out (CarPark.VehicleEvent event);
  boolean vehicle_paid (CarPark.VehicleEvent event);
  boolean add_Ticket (CarPark.Ticket newTicket);
  boolean vehicle_in_car_park (String registration_number);
  boolean vehicle_already_paid (String registration_number);
  double return_cash_total ();
  void add_entry_gate (CarPark.Machine machine);
  void add_exit_gate (CarPark.Machine machine);
  void add_pay_station (CarPark.Machine machine);
} // interface LocalServerOperations
