package CarPark;

/**
* CarPark/DateHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Monday, 1 April 2019 23:20:57 o'clock BST
*/

public final class DateHolder implements org.omg.CORBA.portable.Streamable
{
  public CarPark.Date value = null;

  public DateHolder ()
  {
  }

  public DateHolder (CarPark.Date initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CarPark.DateHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CarPark.DateHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CarPark.DateHelper.type ();
  }

}
