package exception;

public class BusinessException extends Exception{
    public BusinessException() {
        super();
    }

    public BusinessException(String errorMessage) {
        super(errorMessage);
    }

    public BusinessException(Throwable t) {
        super(t);
    }

    public  BusinessException(String errorMessage, Throwable t) {
        super(errorMessage, t);
    }
}
