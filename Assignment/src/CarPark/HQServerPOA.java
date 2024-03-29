package CarPark;


/**
* CarPark/HQServerPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Thursday, 4 April 2019 16:06:45 o'clock BST
*/

public abstract class HQServerPOA extends org.omg.PortableServer.Servant
 implements CarPark.HQServerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("_get_listOfAlerts", new java.lang.Integer (0));
    _methods.put ("_get_listOfLocalServers", new java.lang.Integer (1));
    _methods.put ("raise_alarm", new java.lang.Integer (2));
    _methods.put ("register_local_server", new java.lang.Integer (3));
    _methods.put ("isLocalServerNameUnique", new java.lang.Integer (4));
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
       case 0:  // CarPark/HQServer/_get_listOfAlerts
       {
         CarPark.ParkingTransaction $result[] = null;
         $result = this.listOfAlerts ();
         out = $rh.createReply();
         CarPark.List_of_parking_transactionsHelper.write (out, $result);
         break;
       }

       case 1:  // CarPark/HQServer/_get_listOfLocalServers
       {
         CarPark.Machine $result[] = null;
         $result = this.listOfLocalServers ();
         out = $rh.createReply();
         CarPark.List_of_machinesHelper.write (out, $result);
         break;
       }

       case 2:  // CarPark/HQServer/raise_alarm
       {
         CarPark.ParkingTransaction transaction = CarPark.ParkingTransactionHelper.read (in);
         this.raise_alarm (transaction);
         out = $rh.createReply();
         break;
       }

       case 3:  // CarPark/HQServer/register_local_server
       {
         CarPark.Machine machine = CarPark.MachineHelper.read (in);
         this.register_local_server (machine);
         out = $rh.createReply();
         break;
       }

       case 4:  // CarPark/HQServer/isLocalServerNameUnique
       {
         String name = in.read_string ();
         boolean $result = false;
         $result = this.isLocalServerNameUnique (name);
         out = $rh.createReply();
         out.write_boolean ($result);
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
