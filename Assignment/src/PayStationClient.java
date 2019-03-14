import CarPark.Date;
import CarPark.PayStation;
import CarPark.PayStationHelper;
import CarPark.Time;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PayStationClient extends JFrame {

    static JButton btnAddReg;
    static JTextField txtReg;
    static JTextField txtHrs;
    static JLabel lblCost;
    static JButton btnPay;
    static double hrsStayed;
    static double cost;

    public PayStationClient() {
        JFrame frame = new JFrame("Pay Station");
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);


        JLabel lbl1 = new JLabel("Pay Station");
        JLabel lbl2 = new JLabel("Vehicle Registration:");
        txtReg = new JTextField(30);
        txtHrs = new JTextField(5);
        btnAddReg = new JButton("Press");

        lblCost = new JLabel();
        btnPay = new JButton("Pay");

        panel.add(lbl1);
        panel.add(lbl2);
        panel.add(txtReg);
        panel.add(txtHrs);
        panel.add(btnAddReg); // Adds Button to content pane of frame
        panel.add(lblCost);

        panel.add(btnPay);
        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        PayStationClient paystation = new PayStationClient();

        try {

            // Initialize the ORB
            System.out.println("Initializing the ORB");
            ORB orb = ORB.init(args, null);

            // Get a reference to the Naming service
            org.omg.CORBA.Object nameServiceObj =
                    null;

            nameServiceObj = orb.resolve_initial_references("NameService");

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

            String name = "PayStationClient";
            PayStation payStation = PayStationHelper.narrow(nameService.resolve_str(name));
            //TODO: Get Reg - check if they entered recently.

            //TODO: Show what time they need to leave.

            btnAddReg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        hrsStayed = Math.ceil(Double.parseDouble(txtHrs.getText()));
                        cost = hrsStayed * 1;
                        lblCost.setText("£" + cost);
                    } catch (NumberFormatException nf) {
                        JOptionPane.showMessageDialog(null, "Input must be a number");
                    }
                }
            });


            btnPay.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String reg = txtReg.getText();

                    LocalDateTime dateTime = LocalDateTime.now();

                    Date payDate = new Date();
                    payDate.day = dateTime.getDayOfMonth();
                    payDate.month = dateTime.getMonth().getValue();
                    payDate.year = dateTime.getYear();

                    Time payTime = new Time();
                    payTime.hr = dateTime.getHour();
                    payTime.min = dateTime.getMinute();
                    payTime.sec = dateTime.getSecond();

                    LocalDateTime expiry = dateTime.plusHours((long)cost);

                    System.out.println("Expiry Date" + expiry.getDayOfMonth() + "/" + expiry.getMonth().getValue() + expiry.getYear() + "." + "Time: " + expiry.getHour() + ":" + expiry.getMinute() + ":" + expiry.getSecond());



                    if (payStation.pay(txtReg.getText(),payDate, payTime,4)){
                        StringBuilder ticket = new StringBuilder();
                        ticket.append("Car Reg: " + reg);
                        ticket.append("Entered: " + payDate.day + "/" + payDate.month + "/" + payDate.year);
                        ticket.append(payTime.hr + ":" + payTime.min + ":" + payTime.sec);
                        ticket.append("Leave by: " + payDate.day + "/" + payDate.month + "/" + payDate.year);
                    }
                }
            });

        } catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }
}