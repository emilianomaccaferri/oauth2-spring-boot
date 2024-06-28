package cloud.macca.microservices.grades.error;

public class ExpiredAccessTokenError extends RuntimeException {
    public ExpiredAccessTokenError(String r){
        super(r);
    }
}
