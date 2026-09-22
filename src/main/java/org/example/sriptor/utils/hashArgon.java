package org.example.sriptor.utils;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

import java.security.SecureRandom;
import java.util.Base64;

public class hashArgon {
    byte[] salt;
    private static final int ITERATIONS = 3;
    private static final int MEMORY_KB = 65536;
    private static final int PARALLELISM = 4;
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
        //This is a helper function that changes our password input and hashes it with argon2Id
        private static byte[] computeArgon2id(String password, byte[] salt) {//Takes the password, and the salt and creates our hash
            Argon2Parameters parameters = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)//Implementing argon2Id parameters and state parameters below
                    .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                    .withIterations(hashArgon.ITERATIONS)
                    .withMemoryAsKB(hashArgon.MEMORY_KB)
                    .withParallelism(hashArgon.PARALLELISM)
                    .withSalt(salt)
                    .build();
            Argon2BytesGenerator generator = new Argon2BytesGenerator();//instantiate our hash generator
            generator.init(parameters);//we give the generator the parameters we set
            byte[] result = new byte[hashArgon.HASH_LENGTH];//Generate bytes with the length we declared
            byte[] passwordBytes = Strings.toUTF8ByteArray(password);//convert our password into pure byte data
            try{
                generator.generateBytes(passwordBytes, result, 0, result.length);//generate new byte data from result byte and password to give us our hash
                return result;
            }
            finally {
                Arrays.fill(passwordBytes, (byte) 0);//We do this to make sure that the byte data we created doesn't get wasted, so we fill it with zeros to return it to memory
            }
        }

        //to implement it we create a method
        public static String registerPasswordPHC(String password){//takes password
            byte[] salt = new byte[SALT_LENGTH];//generates salt from ram
            SECURE_RANDOM.nextBytes(salt);//randomize the salt

            byte[] hash = computeArgon2id(password, salt);//take the password and salt and hash it

            String saltB64 = Base64.getEncoder().withoutPadding().encodeToString(salt);//save the salt in a new string after encoding
            String hash64 = Base64.getEncoder().withoutPadding().encodeToString(hash);//save the hash in a new string after encoding

            return String.format("$argon2id$v=19$m=65536,t-3,p=4$%s$%s", saltB64, hash64);//return salt and bash in a string format

        }
}

