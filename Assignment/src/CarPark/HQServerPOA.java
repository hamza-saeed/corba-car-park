package CarPark;


/**
* CarPark/HQServerPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Tuesday, 26 March 2019 18:12:28 o'clock GMT
*/

public abstract class HQServerPOA extends org.omg.PortableServer.Servant
 implements CarPark.HQServerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("raise_alarm", new java.lang.Integer (0));
    _methods.put ("register_local_server", new java.lang.Integer (1));
    _methods.put ("returnEntryGates", new java.lang.Integer (2));
    _methods.put ("returnPayStations", new java.lang.Integer (3));
    _methods.put ("returnExitGates", new java.lang.Integer (4));
    _methods.put ("turn_off_entry_gate", new java.lang.Integer (5));
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
       case 0:  // CarPark/HQServer/raise_alarm
       {
         CarPark.VehicleEvent event = CarPark.VehicleEventHelper.read (in);
         this.raise_alarm (event);
         out = $rh.createReply();
         break;
       }

       case 1:  // CarPark/HQServer/register_local_server
       {
         CarPark.Machine machine = CarPark.MachineHelper.read (in);
         this.register_local_server (machine);
         out = $rh.createReply();
         break;
       }

       case 2:  // CarPark/HQServer/returnEntryGates
       {
         CarPark.Machine $result[] = null;
         $result = this.returnEntryGates ();
         out = $rh.createReply();
         CarPark.List_of_entryGatesHelper.write (out, $result);
         break;
       }

       case 3:  // CarPark/HQServer/returnPayStations
       {
         CarPark.Machine $result[] = null;
         $result = this.returnPayStations ();
         out = $rh.createReply();
         CarPark.List_of_payStationsHelper.write (out, $result);
         break;
       }

       case 4:  // CarPark/HQServer/returnExitGates
       {
         CarPark.Machine $result[] = null;
         $result = this.returnExitGates ();
         out = $rh.createReply();
         CarPark.List_of_exitGatesHelper.write (out, $result);
         break;
       }

       case 5:  // CarPark/HQServer/turn_off_entry_gate
       {
         this.turn_off_entry_gate ();
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
    "IDL:CarPark/HQServer:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public HQServer _this() 
  {
    return HQServerHelper.narrow(
    super._this_object());
  }

  public HQServer _this(org.omg.CORBA.ORB orb) 
  {
    return HQServerHelper.narrow(
    super._this_object(orb));
  }


} // class HQServerPOA
