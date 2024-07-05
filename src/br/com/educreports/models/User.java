package br.com.educreports.models;
import lombok.Data;

@Data
public class User {
    private String username;
    private String email;
    private String login;
    private char[] password;
    private String hierarchy;
    private String status;

    /**
     * Class constructor
     * @param username
     * @param email
     * @param login
     * @param password
     * @param hierarchy
     * @param status
     */
    public User(String username, String email, String login, char[] password, String hierarchy, String status){
        this.username = username;
        this.email = email;
        this.login = login;
        this.password = password;
        this.hierarchy = hierarchy;
        this.status = status;
    }

    /**
     * Class constructor (without password)
     * @param username
     * @param email
     * @param login
     * @param hierarchy
     * @param status
     */
    public User(String username, String email, String login, String hierarchy, String status){
        this.username = username;
        this.email = email;
        this.login = login;
        this.hierarchy = hierarchy;
        this.status = status;
    }

    /**
     * Method responsible for validating a user
     * @return
     */
    public boolean isValid(){
        return isUsernameValid() && isEmailValid() && isLoginValid() && isPasswordValid() && isHierarchyValid() && isStatusValid();
    }

    /**
     * Method responsible for validating a user for updates
     * @return
     */
    public boolean isValidUpdate(){
        return isUsernameValid() && isEmailValid() && isLoginValid() && isHierarchyValid() && isStatusValid();
    }

    /**
     * Method responsible for validate username
     * @return
     */
    public boolean isUsernameValid(){
        return username != null && !username.trim().isEmpty();
    }

    /**
     * Method responsible for validate email
     * @return
     */
    public boolean isEmailValid(){
        return email != null && email.trim().isEmpty();
    }

    /**
     * Method responsible for validate login
     * @return
     */
    public boolean isLoginValid(){
        return login != null && !login.trim().isEmpty();
    }

    /**
     * Method responsible for validate password
     * @return
     */
    public boolean isPasswordValid(){
        String special_char = "!@#$%^&*()-_=+[]{}|;:',.<>?";
        String upper_case = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String low_case = "abcdefghijklmnopqrstuvwxyz";
        Boolean has_special_char = false;
        Boolean has_uppercase = false;
        Boolean has_lowcase = false;
        Boolean has_number = false;
        for(char character : password){
            if(special_char.contains(String.valueOf(character))){
                has_special_char = true;
            }else if (upper_case.contains(String.valueOf(character))){
                has_uppercase = true;
            }else if (low_case.contains(String.valueOf(character))){
                has_lowcase = true;
            }else if(Character.isDigit(character)){
                has_number = true;
            }

            if(has_special_char == true && has_uppercase == true && has_lowcase == true && has_number == true){
                return true;
            }
        }
        return false;
    }

    /**
     * Method responsible for validate hierarchy
     * @return
     */
    public boolean isHierarchyValid(){
        return hierarchy != null && !hierarchy.trim().isEmpty();
    }

    /**
     * Method responsible for validate user status
     * @return
     */
    public boolean isStatusValid(){
        return status.equals("Active");
    }

    /**
     * Method responsible for getting validation errors
     * @return
     */
    public String getValidationErrors(){
        StringBuilder errors = new StringBuilder();
        if(!isUsernameValid()){
            errors.append("Campo nome vazio!");
        }
        if(!isLoginValid()){
            errors.append("Campo login vazio!");
        }
        if(!isEmailValid()){
            errors.append("Campo email vazio!");
        }
        if(!isHierarchyValid()){
            errors.append("Campo de perfil vazio!");
        }
        if(!isStatusValid()){
            errors.append("Usuário desativado!");
        }
        if(!isPasswordValid()){
            errors.append("A senha não se enquadra nas exigências!");
        }
        return errors.toString();
    }
}
