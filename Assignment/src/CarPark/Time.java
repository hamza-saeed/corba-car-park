package CarPark;


/**
* CarPark/Time.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Monday, 18 March 2019 19:04:45 o'clock GMT
*/

public final class Time implements org.omg.CORBA.portable.IDLEntity
{
  public int hr = (int)0;
  public int min = (int)0;
  public int sec = (int)0;

  public Time ()
  {
  } // ctor

  public Time (int _hr, int _min, int _sec)
  {
    hr = _hr;
    min = _min;
    sec = _sec;
  } // ctor

} // class Time
