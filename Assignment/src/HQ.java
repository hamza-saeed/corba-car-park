import CarPark.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HQ extends JFrame {
    static JTable table;
    static DefaultTableModel dtm;
    static JButton btnUpdate;
    public HQ()
    {
        JFrame frame = new JFrame("HEADQUARTERS");
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,500);
        JLabel lbl1 = new JLabel("HQ");
        btnUpdate = new JButton("Update");
        dtm = new DefaultTableModel();
        dtm.addColumn("LocalServer");
        dtm.addColumn("Enabled");
        table = new JTable(dtm);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setBounds(30,40,200,300);
        panel.add(lbl1);
        panel.add(scrollPane);
        panel.add(btnUpdate);
        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        HQ hq = new HQ();
        try {
            // Initialize the ORB
            ORB orb = ORB.init(args, null);
            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Create the Count servant object
            LServerImpl lServerImpl = new LServerImpl();

            // get object reference from the servant
            org.omg.CORBA.Object lServerRef = rootpoa.servant_to_reference(lServerImpl);
            LocalServer lServerCref = LocalServerHelper.narrow(lServerRef);

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

            String[] localServers = getArgs(args, "-LocalServers").split(",");

            for (String localServerNameStr : localServers)
            {
                System.out.println("binding lserver: " + localServerNameStr);
                NameComponent[] lServerNames = nameService.to_name(localServerNameStr);
                nameService.rebind(lServerNames, lServerCref);
            }


            btnUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (HQImpl.listOfLocalServers != null) {
                        dtm.setNumRows(0);
                        for (Machine a : HQImpl.listOfLocalServers) {
                            dtm.addRow(new String[]{a.name, "Yes"});
                        }
                    }
                }
            });

            ListSelectionModel cellSelectionModel = table.getSelectionModel();
            cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {

                    try {
                    if (e.getValueIsAdjusting()) {
                        // Get a reference to the Naming service
                        org.omg.CORBA.Object nameServiceObj1 =
                                null;

                        nameServiceObj1 = orb.resolve_initial_references("NameService");

                        if (nameServiceObj1 == null) {
                            System.out.println("nameServiceObj = null");
                            return;
                        }

                        // Use NamingContextExt instead of NamingContext. This is
                        // part of the Interoperable naming Service.
                        NamingContextExt nameService1 = NamingContextExtHelper.narrow(nameServiceObj1);
                        if (nameService1 == null) {
                            System.out.println("nameService = null");
                            return;
                        }


                        HQServer headquarters = HQServerHelper.narrow(nameService1.resolve_str( "qwerty"));


                        //HQImpl.listOfLocalServers.get(table.getSelectedRow());
                        System.out.println("List of entry gates:");

                        for (Machine entryG : headquarters.returnEntryGates())
//                        for (Machine entryG : lServerCref.logOfEntryGates())
                        {
                            dtm.addRow(new String[] {"ENTRY GATE: " + entryG.name});
                                System.out.println(entryG.name);
                        }
                        System.out.println("List of paystations:");

                        for (Machine pay : headquarters.returnPayStations()) {
                            dtm.addRow(new String[] {"ENTRY GATE: " + pay.name});
                            System.out.println(pay.name);
                        }
                        System.out.println("List of exit gates:");

                        for (Machine exit : headquarters.returnExitGates()) {
                            dtm.addRow(new String[] {"ENTRY GATE: " + exit.name});
                            System.out.println(exit.name);
                        }
                    }
                    } catch (Exception changeError)
                    {
                        changeError.printStackTrace();
                    }

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
