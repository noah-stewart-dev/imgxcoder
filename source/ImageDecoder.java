import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class ImageDecoder {
    private final Path TO_DECODE_PATH;

    public ImageDecoder(Path toDecodePath) {
        TO_DECODE_PATH = toDecodePath;
    }

    public void decodeMessage(File encodedImageFile) throws Exception {
        Color[][] imgColors = getImageColors(encodedImageFile);

        // Check alpha flag to see if a message has been encoded
        if ((imgColors[0][0].getAlpha() & 3) < 2) {
            ArrayList<Character> decodedChars = new ArrayList<>();
            char currentChar = ' ';
            int row = 0;
            int col = 0;

            //Check last char added was not terminating char
            while (currentChar != '\0') {
                if (col >= imgColors[0].length) {
                    col = 0;
                    row++;
                }

                currentChar = getDecodedChar(imgColors[row][col]);
                decodedChars.add(currentChar);

                col++;
            }

            printMessage(decodedChars);
        } else {
            throw new Exception("Image does not contain a message. Please try again with a different image.");
        }
    }

    private Color[][] getImageColors(File imgFile) throws IOException {
        BufferedImage img = ImageIO.read(imgFile);
        int width = img.getWidth();
        int height = img.getHeight();
        Color[][] colors = new Color[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors[i][j] = new Color(img.getRGB(i, j), true);
            }
        }

        return colors;
    }

    private char getDecodedChar(Color encodedColor) {
        // Get hidden bits decimal value based on rgba value
        int alphaBitsDecimal = getEncodedRgbaValue(encodedColor.getAlpha(), ColorComponentEnum.ColorComponent.ALPHA);
        int redBitsDecimal = getEncodedRgbaValue(encodedColor.getRed(), ColorComponentEnum.ColorComponent.RED);
        int greenBitsDecimal = getEncodedRgbaValue(encodedColor.getGreen(), ColorComponentEnum.ColorComponent.GREEN);
        int blueBitsDecimal = getEncodedRgbaValue(encodedColor.getBlue(), ColorComponentEnum.ColorComponent.BLUE);

        return (char) (alphaBitsDecimal + redBitsDecimal + greenBitsDecimal + blueBitsDecimal);
    }

    private int getEncodedRgbaValue(int rgbaValue, ColorComponentEnum.ColorComponent colorComponent) {
        // Get 2 least significant bits decimal value from rgba color value
        int rgbaLsbDecimal = rgbaValue & 3;

        // Assign appropriate decimal values based on bit position
        if (colorComponent == ColorComponentEnum.ColorComponent.ALPHA) {
            rgbaLsbDecimal = 64 * rgbaLsbDecimal;
        } else if (colorComponent == ColorComponentEnum.ColorComponent.RED) {
            rgbaLsbDecimal = 16 * rgbaLsbDecimal;
        } else if (colorComponent == ColorComponentEnum.ColorComponent.GREEN) {
            rgbaLsbDecimal = 4 * rgbaLsbDecimal;
        }

        return rgbaLsbDecimal;
    }

    private void printMessage(ArrayList<Character> messageCharacters) {
        // Set message length to take off terminating char
        int messageLength = messageCharacters.size() - 1;
        StringBuilder message = new StringBuilder(messageLength);

        for (int i = 0; i < messageLength; i++) {
            message.append(messageCharacters.get(i));
        }

        System.out.println(message);
    }

    public void deleteDecoded() {
        File folder = new File(TO_DECODE_PATH.toString());
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }
}