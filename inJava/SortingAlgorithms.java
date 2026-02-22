import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SortingAlgorithms {

    static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[i];
            arr[i] = arr[minIdx];
            arr[minIdx] = temp;
        }
    }

    static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000000);
        }
        return arr;
    }

    static int[] copyArray(int[] arr) {
        int[] copy = new int[arr.length];
        System.arraycopy(arr, 0, copy, 0, arr.length);
        return copy;
    }

    static void plotGraph(int[] sizes, long[] bubbleTimes, long[] selectionTimes, long[] insertionTimes) throws IOException {
        int width = 1000, height = 600;
        int margin = 80;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Find max time for scaling
        long maxTime = 0;
        for (int i = 0; i < sizes.length; i++) {
            maxTime = Math.max(maxTime, Math.max(bubbleTimes[i], Math.max(selectionTimes[i], insertionTimes[i])));
        }
        maxTime = (long)(maxTime * 1.1); // 10% padding

        int plotW = width - 2 * margin;
        int plotH = height - 2 * margin;

        // Draw axes
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawLine(margin, margin, margin, height - margin);
        g.drawLine(margin, height - margin, width - margin, height - margin);

        // Grid lines and Y-axis labels
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setStroke(new BasicStroke(1));
        int numGridLines = 5;
        for (int i = 0; i <= numGridLines; i++) {
            int y = height - margin - (i * plotH / numGridLines);
            long val = i * maxTime / numGridLines;
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(margin, y, width - margin, y);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(val), 10, y + 5);
        }

        // X-axis labels
        for (int i = 0; i < sizes.length; i++) {
            int x = margin + (i * plotW / (sizes.length - 1));
            g.setColor(Color.BLACK);
            String label = String.format("%,d", sizes[i]);
            g.drawString(label, x - 20, height - margin + 20);
        }

        // Plot lines
        Color[] colors = {new Color(31, 119, 180), new Color(255, 127, 14), new Color(44, 160, 44)};
        String[] labels = {"Bubble Sort", "Selection Sort", "Insertion Sort"};
        long[][] allTimes = {bubbleTimes, selectionTimes, insertionTimes};

        g.setStroke(new BasicStroke(2.5f));
        for (int s = 0; s < 3; s++) {
            g.setColor(colors[s]);
            for (int i = 0; i < sizes.length - 1; i++) {
                int x1 = margin + (i * plotW / (sizes.length - 1));
                int y1 = height - margin - (int)(allTimes[s][i] * plotH / maxTime);
                int x2 = margin + ((i + 1) * plotW / (sizes.length - 1));
                int y2 = height - margin - (int)(allTimes[s][i + 1] * plotH / maxTime);
                g.drawLine(x1, y1, x2, y2);
            }
            // Draw markers
            for (int i = 0; i < sizes.length; i++) {
                int x = margin + (i * plotW / (sizes.length - 1));
                int y = height - margin - (int)(allTimes[s][i] * plotH / maxTime);
                g.fillOval(x - 5, y - 5, 10, 10);
            }
        }

        // Title
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString("Time (milliseconds) vs Array Size for Sorting Algorithms", width / 2 - 250, 30);

        // Axis labels
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.drawString("Input Size (N)", width / 2 - 40, height - 10);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.rotate(-Math.PI / 2);
        g2.drawString("Execution Time (milliseconds)", -height / 2 - 80, 20);
        g2.dispose();

        // Legend
        int legendX = width - margin - 160, legendY = margin + 10;
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        for (int s = 0; s < 3; s++) {
            g.setColor(colors[s]);
            g.fillRect(legendX, legendY + s * 22, 15, 15);
            g.setColor(Color.BLACK);
            g.drawString(labels[s], legendX + 20, legendY + s * 22 + 13);
        }

        g.dispose();
        ImageIO.write(image, "png", new File("sorting_comparison_graph.png"));
        System.out.println("Graph saved to sorting_comparison_graph.png");
    }

    public static void main(String[] args) throws IOException {
        int[] sizes = {10000, 25000, 50000, 75000, 100000};

        // Warm-up round: run each sort once to trigger JIT compilation
        System.out.println("Warming up JVM...");
        int[] warmUp = generateRandomArray(1000);
        bubbleSort(copyArray(warmUp));
        selectionSort(copyArray(warmUp));
        insertionSort(copyArray(warmUp));
        System.out.println("Warm-up complete.\n");

        long[] bubbleTimes = new long[sizes.length];
        long[] selectionTimes = new long[sizes.length];
        long[] insertionTimes = new long[sizes.length];

        FileWriter csv = new FileWriter("results.csv");
        csv.write("Size,Bubble Sort,Selection Sort,Insertion Sort\n");

        for (int i = 0; i < sizes.length; i++) {
            int size = sizes[i];
            System.out.println("\nArray Size: " + size);

            int[] arr = generateRandomArray(size);

            int[] copy1 = copyArray(arr);
            long start = System.currentTimeMillis();
            bubbleSort(copy1);
            long end = System.currentTimeMillis();
            bubbleTimes[i] = end - start;
            System.out.println("Running time for Bubble Sort is " + bubbleTimes[i] + " ms");

            int[] copy2 = copyArray(arr);
            start = System.currentTimeMillis();
            selectionSort(copy2);
            end = System.currentTimeMillis();
            selectionTimes[i] = end - start;
            System.out.println("Running time for Selection Sort is " + selectionTimes[i] + " ms");

            int[] copy3 = copyArray(arr);
            start = System.currentTimeMillis();
            insertionSort(copy3);
            end = System.currentTimeMillis();
            insertionTimes[i] = end - start;
            System.out.println("Running time for Insertion Sort is " + insertionTimes[i] + " ms");

            csv.write(size + "," + bubbleTimes[i] + "," + selectionTimes[i] + "," + insertionTimes[i] + "\n");
        }

        csv.close();
        System.out.println("\nResults saved to results.csv");

        plotGraph(sizes, bubbleTimes, selectionTimes, insertionTimes);
    }
}
