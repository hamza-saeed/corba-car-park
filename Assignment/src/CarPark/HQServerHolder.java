package CarPark;

/**
* CarPark/HQServerHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Thursday, 21 March 2019 14:01:20 o'clock GMT
*/

public final class HQServerHolder implements org.omg.CORBA.portable.Streamable
{
  public CarPark.HQServer value = null;

  public HQServerHolder ()
  {
  }

  public HQServerHolder (CarPark.HQServer initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CarPark.HQServerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CarPark.HQServerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CarPark.HQServerHelper.type ();
  }

}
