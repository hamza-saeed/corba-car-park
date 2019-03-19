import CarPark.*;
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
                    Date expiryDate = new Date();
                    expiryDate.day = dateTime.getDayOfMonth();
                    expiryDate.month = dateTime.getMonth().getValue();
                    expiryDate.year = dateTime.getYear();

                    Time expiryTime = new Time();
                    expiryTime.hr = dateTime.getHour();
                    expiryTime.min = dateTime.getMinute();
                    expiryTime.sec = dateTime.getSecond();


                    if (payStation.pay(txtReg.getText(),payDate, payTime,4)){
                        StringBuilder ticket = new StringBuilder();
//                        ticket.append("Car Reg: " + reg + "\n");
//                        ticket.append("Entered: " + payDate.day + "/" + payDate.month + "/" + payDate.year + " " );
//                        ticket.append(payTime.hr + ":" + payTime.min + ":" + payTime.sec + "\n");
//                        ticket.append("Leave by: " + expiry.getDayOfMonth() + "/" + expiry.getMonth().getValue() + "/" +  expiry.getYear() + " ");
//                        ticket.append(expiry.getHour() + ":" + expiry.getMinute() + ":" + expiry.getSecond());
                        Ticket newTicket = new Ticket();
                        System.out.println("Reg:" + reg);
                        System.out.println("amountPaid:" + cost);
                        System.out.println("dateEntered:" + payDate);
                        System.out.println("timeEntered:" + payTime);
                        System.out.println("dateToLeave:" + expiryDate);
                        System.out.println("timeEntered:" + expiryTime);


                        newTicket.registration_number = reg;
                        newTicket.amountPaid = cost;
                        newTicket.dateEntered = payDate;
                        newTicket.timeEntered = payTime;
                        newTicket.dateToLeave = expiryDate;
                        newTicket.timeToLeave = expiryTime;
                        payStation.createTicket(newTicket);
//                        System.out.println(ticket.toString());
                    }

                }
            });

        } catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }
}