import CarPark.HQServer;
import CarPark.HQServerHelper;
import CarPark.LocalServer;
import CarPark.LocalServerHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import javax.swing.*;

public class LServer extends JFrame {

    private static LServerImplementation lserverimpl = new LServerImplementation();


    public static void main(String args[]) {
        try {
            //get number of spaces in local server from arguments
            lserverimpl.numberOfSpaces = Short.parseShort(getArgs(args, "-Spaces"));
        } catch (Exception e) {
            //defaulting to 200 if none entered/invalid value entered
            lserverimpl.numberOfSpaces = 200;
        }

        //set up client/server interaction
        setupClientServerConnections(args);
    }

    public static void setupClientServerConnections(String args[]) {
        //get the name of the local server
        String localServerName = getArgs(args, "-Name");

        try {
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            //get reference to naming service
            org.omg.CORBA.Object nameServiceObject = orb.resolve_initial_references("NameService");
            if (nameServiceObject == null) {
                System.out.println("nameServiceObject = null");
            }
            NamingContextExt nameService = NamingContextExtHelper.narrow(nameServiceObject);
            if (nameService == null) {
                System.out.println("nameService=null");
            }

            //resolve the Headquarters object reference in the naming service
            HQServer hqRef = HQServerHelper.narrow(nameService.resolve_str("HQConn"));
            //set reference in local server implementation
            lserverimpl.HQRef = hqRef;

            //only continue if the set name is unique
            if (hqRef.isLocalServerNameUnique(localServerName)) {

                //register the gate with HQ
                lserverimpl.registerLocalServer(localServerName);

                //get object reference from the servant
                org.omg.CORBA.Object lserverRef = rootpoa.servant_to_reference(lserverimpl);
                LocalServer cref = LocalServerHelper.narrow(lserverRef);

                //bind the object in the naming service
                NameComponent[] lserverName = nameService.to_name(localServerName);
                nameService.rebind(lserverName, cref);

                System.out.println("Server started...");

                //  wait for invocations from clients
                orb.run();
            } else {
                //must be unique!
                System.out.println("Local Server name must be unique");
                return;
            }
        } catch (Exception e) {
            //can't connect to server
            System.out.println("Can not connect to server");
            System.out.println(e);
        }

    }

    /*
    Return the argument passed in corresponding to String var
    */
    private static String getArgs(String[] args, String var) {
        for (int i = 0; i < args.length; i++) {
            String param = args[i];
            if (param.toLowerCase().equals(var.toLowerCase())) {
                return args[i + 1];
            }
        }
        return "Unnamed";
    }


}
