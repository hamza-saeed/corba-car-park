package CarPark;


/**
* CarPark/List_of_payStationsHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Tuesday, 2 April 2019 02:25:46 o'clock BST
*/

abstract public class List_of_payStationsHelper
{
  private static String  _id = "IDL:CarPark/List_of_payStations:1.0";

  public static void insert (org.omg.CORBA.Any a, CarPark.Machine[] that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static CarPark.Machine[] extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = CarPark.MachineHelper.type ();
      __typeCode = org.omg.CORBA.ORB.init ().create_sequence_tc (0, __typeCode);
      __typeCode = org.omg.CORBA.ORB.init ().create_alias_tc (CarPark.List_of_payStationsHelper.id (), "List_of_payStations", __typeCode);
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static CarPark.Machine[] read (org.omg.CORBA.portable.InputStream istream)
  {
    CarPark.Machine value[] = null;
    int _len0 = istream.read_long ();
    value = new CarPark.Machine[_len0];
    for (int _o1 = 0;_o1 < value.length; ++_o1)
      value[_o1] = CarPark.MachineHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, CarPark.Machine[] value)
  {
    ostream.write_long (value.length);
    for (int _i0 = 0;_i0 < value.length; ++_i0)
      CarPark.MachineHelper.write (ostream, value[_i0]);
  }

}
