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

    static HQImpl hqimp = new HQImpl();
    static JTable tableServers;
    static DefaultTableModel dtmServers;
    static JButton btnUpdate;
    static JButton btnToggle;

    public HQ()
    {
        JFrame frame = new JFrame("HEADQUARTERS");
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,500);
        JLabel lbl1 = new JLabel("HQ");
        btnUpdate = new JButton("Update");
        btnToggle = new JButton("Toggle");
        dtmServers = new DefaultTableModel();
        dtmServers.addColumn("LocalServer");
        dtmServers.addColumn("Enabled");
        tableServers = new JTable(dtmServers);
        JScrollPane scrollPane = new JScrollPane(tableServers);
        tableServers.setBounds(30,40,200,300);
        panel.add(lbl1);
        panel.add(scrollPane);
        panel.add(btnUpdate);
        panel.add(btnToggle);
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

            // get object reference from the servant
            org.omg.CORBA.Object lServerRef = rootpoa.servant_to_reference(LServer.lserver);
            LocalServer lServerCref = LocalServerHelper.narrow(lServerRef);

            // Get a reference to the Naming service
            org.omg.CORBA.Object nameServiceObjClient =
                    orb.resolve_initial_references("NameService");
            if (nameServiceObjClient == null) {
                System.out.println("nameServiceObjClient = null");
                return;
            }

            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt nameServiceClient = NamingContextExtHelper.narrow(nameServiceObjClient);
            if (nameServiceClient == null) {
                System.out.println("nameServiceClient = null");
                return;
            }

            String[] localServers = getArgs(args, "-LocalServers").split(",");

            for (String localServerNameStr : localServers)
            {
                System.out.println("binding lserver: " + localServerNameStr);
                NameComponent[] lServerNames = nameServiceClient.to_name(localServerNameStr);
                nameServiceClient.rebind(lServerNames, lServerCref);
            }

            //Code to connect to local server

            // Get a reference to the Naming service
            org.omg.CORBA.Object nameServiceObjServers = orb.resolve_initial_references("NameService");

            if (nameServiceObjServers == null) {
                System.out.println("nameServiceObjServers = null");
                return;
            }

            // Use NamingContextExt instead of NamingContext. This is
            // part of the Interoperable naming Service.
            NamingContextExt nameServiceServers = NamingContextExtHelper.narrow(nameServiceObjServers);
            if (nameServiceServers == null) {
                System.out.println("nameServiceServers = null");
                return;
            }

            btnUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (HQImpl.listOfLocalServers != null) {
                        dtmServers.setNumRows(0);
                        for (Machine machine : HQImpl.listOfLocalServers) {
                            dtmServers.addRow(new String[]{machine.name, machine.enabled == true ? "Yes" : "No"});
                        }
                    }
                }
            });

            btnToggle.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = tableServers.getValueAt(tableServers.getSelectedRow(), 0).toString();
                    try {

                        HQServer hqServer = HQServerHelper.narrow(nameServiceServers.resolve_str(name + "HQCon"));
                        hqServer.turn_off_entry_gate();


                    } catch (Exception e2)
                    {
                        e2.printStackTrace();
                    }
                    }
            });

            ListSelectionModel cellSelectionModel = tableServers.getSelectionModel();
            cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {

                    try {
                    if (e.getValueIsAdjusting()) {


                        String a = tableServers.getValueAt(tableServers.getSelectedRow(),0).toString();
                        System.out.println(a);

                        HQServer headquarters = HQServerHelper.narrow(nameServiceServers.resolve_str( a + "HQCon"));


                        //HQImpl.listOfLocalServers.get(tableServers.getSelectedRow());
                        System.out.println("List of entry gates:");

                        for (Machine entryG : headquarters.returnEntryGates())
//                        for (Machine entryG : lServerCref.logOfEntryGates())
                        {
                            dtmServers.addRow(new String[] {entryG.name, entryG.enabled == true ? "Yes" : "No"});
                                System.out.println(entryG.name);
                        }
                        System.out.println("List of paystations:");

                        for (Machine pay : headquarters.returnPayStations()) {
                            dtmServers.addRow(new String[] {pay.name,pay.enabled == true ? "Yes" : "No"});
                            System.out.println(pay.name);
                        }
                        System.out.println("List of exit gates:");

                        for (Machine exit : headquarters.returnExitGates()) {
                            dtmServers.addRow(new String[] {exit.name,exit.enabled == true ? "Yes" : "No"});
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
