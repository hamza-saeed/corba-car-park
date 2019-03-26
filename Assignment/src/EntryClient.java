import CarPark.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class EntryClient extends JFrame {

    static JButton btnAddReg;
    static JTextField txtReg;
    static EntryGateImpl entryImpl = new EntryGateImpl();

    public EntryClient(){

        JFrame frame = new JFrame("Entry Client");
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        JLabel lbl1 = new JLabel("Car Park Entry Gate");
        JLabel lbl2 = new JLabel("Vehicle Registration:");
         txtReg = new JTextField(30);
         btnAddReg = new JButton("Press");

        panel.add(lbl1);
        panel.add(lbl2);
        panel.add(txtReg);
        panel.add(btnAddReg); // Adds Button to content pane of frame

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String args[])
    {
        EntryClient entryClient = new EntryClient();


        // Initialize the ORB
        System.out.println("Initializing the ORB");

        try {
            ORB orb = ORB.init(args, null);

            //get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            //Get a reference to the Naming service
            org.omg.CORBA.Object nameServiceObjClients = orb.resolve_initial_references("NameService");
            if (nameServiceObjClients == null) {
                System.out.println("nameServiceObjClients = null");
                return;
            }

            // Use NamingContextExt instead of NamingContext. This is
            // part of the Interoperable naming Service.
            NamingContextExt nameServiceClients = NamingContextExtHelper.narrow(nameServiceObjClients);
            if (nameServiceClients == null) {
                System.out.println("nameServiceClients = null");
                return;
            }

            // Create the Entry servant object
            HQImpl hqImpl = new HQImpl();
            // get object reference from the servant
            org.omg.CORBA.Object hqref = rootpoa.servant_to_reference(hqImpl);
            HQServer hqCref = HQServerHelper.narrow(hqref);


            String gateName = getArgs(args,"-Name");

            NameComponent[] hqName = nameServiceClients.to_name(gateName + "HQCon");
            nameServiceClients.rebind(hqName, hqCref);
            EntryGateImpl.EntryGateName = gateName;


            org.omg.CORBA.Object nameServiceObjServer = orb.resolve_initial_references ("NameService");

        if (nameServiceObjServer == null) {
            System.out.println("nameServiceObjServer= null");
            return;
        }

        // Use NamingContextExt instead of NamingContext. This is
        // part of the Interoperable naming Service.
        NamingContextExt nameServiceServer = NamingContextExtHelper.narrow(nameServiceObjServer);
        if (nameServiceServer == null) {
            System.out.println("nameServiceServer = null");
            return;
        }

        // create servant and register it with the ORB
        EntryGate entry = EntryGateHelper.narrow(nameServiceServer.resolve_str(gateName));


            // Get the 'stringified IOR'
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(entryImpl);
            String stringified_ior =
                    orb.object_to_string(ref);

            entry.registerGate(gateName, stringified_ior);

            btnAddReg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LocalDateTime a = LocalDateTime.now();
                    CarPark.Date date = new CarPark.Date();
                    Time time = new Time();
                    date.day = a.getDayOfMonth();
                    date.month = a.getMonth().getValue();
                    date.year = a.getYear();
                    time.hr = a.getHour();
                    time.min = a.getMinute();
                    time.sec = a.getSecond();

                    entry.car_entered(txtReg.getText(),date,time);
                }
            });

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
