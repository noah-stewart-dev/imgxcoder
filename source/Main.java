import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Path TO_ENCODE_PATH = Paths.get("./imagesToEncode");
        final Path ENCODED_PATH = Paths.get("./imagesEncoded");
        final Path TO_DECODE_PATH = Paths.get("./imagesToDecode");
        final AppManager APP_MANAGER = new AppManager(TO_ENCODE_PATH, ENCODED_PATH, TO_DECODE_PATH);
        final Scanner SCANNER = new Scanner(System.in);
        boolean isRunning = true;
        String userInput;

        if (Files.notExists(TO_ENCODE_PATH)) {
            File toEncodeDir = new File(TO_ENCODE_PATH.toString());
            toEncodeDir.mkdir();
        }
        if (Files.notExists(ENCODED_PATH)) {
            File encodedDir = new File(ENCODED_PATH.toString());
            encodedDir.mkdir();
        }
        if (Files.notExists(TO_DECODE_PATH)) {
            File toDecodeDir = new File(TO_DECODE_PATH.toString());
            toDecodeDir.mkdir();
        }

        APP_MANAGER.printLogo();

        while (isRunning) {
            APP_MANAGER.printMainMenu();
            userInput = SCANNER.nextLine();

            switch (userInput) {
                case "1":
                    APP_MANAGER.encodeMessagePrompt();
                    break;
                case "2":
                    APP_MANAGER.decodeMessagePrompt();
                    break;
                case "3":
                    APP_MANAGER.deleteEncodedPrompt();
                    break;
                case "4":
                    APP_MANAGER.deleteDecodedPrompt();
                    break;
                case "5":
                    isRunning = false;
                    break;
                default:
                    APP_MANAGER.printMainMenu();
                    System.out.println("Invalid Input. Try again.");
                    break;
            }
        }
    }
}