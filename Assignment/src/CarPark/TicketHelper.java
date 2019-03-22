package CarPark;


/**
* CarPark/TicketHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Friday, 22 March 2019 11:46:23 o'clock GMT
*/

abstract public class TicketHelper
{
  private static String  _id = "IDL:CarPark/Ticket:1.0";

  public static void insert (org.omg.CORBA.Any a, CarPark.Ticket that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static CarPark.Ticket extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [6];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "registration_number",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_double);
          _members0[1] = new org.omg.CORBA.StructMember (
            "amountPaid",
            _tcOf_members0,
            null);
          _tcOf_members0 = CarPark.DateHelper.type ();
          _members0[2] = new org.omg.CORBA.StructMember (
            "dateEntered",
            _tcOf_members0,
            null);
          _tcOf_members0 = CarPark.TimeHelper.type ();
          _members0[3] = new org.omg.CORBA.StructMember (
            "timeEntered",
            _tcOf_members0,
            null);
          _tcOf_members0 = CarPark.DateHelper.type ();
          _members0[4] = new org.omg.CORBA.StructMember (
            "dateToLeave",
            _tcOf_members0,
            null);
          _tcOf_members0 = CarPark.TimeHelper.type ();
          _members0[5] = new org.omg.CORBA.StructMember (
            "timeToLeave",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (CarPark.TicketHelper.id (), "Ticket", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static CarPark.Ticket read (org.omg.CORBA.portable.InputStream istream)
  {
    CarPark.Ticket value = new CarPark.Ticket ();
    value.registration_number = istream.read_string ();
    value.amountPaid = istream.read_double ();
    value.dateEntered = CarPark.DateHelper.read (istream);
    value.timeEntered = CarPark.TimeHelper.read (istream);
    value.dateToLeave = CarPark.DateHelper.read (istream);
    value.timeToLeave = CarPark.TimeHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, CarPark.Ticket value)
  {
    ostream.write_string (value.registration_number);
    ostream.write_double (value.amountPaid);
    CarPark.DateHelper.write (ostream, value.dateEntered);
    CarPark.TimeHelper.write (ostream, value.timeEntered);
    CarPark.DateHelper.write (ostream, value.dateToLeave);
    CarPark.TimeHelper.write (ostream, value.timeToLeave);
  }

}
