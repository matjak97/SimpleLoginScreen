package Main;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class AccountBase {

    private final ArrayList<PersonAccount> accountList;
    private PersonAccount loggedPerson;
    private File file;
    private final SimpleLoginScreen app;

    public AccountBase(SimpleLoginScreen simpleLoginScreen) {
        accountList = new ArrayList<>();
        app = simpleLoginScreen;
    }

    public void addPerson(PersonAccount person) throws PersonException {
        if(accountSearch(person.getLogin()) == null) {
            accountList.add(person);
        } else if(accountSearch(person.getLogin()) != person)
            throw new PersonException("Ten login jest zajęty.");
    }

    public void removePerson() {
        accountList.remove(loggedPerson);
        loggedPerson = null;
        app.forceUpdateContent();
    }

    public PersonAccount login(String login, char[] password) throws PersonException {
        loggedPerson = accountSearch(login);
        if(loggedPerson == null) throw new PersonException("Konto z podanym loginem nie istnieje.");
        if(!java.util.Arrays.equals(loggedPerson.getPassword(), password)) {
            loggedPerson = null;
            throw new PersonException("Błędne hasło.");
        }
        return loggedPerson;
    }

    public void changePassword(char[] password, PersonAccount person) throws PersonException {
        if(person != loggedPerson) throw new PersonException("Musisz być zalogowany aby dokonać zmian.");
        person.setPassword(password);
    }

    public void changeLogin(String newLogin, PersonAccount person) throws PersonException {
        if(person != loggedPerson) throw new PersonException("Musisz być zalogowany aby dokonać zmian.");
        PersonAccount loginOccupiedBy = accountSearch(newLogin);
        if(loginOccupiedBy == null) {
            person.setLogin(newLogin);
            return;
        }
        if(!loginOccupiedBy.equals(person))
            throw new PersonException("Nie można zmienić loginu, ponieważ ten login jest zajęty.");
    }

    private PersonAccount accountSearch(String login) {
        for (PersonAccount person:accountList) {
            if(person.getLogin().equals(login)) return person;
        }
        return null;
    }

    public void loadData(JFrame app) {
        BufferedReader bufferedReader = null;
        getFile(app);
        try {
            if(file.createNewFile()) JOptionPane.showMessageDialog(null, "Utworzono nowy plik " + file.getName(), "Nie znaleziono pliku " + file.getName(), JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PersonAccount person = null;
        do {
            try {
                person = PersonAccount.readFromFile(bufferedReader);
            } catch (PersonException e) {
                e.printStackTrace();
            }
            if (person != null)
                try {
                    addPerson(person);
                } catch (PersonException e) {
                    e.printStackTrace();
                }
        } while (person != null);
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Zapis nie powiódł się", JOptionPane.ERROR_MESSAGE);
        }
        for (PersonAccount person : accountList) {
            PersonAccount.printToFile(printWriter, person);
        }
        printWriter.close();
    }

    private void getFile(JFrame app) {
        /*JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnVal = fileChooser.showSaveDialog(app);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            return;
        }*/
        file = new File("base.txt");
    }

    public int getAccountCount() {
        return accountList.size();
    }
}
