package Networking;

public class User {

    private int     id;
    private String  userName;
    private String  firstName;
    private String  lastName;
    private String  password;
    private boolean deleted;

    public User()
    {
        id = -1;
        userName  = "Logged-Out";
        firstName = "Not";
        lastName  = "Logged-In";
        password  = "xxxxx";
        deleted   = false;
    }

    public User (int userId, String usrNme, String fstNme, String lstNme, String pssWrd, int deleted){
        setId(userId);
        setUserName(usrNme);
        setFirstName(fstNme);
        setLastName(lstNme);
        setPassword(pssWrd);
        if(deleted == 0)
            setDeleted(false);
        else
            setDeleted(true);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean bool)
    {
        this.deleted =  bool;
    }

    public String toString()
    {
     return "\nid: " + this.id + "\nusername: " + this.userName + "\nfirstname: " + this.firstName + "\nlastname: " + this.lastName + "\ndeleted? " + this.deleted +"\n\n";
    }
}
