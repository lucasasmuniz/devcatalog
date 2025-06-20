package com.lucasasmuniz.devcatalog.services;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lucasasmuniz.devcatalog.controllers.UserController;
import com.lucasasmuniz.devcatalog.dto.UserDTO;
import com.lucasasmuniz.devcatalog.dto.UserInsertDTO;
import com.lucasasmuniz.devcatalog.dto.UserUpdateDTO;
import com.lucasasmuniz.devcatalog.entities.Role;
import com.lucasasmuniz.devcatalog.entities.User;
import com.lucasasmuniz.devcatalog.projections.UserDetailsProjection;
import com.lucasasmuniz.devcatalog.repositories.RoleRepository;
import com.lucasasmuniz.devcatalog.repositories.UserRepository;
import com.lucasasmuniz.devcatalog.services.exceptions.DatabaseException;
import com.lucasasmuniz.devcatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AuthService authService;

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		return repository.findAll(pageable).map(x -> new UserDTO(x)
				.add(linkTo(methodOn(UserController.class).findAllPaged(pageable)).withSelfRel())
				.add(linkTo(methodOn(UserController.class).findById(x.getId())).withRel("GET - User by id")));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		User entity = repository.findById(id).orElseThrow(() -> (new ResourceNotFoundException("Entity not found")));
		return new UserDTO(entity)
				.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel())
				.add(linkTo(methodOn(UserController.class).findAllPaged(null)).withRel("GET - Users"))
				.add(linkTo(methodOn(UserController.class).update(id, null)).withRel("PUT - Update user"))
				.add(linkTo(methodOn(UserController.class).delete(id)).withRel("DELETE - Delete user"));
	}
	
	@Transactional(readOnly = true)
	public UserDTO findLoggedUser() {
		User entity = authService.authenticated();
		UserDTO dto = new UserDTO(entity)
				.add(linkTo(methodOn(UserController.class).update(entity.getId(), null)).withRel("PUT - Update user"));
		return dto;
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDTOToEntity(dto, entity);

		entity.getRoles().clear();
		Role role = roleRepository.findByAuthority("ROLE_OPERATOR");
		entity.getRoles().add(role);

		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity)
				.add(linkTo(methodOn(UserController.class).findById(entity.getId())).withRel("GET - User by id"));
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			copyDTOToEntity(dto, entity);
			repository.save(entity);
			return new UserDTO(entity)
					.add(linkTo(methodOn(UserController.class).findById(entity.getId())).withRel("GET - User by id"));

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Resource not found");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Referential integrity error");
		}
	}

	private void copyDTOToEntity(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> results = repository.searchUserAndRolesByEmail(username);

		if (results.size() == 0) {
			throw new UsernameNotFoundException("Email not found");
		}

		User user = new User();
		user.setEmail(username);
		user.setPassword(results.get(0).getPassword());
		for (UserDetailsProjection projections : results) {
			user.getRoles().add(new Role(projections.getRoleId(), projections.getAuthority()));
		}

		return user;
	}
}
