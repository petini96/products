package br.com.roboticsmind.products.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserUpdateDto {

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String name;

    private String nickname;

    @Size(max = 20, message = "O telefone não pode exceder 20 caracteres.")
    private String phone;
}