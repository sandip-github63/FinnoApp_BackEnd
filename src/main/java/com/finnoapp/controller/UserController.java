package com.finnoapp.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finnoapp.model.Role;
import com.finnoapp.model.User;
import com.finnoapp.model.UserRole;
import com.finnoapp.payload.request.AuthRequest;
import com.finnoapp.payload.request.UserDto;
import com.finnoapp.payload.request.UserRoleRequestDTO;
import com.finnoapp.payload.response.Message;
import com.finnoapp.payload.response.Message2;
import com.finnoapp.payload.response.TokenResponse;
import com.finnoapp.payload.response.UserWithAuthoritiesDTO;
import com.finnoapp.service.JwtService;
import com.finnoapp.service.RoleService;
import com.finnoapp.service.UserRoleService;
import com.finnoapp.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	UserService service;

	@Autowired
	RoleService roleService;

	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody UserDto userReq) {

		User user = new User(userReq.getFirstName(), userReq.getLastName(), userReq.getEmail(), userReq.getUserName(),
				userReq.getPhone(), userReq.isEnable(), userReq.getAbout(), userReq.getPassword(),
				userReq.getProfile());

		System.out.println("user Role  is .. :" + userReq.getRole());

		Role r1;

		if (userReq.getRole() != null && userReq.getRole().equals("ROLE_ADMIN")) {

			System.out.println("user role is ........:" + userReq.getRole());

			r1 = this.roleService.getRole(userReq.getRole());

			System.out.println("User role ID :" + r1.getRoleId());

		} else if (userReq.getRole() != null && userReq.getRole().equals("ROLE_USER")) {

			System.out.println("user role is ........:" + userReq.getRole());

			r1 = this.roleService.getRole(userReq.getRole());

			System.out.println("User role ID :" + r1.getRoleId());

		} else {
			r1 = new Role();

			r1.setRoleName("ROLE_USER");
			r1.setRoleId(46L);

		}

		Set<UserRole> userRoleSet = new HashSet<>();

		UserRole userRole = new UserRole();
		userRole.setRole(r1);
		userRole.setUser(user);

		userRoleSet.add(userRole);

		User u = service.createUser(user, userRoleSet);

		if (u == null) {

			return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("user already exit!!!!", "409", u));

		}

		return ResponseEntity.status(HttpStatus.OK).body(new Message("user created successfully", "200", u));
	}

	@GetMapping("/{username}")
	public ResponseEntity<?> getUser(@PathVariable("username") String username) {

		User user = service.getUserByUserName(username);

		if (user == null) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("invalid username!!!!", "404", user));

		}

		return ResponseEntity.status(HttpStatus.OK).body(new Message("user get successfully", "200", user));

	}

	@DeleteMapping("/{userid}")
	public ResponseEntity<?> deleteUser(@PathVariable("userid") Long userid) {

		boolean result = service.deleteUser(userid);

		if (!result) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message("invalid user id!!!!", "404"));

		}

		return ResponseEntity.status(HttpStatus.OK).body(new Message("user deleted successfully", "200"));

	}

	@PostMapping("/authenticate")
	public ResponseEntity<TokenResponse> authenticateAndGetToken(@RequestBody AuthRequest request) {

		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

		if (authenticate.isAuthenticated()) {

			String generateToken = jwtService.generateToken(request.getUserName());
			TokenResponse response = new TokenResponse(generateToken);
			return ResponseEntity.ok(response);

		} else {
			throw new UsernameNotFoundException("invalid username !!");
		}

	}

	@GetMapping("/currentLoginUser")
	public ResponseEntity<?> getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()
				&& !authentication.getPrincipal().equals("anonymousUser")) {
			String currentUsername = authentication.getName();

			User currentUser = userService.getUserByUserName(currentUsername);

			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

			List<String> authorityNames = authorities.stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());

			UserWithAuthoritiesDTO userWithAuthorities = new UserWithAuthoritiesDTO();
			userWithAuthorities.setUser(currentUser);
			userWithAuthorities.setAuthorities(authorityNames);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new Message2("Current user retrieved successfully", "200", userWithAuthorities));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new Message("No user is currently logged in", "401"));
		}
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllUsers() {
		List<User> users = this.userService.getUsers();

		for (User u : users) {
			u.setPassword(null);
			u.setUserName(null);

		}

		return ResponseEntity.ok(users);
	}

	@GetMapping("/role/{userId}")
	public ResponseEntity<?> getUserRole(@PathVariable("userId") Long userId) {
		List<UserRole> role = this.userRoleService.getRole(userId);

		for (UserRole r : role) {
			r.getUser().setUserName(null);
			r.getUser().setPassword(null);

		}

		return ResponseEntity.ok(role);
	}

	@PutMapping("/role")
	public ResponseEntity<?> updateRoleOfUser(@RequestBody UserRoleRequestDTO userRole) {

		System.out.println(userRole.toString());

		User u = new User();

		Role r = new Role();

		UserRole ur = new UserRole();

		u.setUserId(userRole.getUserId());
		r.setRoleName(userRole.getRoleName());

		ur.setRole(r);
		ur.setUser(u);
		ur.setUserRoleId(userRole.getUserRoleId());

		return ResponseEntity.ok(this.userRoleService.updateRole(ur));
	}

	@GetMapping("/test")
	public String test() {
		return "Your application is working fine";
	}

}
