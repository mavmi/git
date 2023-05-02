package mavmi.git.git;

import mavmi.git.args.Args;
import mavmi.git.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Git {
    private final String sshPrivateKeyPath;
    private final String username;
    private final File workingDirectory;
    private final boolean rmGit;
    private final CONNECTION_TYPE connectionType = CONNECTION_TYPE.SSH;

    private final List<GitRepo> reposList;
    private final SshTransportConfigCallback sshTransportConfigCallback;

    public Git(Args args){
        this.username = args.getUsername();
        this.sshPrivateKeyPath = args.getSshKey();
        this.workingDirectory = new File(args.getOutputDir());
        this.rmGit = args.getRmGit();
        this.reposList = new ArrayList<>();
        this.sshTransportConfigCallback = new SshTransportConfigCallback(this.sshPrivateKeyPath);

        createWorkingDirectory();
        parseReposFile(args.getReposFile());
    }

    public void gitClone(){
        for (GitRepo gitRepo : reposList){
            gitRepo.gitClone();
        }
    }

    public CONNECTION_TYPE getConnectionType(){
        return connectionType;
    }
    public String getUsername(){
        return username;
    }
    public File getWorkingDirectory(){
        return workingDirectory;
    }
    public boolean getRmGit(){
        return rmGit;
    }
    public SshTransportConfigCallback getSshTransportConfigCallback(){
        return sshTransportConfigCallback;
    }

    private void createWorkingDirectory(){
        final String path = workingDirectory.getAbsolutePath();

        if (workingDirectory.exists() && workingDirectory.isFile()) throw new GitException("\"" + path + "\"" + " is file");
        if (workingDirectory.exists() && workingDirectory.isDirectory()) throw new GitException("Directory \"" + path + "\" already exists");
        if (!workingDirectory.mkdir()) throw new GitException("Unable to create directory \"" + path + "\"");
    }

    private void parseReposFile(String reposFilePath){
        try (BufferedReader reader = new BufferedReader(new FileReader(reposFilePath))){
            String line;
            while ((line = reader.readLine()) != null){
                if (Utils.isLineEmpty(line)) continue;
                List<String> parsedLine = split(line);
                GitRepo gitRepo = new GitRepo(this, parsedLine.get(0));
                for (int i = 1; i < parsedLine.size(); i++) gitRepo.addBranch(parsedLine.get(i));
                reposList.add(gitRepo);
            }
        } catch (IOException e){
            throw new GitException(e.getMessage());
        }
    }
    private List<String> split(String line){
        final String errMsg = "Invalid syntax for line: \"" + line + "\"";

        line = line.replaceAll(" ", "");
        String[] arr1 = line.split(":");
        if (arr1.length != 2) throw new GitException(errMsg);
        String[] arr2 = arr1[1].split(",");

        List<String> result = new ArrayList<>();
        result.add(arr1[0]);
        result.addAll(Arrays.asList(arr2));

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("username: ")
                .append(username)
                .append("\n")
                .append("ssh-key file: ")
                .append(sshPrivateKeyPath)
                .append("\n");

        for (int i = 0; i < reposList.size(); i++){
            builder.append(reposList.get(i).toString());
            if (i + 1 != reposList.size()) builder.append("\n");
        }

        return builder.toString();
    }
}
