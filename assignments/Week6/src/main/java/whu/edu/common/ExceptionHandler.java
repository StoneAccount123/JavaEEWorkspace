package whu.edu.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MyException.class)
    public ResponseEntity<String> myexception(MyException ex){
        return ResponseEntity.ok(ex.getMessage());
    }
}
