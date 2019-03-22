import CarPark.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import javax.swing.*;
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

            // bind the Count object in the Naming service
            String localServerName = "LocalServer";
            NameComponent[] lServerNames = nameService.to_name(localServerName);
            nameService.rebind(lServerNames, lServerCref);
//

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

            orb.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}
