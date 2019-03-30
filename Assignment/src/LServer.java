import CarPark.*;
import org.omg.CORBA.*;
import org.omg.CORBA.Object;
import org.omg.CosNaming.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class LServer {

    public static LServerImpl lserver = new LServerImpl();


    static public void main(String[] args) {

        setupClientServerConnections(args);
    }


    private static void setupClientServerConnections(String[] args)
    {
        try {
            // Initialize the ORB
            ORB orb = ORB.init(args, null);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Get a reference to the Naming service
            Object nameServiceObj = orb.resolve_initial_references("NameService");
            if (nameServiceObj == null) {
                System.out.println("nameServiceObj = null");
                return;
            }

            //only continue if the name service references are not null
            NamingContextExt nameServiceClients = NamingContextExtHelper.narrow(nameServiceObj);
            if (nameServiceClients == null) {
                System.out.println("nameServiceClient = null");
                return;
            }
            NamingContextExt nameServiceServer = NamingContextExtHelper.narrow(nameServiceObj);
            if (nameServiceServer == null) {
                System.out.println("nameServiceServer = null");
                return;
            }

            /* Make the local server into a server so that the
            entry gate, paystation and exit gate can connect to it. */


            // get entry gate object reference from the servant
            Object entryRef = rootpoa.servant_to_reference(EntryClient.entryImpl);
            EntryGate entryCref = EntryGateHelper.narrow(entryRef);

            // get paystation object reference from the servant
            Object payRef = rootpoa.servant_to_reference(PayStationClient.payStationImpl);
            PayStation payCRef = PayStationHelper.narrow(payRef);

            // get exit gate object reference from the servant
            Object exitRef = rootpoa.servant_to_reference(ExitClient.exitImpl);
            ExitGate exitCRef = ExitGateHelper.narrow(exitRef);

            //get the names for the local server, entry gate, paystation and exit gates
            String lServerName = getArgs(args, "-Name");
            String[] entryGates = getArgs(args, "-EntryGates").split(",");
            String[] payStations = getArgs(args, "-PayStations").split(",");
            String[] exitGates = getArgs(args, "-ExitGates").split(",");

            // bind the entry object in the Naming service
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

            /*Make the LServer into a client
            so that it can connect to the HQ. */


            // get object reference from the servant
            Object hqRef = rootpoa.servant_to_reference(HQ.hqimp);
            HQServer hqCRef = HQServerHelper.narrow(hqRef);
            NameComponent[] hqName = nameServiceClients.to_name(lServerName + "HQCon");
            nameServiceClients.rebind(hqName, hqCRef);

            // create servant and register it with the ORB
            LocalServer lServer = LocalServerHelper.narrow(nameServiceServer.resolve_str(lServerName));

            // get reference to rootpoa & activate the POAManager
            rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Get the 'stringified IOR'
            Object ref = rootpoa.servant_to_reference(LServer.lserver);
            String stringified_ior =
                    orb.object_to_string(ref);

            //register server with HQ
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
