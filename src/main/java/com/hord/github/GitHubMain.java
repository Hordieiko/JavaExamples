package com.hord.github;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GitHubMain {

    private static final Logger logger = LogManager.getLogger(GitHubMain.class);

    private static final String CLM_26867_6_10_4_DocuSign_v2_1_OAuth_2_0 = "2c6b6f2bb1e6c6120b9ff216591237a64d057000";
    private static final String CLM_26868_6_9_5_DocuSign_v2_1_OAuth_2_0 = "0322c4e77850d4f8cd28d22daf69451b8f504690";
    private static final String CLM_26872_6_9_0_DocuSign_v2_1_OAuth_2_0 = "de4ae3fcdbfb57f464f32fa6eb790cba3aadaef8";
    private static final String CLM_26869_6_8_1_DocuSign_v2_1_OAuth_2_0 = "9c6954a01aa658f43ea721df0f9f3c1fe454462a";

    public static void main(String[] args) {
        try {
            String owner = "Selectica";
            String repo = "CLM";
            String commitSHA = CLM_26869_6_8_1_DocuSign_v2_1_OAuth_2_0;

            logger.info("Repository: {}/{}", owner, repo);
            logger.info("Commit SHA: {}", commitSHA);

            List<String> fileNameList = getFileNameList("fileNameList");
            GitHubWrapper gitHub = new GitHubWrapper(getDefaultGitHub(), owner);
            GHRepository repository = gitHub.getRepository(repo);

            List<GHBranch> branches = repository.getCommit(commitSHA).listBranchesWhereHead().toList();
            if (branches.size() == 1)
                logger.info("Branch: {}", branches.get(0).getName());

            List<GHCommit.File> files = getAddedOrListedFilesByCommit(repository, commitSHA, fileNameList);
            logger.info("{} files ready for copy", files.size());

            String sourceDirPath = "C:\\Users\\vhordieiko\\IdeaProjects\\CLM_2\\CLM";
            String targetDirPath = "C:\\Users\\vhordieiko\\IdeaProjects\\CLM";
            copyFiles(sourceDirPath, targetDirPath, files);
        } catch (IOException e) {
            logger.error("Something went wrong!", e);
        }
    }

    private static void printModifiedJavaClasses(GHRepository repository, String commitSHA) throws IOException {
        List<GHCommit.File> modifiedJavaClasses = getFilesByCommit(repository, commitSHA).stream()
                .filter(file -> "modified".equals(file.getStatus()) && file.getFileName().endsWith(".java"))
                .collect(Collectors.toList());
        logger.info("{} modified Java classes collected", modifiedJavaClasses.size());
        printFileNames(modifiedJavaClasses);
    }

    private static void printUnfitFilesInfo(String commitSHA, List<String> fileNameList, GHRepository repository) throws IOException {
        List<GHCommit.File> unfitFiles = getFilesByCommit(repository, commitSHA).stream()
                .filter(file -> !"added".equals(file.getStatus()) && !fileNameList.contains(file.getFileName()))
                .collect(Collectors.toList());
        logger.info("{} unfit files collected", unfitFiles.size());
        printFileNames(unfitFiles);
    }

    private static void printFileNames(List<GHCommit.File> files) {
        String listFileNames = files.stream()
                .map(GHCommit.File::getFileName)
                .collect(Collectors.joining("\n"));
        logger.info("Collected files:\n{}", listFileNames);
    }

    private static List<String> getFileNameList(String resourceName) {
        try {
            return Files.readAllLines(Paths.get(ClassLoader.getSystemResource(resourceName).toURI()));
        } catch (IOException e) {
            logger.error("Error upon reading resource file: {}", resourceName);
        } catch (URISyntaxException e) {
            logger.error("Error upon getting resource file: {}", resourceName);
        }
        return Collections.emptyList();
    }

    private static void copyFiles(String sourceDirPath, String targetDirPath, List<GHCommit.File> files) {
        Path sourcePath = Paths.get(sourceDirPath);
        Path targetPath = Paths.get(targetDirPath);
        logger.info("Source path: {}", sourcePath);
        logger.info("Target path: {}", targetPath);

        AtomicInteger copyCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();
        Map<String, AtomicInteger> fileStatusCounter = new HashMap<>();
        files.forEach(file -> {
            String fileName = file.getFileName();
            Path source = sourcePath.resolve(fileName);
            Path target = targetPath.resolve(fileName);

            if (!Files.exists(source)) {
                logger.error("Skip copying! Source file doesn't exist by path: {}", source);
                return;
            }

            try {
                Files.deleteIfExists(target);
            } catch (IOException e) {
                logger.error("Skip copying! Error on deleting file by path: {}", target);
                return;
            }

            File targetFile = target.toFile();
            targetFile.getParentFile().mkdirs();

            String fileStatus = file.getStatus();
            try {
                Files.copy(source, target);
                fileStatusCounter.computeIfAbsent(fileStatus, k -> new AtomicInteger()).getAndIncrement();
                logger.info("Copied '{}' file to: {}", fileStatus, target);
                copyCount.getAndIncrement();
            } catch (IOException e) {
                logger.error("Error on copying '{}' file \nfrom: {}\n to: {}", fileStatus, source, target);
                errorCount.getAndIncrement();
            }
        });
        logger.info("{} failed copy attempts", errorCount.get());
        logger.info("{} files copied \nfrom: {} \nto: {}", copyCount.get(), sourceDirPath, targetDirPath);
        fileStatusCounter.forEach((status, count) -> logger.info("{} '{}' files copied", count, status));
    }

    private static List<GHCommit.File> getAddedFilesByCommit(GHRepository repository, String commitSHA) throws IOException {
        return repository.getCommit(commitSHA).getFiles().stream()
                .filter(file -> "added".equals(file.getStatus()))
                .collect(Collectors.toList());
    }

    private static List<GHCommit.File> getAddedOrListedFilesByCommit(GHRepository repository, String commitSHA, List<String> fileNameList) throws IOException {
        List<GHCommit.File> files = getFilesByCommit(repository, commitSHA);
        logger.info("{} changed files", files.size());
        List<GHCommit.File> unfitFiles = new ArrayList<>();
        List<GHCommit.File> addedOrListedFiles = files.stream()
                .filter(file -> {
                    if ("added".equals(file.getStatus()) || fileNameList.contains(file.getFileName())) {
                        return true;
                    } else {
                        unfitFiles.add(file);
                        return false;
                    }
                })
                .collect(Collectors.toList());
        logger.info("{} unfit files", unfitFiles.size());
        String listUnfitFileNames = unfitFiles.stream().map(GHCommit.File::getFileName).collect(Collectors.joining("\n"));
        listUnfitFileNames += "\n\n";
        logger.info("Unfit files:\n{}", listUnfitFileNames);
        return addedOrListedFiles;
    }

    private static List<GHCommit.File> getFilesByCommit(GHRepository repository, String commitSHA) throws IOException {
        return repository.getCommit(commitSHA).getFiles();
    }

    private static GitHub getDefaultGitHub() throws IOException {
        return GitHubBuilder.fromPropertyFile("./github.properties").build();
    }
}
