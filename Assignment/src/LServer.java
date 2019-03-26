import CarPark.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class LServer {

    public static LServerImpl lserver = new LServerImpl();


    static public void main(String[] args) {

        try {
            // Initialize the ORB
            ORB orb = ORB.init(args, null);
            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Get a reference to the Naming service
            org.omg.CORBA.Object nameServiceObjClients =
                    orb.resolve_initial_references("NameService");
            if (nameServiceObjClients == null) {
                System.out.println("nameServiceObjClient = null");
                return;
            }

            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt nameServiceClients = NamingContextExtHelper.narrow(nameServiceObjClients);
            if (nameServiceClients == null) {
                System.out.println("nameServiceClient = null");
                return;
            }

            // Create the Entry servant object
            // get object reference from the servant
            org.omg.CORBA.Object entryRef = rootpoa.servant_to_reference(EntryClient.entryImpl);
            EntryGate entryCref = EntryGateHelper.narrow(entryRef);

            // get object reference from the servant
            org.omg.CORBA.Object payRef = rootpoa.servant_to_reference(PayStationClient.payStationImpl);
            PayStation payCRef = PayStationHelper.narrow(payRef);

            // Create the Exit servant object
            // get object reference from the servant
            org.omg.CORBA.Object exitRef = rootpoa.servant_to_reference(ExitClient.exitImpl);
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
                NameComponent[] entryName = nameServiceClients.to_name(entryNameStr);
                nameServiceClients.rebind(entryName, entryCref);
            }
            // bind the paystation objects in the Naming service
            for (String payStationNameStr : payStations)
            {
                System.out.println("binding pay: " + payStationNameStr);
                NameComponent[] payStationName = nameServiceClients.to_name(payStationNameStr);
                nameServiceClients.rebind(payStationName, payCRef);
            }
            // bind the exitgate objects in the Naming service
            for (String exitNameStr : exitGates)
            {
                NameComponent[] exitName = nameServiceClients.to_name(exitNameStr);
                nameServiceClients.rebind(exitName, exitCRef);
            }


            // get object reference from the servant
            org.omg.CORBA.Object hqRef = rootpoa.servant_to_reference(HQ.hqimp);
            HQServer hqCRef = HQServerHelper.narrow(hqRef);
            NameComponent[] hqName = nameServiceClients.to_name(lServerName + "HQCon");
            nameServiceClients.rebind(hqName, hqCRef);


            org.omg.CORBA.Object nameServiceObjServer = orb.resolve_initial_references("NameService");

            if (nameServiceObjServer == null) {
                System.out.println("nameServiceObjServer = null");
                return;
            }

            // Use NamingContextExt instead of NamingContext. This is
            // part of the Interoperable naming Service.
            NamingContextExt nameServiceServer = NamingContextExtHelper.narrow(nameServiceObjServer);
            if (nameServiceServer == null) {
                System.out.println("nameServiceServer = null");
                return;
            }

            LocalServer lServer = LocalServerHelper.narrow(nameServiceServer.resolve_str(lServerName));

            // get reference to rootpoa & activate the POAManager
            rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();


            // create servant and register it with the ORB

            // Get the 'stringified IOR'
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(LServer.lserver);
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
