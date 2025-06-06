package com.lynki.lynki.infra;

import com.lynki.lynki.domain.User;
import com.lynki.lynki.domain.enums.UserRole;
import com.lynki.lynki.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2SuccessHandler  implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Value("${FRONT_BASE_URL:http://localhost:3000}")
    private String baseUrl;

    public OAuth2SuccessHandler(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException {

        Map<String, String> usernameAttributes = Map.of(
                "google", "given_name",
                "github", "name",
                "discord", "username"
        );


        String registrationId = null;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

      if (authentication instanceof OAuth2AuthenticationToken) {
          registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
      }

      String usernameAttribute = usernameAttributes.getOrDefault(registrationId, "name");
      String username = oAuth2User.getAttribute(usernameAttribute);

      String email = oAuth2User.getAttribute("email");

      User user = userRepository.findByEmail(email);
        if (user == null) {
            User newUser = new User(username, null, email, UserRole.USER );
            userRepository.save(newUser);
            String token = tokenService.generateToken(newUser);
            String redirectUrl = baseUrl + "/oauth2/success?token=" + token;

            response.sendRedirect(redirectUrl);

        } else {
            String token = tokenService.generateToken(user);
            String redirectUrl = baseUrl + "/oauth2/success?token=" + token;

            response.sendRedirect(redirectUrl);
        }
  }

}
