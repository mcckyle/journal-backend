//***************************************************************************************
//
//     Filename: JwtUtils.java
//     Author: Kyle McColgan
//     Date: 04 December 2024
//     Description: This file contains the auth token generation process.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

//***************************************************************************************

@Component
public class JwtUtils
{
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Integer userId, String username,
                                   Collection<? extends GrantedAuthority> authorities)
    {
        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)  // Include userId in the JWT
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public Claims getClaimsFromToken(String token)
    {
        try
        {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (ExpiredJwtException e)
        {
            System.out.println("JWT token is expired: " + e.getMessage());
        }
        catch (UnsupportedJwtException e)
        {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        }
        catch (MalformedJwtException e)
        {
            System.out.println("Malformed JWT token: " + e.getMessage());
        }
        catch (JwtException | IllegalArgumentException e)
        {
            System.out.println("Invalid JWT token: " + e.getMessage());
        }
        return null;
    }

    public String getUserNameFromJwtToken(String token)
    {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    public boolean validateJwtToken(String authToken)
    {
        try
        {
            Claims claims = getClaimsFromToken(authToken);
            return claims != null && !isTokenExpired(claims);
        }
        catch (Exception e)
        {
            System.out.println("Error during token validation: " + e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims)
    {
        return claims.getExpiration().before(new Date());
    }
}

//***************************************************************************************
