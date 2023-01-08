package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleLoginScreen extends JFrame {
    private final JButton cancelButton;
    private final JButton registerButton;
    private final JButton loginButton;
    private final JLabel mainLabel;
    private final JLabel loginLabel;
    private final JLabel passwordLabel;
    private final JTextField loginField;
    private final JPasswordField passwordField;
    private final JLabel accountCount;
    private PersonAccount loggedPerson = null;
    private final AccountBase base;
    private final JPanel contentPane;
    private final Thread accountEditWindowThread;
    private final SimpleAccountEdit accountEditWindow;

    public SimpleLoginScreen() {
        setTitle("Ekran logowania");
        base = new AccountBase(this);
        contentPane = new JPanel();
        cancelButton = new JButton("Cancel");
        registerButton = new JButton("Register");
        loginButton = new JButton("Login");
        mainLabel = new JLabel("Wpisz dane do logowania:");
        loginField = new JTextField();
        loginLabel = new JLabel("Login: ");
        passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField();
        accountCount = new JLabel("Liczba zarejestrowanych kont: 0");
        accountEditWindow = new SimpleAccountEdit(base);
        accountEditWindowThread = new Thread(accountEditWindow);
    }

    public static void main(String[] args) {
        SimpleLoginScreen app = new SimpleLoginScreen();
        javax.swing.SwingUtilities.invokeLater(app::runApp);
    }

    private void runApp() {
        accountEditWindowThread.start();
        base.loadData(this);
        initialize();
    }

    private void initialize() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                base.saveData();
                super.windowClosing(e);
            }
        });
        setVisible(true);
        contentPane.setLayout(null);
        contentPane.setBounds(0,0,400,250);
        setLayout(null);
        setMinimumSize(new Dimension(350,190));
        pack();

        // Fields settings
        loginField.setBounds(100, 60, 100,20);
        passwordField.setBounds(100, 90, 100, 20);

        // Labels settings
        mainLabel.setBounds(100,30,200,20);
        loginLabel.setBounds(20, 60, 80, 20);
        passwordLabel.setBounds(20, 90, 100, 20);
        accountCount.setBounds(20, 120, 200, 20);

        // Position and dimensions of the main window
        setBounds(100, 100, 380, 270);

        // Buttons settings
        cancelButton.setBounds(10, 220, 100, 20);
        cancelButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountEditWindowThread.interrupt();
                base.saveData();
                System.exit(0);
            }
        });
        registerButton.setBounds(160, 220, 100, 20);
        registerButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAccount(PersonConsoleApp.createNewPerson());
                updateContent();
            }
        });
        loginButton.setBounds(310, 220, 100, 20);
        loginButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean b = accountLogin(loginField.getText(), passwordField.getPassword());
                if(!b) passwordField.setBackground(Color.RED);
                else {
                    passwordField.setBackground(Color.GREEN);
                    accountEditWindow.loginPerson(loggedPerson);
                }
            }
        });

        // Put everything together
        add(contentPane);
        contentPane.add(cancelButton);
        contentPane.add(registerButton);
        contentPane.add(loginButton);
        contentPane.add(mainLabel);
        contentPane.add(loginField);
        contentPane.add(loginLabel);
        contentPane.add(passwordLabel);
        contentPane.add(passwordField);
        contentPane.add(accountCount);
        getContentPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateContent();
            }
        });
        updateContent();
    }

    private void updateContent() {
        contentPane.setSize(getContentPane().getSize());
        cancelButton.setLocation(10,contentPane.getHeight()-30);
        registerButton.setLocation((contentPane.getWidth()-registerButton.getWidth())/2,contentPane.getHeight()-30);
        loginButton.setLocation(contentPane.getWidth()-loginButton.getWidth()-10,contentPane.getHeight()-30);
        loginField.setSize(contentPane.getWidth()-130,20);
        passwordField.setSize(contentPane.getWidth()-130,20);
        accountCount.setText("Liczba zarejestrowanych kont: " + base.getAccountCount());
    }

    private void registerAccount(Person person) {
        if(person == null) return;
        PersonAccount account;
        String login = JOptionPane.showInputDialog(null, "Wprowadż swój login:", "Tworzenie konta", JOptionPane.QUESTION_MESSAGE);
        char[] password = JOptionPane.showInputDialog(null, "Wprowadż swoje hasło:", "Tworzenie konta", JOptionPane.QUESTION_MESSAGE).toCharArray();
        try {
            account = new PersonAccount(person, login, password);
        } catch (PersonException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Błędne dane", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            base.addPerson(account);
        } catch (PersonException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Konto nie zostało utworzone", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean accountLogin(String login, char[] password) {
        passwordField.setText("");
        try {
            loggedPerson = base.login(login, password);
        } catch (PersonException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Błędne dane", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void forceUpdateContent() {
        updateContent();
    }
}
