package com.evaluacion.dSegovia;

import com.evaluacion.dSegovia.dto.PhoneDTO;
import com.evaluacion.dSegovia.dto.UserDTO;
import com.evaluacion.dSegovia.mapper.UserMapper;
import com.evaluacion.dSegovia.model.User;
import com.evaluacion.dSegovia.repository.UserRepository;
import com.evaluacion.dSegovia.service.UserService;
import com.evaluacion.dSegovia.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private Utils utils;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;
    @BeforeEach
    void setUp() throws Exception {
        Field jwtSecretField = UserService.class.getDeclaredField("jwtSecret");
        jwtSecretField.setAccessible(true);
        jwtSecretField.set(userService, "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTcwMjQwNzQzMiwiaWF0IjoxNzAyNDA3NDMyfQ.SImiUtEjvNhPiNtSd5U0HFen6JRJ3rz6XWtgJdHUBS0");
    }

    @Test
    void login_ValidCredentials_ReturnsToken() {
        String email = "test@example.com";
        String password = "Password123!";
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
        String token = userService.login(email, password);
        assertNotNull(token);
    }
    @Test
    void login_InvalidCredentials_ThrowsException() {
        String email = "wrong@example.com";
        String password = "wrongPassword";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(email, password);
        });
    }



    @Test
    void saveUser_ValidUser_ReturnsUserDTO() {
        UserDTO userDTO = new UserDTO();
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setCitycode("1");
        phoneDTO.setContrycode("57");
        phoneDTO.setNumber("1234567");
        List<PhoneDTO> phoneDTOs = Collections.singletonList(phoneDTO);

        userDTO.setName("Test");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("Password123!");
        userDTO.setActive(true);
        userDTO.setPhones(phoneDTOs);
        System.out.println("userDTO: " + userDTO);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword("$2a$12$8ArW/w9gY84YXKSIKJickOqh0yWhSn2OoY1g/NsgLQJ1r4qYezuzO");
        user.setLastLogin(user.getCreated());
        user.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzanVhbnJvQGRyaWd1ZXoub3JnIiwiaWF0IjoxNzAyNDA3NTI4LCJleHAiOjE3MDI0OTM5Mjh9.-si3Ul8fKhnxZrNa4jU8TqVcGB7PZbjZAjTp4UNycrMfg-fOsPIcLbbt1bvfiQo51jCX2aXdtmZnuKg-ZivBFw");
        user.setActive(true);
        System.out.println("user: " + user);


        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(utils.isValidPassword(anyString())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);


        UserDTO savedUserDTO = userService.saveUser(userDTO);
        assertNotNull(savedUserDTO);
        assertEquals(userDTO.getEmail(), savedUserDTO.getEmail());
        assertEquals(userDTO.getName(), savedUserDTO.getName());
        assertNotNull(savedUserDTO.getPhones());
        assertFalse(savedUserDTO.getPhones().isEmpty());
        assertEquals(phoneDTO.getNumber(), savedUserDTO.getPhones().get(0).getNumber());
        assertTrue(savedUserDTO.isActive());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void saveUser_EmailAlreadyExists_ThrowsRuntimeException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("existing@example.com");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(new User()));
        assertThrows(RuntimeException.class, () -> {
            userService.saveUser(userDTO);
        });
    }

    @Test
    void getUserByEmail_ValidEmail_ReturnsUserDTO() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void getUserByEmail_EmailNotFound_ThrowsIllegalArgumentException() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByEmail(email);
        });
    }

    @Test
    void updateUser_EmailNotFound_ThrowsIllegalArgumentException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("nonexistent@example.com");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(userDTO);
        });
    }

    @Test
    void deleteUser_IdMismatch_ThrowsIllegalArgumentException() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setId(UUID.randomUUID());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(email, UUID.randomUUID());
        });

    }
    }
