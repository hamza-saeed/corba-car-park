import CarPark.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class LServer {

    static public void main(String[] args) {

   try {
       // Initialize the ORB
       ORB orb = ORB.init(args, null);
       // get reference to rootpoa & activate the POAManager
       POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
       rootpoa.the_POAManager().activate();

       // Create the Count servant object
       EntryGateImpl entryImpl = new EntryGateImpl();

       // get object reference from the servant
       org.omg.CORBA.Object entryRef = rootpoa.servant_to_reference(entryImpl);
       EntryGate entryCref = EntryGateHelper.narrow(entryRef);

//       // Create the Count servant object
       PayStationImpl payStationImpl = new PayStationImpl();



       //
//       // get object reference from the servant
       org.omg.CORBA.Object payRef = rootpoa.servant_to_reference(payStationImpl);
       PayStation payCRef = PayStationHelper.narrow(payRef);


       // Get a reference to the Naming service
       org.omg.CORBA.Object nameServiceObj =
               orb.resolve_initial_references("NameService");
       if (nameServiceObj == null) {
           System.out.println("nameServiceObj = null");
           return;
       }

       // Use NamingContextExt which is part of the Interoperable
       // Naming Service (INS) specification.
       NamingContextExt nameService = NamingContextExtHelper.narrow(nameServiceObj);
       if (nameService == null) {
           System.out.println("nameService = null");
           return;
       }

       // bind the Count object in the Naming service
       String entryN = "EntryClient";
       NameComponent[] entryName = nameService.to_name(entryN);
       nameService.rebind(entryName, entryCref);
//

       // bind the Count object in the Naming service
       String payN = "PayStationClient";
       NameComponent[] payName = nameService.to_name(payN);
       nameService.rebind(payName, payCRef);





       org.omg.CORBA.Object nameServiceObj1  = orb.resolve_initial_references("NameService");

           if (nameServiceObj1 == null) {
               System.out.println("nameServiceObj1 = null");
               return;
           }

           // Use NamingContextExt instead of NamingContext. This is
           // part of the Interoperable naming Service.
           NamingContextExt nameService1 = NamingContextExtHelper.narrow(nameServiceObj1);
           if (nameService1 == null) {
               System.out.println("nameService1 = null");
               return;
           }

           String  name = "LocalServer";
       LocalServer lServer = LocalServerHelper.narrow(nameService1.resolve_str(name));

       String lServerName = "lServer";
       for (int i = 0; i < args.length; i++)
       {
           String param = args[i];
           if (param.toLowerCase().equals("-name"))
           {
               lServerName = args[i+1];
           }
       }
       lServer.registerLocalServer(lServerName);


       //  wait for invocations from clients
       orb.run();

   } catch (Exception e) {
    e.printStackTrace();
   }
   }
}
