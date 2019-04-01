package CarPark;


/**
* CarPark/EventTypeHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Monday, 1 April 2019 16:56:03 o'clock BST
*/

abstract public class EventTypeHelper
{
  private static String  _id = "IDL:CarPark/EventType:1.0";

  public static void insert (org.omg.CORBA.Any a, CarPark.EventType that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static CarPark.EventType extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_enum_tc (CarPark.EventTypeHelper.id (), "EventType", new String[] { "Entered", "Paid", "Exited"} );
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static CarPark.EventType read (org.omg.CORBA.portable.InputStream istream)
  {
    return CarPark.EventType.from_int (istream.read_long ());
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, CarPark.EventType value)
  {
    ostream.write_long (value.value ());
  }

}
