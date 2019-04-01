package CarPark;


/**
* CarPark/PayStationPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Monday, 1 April 2019 23:20:57 o'clock BST
*/

public abstract class PayStationPOA extends org.omg.PortableServer.Servant
 implements CarPark.PayStationOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("_get_machine_name", new java.lang.Integer (0));
    _methods.put ("registerPaystation", new java.lang.Integer (1));
    _methods.put ("toggleEnabled", new java.lang.Integer (2));
    _methods.put ("reset", new java.lang.Integer (3));
    _methods.put ("pay", new java.lang.Integer (4));
    _methods.put ("return_cash_total", new java.lang.Integer (5));
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

       case 1:  // CarPark/PayStation/registerPaystation
       {
         String machineName = in.read_string ();
         this.registerPaystation (machineName);
         out = $rh.createReply();
         break;
       }

       case 2:  // CarPark/PayStation/toggleEnabled
       {
         this.toggleEnabled ();
         out = $rh.createReply();
         break;
       }

       case 3:  // CarPark/PayStation/reset
       {
         this.reset ();
         out = $rh.createReply();
         break;
       }

       case 4:  // CarPark/PayStation/pay
       {
         String carReg = in.read_string ();
         int duration = in.read_long ();
         double amountPaid = in.read_double ();
         boolean $result = false;
         $result = this.pay (carReg, duration, amountPaid);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 5:  // CarPark/PayStation/return_cash_total
       {
         double $result = (double)0;
         $result = this.return_cash_total ();
         out = $rh.createReply();
         out.write_double ($result);
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
