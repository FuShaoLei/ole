package ole.core;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "ole", description = "a tool make md file to html", mixinStandardHelpOptions = true)
public class OleOption {

    @Option(names = {"-g", "--gen"}, description = "generate")
    private boolean gen;

    @Option(names = {"-h", "--help"}, description = "prints this message", usageHelp = true)
    private boolean needHelp;


    public boolean isGen() {
        return gen;
    }

    public boolean isHelpNeeded() {
        return needHelp || !gen;
    }

    public String getRootGenLocalPath() {
        return System.getProperty("user.dir");
    }
}
