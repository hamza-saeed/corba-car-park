package CarPark;

/**
* CarPark/EntryGateHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Thursday, 4 April 2019 16:06:45 o'clock BST
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
