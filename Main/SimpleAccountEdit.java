package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SimpleAccountEdit extends JFrame implements Runnable{

    private static PersonAccount currentPerson = null;
    private final AccountBase accountBase;
    private final JButton logoutButton;
    private final JButton deleteButton;
    private final JButton saveButton;
    private final JPanel contentPane;
    private final JLabel firstNameLabel;
    private final JTextField firstNameField;
    private final JLabel lastNameLabel;
    private final JTextField lastNameField;
    private final JLabel birthYearLabel;
    private final JTextField birthYearField;
    private final JLabel loginLabel;
    private final JTextField loginField;
    private final JLabel passwordLabel;
    private final JPasswordField passwordField;


    public SimpleAccountEdit(AccountBase base) {
        setTitle("Edytor danych konta");
        contentPane = new JPanel();
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        contentPane.setLayout(null);
        setLayout(null);
        logoutButton = new JButton("Logout");
        deleteButton = new JButton("Delete");
        saveButton = new JButton("Save");
        firstNameLabel = new JLabel("First name:");
        lastNameLabel = new JLabel("Last name:");
        birthYearLabel = new JLabel("Birth year:");
        passwordLabel = new JLabel("Password:");
        loginLabel = new JLabel("Login:");
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        birthYearField = new JTextField();
        loginField = new JTextField();
        passwordField = new JPasswordField();
        accountBase = base;
    }

    @Override
    public void run() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 700;
        int height = 500;
        setBounds((screen.width-width)/2, (screen.height-height)/2, width, height);
        contentPane.setBounds(0, 0, width, height);
        setLayout(null);
        add(contentPane);
        contentPane.setLayout(null);
        setMinimumSize(new Dimension(350,230));

        //Buttons settings
        logoutButton.setBounds(10, contentPane.getHeight()-30, 100, 20);
        logoutButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                currentPerson = null;
            }
        });
        deleteButton.setBounds((contentPane.getWidth()-100)/2, contentPane.getHeight()-30, 100, 20);
        deleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountBase.removePerson();
                setVisible(false);
                currentPerson = null;
            }
        });
        saveButton.setBounds(contentPane.getWidth()-110, contentPane.getHeight()-30, 100, 20);
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePersonAccountData();
                showCurrentPerson();
                setVisible(false);
            }
        });

        //Labels settings
        firstNameLabel.setBounds(10, 10, 100, 20);
        lastNameLabel.setBounds(10, 40, 100, 20);
        birthYearLabel.setBounds(10, 70, 100, 20);
        loginLabel.setBounds(10, 100, 100, 20);
        passwordLabel.setBounds(10, 130, 100, 20);

        //Fields settings
        firstNameField.setBounds(130, 10, 200, 20);
        lastNameField.setBounds(130, 40, 200, 20);
        birthYearField.setBounds(130, 70, 200, 20);
        loginField.setBounds(130, 100, 200, 20);
        passwordField.setBounds(130, 130, 200, 20);

        //Put everything together
        contentPane.add(saveButton);
        contentPane.add(deleteButton);
        contentPane.add(logoutButton);
        contentPane.add(firstNameLabel);
        contentPane.add(lastNameLabel);
        contentPane.add(birthYearLabel);
        contentPane.add(passwordLabel);
        contentPane.add(loginLabel);
        contentPane.add(firstNameField);
        contentPane.add(lastNameField);
        contentPane.add(birthYearField);
        contentPane.add(loginField);
        contentPane.add(passwordField);
        pack();
        getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateContent();
            }
        });
    }

    private void updateContent() {
        contentPane.setSize(getContentPane().getSize());
        logoutButton.setLocation(10, contentPane.getHeight()-30);
        deleteButton.setLocation((contentPane.getWidth()-100)/2, contentPane.getHeight()-30);
        saveButton.setLocation(contentPane.getWidth()-110, contentPane.getHeight()-30);
        firstNameField.setSize(contentPane.getWidth()-160, 20);
        lastNameField.setSize(contentPane.getWidth()-160, 20);
        birthYearField.setSize(contentPane.getWidth()-160, 20);
        loginField.setSize(contentPane.getWidth()-160, 20);
        passwordField.setSize(contentPane.getWidth()-160, 20);
    }

    private void showCurrentPerson() {
        firstNameField.setText(currentPerson.getFirstName());
        lastNameField.setText(currentPerson.getLastName());
        birthYearField.setText(String.valueOf(currentPerson.getBirthYear()));
        loginField.setText(currentPerson.getLogin());
        passwordField.setText(String.valueOf(currentPerson.getPassword()));
    }

    public void loginPerson(PersonAccount p) {
        currentPerson = p;
        setVisible(true);
        showCurrentPerson();
        updateContent();
    }

    private void changePersonAccountData() {
        try {
            currentPerson.setFirstName(firstNameField.getText());
            currentPerson.setLastName(lastNameField.getText());
            currentPerson.setBirthYear(birthYearField.getText());
            accountBase.changeLogin(loginField.getText(), currentPerson);
            accountBase.changePassword(passwordField.getPassword(), currentPerson);
        } catch (PersonException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Zmiana nie powiodła się", JOptionPane.ERROR_MESSAGE);
        }
    }
}
