
package sn.uasz.gestionConge.gestionConge.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author dev 2
 */
@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenExceptions extends RuntimeException{
    private final String message;

    public ForbiddenExceptions(String message) {
        this.message = message;
    }
    
    
    
    
}
