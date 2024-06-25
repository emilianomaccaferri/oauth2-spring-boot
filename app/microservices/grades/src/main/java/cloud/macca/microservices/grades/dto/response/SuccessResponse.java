package cloud.macca.microservices.grades.dto.response;

public class SuccessResponse<T> {
    private T result;
    private final boolean success = true;

    public SuccessResponse(T item){
        this.result = item;
    }
    public void setResult(T result) {
        this.result = result;
    }
    public T getResult() {
        return result;
    }
}
