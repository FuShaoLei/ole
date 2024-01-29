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


    public boolean isGen() {
        return localPath != null;
    }

    public boolean isHelpNeeded() {
        return needHelp || localPath == null;
    }

    public String getLocalPath() {
        return localPath;
    }
}
