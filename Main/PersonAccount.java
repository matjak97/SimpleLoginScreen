package Main;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class PersonAccount extends Person{

    private String login;
    private char[] password;

    public PersonAccount(String first_name, String last_name, String login, char[] password) throws PersonException {
        super(first_name, last_name);
        setLogin(login);
        setPassword(password);
    }

    public PersonAccount(Person person, String login, char[] password) throws PersonException{
        super(person.getFirstName(), person.getLastName());
        setBirthYear(person.getBirthYear());
        setJob(person.getJob());
        setLogin(login);
        setPassword(password);
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String newLogin) throws PersonException {
        if(newLogin == null || newLogin.equals("")) throw new PersonException("Pole Login nie może być puste.");
        login = newLogin;
    }

    protected char[] getPassword() {
        return password;
    }

    public void setPassword(char[] newPassword) throws PersonException {
        if(newPassword == null) throw new PersonException("Hasło nie może być puste.");
        password = newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonAccount that = (PersonAccount) o;
        return getLogin().equals(that.getLogin()) && Arrays.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getLogin());
        result = 31 * result + Arrays.hashCode(getPassword());
        return result;
    }


    public static void printToFile(PrintWriter writer, PersonAccount person){
        writer.println(person.getFirstName() + "#" + person.getLastName() +
                "#" + person.getBirthYear() + "#" + person.getJob() + "#" +
                person.getLogin() + "#" + String.valueOf(person.getPassword()));
    }

    public static PersonAccount readFromFile(BufferedReader reader) throws PersonException{
        if (reader == null) return null;
        try {
            String line = reader.readLine();
            if (line == null || line.equals("")) return null;
            String[] txt = line.split("#");
            PersonAccount person = new PersonAccount(txt[0], txt[1], txt[4], txt[5].toCharArray());
            person.setBirthYear(txt[2]);
            person.setJob(txt[3]);
            return person;
        } catch(IOException e){
            throw new PersonException("Wystąpił błąd podczas odczytu danych z pliku.");
        }
    }

}
