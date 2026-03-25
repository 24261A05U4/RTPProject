import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.TitledBorder;  

public class CareerGUIPro {

    JFrame frame;
    JTextField cgpaField;
    JComboBox<String> graduationBox, interestBox;
    DefaultListModel<String> listModel;
    JList<String> careerList;

    JPanel detailsPanel;

    Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
    Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);
    Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
    Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

    public CareerGUIPro() {

        frame = new JFrame("Career Guidance System");
        frame.setSize(950, 650);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 244, 248));

        
// ===== TOP PANEL =====
JPanel top = new JPanel(new GridBagLayout());
top.setBackground(Color.WHITE);
top.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("User Input"),
        BorderFactory.createEmptyBorder(15,15,15,15)
));

// Make title better
TitledBorder title = BorderFactory.createTitledBorder("User Input");
title.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
title.setTitleColor(new Color(44,62,80));

top.setBorder(BorderFactory.createCompoundBorder(
        title,
        BorderFactory.createEmptyBorder(15,15,15,15)
));


GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(10, 15, 10, 15);
gbc.fill = GridBagConstraints.HORIZONTAL;

// ===== CREATING COMPONENTS =====
cgpaField = new JTextField(12);
graduationBox = new JComboBox<>(new String[]{"Engineering","Medical","Arts","Commerce"});
interestBox = new JComboBox<>(new String[]{
        "Technical","Creative","Social Service",
        "Medical Health","Business and Management"
});

// ===== APPLYING STYLES =====
cgpaField.setFont(inputFont);
graduationBox.setFont(inputFont);
interestBox.setFont(inputFont);

cgpaField.setPreferredSize(new Dimension(150,30));
graduationBox.setPreferredSize(new Dimension(150,30));
interestBox.setPreferredSize(new Dimension(150,30));

// Optional padding inside field
cgpaField.setBorder(BorderFactory.createCompoundBorder(
    BorderFactory.createLineBorder(new Color(180,180,180)),
    BorderFactory.createEmptyBorder(5,10,5,10)
));

// ===== ROW 1 =====

// CGPA Label
gbc.gridx = 0; gbc.gridy = 0;
JLabel cgpaLabel = new JLabel(" CGPA");
cgpaLabel.setFont(labelFont);
top.add(cgpaLabel, gbc);

// CGPA Field
gbc.gridx = 1;
top.add(cgpaField, gbc);

// Graduation Label
gbc.gridx = 2;
JLabel gradLabel = new JLabel(" Graduation");
gradLabel.setFont(labelFont);
top.add(gradLabel, gbc);

// Graduation Box
gbc.gridx = 3;
top.add(graduationBox, gbc);

// ===== ROW 2 =====

// Interest Label
gbc.gridx = 0; gbc.gridy = 1;
JLabel interestLabel = new JLabel(" Interest");
interestLabel.setFont(labelFont);
top.add(interestLabel, gbc);

// Interest Box
gbc.gridx = 1;
top.add(interestBox, gbc);

