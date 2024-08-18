package com.SpringSecurity.SpringSecurity.AuthRestController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authenticationrequest {
    private String Email;
    private String Password;
}
