package CarPark;

/**
* CarPark/EntryGateHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Thursday, 14 March 2019 10:41:23 o'clock GMT
*/

public final class EntryGateHolder implements org.omg.CORBA.portable.Streamable
{
  public CarPark.EntryGate value = null;

  public EntryGateHolder ()
  {
  }

  public EntryGateHolder (CarPark.EntryGate initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CarPark.EntryGateHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CarPark.EntryGateHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CarPark.EntryGateHelper.type ();
  }

}
