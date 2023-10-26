package mavmi.git.git;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mavmi.git.utils.Utils;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GitRepo {
    private final Git parent;
    private final List<String> branches = new ArrayList<>();

    @Getter
    @Setter
    private String name;

    public GitRepo(Git parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public void gitClone() {
        log.info("Cloning repo {}", name);

        final String repoDirPath = parent.getWorkingDirectory().getAbsolutePath() + "/" + name;
        final File repoDir = new File(repoDirPath);
        if (!repoDir.mkdir()) {
            throw new GitException("Unable to create repo dir: \"" + repoDir + "\"");
        }

        for (int i = 0; i < branches.size(); i++) {
            final String branchName = branches.get(i);
            final String branchGitRef = "refs/heads/" + branchName;

            log.info("branch {}", branchName);

            final String branchDirPath = repoDirPath + "/" + branchName;
            final File branchDir = new File(branchDirPath);
            if (!branchDir.mkdir()) {
                log.error("Unable to create branch dir {}", branchDir);
                continue;
            }

            try {
                org.eclipse.jgit.api.Git
                        .cloneRepository()
                        .setDirectory(branchDir)
                        .setTransportConfigCallback(parent.getSshTransportConfigCallback())
                        .setURI(getBranch(i))
                        .setBranchesToClone(List.of(branchGitRef))
                        .setBranch(branchGitRef)
                        .call()
                        .close();
                if (parent.getRmGit()) {
                    Utils.deleteDir(branchDirPath + "/.git");
                }
            } catch (GitAPIException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    public void addBranch(String branch) {
        branches.add(branch);
    }

    public String getBranchName(int pos) {
        return branches.get(pos);
    }

    public String getBranch(int pos) {
        if (parent.getConnectionType() == CONNECTION_TYPE.SSH) {
            return "git@github.com:" + parent.getUsername() + "/" + name + ".git";
        } else {
            return "https://github.com/" + parent.getUsername() + "/" + name + ".git";
        }
    }

    public int getBranchesCount() {
        return branches.size();
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
