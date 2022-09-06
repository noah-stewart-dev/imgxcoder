import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class AppManager {
    private final ImageEncoder IMAGE_ENCODER;
    private final ImageDecoder IMAGE_DECODER;
    private final Path TO_ENCODE_PATH;
    private final Path TO_DECODE_PATH;
    private static final Scanner SCANNER = new Scanner(System.in);
    private static String userInput;

    public AppManager(Path toEncodePath, Path encodedPath, Path toDecodePath) {
        TO_ENCODE_PATH = toEncodePath;
        TO_DECODE_PATH = toDecodePath;
        IMAGE_ENCODER = new ImageEncoder(encodedPath);
        IMAGE_DECODER = new ImageDecoder(toDecodePath);
    }

    public void printLogo() {
        System.out.println("\n" +
                "██╗███╗   ███╗ █████╗  ██████╗ ███████╗    ███████╗███╗   ██╗ ██████╗ ██████╗ ██████╗ ███████╗██████╗ \n" +
                "██║████╗ ████║██╔══██╗██╔════╝ ██╔════╝    ██╔════╝████╗  ██║██╔════╝██╔═══██╗██╔══██╗██╔════╝██╔══██╗\n" +
                "██║██╔████╔██║███████║██║  ███╗█████╗      █████╗  ██╔██╗ ██║██║     ██║   ██║██║  ██║█████╗  ██████╔╝\n" +
                "██║██║╚██╔╝██║██╔══██║██║   ██║██╔══╝      ██╔══╝  ██║╚██╗██║██║     ██║   ██║██║  ██║██╔══╝  ██╔══██╗\n" +
                "██║██║ ╚═╝ ██║██║  ██║╚██████╔╝███████╗    ███████╗██║ ╚████║╚██████╗╚██████╔╝██████╔╝███████╗██║  ██║\n" +
                "╚═╝╚═╝     ╚═╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝    ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝\n");
    }

    public void printMainMenu() {
        System.out.println("1) Encode Message");
        System.out.println("2) Decode Message");
        System.out.println("3) Delete All Encoded Messages");
        System.out.println("4) Delete All Decoded Messages");
        System.out.println("5) Exit");
    }

    public void encodeMessagePrompt() {
        int numOptions = getDirectoryFileCount(TO_ENCODE_PATH.toString()) + 1;
        int optionSelected;
        boolean isRunning = true;
        boolean error;

        System.out.println("Select file to encode: ");
        printFileSelectionMenu(TO_ENCODE_PATH.toString());

        while (isRunning) {
            userInput = SCANNER.nextLine();
            error = validateInput(userInput, numOptions);

            if (error) {
                encodeMessageErrorAlert();
                continue;
            }

            optionSelected = Integer.parseInt(userInput);
            // Check quit was not selected
            if (optionSelected != numOptions) {
                System.out.println("Message to encode: ");
                userInput = SCANNER.nextLine();

                try {
                    IMAGE_ENCODER.encodeMessage(getFile(TO_ENCODE_PATH.toString(), optionSelected - 1), userInput);
                } catch (IOException ex) {
                    System.out.println("Something went wrong retrieving or storing the image. Please try again or select another image to encode.");
                    System.out.println("Press enter to go back: ");
                    userInput = SCANNER.nextLine();
                    break;
                } catch (Exception ex) {
                    System.out.println("Could not encode message. " + ex.getMessage() + " Press enter to go back: ");
                    userInput = SCANNER.nextLine();
                    break;
                }
                System.out.println("Image successfully encoded! Press enter to go back: ");
                userInput = SCANNER.nextLine();
            }

            isRunning = false;
        }
    }

    private void encodeMessageErrorAlert() {
        System.out.println("Select file to encode: ");
        printFileSelectionMenu(TO_ENCODE_PATH.toString());
        System.out.println("Invalid Input. Try again.");
    }

    public void decodeMessagePrompt() {
        int numOptions = getDirectoryFileCount(TO_DECODE_PATH.toString()) + 1;
        int optionSelected;
        boolean isRunning = true;
        boolean error;

        System.out.println("Select file to decode: ");
        printFileSelectionMenu(TO_DECODE_PATH.toString());

        while (isRunning) {
            userInput = SCANNER.nextLine();
            error = validateInput(userInput, numOptions);

            if (error) {
                decodeMessageErrorAlert();
                continue;
            }

            optionSelected = Integer.parseInt(userInput);
            // Check quit was not selected
            if (optionSelected != numOptions) {
                System.out.println("Message: ");
                try {
                    IMAGE_DECODER.decodeMessage(getFile(TO_DECODE_PATH.toString(), (optionSelected - 1)));
                } catch (IOException ex) {
                    System.out.println("Something went wrong retrieving or storing the image. Please try again or select another image to decode.");
                    System.out.println("Press enter to go back: ");
                    userInput = SCANNER.nextLine();
                    break;
                } catch (Exception ex) {
                    System.out.println("Could not decode message. " + ex.getMessage());
                    System.out.println("Press enter to go back: ");
                    userInput = SCANNER.nextLine();
                    break;
                }
                System.out.println("Image successfully decoded! Press enter to go back: ");
                userInput = SCANNER.nextLine();
            }

            isRunning = false;
        }
    }

    private void decodeMessageErrorAlert() {
        System.out.println("Select file to decode: ");
        printFileSelectionMenu(TO_DECODE_PATH.toString());
        System.out.println("Invalid Input. Try again.");
    }

    public void deleteEncodedPrompt() {
        boolean isRunning = true;
        boolean error = false;

        while (isRunning) {
            System.out.println("Delete all encoded messages? (y/n): ");

            if (error) {
                System.out.println("Invalid input. Try again.");
                error = false;
            }

            userInput = SCANNER.nextLine().toLowerCase();
            switch (userInput) {
                case "y":
                    IMAGE_ENCODER.deleteEncoded();
                    isRunning = false;
                    break;
                case "n":
                    isRunning = false;
                    break;
                default:
                    error = true;
                    break;
            }
        }
    }

    public void deleteDecodedPrompt() {
        boolean isRunning = true;
        boolean error = false;

        while (isRunning) {
            System.out.println("Delete all decoded messages? (y/n): ");

            if (error) {
                System.out.println("Invalid input. Try again.");
                error = false;
            }

            userInput = SCANNER.nextLine().toLowerCase();
            switch (userInput) {
                case "y":
                    IMAGE_DECODER.deleteDecoded();
                    isRunning = false;
                    break;
                case "n":
                    isRunning = false;
                    break;
                default:
                    error = true;
                    break;
            }
        }
    }

    public void printFileSelectionMenu(String pathName) {
        if (pathName != null) {
            File folder = new File(pathName);
            File[] listOfFiles = folder.listFiles();

            int fileCount = getDirectoryFileCount(pathName);

            for (int i = 0; i < fileCount; i++) {
                String fileName = listOfFiles[i].toString().split("\\\\")[2];
                System.out.println((i + 1) + ") " + fileName);
            }
            System.out.println((listOfFiles.length + 1) + ") Back");
        }
    }

    private int getDirectoryFileCount(String pathName) {
        File folder = new File(pathName);

        if (folder.exists()) {
            return folder.listFiles().length;
        }

        return -1;
    }

    private File getFile(String pathName, int index) {
        File folder = new File(pathName);
        File[] listOfFiles = folder.listFiles();

        return listOfFiles[index];
    }

    private boolean validateInput(String userInput, int numOptions) {
        int inputNum;

        try {
            inputNum = Integer.parseInt(userInput);
        } catch (NumberFormatException ex) {
            return true;
        }

        // Return true if input is out of options bounds
        return (inputNum > numOptions || inputNum < 1);
    }
}