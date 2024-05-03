package rail.estore.dto;

import lombok.*;
import rail.estore.model.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Role role;

}
