package net.esliceu.Rest_Api_Forum.Utils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HashUtil {
    public static String hash(String text){
        return Arrays.toString(DigestUtils.md5Digest(text.getBytes(StandardCharsets.UTF_8)));
    }
}
