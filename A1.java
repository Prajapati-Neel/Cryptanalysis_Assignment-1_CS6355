import java.math.BigInteger;
import java.security.SecureRandom;

public class A1 {
    public static void main(String[] args) {
        //Key Generation
        BigInteger P = new BigInteger("15554903035303856344007671063568213071669822184616101992595534860863803506262760067615727000088295330493705796902296102481798240988227195060316199080930616035532980617309644098719341753037782435645781436420697261984870969742096465765855782491538043554917285285471407866976465359446400695692459955929581561107496250057761324472438514351159746606737260676765872636140119669971105314539393270612398055538928361845237237855336149792618908050931870177925910819318623");
        BigInteger Q = new BigInteger("15239930048457525970295803203207379514343031714151154517998415248470711811442956493342175286216470497855132510489015253513519073889825927436792580707512051299817290925038739023722366499292196400002204764665762114445764643179358348705750427753416977399694184804769596469561594013716952794631383872745339020403548881863215482480719445814165242627056637786302612482697923973303250588684822021988008175106735736411689800380179302347354882715496632291069525885653297");
        SecureRandom random = new SecureRandom();

        BigInteger n = P.multiply(Q);
        BigInteger totient = (P.subtract(new BigInteger("1"))).multiply(Q.subtract(new BigInteger("1")));
        BigInteger e,d;
        do {
            e = BigInteger.valueOf(random.nextLong());
        }
        while((totient.gcd(e).longValue())!=1 ||  e.longValue()<=1 || totient.compareTo(e)==-1);
        d = e.modInverse(totient);
        System.out.println("The first prime is p = " + P);
        System.out.println("The second prime is q = " + Q);
        System.out.println("The composite modulus n = " + n);
        System.out.println("The encryption exponent e = " + e);
        System.out.println("The decryption exponent d = " + d);
        System.out.println("-------------------------------------------");


        //Encryption
        BigInteger plaintext;
        do {
            plaintext = BigInteger.valueOf(random.nextLong());
        }while (plaintext.compareTo(BigInteger.valueOf(0))==-1);
        BigInteger ciphertext = plaintext.modPow(e,n);
        System.out.println("Encryption:");
        System.out.println("Plaintext to be encrypted is m = " + plaintext);
        System.out.println("Ciphertext is c = " + ciphertext);
        System.out.println("-------------------------------------------");

        //Decryption
        BigInteger Qdash = Q.modInverse(P);
        BigInteger Pdash = P.modInverse(Q);
        BigInteger dP = d.mod(P.subtract(new BigInteger("1")));
        BigInteger dQ = d.mod(Q.subtract(new BigInteger("1")));
        BigInteger cP = ciphertext.mod(P);
        BigInteger cQ = ciphertext.mod(Q);
        BigInteger mP = cP.modPow(dP,P);
        BigInteger mQ = cQ.modPow(dQ,Q);
        BigInteger recovered_plaintext = ((mP.multiply(Q).multiply(Qdash)).add(mQ.multiply(P).multiply(Pdash))).mod(n);
        System.out.println("Decryption:");
        System.out.println("Ciphertext to be decrypted is c = " + ciphertext);
        System.out.println("Decrypted plaintext is m = " + recovered_plaintext);
        System.out.println("-------------------------------------------");
    }
}