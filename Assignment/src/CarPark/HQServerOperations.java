package CarPark;


/**
* CarPark/HQServerOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Friday, 22 March 2019 11:46:24 o'clock GMT
*/

public interface HQServerOperations 
{
  void raise_alarm (CarPark.VehicleEvent event);
  void register_local_server (CarPark.Machine machine);
} // interface HQServerOperations
