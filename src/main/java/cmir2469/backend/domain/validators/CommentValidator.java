package cmir2469.backend.domain.validators;

import cmir2469.backend.domain.Comment;

public class CommentValidator implements Validator<Comment> {
    @Override
    public void validate(Comment entity) throws ValidationException {
        String message = "";
        if(entity.getCreatedDate() == null)
            message += "Invalid date!\n";
        if(entity.getDescription() == null || entity.getDescription().equals(""))
            message += "Invalid description!\n";
        if (!message.equals("")) {
            throw new Validator.ValidationException(message);
        }
    }
}
