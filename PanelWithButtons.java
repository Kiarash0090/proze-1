import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PanelWithButtons {
    private static JFrame frame;
    private static JPanel panel;
    private static BufferedImage image;

    public static void main(String[] args) {
        frame = new JFrame("Image Viewer");
        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        JButton button1 = new JButton("Choose Image");
        JButton button2 = new JButton("Show Image");
        JButton button3 = new JButton("Brightness");
        JButton button4 = new JButton("Gray Scale");
        JButton button5 = new JButton("Resize");
        JButton button6 = new JButton("Exit");

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        image = ImageIO.read(selectedFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (image != null) {
                    JFrame imageFrame = new JFrame("Image");
                    JLabel label = new JLabel(new ImageIcon(image));
                    imageFrame.add(label);
                    imageFrame.pack();
                    imageFrame.setVisible(true);
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panel.removeAll();
                panel.setLayout(new GridLayout(3, 1));

                JLabel brightnessLabel = new JLabel("Enter brightness value (between 0 and 1):");
                JTextField brightnessField = new JTextField(10);
                JButton resultButton = new JButton("Result");
                JButton backButton = new JButton("Back");

                resultButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (image != null) {
                            try {
                                float brightnessValue = Float.parseFloat(brightnessField.getText());
                                if (brightnessValue >= 0 && brightnessValue <= 1) {
                                    BufferedImage brightenedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                                    for (int y = 0; y < image.getHeight(); y++) {
                                        for (int x = 0; x < image.getWidth(); x++) {
                                            int rgb = image.getRGB(x, y);
                                            int alpha = (rgb >> 24) & 0xFF;
                                            int red = (rgb >> 16) & 0xFF;
                                            int green = (rgb >> 8) & 0xFF;
                                            int blue = rgb & 0xFF;

                                            red = (int) (red * brightnessValue);
                                            green = (int) (green * brightnessValue);
                                            blue = (int) (blue * brightnessValue);

                                            int newRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                                            brightenedImage.setRGB(x, y, newRGB);
                                        }
                                    }

                                    JFrame resultFrame = new JFrame("Brightened Image");
                                    JLabel resultLabel = new JLabel(new ImageIcon(brightenedImage));
                                    resultFrame.add(resultLabel);
                                    resultFrame.pack();
                                    resultFrame.setVisible(true);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Please enter a value between 0 and 1 for brightness.");
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Please enter a valid number for brightness.");
                            }
                        }
                    }
                });
                button4.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Load the selected image
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.showOpenDialog(null);
                            File file = fileChooser.getSelectedFile();
                            image = ImageIO.read(file);

                            // Convert the image to grayscale
                            for (int y = 0; y < image.getHeight(); y++) {
                                for (int x = 0; x < image.getWidth(); x++) {
                                    int rgb = image.getRGB(x, y);
                                    int r = (rgb >> 16) & 0xFF;
                                    int g = (rgb >> 8) & 0xFF;
                                    int b = (rgb & 0xFF);
                                    int gray = (r + g + b) / 3;
                                    int grayValue = (gray << 16) + (gray << 8) + gray;
                                    image.setRGB(x, y, grayValue);
                                }
                            }

                            // Display the grayscale image in a new frame
                            JFrame grayscaleFrame = new JFrame("Grayscale Image");
                            grayscaleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            grayscaleFrame.add(new JLabel(new ImageIcon(image)));
                            grayscaleFrame.pack();
                            grayscaleFrame.setVisible(true);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });



                backButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        panel.removeAll();
                        panel.setLayout(new GridLayout(3, 3));
                        panel.add(button1);
                        panel.add(button2);
                        panel.add(button3);
                        panel.add(button4);
                        panel.add(button5);
                        panel.add(button6);
                        frame.revalidate();
                        frame.repaint();
                    }
                });

                panel.add(brightnessLabel);
                panel.add(brightnessField);
                panel.add(resultButton);
                panel.add(backButton);
                frame.revalidate();
                frame.repaint();
            }
        });

        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String widthInput = JOptionPane.showInputDialog("لطفا عرض جدید را وارد کنید:");
                String heightInput = JOptionPane.showInputDialog("لطفا ارتفاع جدید را وارد کنید:");

                int newWidth = Integer.parseInt(widthInput);
                int newHeight = Integer.parseInt(heightInput);

                try {
                    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(image, 0, 0, newWidth, newHeight, null);
                    g.dispose();

                    JFrame newFrame = new JFrame("تصویر تغییر اندازه یافته");
                    JLabel label = new JLabel(new ImageIcon(resizedImage));
                    newFrame.add(label);
                    newFrame.pack();
                    newFrame.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);
        panel.add(button5);
        panel.add(button6);

        frame.add(panel);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}