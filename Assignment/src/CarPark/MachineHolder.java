package CarPark;

/**
* CarPark/MachineHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Friday, 22 March 2019 11:46:23 o'clock GMT
*/

public final class MachineHolder implements org.omg.CORBA.portable.Streamable
{
  public CarPark.Machine value = null;

  public MachineHolder ()
  {
  }

  public MachineHolder (CarPark.Machine initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CarPark.MachineHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CarPark.MachineHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CarPark.MachineHelper.type ();
  }

}
