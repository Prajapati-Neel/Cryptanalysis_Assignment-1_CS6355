import java.io.FileOutputStream;
import java.util.Random;

public class Random_ASCII_Plaintext_Generation {
    public static void main(String[] args) {
        int number_of_bytes = 10485760;
        byte write[] = new byte[number_of_bytes];

        try (FileOutputStream f0 = new FileOutputStream("Origin_Plaintext.txt"))
        {
            Random random= new Random();
            for (int i=0; i < number_of_bytes; i++)
            {
               write[i] = (byte)random.nextInt(127);
            }
            f0.write(write);
        }catch (Exception e)
        {
            System.out.print(e);
        }
    }
}
