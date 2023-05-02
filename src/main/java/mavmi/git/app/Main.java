package mavmi.git.app;

import mavmi.git.args.ArgsException;
import mavmi.git.git.Git;
import mavmi.git.args.Args;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements ApplicationRunner{
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            Git git = new Git(new Args(args));
            git.gitClone();
        } catch (ArgsException e){
            raiseError(e.getMessage());
        }
    }

    private void raiseError(String msg){
        System.err.println(msg);
        System.exit(1);
    }

}
