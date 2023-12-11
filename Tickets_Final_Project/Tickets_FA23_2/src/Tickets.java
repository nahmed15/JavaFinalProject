
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	private JPanel bottomPanel;
	 	private JTextField txtUttD;
	    private JTextField txtUser;
	    private JTextField txtTicketDesc;
	    private JTextField txtStartDate;
	    private JTextField txtEndDate;

	    // Buttons
	    private JButton btnRefresh;
	    private JButton btnUpdate;
	    private JButton btnDelete;
	    private JButton addNew;
	public Tickets(Boolean isAdmin) {

		chkIfAdmin = isAdmin;
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);

		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above
		 */
	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuAdmin);
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);


	    // Create the bottom panel
	    bottomPanel = new JPanel();
	    bottomPanel.setBackground(Color.GRAY);
	    bottomPanel.setLayout(new GridLayout(4, 4, 10, 10));  // Set the layout manager to GridLayout

	    // Create user input fields
	    txtUttD = new JTextField(15);
	    txtUttD.setEnabled(false);
	    txtUser = new JTextField(15);
	    txtTicketDesc = new JTextField(15);
	    txtStartDate = new JTextField(15);
	    txtStartDate.setToolTipText("yyyy-MM-DD");
	    txtStartDate.setText("yyyy-MM-DD");
	    txtEndDate = new JTextField(15);
	    txtEndDate.setToolTipText("YYYY-MM-DD");
	    txtEndDate.setText("YYYY-MM-DD");

	    // Create buttons
	    btnRefresh = new JButton("Refresh");
	    btnUpdate = new JButton("Update");
	    btnDelete = new JButton("Delete");
	    addNew=new JButton("ADD NEW");
	    
	  

	    // Add components to the bottom panel
	    bottomPanel.add(new JLabel("TID:"));
	    bottomPanel.add(txtUttD);
	    bottomPanel.add(new JLabel("User:"));
	    bottomPanel.add(txtUser);
	    bottomPanel.add(new JLabel("Ticket Description:"));
	    bottomPanel.add(txtTicketDesc);
	    bottomPanel.add(new JLabel("Start Date:"));
	    bottomPanel.add(txtStartDate);
	    bottomPanel.add(new JLabel("End Date:"));
	    bottomPanel.add(txtEndDate);
	    bottomPanel.add(addNew); 
	    bottomPanel.add(btnUpdate);
	    bottomPanel.add(btnDelete);
	    bottomPanel.add(btnRefresh);
	    // Add the new button

	    // Add the bottom panel to the frame
	    add(bottomPanel, BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
		
		
		
		btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				dao.deleteRecord(Integer.parseInt(txtUttD.getText()));
				getFullRecords();
				//clear the Text fields
   				txtUttD.setText("");
   				txtUser.setText("");
   				txtTicketDesc.setText("");
   				txtStartDate.setText("YYYY-MM-DD");
   				txtEndDate.setText("YYYY-MM-DD");
				}catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getFullRecords();
				
			}
		});
		
		

	   // Add ActionListener to the "UPDATE" button
		btnUpdate.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               // Handle the button click event here
        	   int uid=Integer.parseInt(txtUttD.getText());
               String user=txtUser.getText();
               String ticketDescription=txtTicketDesc.getText();
               String startDate=txtStartDate.getText();
               String endDate=txtEndDate.getText();
               if(user.length()==0||ticketDescription.length()==0||startDate.length()==0||endDate.length()==0)
               {
               	JOptionPane.showMessageDialog(null, "Input Data Error ,Please check .", "Failure", JOptionPane.ERROR_MESSAGE);
               }
               
               Date startdate = null, endDate1 = null;
   			try {
   				startdate = convertStringToSqlDate(startDate);

   			} catch (Exception e1) {
   				System.out.println("Error in Start Date You Entered");
               	JOptionPane.showMessageDialog(null, "Error in Start Date You Entered .", "Failure", JOptionPane.ERROR_MESSAGE);

   				return;
   			}
   			try {
   				endDate1 = convertStringToSqlDate(endDate);

   			} catch (Exception e1) {
   				System.out.println("Error in End Date You Entered");
               	JOptionPane.showMessageDialog(null, "Error in End Date You Entered .", "Failure", JOptionPane.ERROR_MESSAGE);
   				return;
   			}
   			    dao.updateRecords(uid,user, ticketDescription, startdate, endDate1);

   				//clear the Text fields
   				txtUttD.setText("");
   				txtUser.setText("");
   				txtTicketDesc.setText("");
   				txtStartDate.setText("YYYY-MM-DD");
   				txtEndDate.setText("YYYY-MM-DD");
   			
           }
       });
		
		
		
		
		
		 // Add ActionListener to the "ADD NEW" button
        addNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the button click event here
                String user=txtUser.getText();
                String ticketDescription=txtTicketDesc.getText();
                String startDate=txtStartDate.getText();
                String endDate=txtEndDate.getText();
                if(user.length()==0||ticketDescription.length()==0||startDate.length()==0||endDate.length()==0)
                {
                	JOptionPane.showMessageDialog(null, "Input Data Error ,Please check .", "Failure", JOptionPane.ERROR_MESSAGE);
                }
                
                Date startdate = null, endDate1 = null;
    			try {
    				startdate = convertStringToSqlDate(startDate);

    			} catch (Exception e1) {
    				System.out.println("Error in Start Date You Entered");
                	JOptionPane.showMessageDialog(null, "Error in Start Date You Entered .", "Failure", JOptionPane.ERROR_MESSAGE);

    				return;
    			}
    			try {
    				endDate1 = convertStringToSqlDate(endDate);

    			} catch (Exception e1) {
    				System.out.println("Error in End Date You Entered");
                	JOptionPane.showMessageDialog(null, "Error in End Date You Entered .", "Failure", JOptionPane.ERROR_MESSAGE);
    				return;
    			}
    			int id = dao.insertRecords(user, ticketDescription, startdate, endDate1);

    			// display results if successful or not to console / dialog box
    			if (id != 0) {
    				System.out.println("Ticket ID : " + id + " created successfully!!!");
    				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
    			} else
    			{
    				System.out.println("Ticket cannot be created!!!");
                	JOptionPane.showMessageDialog(null, "Ticket cannot be created!!!", "Failure", JOptionPane.ERROR_MESSAGE);

    			}
    			
            }
        });
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {

			System.out.println("Use Buttons");
			// get ticket information
			//String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
			//String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

			// insert ticket information to database

//			int id = dao.insertRecords(ticketName, ticketDesc, null, null);
//
//			// display results if successful or not to console / dialog box
//			if (id != 0) {
//				System.out.println("Ticket ID : " + id + " created successfully!!!");
//				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
//			} else
//				System.out.println("Ticket cannot be created!!!");
		}

		else if (e.getSource() == mnuItemViewTicket) {

			getFullRecords();
		}
		
	}

	private void getFullRecords() {
		// retrieve all tickets details for viewing in JTable
		try {

			// Use JTable built in functionality to build a table model and
			// display the table model off your result set!!!
			JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
			jt.setBounds(30, 40, 200, 400);
			JScrollPane sp = new JScrollPane(jt);
			add(sp);
			setVisible(true); // refreshes or repaints frame on screen

			jt.addMouseListener(new MouseAdapter() {
		        @Override
		        public void mouseClicked(MouseEvent e) {
		            int selectedRow = jt.getSelectedRow();
		            // Retrieve data from the selected row
		            String uttd = jt.getValueAt(selectedRow, 0).toString();
		            String user = jt.getValueAt(selectedRow, 1).toString();
		            String ticketDesc = jt.getValueAt(selectedRow, 2).toString();
		            String startDate = jt.getValueAt(selectedRow, 3).toString();
		            String endDate = jt.getValueAt(selectedRow, 4).toString();

		            // Now you have the data, you can use it as needed
		            System.out.println("UTTD: " + uttd);
		            System.out.println("User: " + user);
		            System.out.println("Ticket Description: " + ticketDesc);
		            System.out.println("Start Date: " + startDate);
		            System.out.println("End Date: " + endDate);
		            
		           txtUttD.setText(uttd);
		    	   txtUser.setText(user);
		    	   txtTicketDesc.setText(ticketDesc);
		    	   txtStartDate.setText(startDate);
		    	   txtEndDate.setText(endDate);
		            
		            
		        }
		    });
			
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private static Date convertStringToSqlDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
