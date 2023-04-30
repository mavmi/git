package mavmi.git;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class Main implements ApplicationRunner{
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final String errMsg = "Invalid arguments`1\n" +
                "Usage: java -jar git.jar --repos-list=[PATH_TO_REPOS_FILE] {--ssh-key=[PATH_TO_SSH_PUBLIC_KEY]}";

        String reposFilePath, sshPublicKeyPath;
        Set<String> argsNames = args.getOptionNames();
        if (argsNames.contains("--repos-list")){
            raiseError(errMsg);
        } else {
            
        }
    }

    private void raiseError(String msg){
        System.err.println(msg);
        System.exit(1);
    }

}
