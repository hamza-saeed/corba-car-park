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

    static LServerImplementation lserverimpl = new LServerImplementation();


    public static void main(String args[]) {
        try {
            lserverimpl.numberOfSpaces = Short.parseShort(getArgs(args, "-Spaces"));
        } catch (Exception e)
        {
            //defaulting.
            lserverimpl.numberOfSpaces = 200;
        }
        setupClientServerConnections(args);
    }

    public static void setupClientServerConnections(String args[]) {
        String localServerName = getArgs(args,"-Name");

        try {
            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object nameServiceObject = orb.resolve_initial_references("NameService");
            if (nameServiceObject == null) {
                System.out.println("nameServiceObject = null");
            }

            NamingContextExt nameService = NamingContextExtHelper.narrow(nameServiceObject);
            if (nameService == null) {
                System.out.println("nameService=null");
            }

            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            HQServer hqRef = HQServerHelper.narrow(nameService.resolve_str("HQConn"));
            lserverimpl.HQRef = hqRef;

            if (hqRef.isLocalServerNameUnique(localServerName)) {
                lserverimpl.registerLocalServer(localServerName);

                org.omg.CORBA.Object lserverRef = rootpoa.servant_to_reference(lserverimpl);
                LocalServer cref = LocalServerHelper.narrow(lserverRef);
                NameComponent[] lserverName = nameService.to_name(localServerName);
                nameService.rebind(lserverName, cref);

                System.out.println("Server started...");
                orb.run();
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Local server name must be unique");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
