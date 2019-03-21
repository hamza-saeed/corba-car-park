package CarPark;


/**
* CarPark/MachineHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Thursday, 21 March 2019 14:01:20 o'clock GMT
*/

abstract public class MachineHelper
{
  private static String  _id = "IDL:CarPark/Machine:1.0";

  public static void insert (org.omg.CORBA.Any a, CarPark.Machine that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static CarPark.Machine extract (org.omg.CORBA.Any a)
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
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [2];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "name",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[1] = new org.omg.CORBA.StructMember (
            "ior",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (CarPark.MachineHelper.id (), "Machine", _members0);
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

  public static CarPark.Machine read (org.omg.CORBA.portable.InputStream istream)
  {
    CarPark.Machine value = new CarPark.Machine ();
    value.name = istream.read_string ();
    value.ior = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, CarPark.Machine value)
  {
    ostream.write_string (value.name);
    ostream.write_string (value.ior);
  }

}