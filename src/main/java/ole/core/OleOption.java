package ole.core;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "ole", description = "a tool make md file to html", mixinStandardHelpOptions = true)
public class OleOption {

    @Option(names = {"-g", "--genPath"}, description = "generate")
    private String localPath;

    @Option(names = {"-h", "--help"}, description = "prints this message", usageHelp = true)
    private boolean needHelp;

    @Option(names = {"-s","--server"}, description = "run server by fucking path :)")
    private boolean runServer; // 先做成这种形式的，虽然说我很不想，淦！


    @Option(names = {"-p", "--path"}, description = "generate fucking path")
    private String path;


    public boolean isGen() {
        return localPath != null;
    }

    public boolean isHelpNeeded() {
        return needHelp;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getPath() {
        return path;
    }

    public boolean isHasPath() {
        return path != null && path.length() > 0;
    }

    public boolean isRunServer() {
        return runServer;
    }
}
