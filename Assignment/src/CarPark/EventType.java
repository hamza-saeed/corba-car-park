package CarPark;


/**
* CarPark/EventType.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Thursday, 4 April 2019 16:06:45 o'clock BST
*/

public class EventType implements org.omg.CORBA.portable.IDLEntity
{
  private        int __value;
  private static int __size = 3;
  private static CarPark.EventType[] __array = new CarPark.EventType [__size];

  public static final int _Entered = 0;
  public static final CarPark.EventType Entered = new CarPark.EventType(_Entered);
  public static final int _Paid = 1;
  public static final CarPark.EventType Paid = new CarPark.EventType(_Paid);
  public static final int _Exited = 2;
  public static final CarPark.EventType Exited = new CarPark.EventType(_Exited);

  public int value ()
  {
    return __value;
  }

  public static CarPark.EventType from_int (int value)
  {
    if (value >= 0 && value < __size)
      return __array[value];
    else
      throw new org.omg.CORBA.BAD_PARAM ();
  }

  protected EventType (int value)
  {
    __value = value;
    __array[__value] = this;
  }
} // class EventType
