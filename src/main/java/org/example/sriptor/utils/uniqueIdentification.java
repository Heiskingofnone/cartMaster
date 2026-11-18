package org.example.sriptor.utils;

import com.github.f4b6a3.uuid.UuidCreator;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.UUID;

public class uniqueIdentification {

    public static UUID generateUID(){
        long customEpochMillSecs = 0xF4B6A3000000L;
        return UuidCreator.getTimeOrderedEpoch(
                Instant.ofEpochMilli(customEpochMillSecs)
        );
    }

    public static byte[] uuidToBytes(UUID uuid){
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    public static UUID bytesToUuid(byte[] bytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long mostSignificantBits = byteBuffer.getLong();
        long leastSignificantBits = byteBuffer.getLong();
        return new UUID(mostSignificantBits, leastSignificantBits);
    }
}
