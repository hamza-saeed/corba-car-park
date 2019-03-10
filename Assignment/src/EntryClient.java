import CarPark.EntryGate;
import CarPark.EntryGateHelper;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntryClient extends JFrame {

    static JButton btnAddReg;
    static JTextField txtReg;

    public EntryClient(){

        JFrame frame = new JFrame("My first GUI");
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);
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

        String  name = "countName";
        EntryGate entry = EntryGateHelper.narrow(nameService.resolve_str(name));


            btnAddReg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    entry.car_entered(txtReg.getText(),10,10);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
