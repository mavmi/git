package mavmi.git.args;

import lombok.Getter;
import org.springframework.boot.ApplicationArguments;

import java.util.List;
import java.util.Set;

@Getter
public class Args {
    private static final String REPOS_FILE_FLAG = "repos-list";
    private static final String USERNAME_FLAG = "username";
    private static final String SSH_KEY_FLAG = "ssh-key";
    private static final String OUTPUT_DIR_FLAG = "output-dir";
    private static final String RM_GIT_FLAG = "rm-git";
    private static final String ERR_MSG = "Invalid arguments\n" +
            "Usage: java -jar git.jar\n" +
            "\t--" + REPOS_FILE_FLAG + "=[PATH_TO_REPOS_FILE]\n" +
            "\t--" + USERNAME_FLAG + "=[GITHUB_USERNAME]\n" +
            "\t{--" + SSH_KEY_FLAG + "=[PATH_TO_SSH_PRIVATE_KEY]}\n" +
            "\t{--" + OUTPUT_DIR_FLAG + "=[OUTPUT_DIRECTORY_PATH]}\n" +
            "\t{--" + RM_GIT_FLAG + "=[true/false]}";

    private final String reposFile;
    private final String username;
    private final String sshKey;
    private final String outputDir;
    private final Boolean rmGit;

    public Args(ApplicationArguments args) {
        final Set<String> optionNames = args.getOptionNames();
        final List<String> nonOptionArgs = args.getNonOptionArgs();

        if (!nonOptionArgs.isEmpty() || !optionNames.contains(REPOS_FILE_FLAG) || !optionNames.contains(USERNAME_FLAG)) {
            throw new ArgsException(ERR_MSG);
        }

        {
            List<String> optionValues = args.getOptionValues(REPOS_FILE_FLAG);
            if (optionValues == null || optionValues.size() != 1) {
                throw new ArgsException(ERR_MSG);
            }
            reposFile = optionValues.get(0);
        }
        {
            List<String> optionValues = args.getOptionValues(USERNAME_FLAG);
            if (optionValues == null || optionValues.size() != 1) {
                throw new ArgsException(ERR_MSG);
            }
            username = optionValues.get(0);
        }
        {
            List<String> optionValues = args.getOptionValues(SSH_KEY_FLAG);
            if (optionValues == null || optionValues.isEmpty()) {
                sshKey = System.getProperty("user.home") + "/.ssh/id_ecdsa";
            } else if (optionValues.size() > 1) {
                throw new ArgsException(ERR_MSG);
            } else {
                sshKey = optionValues.get(0);
            }
        }
        {
            List<String> optionValues = args.getOptionValues(OUTPUT_DIR_FLAG);
            if (optionValues == null || optionValues.isEmpty()) {
                outputDir = System.getProperty("user.home") + "/GitCloneOutput";
            } else if (optionValues.size() > 1) {
                throw new ArgsException(ERR_MSG);
            } else {
                outputDir = optionValues.get(0);
            }
        }
        {
            List<String> optionValues = args.getOptionValues(RM_GIT_FLAG);
            if (optionValues == null || optionValues.isEmpty()) {
                rmGit = true;
            } else if (optionValues.size() > 1) {
                throw new ArgsException(ERR_MSG);
            } else {
                rmGit = Boolean.getBoolean(optionValues.get(0));
            }
        }
    }
}
