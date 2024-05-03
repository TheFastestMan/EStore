package rail.estore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rail.estore.dto.UserDto;
import rail.estore.dto.UserRequestDto;
import rail.estore.map.UserMapper;
import rail.estore.model.User;
import rail.estore.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::map).orElseThrow(
                () -> new RuntimeException("User not found")//todo to create some custom exception
        );
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::map);
    }

    @Transactional
    public UserDto registrationUser(UserRequestDto userRequestDto) {
        boolean exists = userRepository.existsByUsernameAndEmail(userRequestDto.getUsername(), userRequestDto.getEmail());

        if (exists) {
            throw new RuntimeException();//todo to create some custom exception
        }

        User user = userMapper.map(userRequestDto);
        User userSaved = userRepository.save(user);
        return userMapper.map(userSaved);

    }

    public UserDto login(UserRequestDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow();

        if (passwordEncoder.matches(userDto.getPassword(), user.getPassHash())) {
            return userMapper.map(user);
        }
        throw new RuntimeException(); //todo to create some custom exception
    }
}
