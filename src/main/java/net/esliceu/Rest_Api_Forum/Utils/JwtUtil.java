package net.esliceu.Rest_Api_Forum.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.esliceu.Rest_Api_Forum.Entities.User;
import net.esliceu.Rest_Api_Forum.Exceptions.ErrorInJWTException;
import net.esliceu.Rest_Api_Forum.Services.FindService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    @Autowired
    private static FindService findService;

    private static final String SECRET = "testKey";
    private static final long EXPIRATION = 3600000;

    public static String getToken(UserPermissions user) throws JsonProcessingException {
        HashMap<String, Object> permissions = new HashMap<>();
        permissions.put("categories", user.getPermissions().getCategories());
        permissions.put("root", user.getPermissions().getRoot());
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("role", user.getRole())
                .withClaim("_id", user.get_id())
                .withClaim("permissions", permissions)
                .withClaim("id", user.getId())
                .withClaim("name", user.getName())
                .withClaim("email", user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static DecodedJWT decode(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(token.substring(7));

    }

    public static boolean validate(String token){
        try{
            decode(token);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static String getIat(String token) throws ErrorInJWTException {
        try{
            DecodedJWT decoded = decode(token);
            return decoded.getIssuedAt().toString();
        }catch (Exception e){
            throw new ErrorInJWTException();
        }

    }

    public static Long getUserFromToken(String token) throws ErrorInJWTException {
        try{
            DecodedJWT decoded = decode(token);
            return decoded.getClaim("id").asLong();
        }catch (Exception e){
            throw new ErrorInJWTException();
        }
    }

}
