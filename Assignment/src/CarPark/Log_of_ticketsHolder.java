package CarPark;


/**
* CarPark/Log_of_ticketsHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Tuesday, 26 March 2019 18:12:28 o'clock GMT
*/

public final class Log_of_ticketsHolder implements org.omg.CORBA.portable.Streamable
{
  public CarPark.Ticket value[] = null;

  public Log_of_ticketsHolder ()
  {
  }

  public Log_of_ticketsHolder (CarPark.Ticket[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CarPark.Log_of_ticketsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CarPark.Log_of_ticketsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CarPark.Log_of_ticketsHelper.type ();
  }

}
