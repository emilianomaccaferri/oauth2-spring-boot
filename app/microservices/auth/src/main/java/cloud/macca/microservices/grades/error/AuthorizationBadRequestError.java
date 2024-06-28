package cloud.macca.microservices.grades.error;

public class AuthorizationBadRequestError extends RuntimeException {
    public AuthorizationBadRequestError(String r){
        super(r);
    }
}
