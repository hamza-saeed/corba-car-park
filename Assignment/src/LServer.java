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

            // Create the Entry servant object
            EntryGateImpl entryImpl = new EntryGateImpl();
            // get object reference from the servant
            org.omg.CORBA.Object entryRef = rootpoa.servant_to_reference(entryImpl);
            EntryGate entryCref = EntryGateHelper.narrow(entryRef);

            // Create the Paystation servant object
            PayStationImpl payStationImpl = new PayStationImpl();
            // get object reference from the servant
            org.omg.CORBA.Object payRef = rootpoa.servant_to_reference(payStationImpl);
            PayStation payCRef = PayStationHelper.narrow(payRef);

            // Create the Exit servant object
            ExitGateImpl exitGateImpl = new ExitGateImpl();
            // get object reference from the servant
            org.omg.CORBA.Object exitRef = rootpoa.servant_to_reference(exitGateImpl);
            ExitGate exitCRef = ExitGateHelper.narrow(exitRef);

            //get arguments
            String lServerName = getArgs(args, "-Name");
            String[] entryGates = getArgs(args, "-EntryGates").split(",");
            String[] payStations = getArgs(args, "-PayStations").split(",");
            String[] exitGates = getArgs(args, "-ExitGates").split(",");

            // bind the entry object sin the Naming service
            for (String entryNameStr : entryGates)
            {
                System.out.println("binding entry: " + entryNameStr);
                NameComponent[] entryName = nameService.to_name(entryNameStr);
                nameService.rebind(entryName, entryCref);
            }
            // bind the paystation objects in the Naming service
            for (String payStationNameStr : payStations)
            {
                System.out.println("binding pay: " + payStationNameStr);
                NameComponent[] payStationName = nameService.to_name(payStationNameStr);
                nameService.rebind(payStationName, payCRef);
            }
            // bind the exitgate objects in the Naming service
            for (String exitNameStr : exitGates)
            {
                NameComponent[] exitName = nameService.to_name(exitNameStr);
                nameService.rebind(exitName, exitCRef);
            }


            HQImpl hqImpl = new HQImpl();
            // get object reference from the servant
            org.omg.CORBA.Object hqRef = rootpoa.servant_to_reference(hqImpl);
            HQServer hqCRef = HQServerHelper.narrow(hqRef);
            NameComponent[] hqName = nameService.to_name(lServerName + "HQCon");
            nameService.rebind(hqName, hqCRef);


            org.omg.CORBA.Object nameServiceObj1 = orb.resolve_initial_references("NameService");

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

            LocalServer lServer = LocalServerHelper.narrow(nameService1.resolve_str(lServerName));

            // get reference to rootpoa & activate the POAManager
            rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();


            // create servant and register it with the ORB
            LServerImpl lServerImp = new LServerImpl();

            // Get the 'stringified IOR'
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(lServerImp);
            String stringified_ior =
                    orb.object_to_string(ref);

            lServer.registerLocalServer(lServerName, stringified_ior);


            //  wait for invocations from clients
            orb.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getArgs(String[] args, String var) {
        for (int i = 0; i < args.length; i++) {
            String param = args[i];
            if (param.toLowerCase().equals(var.toLowerCase())) {
                return args[i + 1];
            }
        }
        return "Unnamed";
    }

}
