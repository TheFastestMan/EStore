package rail.estore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import rail.estore.dto.UserDto;
import rail.estore.dto.UserRequestDto;
import rail.estore.service.UserService;


@RestController
@RequestMapping("v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public Page<UserDto> getUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @PostMapping
    public UserDto registerUser(@RequestBody UserRequestDto userDto) {
        return userService.registrationUser(userDto);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody UserRequestDto userDto) {
        return userService.login(userDto);
    }

}
