package mavmi.git.git.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mavmi.git.utils.Utils;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.util.List;

@Slf4j
@Getter
public class GirBranch {
    private final Git git;
    private final GitRepo gitRepo;
    private final String name;
    private final String ref;

    public GirBranch(Git git, GitRepo gitRepo, String name) {
        this.git = git;
        this.gitRepo = gitRepo;
        this.name = name;
        this.ref = "refs/heads/" + name;
    }

    public void gitClone() {
        log.info("Cloning branch {}", name);

        String dirPath = gitRepo.getWorkingDirectory().getAbsolutePath() + "/" + name;
        File dirFile = new File(dirPath);

        if (!dirFile.mkdir()) {
            log.error("Unable to create branch directory: {}", dirPath);
        } else {
            try {
                org.eclipse.jgit.api.Git
                        .cloneRepository()
                        .setDirectory(dirFile)
                        .setTransportConfigCallback(git.getSshTransportConfigCallback())
                        .setURI(gitRepo.getSshUrl())
                        .setBranchesToClone(List.of(ref))
                        .setBranch(ref)
                        .call()
                        .close();
            } catch (GitAPIException e) {
                e.printStackTrace(System.err);
            } finally {
                if (git.getRmGit()) {
                    Utils.deleteDir(dirPath + "/.git");
                }
            }
        }
    }
}
