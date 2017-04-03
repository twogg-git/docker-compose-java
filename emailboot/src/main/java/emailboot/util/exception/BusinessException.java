package emailboot.util.exception;


public class BusinessException extends Exception{

    public BusinessException(BusinessExceptionEnum businessExceptionEnum){
        super(businessExceptionEnum.getMessage());
    }
}

