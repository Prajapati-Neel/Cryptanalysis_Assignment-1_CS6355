import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;

public class A3 {
    public static void main(String[] args) throws IOException {
        long RSA_encryption_time = 0, RSA_decryption_time = 0, AES_encryption_time = 0, AES_decryption_time = 0;
        long start_time, stop_time;
        // RSA Key Generation
        BigInteger P = new BigInteger("15554903035303856344007671063568213071669822184616101992595534860863803506262760067615727000088295330493705796902296102481798240988227195060316199080930616035532980617309644098719341753037782435645781436420697261984870969742096465765855782491538043554917285285471407866976465359446400695692459955929581561107496250057761324472438514351159746606737260676765872636140119669971105314539393270612398055538928361845237237855336149792618908050931870177925910819318623");
        BigInteger Q = new BigInteger("15239930048457525970295803203207379514343031714151154517998415248470711811442956493342175286216470497855132510489015253513519073889825927436792580707512051299817290925038739023722366499292196400002204764665762114445764643179358348705750427753416977399694184804769596469561594013716952794631383872745339020403548881863215482480719445814165242627056637786302612482697923973303250588684822021988008175106735736411689800380179302347354882715496632291069525885653297");
        BigInteger n = P.multiply(Q);
        BigInteger totient = (P.subtract(new BigInteger("1"))).multiply(Q.subtract(new BigInteger("1")));
        BigInteger e = new BigInteger("65537");
        BigInteger d = e.modInverse(totient);
        int file_size_in_bytes, block_size_in_bytes, no_of_blocks;

        // RSA Encryption
        try ( FileInputStream input_file = new FileInputStream("E:\\Assignment RSA\\untitled\\Origin_Plaintext.txt") ; FileOutputStream output_file = new FileOutputStream("Ciphertext_RSA.txt")) {
            start_time = System.currentTimeMillis();
            BigInteger plaintext, ciphertext, read_ciphertext;
            file_size_in_bytes = input_file.available();
            block_size_in_bytes = (n.bitLength()/8) - 1;
            byte blocks[][]=null;
            if(file_size_in_bytes % block_size_in_bytes!=0) {
                no_of_blocks = (file_size_in_bytes / block_size_in_bytes) + 1;
                blocks = new byte[no_of_blocks][block_size_in_bytes];
                blocks[no_of_blocks-1] = new byte[file_size_in_bytes % block_size_in_bytes];
            }else {
                no_of_blocks = (file_size_in_bytes / block_size_in_bytes);
                blocks = new byte[no_of_blocks][block_size_in_bytes];
            }
            for (int i = 0; i < no_of_blocks; i++) {
                input_file.read(blocks[i]);
                plaintext = new BigInteger(blocks[i]);
                ciphertext = plaintext.modPow(e,n);
                byte ciphertext_ascii_output[] = new byte[(n.bitLength()/8)+1];
                byte ciphertext_bytes[] = ciphertext.toByteArray();
                System.arraycopy(ciphertext_bytes,0,ciphertext_ascii_output,(n.bitLength()/8)+1-ciphertext_bytes.length,ciphertext_bytes.length);
                output_file.write(ciphertext_ascii_output);
            }
            stop_time = System.currentTimeMillis();
            RSA_encryption_time = stop_time - start_time;
        }catch (Exception E)
        {
            System.out.print(E);
        }

        // RSA Decryption
        try ( FileInputStream input_file = new FileInputStream("Ciphertext_RSA.txt") ; FileOutputStream output_file = new FileOutputStream("Plaintext_Output_RSA.txt")) {
            start_time = System.currentTimeMillis();
            BigInteger ciphertext, plaintext;
            BigInteger Qdash = Q.modInverse(P);
            BigInteger Pdash = P.modInverse(Q);
            BigInteger dP = d.mod(P.subtract(new BigInteger("1")));
            BigInteger dQ = d.mod(Q.subtract(new BigInteger("1")));
            BigInteger cP, cQ, mP, mQ;
            file_size_in_bytes = input_file.available();
            block_size_in_bytes = (n.bitLength()/8) + 1;
            no_of_blocks = (file_size_in_bytes/block_size_in_bytes) ;
            byte b[][] = new byte[no_of_blocks][block_size_in_bytes];
            for (int i = 0; i < no_of_blocks; i++) {
                input_file.read(b[i]);
                ciphertext = new BigInteger(b[i]);
                cP = ciphertext.mod(P);
                cQ = ciphertext.mod(Q);
                mP = cP.modPow(dP,P);
                mQ = cQ.modPow(dQ,Q);
                plaintext = ((mP.multiply(Q).multiply(Qdash)).add(mQ.multiply(P).multiply(Pdash))).mod(n);
                byte plaintext_bytes[] = plaintext.toByteArray();
                byte plaintext_ascii_output[] = new byte[(n.bitLength()/8)-1];
                if(i<no_of_blocks-1) {
                    System.arraycopy(plaintext_bytes, 0, plaintext_ascii_output, (n.bitLength() / 8) - 1 - plaintext_bytes.length, plaintext_bytes.length);
                    output_file.write(plaintext_ascii_output);
                }else
                {
                    output_file.write(plaintext_bytes);
                }
                stop_time = System.currentTimeMillis();
                RSA_decryption_time = stop_time - start_time;
            }
        }catch (Exception E)
        {
            throw (E);
        }

        // AES Encryption
        Key secretKey=null;
        block_size_in_bytes = 16;
        try (FileInputStream input_file = new FileInputStream("E:\\Assignment RSA\\untitled\\Origin_Plaintext.txt"); FileOutputStream output_file = new FileOutputStream("Ciphertext_AES.txt")) { //new FileOutputStream("E:\\Assignment RSA\\untitled\\Ciphertext_AES.txt")) {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            secretKey = keyGenerator.generateKey();
            start_time = System.currentTimeMillis();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            file_size_in_bytes = input_file.available();
            byte plaintext[][]=null;
            if(file_size_in_bytes % block_size_in_bytes!=0) {
                no_of_blocks = (file_size_in_bytes / block_size_in_bytes) + 1;
                plaintext = new byte[no_of_blocks][block_size_in_bytes];
                plaintext[no_of_blocks-1] = new byte[file_size_in_bytes % block_size_in_bytes];
            }else {
                no_of_blocks = (file_size_in_bytes / block_size_in_bytes);
                plaintext = new byte[no_of_blocks][block_size_in_bytes];
            }
            for (int i = 0; i < no_of_blocks; i++) {
                input_file.read(plaintext[i]);
                output_file.write(cipher.update(plaintext[i],0,plaintext[i].length));
            }
            output_file.write(cipher.doFinal());
            stop_time = System.currentTimeMillis();
            AES_encryption_time = stop_time - start_time;
        }catch (Exception E)
        {
            System.out.print(E);
        }

        // AES Decryption
        try (FileInputStream input_file = new FileInputStream("Ciphertext_AES.txt"); FileOutputStream output_file = new FileOutputStream("Plaintext_Output_AES.txt")) {
            start_time = System.currentTimeMillis();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            file_size_in_bytes = input_file.available();
            byte ciphertext[][]=null;
            if(file_size_in_bytes % block_size_in_bytes!=0) {
                no_of_blocks = (file_size_in_bytes / block_size_in_bytes) + 1;
                ciphertext = new byte[no_of_blocks][block_size_in_bytes];
                ciphertext[no_of_blocks-1] = new byte[file_size_in_bytes % block_size_in_bytes];
            }else {
                no_of_blocks = (file_size_in_bytes / block_size_in_bytes);
                ciphertext = new byte[no_of_blocks][block_size_in_bytes];
            }
            for (int i = 0; i < no_of_blocks; i++) {
                input_file.read(ciphertext[i]);
                output_file.write(cipher.update(ciphertext[i],0,ciphertext[i].length));
            }
            output_file.write(cipher.doFinal());
            stop_time = System.currentTimeMillis();
            AES_decryption_time = stop_time - start_time;
        }catch (Exception E)
        {
            System.out.print(E);
        }
        System.out.println("The RSA encryption time in milliseconds: " + RSA_encryption_time);
        System.out.println("The AES encryption time in milliseconds: " + AES_encryption_time);
        System.out.println("Overhead of RSA encryption compared to AES: " + ((float)RSA_encryption_time/AES_encryption_time)*100 + "%");

        System.out.println("\nThe RSA decryption time in milliseconds: " + RSA_decryption_time);
        System.out.println("The AES decryption time in milliseconds: " + AES_decryption_time);
        System.out.println("Overhead of RSA decryption compared to AES: " + ((float)RSA_decryption_time/AES_decryption_time)*100 + "%");
    }
}