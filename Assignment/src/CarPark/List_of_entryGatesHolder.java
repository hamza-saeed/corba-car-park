package CarPark;


/**
* CarPark/List_of_entryGatesHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Monday, 1 April 2019 23:20:57 o'clock BST
*/

public final class List_of_entryGatesHolder implements org.omg.CORBA.portable.Streamable
{
  public CarPark.Machine value[] = null;

  public List_of_entryGatesHolder ()
  {
  }

  public List_of_entryGatesHolder (CarPark.Machine[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CarPark.List_of_entryGatesHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CarPark.List_of_entryGatesHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CarPark.List_of_entryGatesHelper.type ();
  }

}
