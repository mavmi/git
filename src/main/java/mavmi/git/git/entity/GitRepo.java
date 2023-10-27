package mavmi.git.git.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mavmi.git.git.GitException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GitRepo {
    private final Git git;
    private final List<GirBranch> branches;
    @Getter
    private final File workingDirectory;
    @Getter
    private final String sshUrl;
    @Getter
    private final String httpsUrl;

    @Getter
    @Setter
    private String name;

    public GitRepo(Git git, String name) {
        this.git = git;
        this.branches = new ArrayList<>();
        this.workingDirectory = new File(git.getWorkingDirectory().getAbsolutePath() + "/" + name);
        this.sshUrl = "git@github.com:" + git.getUsername() + "/" + name + ".git";
        this.httpsUrl = "https://github.com/" + git.getUsername() + "/" + name + ".git";
        this.name = name;
    }

    public void gitClone() {
        log.info("Cloning repo {}", name);

        if (!workingDirectory.mkdir()) {
            throw new GitException("Unable to create repo dir: \"" + workingDirectory + "\"");
        } else {
            for (GirBranch girBranch : branches) {
                girBranch.gitClone();
            }
        }
    }

    public void addBranch(GirBranch gitBranch) {
        branches.add(gitBranch);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n")
                .append("\trepo name: ")
                .append(name)
                .append(",\n")
                .append("\tbranches list: [\n");

        for (int i = 0; i < branches.size(); i++) {
            builder.append("\t\t");
            builder.append(branches.get(i));
            if (i + 1 != branches.size()) builder.append(",\n");
            else builder.append("\n\t]\n");
        }

        builder.append("}");
        return builder.toString();
    }

}
