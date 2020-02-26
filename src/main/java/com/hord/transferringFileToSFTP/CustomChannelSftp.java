package com.hord.transferringFileToSFTP;

import com.jcraft.jsch.*;

import java.util.Properties;

public class CustomChannelSftp  {

    private final static String username = "disney";
    private final static String password = "1t4%Kh&d2UDQhze&";
    private final static String remoteHost = "prod-sftp.selectica.com";

    private ChannelSftp setupJsch() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, remoteHost, 22);
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        return (ChannelSftp) session.openChannel("sftp");
    }

    public static void main(String[] args) throws JSchException, SftpException {
        ChannelSftp channelSftp = new CustomChannelSftp().setupJsch();
        channelSftp.connect();

        String localFile = "C:\\Users\\vhordieiko\\IdeaProjects\\JavaExamples\\src\\main\\resources\\toSFTP\\file.txt";
        String remoteDir = "write_folder/Determine/test/";

        channelSftp.put(localFile, remoteDir + "jschFile.txt");
        channelSftp.exit();
    }
}
