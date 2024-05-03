package rail.estore.map;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rail.estore.dto.UserDto;
import rail.estore.dto.UserRequestDto;
import rail.estore.model.User;

import static rail.estore.model.Role.USER;

@Component
@RequiredArgsConstructor
public class UserMapper {

   private final PasswordEncoder passwordEncoder;

    public UserDto map(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public User map(UserRequestDto userDto) {
        String password = userDto.getPassword();
        String hashedPassword = passwordEncoder.encode(password);

        return new User(userDto.getUsername(), hashedPassword, userDto.getEmail(), USER);
    }

}
