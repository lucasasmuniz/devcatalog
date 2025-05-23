package com.lucasasmuniz.devcatalog.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lucasasmuniz.devcatalog.dto.UserDTO;
import com.lucasasmuniz.devcatalog.dto.UserInsertDTO;
import com.lucasasmuniz.devcatalog.dto.UserUpdateDTO;
import com.lucasasmuniz.devcatalog.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Users", description = "Controller for Users")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(description = "Get all users",
    		summary = "Get a page of users",
    		responses = {
			 	 @ApiResponse(description = "Ok", responseCode = "200"),
				 @ApiResponse(description = "Forbidden", responseCode = "403"),
   		         @ApiResponse(description = "Unauthorized", responseCode = "401")
    		})
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserDTO>> findAllPaged(Pageable pageable){
        return ResponseEntity.ok(service.findAllPaged(pageable));
    }

    @Operation(description = "Get user by id",
    		summary = "Get a user by id",
    		responses = {
			 	 @ApiResponse(description = "Ok", responseCode = "200"),
			 	@ApiResponse(description = "Not Found", responseCode = "404"),
				 @ApiResponse(description = "Forbidden", responseCode = "403"),
   		         @ApiResponse(description = "Unauthorized", responseCode = "401")
    		})
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @Operation(description = "Get user logged",
    		summary = "Get user logged",
    		responses = {
			 	 @ApiResponse(description = "Ok", responseCode = "200"),
				 @ApiResponse(description = "Forbidden", responseCode = "403"),
   		         @ApiResponse(description = "Unauthorized", responseCode = "401")
    		})
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ROLE_OPERATOR','ROLE_ADMIN')")
    @GetMapping(value = "/logged", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> findLoggedUser(){
        return ResponseEntity.ok(service.findLoggedUser());
    }
    
    @Operation(description = "Post a user",
    		summary = "Save a new user on database",
    		responses = {
			 	 @ApiResponse(description = "Created", responseCode = "201"),
				 @ApiResponse(description = "Forbidden", responseCode = "403"),
   		         @ApiResponse(description = "Unauthorized", responseCode = "401"),
		         @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
    		})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO insertDTO) {
        UserDTO dto = service.insert(insertDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);

    }

    @Operation(description = "Update a user",
    		summary = "Update an existing user",
    		responses = {
			 	 @ApiResponse(description = "Ok", responseCode = "200"),
			 	 @ApiResponse(description = "Not Found", responseCode = "404"),
				 @ApiResponse(description = "Forbidden", responseCode = "403"),
   		         @ApiResponse(description = "Unauthorized", responseCode = "401"),
		         @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
    		})
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id,@Valid @RequestBody UserUpdateDTO dto) {
    	UserDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);

    }

    @Operation(description = "Delete a user",
    		summary = "Delete an existing user",
    		responses = {
			 	 @ApiResponse(description = "No Content", responseCode = "204"),
			 	 @ApiResponse(description = "Not Found", responseCode = "404"),
			 	 @ApiResponse(description = "Bad Request", responseCode = "400"),
				 @ApiResponse(description = "Forbidden", responseCode = "403"),
   		         @ApiResponse(description = "Unauthorized", responseCode = "401"),
		         @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
    		})
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

