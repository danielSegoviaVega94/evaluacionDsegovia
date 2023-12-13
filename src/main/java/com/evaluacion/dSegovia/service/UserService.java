package com.evaluacion.dSegovia.service;

import com.evaluacion.dSegovia.dto.UserDTO;
import com.evaluacion.dSegovia.mapper.UserMapper;
import com.evaluacion.dSegovia.model.User;
import com.evaluacion.dSegovia.repository.UserRepository;
import com.evaluacion.dSegovia.utils.Utils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private Utils utils;

//Configuracion para el login
@Autowired
private BCryptPasswordEncoder bCryptPasswordEncoder;
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return generateToken(user);
        } else {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
    }


//Guarda el usuario validando que el email sea valido y que no exista previamente
    public UserDTO saveUser(UserDTO userDTO) {
        if (!Utils.isValidEmail(userDTO.getEmail())) {
            throw new RuntimeException("Formato de correo electrónico inválido");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya se encuentra registrado");
        }
        if (!utils.isValidPassword(userDTO.getPassword())) {
            throw new RuntimeException("La contraseña no cumple con los requisitos de seguridad");
        }

        User user = userMapper.userDTOToUser(userDTO);
        System.out.println("user: " + user);
        // Encriptación de la passsword
        user.setPassword(Utils.hashPassword(userDTO.getPassword()));
        user.setToken(generateToken(user));
        user.setActive(true);


        User savedUser = userRepository.save(user);
        System.out.println("savedUser: " + savedUser);
        return userMapper.userToUserDTO(savedUser);
    }



    public String generateToken(User user) {
        long currentTimeMillis = System.currentTimeMillis();
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + 86400000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
//Obtiene todos los usuarios
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }
//Obtiene todos los usuarios paginados
    public Page<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::userToUserDTO);
    }
//Obtiene un usuario por email
public UserDTO getUserByEmail(String email) {
    if (!Utils.isValidEmail(email)) {
        throw new IllegalArgumentException("Email inválido");
    }

    return userRepository.findByEmail(email)
            .map(userMapper::userToUserDTO)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + email));
}

//Actualiza un usuario validando que el email sea valido y que exista previamente
public UserDTO updateUser(UserDTO userDTO) {
    if (!Utils.isValidEmail(userDTO.getEmail())) {
        throw new IllegalArgumentException("Email inválido");
    }

    User existingUser = userRepository.findByEmail(userDTO.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + userDTO.getEmail()));


    existingUser.setName(userDTO.getName());

    User updatedUser = userRepository.save(existingUser);
    return userMapper.userToUserDTO(updatedUser);
}

// Elimina un usuario por UUID validando que el email proporcionado sea válido y que exista previamente
public void deleteUser(String email, UUID id) {
    if (!Utils.isValidEmail(email)) {
        throw new IllegalArgumentException("Email inválido");
    }

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + email));

    if (!user.getId().equals(id)) {
        throw new IllegalArgumentException("El ID proporcionado no coincide con el email");
    }

    userRepository.deleteById(id);
}


}
