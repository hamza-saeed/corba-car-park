package CarPark;


/**
* CarPark/ExitGateOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Monday, 25 March 2019 15:00:50 o'clock GMT
*/

public interface ExitGateOperations 
{
  String machine_name ();
  void registerGate (String machineName, String ior);
  void car_exited (String reg, CarPark.Date date, CarPark.Time time);
  void turn_on ();
  void turn_off ();
  void reset ();
} // interface ExitGateOperations
