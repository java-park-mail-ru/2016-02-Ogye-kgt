package models;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "UserProfile")
public class UserProfile {
    private static final int MIN_LOGIN_LENGTH = 3;
    private static final int MIN_PASS_LENGTH = 5;
    private static final String EMAIL_REGEXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
            + "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @NotNull
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEXP);

    @NotNull
    @Column(unique = true)
    private String login;
    @NotNull
    private String password;
    @NotNull
    @Column(unique = true)
    private String email;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    public UserProfile() {
    }

    public UserProfile(@NotNull String login, @NotNull String password, @NotNull String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        setId();
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        if (isLoginValid(login))
            this.login = login;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        if (isPasswordValid(password))
            this.password = password;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        if (isEmailValid(email))
            this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId() {
    }

    public static boolean isLoginValid(String login) {
        if (login == null) return false;
        if (login.length() < MIN_LOGIN_LENGTH) return false;
        return true;
    }

    public static boolean isPasswordValid(String password) {
        if (password == null) return false;
        if (password.length() < MIN_PASS_LENGTH) return false;
        return true;
    }

    public static boolean isEmailValid(String email) {
        if (email == null) return false;
        final Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
        return emailMatcher.matches();
    }

    public boolean isValid() {
        return isLoginValid(login) && isEmailValid(email) && isPasswordValid(password);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProfile that = (UserProfile) o;

        if (!login.equals(that.login)) return false;
        if (!password.equals(that.password)) return false;
        return email.equals(that.email);

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