// Button
gbc.gridx = 2; gbc.gridwidth = 2;
JButton loadBtn = new JButton(" Show Careers");
styleButton(loadBtn, new Color(46,204,113));
loadBtn.setPreferredSize(new Dimension(180,35));
top.add(loadBtn, gbc);
       frame.add(top, BorderLayout.NORTH);


        // ===== CENTER =====
        JPanel center = new JPanel(new GridLayout(1,2,10,10));

        listModel = new DefaultListModel<>();
        careerList = new JList<>(listModel);
        careerList.setFont(valueFont);

        JScrollPane listScroll = new JScrollPane(careerList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Career Options"));

        // DETAILS PANEL
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);

        JScrollPane detailScroll = new JScrollPane(detailsPanel);
        detailScroll.setBorder(BorderFactory.createTitledBorder("Career Details"));

        center.add(listScroll);
        center.add(detailScroll);

        frame.add(center, BorderLayout.CENTER);

        // ===== BOTTOM =====
        JButton detailBtn = new JButton(" Show Details");
        styleButton(detailBtn, new Color(52,152,219));

	JButton exitBtn = new JButton(" Exit");
	styleButton(exitBtn, new Color(231,76,60)); // red color

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(detailBtn);
	bottom.add(exitBtn);

        frame.add(bottom, BorderLayout.SOUTH);

        // ACTIONS
        loadBtn.addActionListener(e -> loadCareers());
        detailBtn.addActionListener(e -> showDetails());
	exitBtn.addActionListener(e -> System.exit(0));

        careerList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) showDetails();
            }
        });

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ===== BUTTON STYLE =====
    void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }

    // ===== LOAD CAREERS =====
    void loadCareers() {
        try {
	    boolean found = false;
            double cgpa = Double.parseDouble(cgpaField.getText());
            String interest = (String) interestBox.getSelectedItem();
            String graduation = (String) graduationBox.getSelectedItem();

            listModel.clear();
	    detailsPanel.removeAll();
	    detailsPanel.revalidate();
  	    detailsPanel.repaint();

            if (interest.equalsIgnoreCase("Medical Health") &&
                !graduation.equalsIgnoreCase("Medical")) {

                listModel.addElement("Mismatch: Medical requires Medical background");
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader("careers.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] t = line.split("\t");

                if (t[0].equalsIgnoreCase(interest)) {
                    if (cgpa >= Double.parseDouble(t[2])) {
                        listModel.addElement(t[1]);
			found = true; 
                    }
                }
            }
            br.close();
	    if (!found) {
    listModel.addElement("No suggestions available");  // list placeholder

    JOptionPane.showMessageDialog(
        frame,
        "No matching careers found based on your criteria.\nPlease modify your inputs to explore more options.",
        "Suggestion",
        JOptionPane.INFORMATION_MESSAGE
    );
}

        } catch (Exception e) {
            listModel.addElement("Invalid input");
        }
    }

    // ===== CREATE BEAUTIFUL ROW =====
    void addRow(String title, String value, Color color) {

    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(new Color(250, 250, 250));
    card.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(titleFont);
    titleLabel.setForeground(color);

    JTextArea valueArea = new JTextArea(value);
    valueArea.setFont(valueFont);
    valueArea.setLineWrap(true);
    valueArea.setWrapStyleWord(true);
    valueArea.setEditable(false);
    valueArea.setBackground(new Color(250,250,250));

    JPanel textPanel = new JPanel(new BorderLayout());
    textPanel.setBackground(new Color(250,250,250));
    textPanel.add(titleLabel, BorderLayout.NORTH);
    textPanel.add(valueArea, BorderLayout.CENTER);

    // ONLY THIS LINE (no WEST anymore)
    card.add(textPanel, BorderLayout.CENTER);

    detailsPanel.add(card);
    detailsPanel.add(Box.createVerticalStrut(10));
}
    // ===== SHOW DETAILS =====
    void showDetails() {

        detailsPanel.removeAll();

        String selected = careerList.getSelectedValue();
        if (selected == null|| selected.equals("No suggestions available")) return;

        try {
            BufferedReader br = new BufferedReader(new FileReader("careers.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] t = line.split("\t");

                if (t[1].equalsIgnoreCase(selected)) {

                    addRow("Interest", t[0], new Color(52,152,219));
             
                    addRow("Career Name", t[1], new Color(41,128,185));          

                    addRow("MIN CGPA", t[2], new Color(46,204,113));           

                    addRow("Description", t[3], new Color(44,62,80));

                    addRow("Exams", t[4], new Color(155,89,182));

                    String fee = t[5].replace(",", " - ");
		    addRow("Fees", fee, new Color(243,156,18));                 

                    addRow("Exam Month", t[6], new Color(241,196,15));           

                    addRow("Competition Level", t[7], new Color(52,73,94));      

                    addRow("Subjects / Skills", t[8], new Color(26,188,156));

                    addRow("Preparation Time", t[9], new Color(127,140,141));

                    addRow("Coaching Institutes", t[10], new Color(22,160,133));

                    addRow("Top Colleges", t[11], new Color(52,73,94));

                    addRow("Work Department", t[12], new Color(41,128,185));

                    addRow("Salary", t[13], new Color(39,174,96));

                    addRow("Future Scope", t[14], new Color(211,84,0));

                    addRow("Alternative Careers", t[15], new Color(192,57,43));
                    break;
                }
            }
            br.close();

        } catch (Exception e) {
            addRow( "Error", "Unable to load details", Color.RED);
        }

        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
	

    public static void main(String[] args) {
        new CareerGUIPro();
    }
}