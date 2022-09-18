package com.example.demo.Model.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AppUserRequest {

    @Schema(description = "The email address of user.", example = "test@gmail.com")
    @NotBlank
    private String emailAddress;
    @Schema(description = "The password of user.", example = "123456", minLength = 6)
    @NotBlank
    private String password;
    @Schema(description = "The full name of user.")
    @NotBlank
    private String name;

    // 驗證request body資料的方式可能是另外處理，所以沒加上@NotEmpty這樣的驗證標記。
    // 若仍想將欄位必填的條件在文件表示出來，可以在@Schema傳入required參數。
    @Schema(description = "The authority of user.", required = true)
    @NotEmpty
    private List<UserAuthority> authorities;
}
