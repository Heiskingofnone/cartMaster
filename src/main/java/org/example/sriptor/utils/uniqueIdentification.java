package org.example.sriptor.utils;

import com.github.f4b6a3.uuid.UuidCreator;
import org.slf4j.LoggerFactory;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;

import static java.lang.System.out;

public class uniqueIdentification {
    private static final Logger logger = LoggerFactory.getLogger(uniqueIdentification.class);

    public static UUID generateUID(){
        logger.info("Creating new UUID");
        long customEpochMillSecs = 0xF4B6A3000000L;
        //out.println(UuidCreator.getTimeOrderedEpoch( Instant.ofEpochMilli(customEpochMillSecs)));
        return UuidCreator.getTimeOrderedEpoch(
                Instant.ofEpochMilli(customEpochMillSecs)
        );
    }

    public static byte[] uuidToBytes(UUID uuid){
        logger.info("Converting UUID to bytes");
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    public static UUID bytesToUuid(byte[] bytes){
        logger.info("Converting bytes to UUID");
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long mostSignificantBits = byteBuffer.getLong();
        long leastSignificantBits = byteBuffer.getLong();
        return new UUID(mostSignificantBits, leastSignificantBits);
    }
}
