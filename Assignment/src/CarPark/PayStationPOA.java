package CarPark;


/**
* CarPark/PayStationPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Thursday, 14 March 2019 10:41:24 o'clock GMT
*/

public abstract class PayStationPOA extends org.omg.PortableServer.Servant
 implements CarPark.PayStationOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("_get_machine_name", new java.lang.Integer (0));
    _methods.put ("turn_on", new java.lang.Integer (1));
    _methods.put ("turn_off", new java.lang.Integer (2));
    _methods.put ("reset", new java.lang.Integer (3));
    _methods.put ("checkVehicleInCarPark", new java.lang.Integer (4));
    _methods.put ("pay", new java.lang.Integer (5));
    _methods.put ("return_cash_total", new java.lang.Integer (6));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // CarPark/PayStation/_get_machine_name
       {
         String $result = null;
         $result = this.machine_name ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // CarPark/PayStation/turn_on
       {
         this.turn_on ();
         out = $rh.createReply();
         break;
       }

       case 2:  // CarPark/PayStation/turn_off
       {
         this.turn_off ();
         out = $rh.createReply();
         break;
       }

       case 3:  // CarPark/PayStation/reset
       {
         this.reset ();
         out = $rh.createReply();
         break;
       }

       case 4:  // CarPark/PayStation/checkVehicleInCarPark
       {
         String carReg = in.read_string ();
         boolean $result = false;
         $result = this.checkVehicleInCarPark (carReg);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 5:  // CarPark/PayStation/pay
       {
         String carReg = in.read_string ();
         CarPark.Date payDate = CarPark.DateHelper.read (in);
         CarPark.Time payTime = CarPark.TimeHelper.read (in);
         int duration = in.read_long ();
         boolean $result = false;
         $result = this.pay (carReg, payDate, payTime, duration);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 6:  // CarPark/PayStation/return_cash_total
       {
         int $result = (int)0;
         $result = this.return_cash_total ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:CarPark/PayStation:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public PayStation _this() 
  {
    return PayStationHelper.narrow(
    super._this_object());
  }

  public PayStation _this(org.omg.CORBA.ORB orb) 
  {
    return PayStationHelper.narrow(
    super._this_object(orb));
  }


} // class PayStationPOA
