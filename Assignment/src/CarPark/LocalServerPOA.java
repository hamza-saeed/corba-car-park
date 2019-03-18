package CarPark;


/**
* CarPark/LocalServerPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CarPark.idl
* Monday, 18 March 2019 15:29:12 o'clock GMT
*/

public abstract class LocalServerPOA extends org.omg.PortableServer.Servant
 implements CarPark.LocalServerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("_get_location", new java.lang.Integer (0));
    _methods.put ("_get_log", new java.lang.Integer (1));
    _methods.put ("vehicle_in", new java.lang.Integer (2));
    _methods.put ("vehicle_out", new java.lang.Integer (3));
    _methods.put ("vehicle_paid", new java.lang.Integer (4));
    _methods.put ("add_Ticket", new java.lang.Integer (5));
    _methods.put ("vehicle_in_car_park", new java.lang.Integer (6));
    _methods.put ("return_cash_total", new java.lang.Integer (7));
    _methods.put ("add_entry_gate", new java.lang.Integer (8));
    _methods.put ("add_exit_gate", new java.lang.Integer (9));
    _methods.put ("add_pay_station", new java.lang.Integer (10));
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
       case 0:  // CarPark/LocalServer/_get_location
       {
         String $result = null;
         $result = this.location ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // CarPark/LocalServer/_get_log
       {
         CarPark.VehicleEvent $result[] = null;
         $result = this.log ();
         out = $rh.createReply();
         CarPark.Log_of_vehicle_eventsHelper.write (out, $result);
         break;
       }

       case 2:  // CarPark/LocalServer/vehicle_in
       {
         CarPark.VehicleEvent event = CarPark.VehicleEventHelper.read (in);
         this.vehicle_in (event);
         out = $rh.createReply();
         break;
       }

       case 3:  // CarPark/LocalServer/vehicle_out
       {
         CarPark.VehicleEvent event = CarPark.VehicleEventHelper.read (in);
         this.vehicle_out (event);
         out = $rh.createReply();
         break;
       }

       case 4:  // CarPark/LocalServer/vehicle_paid
       {
         CarPark.VehicleEvent event = CarPark.VehicleEventHelper.read (in);
         boolean $result = false;
         $result = this.vehicle_paid (event);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 5:  // CarPark/LocalServer/add_Ticket
       {
         CarPark.Ticket newTicket = CarPark.TicketHelper.read (in);
         boolean $result = false;
         $result = this.add_Ticket (newTicket);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 6:  // CarPark/LocalServer/vehicle_in_car_park
       {
         String registration_number = in.read_string ();
         boolean $result = false;
         $result = this.vehicle_in_car_park (registration_number);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 7:  // CarPark/LocalServer/return_cash_total
       {
         double $result = (double)0;
         $result = this.return_cash_total ();
         out = $rh.createReply();
         out.write_double ($result);
         break;
       }

       case 8:  // CarPark/LocalServer/add_entry_gate
       {
         String gate_name = in.read_string ();
         String gate_ior = in.read_string ();
         this.add_entry_gate (gate_name, gate_ior);
         out = $rh.createReply();
         break;
       }

       case 9:  // CarPark/LocalServer/add_exit_gate
       {
         String gate_name = in.read_string ();
         String gate_ior = in.read_string ();
         this.add_exit_gate (gate_name, gate_ior);
         out = $rh.createReply();
         break;
       }

       case 10:  // CarPark/LocalServer/add_pay_station
       {
         String station_name = in.read_string ();
         String station_ior = in.read_string ();
         this.add_pay_station (station_name, station_ior);
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
    "IDL:CarPark/LocalServer:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public LocalServer _this() 
  {
    return LocalServerHelper.narrow(
    super._this_object());
  }

  public LocalServer _this(org.omg.CORBA.ORB orb) 
  {
    return LocalServerHelper.narrow(
    super._this_object(orb));
  }


} // class LocalServerPOA
