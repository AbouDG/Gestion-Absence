
package sn.uasz.gestionConge.gestionConge.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Mohamed Chérif Diallo
 */
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestExceptions extends RuntimeException{
    
    private final String message;

    public BadRequestExceptions(String message) {
        super(message);
        this.message = message;
    }

    
    
}
