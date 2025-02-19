
package sn.uasz.gestionConge.gestionConge.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Mohamed Chérif Diallo
 */
@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DataIntegrityExceptions extends RuntimeException{
    
    private final String message;

    public DataIntegrityExceptions(String message) {
        super(message);
        this.message = message;
    }

    
    
}
