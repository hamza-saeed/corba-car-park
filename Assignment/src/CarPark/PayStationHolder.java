package CarPark;

/**
* CarPark/PayStationHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from PayStation.idl
* Friday, 8 March 2019 11:07:11 o'clock GMT
*/

public final class PayStationHolder implements org.omg.CORBA.portable.Streamable
{
  public CarPark.PayStation value = null;

  public PayStationHolder ()
  {
  }

  public PayStationHolder (CarPark.PayStation initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CarPark.PayStationHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CarPark.PayStationHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CarPark.PayStationHelper.type ();
  }

}