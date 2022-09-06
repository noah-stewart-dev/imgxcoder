import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class ImageEncoder {
    private final Path ENCODED_PATH;

    public ImageEncoder(Path encodedPath) {
        ENCODED_PATH = encodedPath;
    }

    public void encodeMessage(File imgFile, String message) throws Exception {
        Color[][] imgColors = getImageColors(imgFile);

        if (isValidMessageLength(imgFile, message)) {
            int row = 0;
            int col = 0;

            for (int i = 0; i < message.length() + 1; i++) {
                if (col >= imgColors[0].length) {
                    col = 0;
                    row++;
                }

                // Get current char from message to encode. If all chars added, add terminating char instead
                imgColors[row][col] = i == message.length() ? getEncodedColor(imgColors[row][col], '\0') : getEncodedColor(imgColors[row][col], message.charAt(i));

                col++;
            }

            createNewImage(imgColors);
        } else {
            throw new Exception("Message length was too long. Please enter a shorter message or select a larger image to encode.");
        }
    }

    private Color[][] getImageColors(File imgFile) throws IOException {
        BufferedImage img = ImageIO.read(imgFile);
        int width = img.getWidth();
        int height = img.getHeight();
        Color[][] imgColors = new Color[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imgColors[i][j] = new Color(img.getRGB(i, j), true);
            }
        }

        return imgColors;
    }

    private boolean isValidMessageLength(File imgFile, String message) throws IOException {
        BufferedImage img = ImageIO.read(imgFile);

        return message.length() + 1 < img.getWidth() * img.getHeight();
    }

    private Color getEncodedColor(Color colorToEncode, char charToEncode) {
        int encodedAlpha = getEncodedRgbaValue(colorToEncode.getAlpha(), charToEncode, ColorComponentEnum.ColorComponent.ALPHA);
        int encodedRed = getEncodedRgbaValue(colorToEncode.getRed(), charToEncode, ColorComponentEnum.ColorComponent.RED);
        int encodedGreen = getEncodedRgbaValue(colorToEncode.getGreen(), charToEncode, ColorComponentEnum.ColorComponent.GREEN);
        int encodedBlue = getEncodedRgbaValue(colorToEncode.getBlue(), charToEncode, ColorComponentEnum.ColorComponent.BLUE);

        return new Color(encodedRed, encodedGreen, encodedBlue, encodedAlpha);
    }

    private int getEncodedRgbaValue(int rgbaValue, int charToEncode, ColorComponentEnum.ColorComponent colorComponent) {
        int rgbaLsb = rgbaValue & 3;
        int charToEncodeLsb = ((charToEncode >> colorComponent.getComponentBitPosition()) & 3);

        return rgbaValue + (charToEncodeLsb - rgbaLsb);
    }

    private void createNewImage(Color[][] imgColors) throws IOException {
        File imgFile = new File(ENCODED_PATH + "\\" + UUID.randomUUID() + ".png");
        BufferedImage encodedImg = new BufferedImage(imgColors.length, imgColors[0].length, BufferedImage.TYPE_INT_ARGB);

        for (int row = 0; row < imgColors.length; row++) {
            for (int col = 0; col < imgColors[row].length; col++) {
                encodedImg.setRGB(row, col, imgColors[row][col].getRGB());
            }
        }

        FileOutputStream stream = new FileOutputStream(imgFile);
        ImageIO.write(encodedImg, "png", stream);
        stream.close();
    }

    public void deleteEncoded() {
        File folder = new File(ENCODED_PATH.toString());
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }
}