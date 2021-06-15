package cmir2469.backend.domain.validators;

import cmir2469.backend.domain.User;

import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {

    public static boolean mailValidation(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    public void validate(User user) throws ValidationException {
//        String message = "";
//        if(user.getUsername() == null || user.getUsername().equals(""))
//            message += "Invalid username!\n";
//        if(user.getPassword() == null || user.getPassword().equals(""))
//            message +="Invalid password!\n";
//        if(!mailValidation(user.getEmail()))
//            message += "Invalid Email!\n";
//        if (!message.equals("")) {
//            throw new Validator.ValidationException(message);
//        }

    }
}
