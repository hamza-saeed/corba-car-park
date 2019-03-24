import CarPark.EntryGate;
import CarPark.EntryGateHelper;
import CarPark.Time;
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
        ORB orb = ORB.init(args, null);
        // Get a reference to the Naming service
        org.omg.CORBA.Object nameServiceObj =
                null;
        try {
            nameServiceObj = orb.resolve_initial_references ("NameService");

        if (nameServiceObj == null) {
            System.out.println("nameServiceObj = null");
            return;
        }

        // Use NamingContextExt instead of NamingContext. This is
        // part of the Interoperable naming Service.
        NamingContextExt nameService = NamingContextExtHelper.narrow(nameServiceObj);
        if (nameService == null) {
            System.out.println("nameService = null");
            return;
        }

        String gateName = getArgs(args,"-Name");

        EntryGate entry = EntryGateHelper.narrow(nameService.resolve_str(gateName));

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();


            // create servant and register it with the ORB
            EntryGateImpl entryImpl = new EntryGateImpl();

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
