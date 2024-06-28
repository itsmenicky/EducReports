package br.com.educreports.session;
import br.com.educreports.models.User;
import lombok.Data;

@Data
public class userSession {
    private static userSession instance;
    private User user;

    private userSession(){}

    public static userSession getInstance(){
        if(instance == null){
            instance = new userSession();
        }
        return instance;
    }

    public void clearSession(){
        user = null;
    }
}
