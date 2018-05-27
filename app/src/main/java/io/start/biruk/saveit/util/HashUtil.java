package io.start.biruk.saveit.util;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.util.UUID;

/**
 * Created by biruk on 5/10/2018.
 */
public class HashUtil {

    public static String getHash(String url) {
        return Hashing.murmur3_32().newHasher()
                .putBytes(url.getBytes())
                .hash()
                .toString();

    }
}
