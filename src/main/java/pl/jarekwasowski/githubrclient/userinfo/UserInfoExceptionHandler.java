package pl.jarekwasowski.githubrclient.userinfo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.jarekwasowski.githubrclient.client.UserNotFoundException;

@ControllerAdvice
public class UserInfoExceptionHandler {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {UserNotFoundException.class})
    @ResponseBody
    public ExceptionResponse handleUserNotFoundException(Exception ex) {
        return ExceptionResponse
                .builder()
                .code(Integer.toString(HttpStatus.NOT_FOUND.value()))
                .message("GitHub user not found.")
                .build();
    }
}
