package CarPark;


/**
* CarPark/ExitGatePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Thursday, 14 March 2019 10:41:23 o'clock GMT
*/

public abstract class ExitGatePOA extends org.omg.PortableServer.Servant
 implements CarPark.ExitGateOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("_get_machine_name", new java.lang.Integer (0));
    _methods.put ("registerGate", new java.lang.Integer (1));
    _methods.put ("car_exited", new java.lang.Integer (2));
    _methods.put ("turn_on", new java.lang.Integer (3));
    _methods.put ("turn_off", new java.lang.Integer (4));
    _methods.put ("reset", new java.lang.Integer (5));
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
       case 0:  // CarPark/ExitGate/_get_machine_name
       {
         String $result = null;
         $result = this.machine_name ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // CarPark/ExitGate/registerGate
       {
         String machineName = in.read_string ();
         this.registerGate (machineName);
         out = $rh.createReply();
         break;
       }

       case 2:  // CarPark/ExitGate/car_exited
       {
         String reg = in.read_string ();
         CarPark.Date date = CarPark.DateHelper.read (in);
         CarPark.Time time = CarPark.TimeHelper.read (in);
         this.car_exited (reg, date, time);
         out = $rh.createReply();
         break;
       }

       case 3:  // CarPark/ExitGate/turn_on
       {
         this.turn_on ();
         out = $rh.createReply();
         break;
       }

       case 4:  // CarPark/ExitGate/turn_off
       {
         this.turn_off ();
         out = $rh.createReply();
         break;
       }

       case 5:  // CarPark/ExitGate/reset
       {
         this.reset ();
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:CarPark/ExitGate:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public ExitGate _this() 
  {
    return ExitGateHelper.narrow(
    super._this_object());
  }

  public ExitGate _this(org.omg.CORBA.ORB orb) 
  {
    return ExitGateHelper.narrow(
    super._this_object(orb));
  }


} // class ExitGatePOA
