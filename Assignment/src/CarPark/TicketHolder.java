package CarPark;

/**
* CarPark/TicketHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Tuesday, 26 March 2019 18:12:28 o'clock GMT
*/

public final class TicketHolder implements org.omg.CORBA.portable.Streamable
{
  public CarPark.Ticket value = null;

  public TicketHolder ()
  {
  }

  public TicketHolder (CarPark.Ticket initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CarPark.TicketHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CarPark.TicketHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CarPark.TicketHelper.type ();
  }

}
