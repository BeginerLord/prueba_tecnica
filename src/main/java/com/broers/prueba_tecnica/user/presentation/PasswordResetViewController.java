package com.broers.prueba_tecnica.user.presentation;

import com.broers.prueba_tecnica.constants.EndpointsConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetViewController {

    @GetMapping(EndpointsConstants.ENDPOINT_RESET_FORM)
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password-form";
    }
}
