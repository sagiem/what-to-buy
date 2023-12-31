package ru.sagiem.whattobuy.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sagiem.whattobuy.model.user.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private String confirmPassword;
  private Role role;
}
