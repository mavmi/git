package mavmi.git.args;

import org.springframework.boot.ApplicationArguments;

import java.util.List;
import java.util.Set;

public class Args {
    private static final String reposFileFlag = "repos-list";
    private static final String usernameFlag = "username";
    private static final String sshKeyFlag = "ssh-key";
    private static final String outputDirFlag = "output-dir";
    private static final String rmGitFlag = "rm-git";
    private static final String errMsg = "Invalid arguments\n" +
                                            "Usage: java -jar git.jar\n" +
                                            "\t--" + reposFileFlag + "=[PATH_TO_REPOS_FILE]\n" +
                                            "\t--" + usernameFlag + "=[GITHUB_USERNAME]\n" +
                                            "\t{--" + sshKeyFlag + "=[PATH_TO_SSH_PRIVATE_KEY]}\n" +
                                            "\t{--" + outputDirFlag + "=[OUTPUT_DIRECTORY_PATH]}\n" +
                                            "\t{--" + rmGitFlag + "=[true/false]}";

    private final String reposFile;
    private final String username;
    private final String sshKey;
    private final String outputDir;
    private final boolean rmGit;

    public Args(ApplicationArguments args){
        final Set<String> optionNames = args.getOptionNames();
        final List<String> nonOptionArgs = args.getNonOptionArgs();

        if (nonOptionArgs.size() != 0 || !optionNames.contains(reposFileFlag) || !optionNames.contains(usernameFlag)){
            throw new ArgsException(errMsg);
        }

        {
            List<String> optionValues = args.getOptionValues(reposFileFlag);
            if (optionValues == null || optionValues.size() != 1) throw new ArgsException(errMsg);
            reposFile = optionValues.get(0);
        }
        {
            List<String> optionValues = args.getOptionValues(usernameFlag);
            if (optionValues == null || optionValues.size() != 1) throw new ArgsException(errMsg);
            username = optionValues.get(0);
        }
        {
            List<String> optionValues = args.getOptionValues(sshKeyFlag);
            if (optionValues == null || optionValues.size() == 0) sshKey = System.getProperty("user.home") + "/.ssh/id_ecdsa";
            else if (optionValues.size() > 1) throw new ArgsException(errMsg);
            else sshKey = optionValues.get(0);
        }
        {
            List<String> optionValues = args.getOptionValues(outputDirFlag);
            if (optionValues == null || optionValues.size() == 0) outputDir = System.getProperty("user.home") + "/GitCloneOutput";
            else if (optionValues.size() > 1) throw new ArgsException(errMsg);
            else outputDir = optionValues.get(0);
        }
        {
            List<String> optionValues = args.getOptionValues(rmGitFlag);
            if (optionValues == null || optionValues.size() == 0) rmGit = true;
            else if (optionValues.size() > 1) throw new ArgsException(errMsg);
            else rmGit = Boolean.getBoolean(optionValues.get(0));
        }
    }

    public String getReposFile(){
        return reposFile;
    }
    public String getUsername(){
        return username;
    }
    public String getSshKey(){
        return sshKey;
    }
    public String getOutputDir(){
        return outputDir;
    }
    public boolean getRmGit(){
        return rmGit;
    }

}
