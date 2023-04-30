package mavmi.git;

public class Git {
    final private String reposFilePath;
    final private String sshPublicKeyPath;

    public Git(String reposFilePath){
        this.reposFilePath = reposFilePath;
        sshPublicKeyPath = System.getProperty("user.home") + "/.ssh/id_rsa.pub";
    }
    public Git(String reposFilePath, String sshPublicKeyPath){
        this.reposFilePath = reposFilePath;
        this.sshPublicKeyPath = sshPublicKeyPath;
    }

}
