package com.finnoapp.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.finnoapp.config.ChildUserDetailsService;
import com.finnoapp.service.JwtService;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private ChildUserDetailsService childUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = null;

		String username = null;

		String authHeader = request.getHeader("Authorization");

		System.out.println("header  :" + authHeader);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {

			token = authHeader.substring(7);

			System.out.println("token  :" + token);

			username = jwtService.extractUsername(token);

		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = childUserDetailsService.loadUserByUsername(username);

			if (jwtService.validateToken(token, userDetails)) {

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);

			}

		}

		filterChain.doFilter(request, response);
	}

}
