package CarPark;


/**
* CarPark/Machine.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Tuesday, 2 April 2019 02:25:46 o'clock BST
*/

public final class Machine implements org.omg.CORBA.portable.IDLEntity
{
  public String name = null;
  public boolean enabled = false;

  public Machine ()
  {
  } // ctor

  public Machine (String _name, boolean _enabled)
  {
    name = _name;
    enabled = _enabled;
  } // ctor

} // class Machine
