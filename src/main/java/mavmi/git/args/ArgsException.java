package mavmi.git.args;

public class ArgsException extends RuntimeException{
    public ArgsException(String msg){
        super(msg);
    }
    public ArgsException(Exception e){
        super(e);
    }
}
