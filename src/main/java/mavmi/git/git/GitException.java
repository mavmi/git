package mavmi.git.git;

public class GitException extends RuntimeException {
    public GitException(String msg) {
        super(msg);
    }

    public GitException(Exception e) {
        super(e);
    }
}
