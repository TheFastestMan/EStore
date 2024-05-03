package rail.estore.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import rail.estore.dto.UserDto;
import rail.estore.dto.UserRequestDto;
import rail.estore.map.UserMapper;
import rail.estore.model.User;
import rail.estore.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String PASSWORD = "password";
    private static final Long ID = 1L;
    private static final String EMAIL = "email_email@email.com";

    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;


    @Test
    void getUserById() {
        User user = new User();
        UserDto userDto = new UserDto();
        userDto.setId(ID);

        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.map(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(ID);

        Assertions.assertEquals(ID, result.getId());
    }

    @Test
    void getUserByIdShouldBeNotFount() {
        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> userService.getUserById(ID));
    }

    @Test
    void getAllUsers() {
        Pageable pageable = PageRequest.of(0, 2);

        List<User> users = new ArrayList<>();
        List<UserDto> userDtos = new ArrayList<>();

        User testUser1 = new User();
        User testUser2 = new User();

        UserDto testUserDto1 = new UserDto();
        UserDto testUserDto2 = new UserDto();

        users.add(testUser1);
        users.add(testUser2);

        userDtos.add(testUserDto1);
        userDtos.add(testUserDto2);

        Page<UserDto> userDtoPage = new PageImpl<>(userDtos, pageable, userDtos.size());
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        Mockito.when(userRepository.findAll(pageable)).thenReturn(userPage);
        Mockito.when(userMapper.map(testUser1)).thenReturn(testUserDto1);
        Mockito.when(userMapper.map(testUser2)).thenReturn(testUserDto2);

        Page<UserDto> pageOfUsers = userService.getAllUsers(pageable);

        Assertions.assertEquals(pageOfUsers.getNumber(), userDtoPage.getNumber());
    }

    @Test
    void registrationUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        UserDto userDto = new UserDto();
        userDto.setId(ID);
        User user = new User();

        Mockito.when(userRepository.existsByUsernameAndEmail(
                userRequestDto.getUsername(), userRequestDto.getEmail())
        ).thenReturn(false);

        Mockito.when(userMapper.map(userRequestDto)).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.map(user)).thenReturn(userDto);

        UserDto result = userService.registrationUser(userRequestDto);

        Assertions.assertEquals(userDto.getId(), result.getId());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void registrationUserShouldExist() {
        UserRequestDto userRequestDto = new UserRequestDto();

        Mockito.when(userRepository.existsByUsernameAndEmail(
                userRequestDto.getUsername(), userRequestDto.getEmail())
        ).thenReturn(true);

        Assertions.assertThrows(RuntimeException.class, () -> userService.registrationUser(userRequestDto));
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void login() {
        User user = new User();
        user.setPassHash(PASSWORD);
        user.setEmail(EMAIL);

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setPassword(PASSWORD);
        userRequestDto.setEmail(EMAIL);

        UserDto userDto = new UserDto();
        userDto.setEmail(EMAIL);

        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(
                userRequestDto.getPassword(), user.getPassHash())).thenReturn(true);
        Mockito.when(userMapper.map(user)).thenReturn(userDto);

        UserDto result = userService.login(userRequestDto);

        Assertions.assertEquals(userDto, result);

    }

    @Test
    void loginShouldNotFindUserByEmail() {
        UserRequestDto userRequestDto = new UserRequestDto();

        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> userService.login(userRequestDto));

    }

    @Test
    void loginShouldNotMAches() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail(EMAIL);
        userRequestDto.setPassword(PASSWORD);

        User user = new User();
        user.setPassHash(PASSWORD);
        user.setEmail(EMAIL);

        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(
                userRequestDto.getPassword(), user.getPassHash())).thenReturn(false);

        Assertions.assertThrows(
                RuntimeException.class, () -> userService.login(userRequestDto));

    }
}
