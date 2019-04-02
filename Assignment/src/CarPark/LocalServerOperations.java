package CarPark;


/**
* CarPark/LocalServerOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Tuesday, 2 April 2019 02:25:46 o'clock BST
*/

public interface LocalServerOperations 
{
  String location ();
  CarPark.ParkingTransaction[] log ();
  CarPark.Machine[] listOfEntryGates ();
  CarPark.Machine[] listOfPayStations ();
  CarPark.Machine[] listOfExitGates ();
  void registerLocalServer (String machineName);
  boolean vehicle_in (String carReg);
  boolean vehicle_out (String reg);
  boolean vehicle_payment (String reg, String paystationName, short hrsStay, double amountPaid);
  boolean vehicle_in_car_park (String registration_number);
  boolean vehicle_already_paid (String registration_number);
  double return_cash_total ();
  void add_entry_gate (CarPark.Machine machine);
  void add_exit_gate (CarPark.Machine machine);
  void add_pay_station (CarPark.Machine machine);
  void updateEntryGate (String machineName, boolean enabled);
  void updatePayStation (String machineName, boolean enabled);
  void updateExitGate (String machineName, boolean enabled);
  double getCost ();
  void setCost (double newCost);
  short returnNumberofSpaces ();
  short returnAvailableSpaces ();
  CarPark.ParkingTransaction getParkingTransaction (String reg);
  boolean isEntryNameUnique (String name);
  boolean isPayStationNameUnique (String name);
  boolean isExitGateNameUnique (String name);
} // interface LocalServerOperations
