package ole.core;

import picocli.CommandLine;

import java.io.File;
import java.nio.file.Paths;

public class Main {

    private final Ole ole = new Ole();

    public static void main(String[] args) {

        new Main().run(args);
    }

    private void run(String[] args) {
        OleOption oleOption = new Main().parseArguments(args);

        if (oleOption.isHelpNeeded()) {
            printUsage(oleOption);

            System.err.println("==> user.dir = " + System.getProperty("user.dir"));

            System.err.println("==> java.class.path = "+System.getProperty("java.class.path"));

            String currentDir = Paths.get("").toAbsolutePath().toString();
            System.out.println("currentDirectory: " + currentDir);

            String newFileCurrentDir = new File("").getAbsolutePath().toString();
            System.out.println("newFileCurrentDir: " + newFileCurrentDir);

            try {String callerPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                System.out.println("调用程序的路径是：" + callerPath);

            } catch (Exception e){}


            return;
        }

        if (oleOption.isGen()) {
            System.err.println("help oleOption.getRootGenLocalPath()= " + oleOption.getRootGenLocalPath());
            ole.generate(oleOption.getRootGenLocalPath());
            return;
        }


    }

    private void printUsage(Object options) {
        CommandLine cli = new CommandLine(options);
        cli.setUsageHelpLongOptionsMaxWidth(28);
        cli.usage(System.err);
    }

    private OleOption parseArguments(String[] args) {
        return CommandLine.populateCommand(new OleOption(), args);
    }
}
