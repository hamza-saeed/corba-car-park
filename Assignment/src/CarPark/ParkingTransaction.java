package CarPark;


/**
* CarPark/ParkingTransaction.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Monday, 1 April 2019 16:56:03 o'clock BST
*/

public final class ParkingTransaction implements org.omg.CORBA.portable.IDLEntity
{
  public CarPark.Time entryTime = null;
  public CarPark.Date entryDate = null;
  public String registration_number = null;
  public CarPark.EventType event = null;
  public short hrsStay = (short)0;
  public double amountPaid = (double)0;
  public String paystationName = null;

  public ParkingTransaction ()
  {
  } // ctor

  public ParkingTransaction (CarPark.Time _entryTime, CarPark.Date _entryDate, String _registration_number, CarPark.EventType _event, short _hrsStay, double _amountPaid, String _paystationName)
  {
    entryTime = _entryTime;
    entryDate = _entryDate;
    registration_number = _registration_number;
    event = _event;
    hrsStay = _hrsStay;
    amountPaid = _amountPaid;
    paystationName = _paystationName;
  } // ctor

} // class ParkingTransaction
