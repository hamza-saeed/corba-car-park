package CarPark;


/**
* CarPark/Date.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Friday, 22 March 2019 11:46:23 o'clock GMT
*/

public final class Date implements org.omg.CORBA.portable.IDLEntity
{
  public int day = (int)0;
  public int month = (int)0;
  public int year = (int)0;

  public Date ()
  {
  } // ctor

  public Date (int _day, int _month, int _year)
  {
    day = _day;
    month = _month;
    year = _year;
  } // ctor

} // class Date
