package CarPark;

/**
* CarPark/EventTypeHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Thursday, 21 March 2019 14:01:20 o'clock GMT
*/

public final class EventTypeHolder implements org.omg.CORBA.portable.Streamable
{
  public CarPark.EventType value = null;

  public EventTypeHolder ()
  {
  }

  public EventTypeHolder (CarPark.EventType initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CarPark.EventTypeHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CarPark.EventTypeHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CarPark.EventTypeHelper.type ();
  }

}
