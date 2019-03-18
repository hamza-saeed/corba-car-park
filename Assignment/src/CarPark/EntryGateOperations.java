package CarPark;


/**
* CarPark/EntryGateOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Monday, 18 March 2019 15:29:12 o'clock GMT
*/

public interface EntryGateOperations 
{
  String machine_name ();
  void registerGate (String machineName);
  void car_entered (String reg, CarPark.Date date, CarPark.Time time);
  void turn_on ();
  void turn_off ();
  void reset ();
} // interface EntryGateOperations
