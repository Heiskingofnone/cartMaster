package org.example.sriptor.models;

public class user {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Role role;
    private Boolean isActive;
    private String createdDate;
    private String updatedDate;


    public enum Role{
        CASHIER("Cashier"),
        ADMIN("Administrator");
        private  final String displayName;

        Role(String displayName){
            this.displayName = displayName;
        }

        public String toString(){
            return displayName;
        }
    }

    public enum CountryCodes{
        GHANA("+233"),
        Nigeria("+234"),
        USA("+1");

        private final String displayName;
        CountryCodes(String displayName){ this.displayName = displayName;}
        public String toString() {return displayName;}
    }

    public user(String firstName, String lastName, String email, String password, Role role, String phoneNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }
    //setters

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public void setPhoneNumber(String phoneNumber){this.phoneNumber =phoneNumber;}
    //getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getPhoneNumber(){return phoneNumber;}

    public String getUpdatedDate() {
        return updatedDate;
    }
}
