package CarPark;


/**
* CarPark/EntryGateOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Monday, 1 April 2019 23:20:57 o'clock BST
*/

public interface EntryGateOperations 
{
  String machine_name ();
  void registerGate (String machineName);
  void car_entered (String reg);
  void toggleEnabled ();
  void reset ();
} // interface EntryGateOperations
