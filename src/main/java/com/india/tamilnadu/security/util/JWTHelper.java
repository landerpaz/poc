package com.india.tamilnadu.security.util;

import java.util.Date;

import org.joda.time.DateTime;

import com.india.tamilnadu.vo.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTHelper {

	public static String getJWT(User user) throws Exception {
		
		return Jwts.builder()
				  .setSubject("users/TzMUocMF4p")
				  .setExpiration(new DateTime().plusMinutes(60).toDate())
				  .claim("name", user.getFirstName() + " " + user.getLastName())
				  .claim("scope", user.getRole())
				  .signWith(
				    SignatureAlgorithm.HS256,
				    "secret".getBytes("UTF-8")
				  )
				  .compact();
	}
	
	public static String validateJWT(String jwt) throws Exception {
		
		String scope = null;
		
		try {
			
			Jws<Claims> claims = Jwts.parser()
			  .setSigningKey("secret".getBytes("UTF-8"))
			  .parseClaimsJws(jwt);
		
			scope = (String)claims.getBody().get("scope");
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		return scope;
	}
	
	public static void main(String a[]) {
		try {
			
			User user = new User();
			user.setFirstName("FirstName");
			user.setLastName("LastName");
			user.setRole("Admin");
			//String jwt = getJWT(user);
			
			//System.out.println(jwt);
			
			System.out.println(validateJWT("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2Vycy9Uek1Vb2NNRjRwIiwiZXhwIjoxNTEyMjM5MTYxLCJuYW1lIjoiRmlyc3ROYW1lIExhc3ROYW1lIiwic2NvcGUiOiJBZG1pbiJ9.9rTlwGI6ERK2w7Y0B9M_NKzjYpN9WrXQFhh5T1562I8"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
