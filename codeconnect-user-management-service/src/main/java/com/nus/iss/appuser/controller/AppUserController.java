package com.nus.iss.appuser.controller;

import com.nus.iss.appuser.dto.AppUserDto;
import com.nus.iss.appuser.dto.JwtAccessTokenDTO;
import com.nus.iss.appuser.entity.AppUser;
import com.nus.iss.appuser.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody AppUser appUser) {
        AppUser registeredUser = appUserService.registerUser(appUser);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAccessTokenDTO> login(@RequestBody AppUser appUser) {
        JwtAccessTokenDTO tokenDTO = appUserService.login(appUser.getUsername(), appUser.getPassword());
        return ResponseEntity.ok(tokenDTO);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUserAcc(@RequestParam String token) {
        appUserService.activateUser(token);
        return ResponseEntity.ok("User activated successfully");
    }

    @PostMapping("/update-password")
    public ResponseEntity<AppUser> updatePassword(@RequestBody AppUserDto appUserDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ("anonymousUser".equalsIgnoreCase((String) authentication.getPrincipal())) {
            throw new RuntimeException("Anonymous user cannot update password");
        } else if (!appUserDto.getUsername().equalsIgnoreCase((String) authentication.getPrincipal())) {
            throw new RuntimeException("User can update only their own password");
        } else {
            appUserDto.setUsername((String) authentication.getPrincipal());
            AppUser appUser = appUserService.updatePassword(appUserDto);
            return ResponseEntity.ok(appUser);
        }
    }

    @GetMapping("/test-token-admin")
    public ResponseEntity<String> testTokenAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok("Hello " + authentication.getPrincipal());
        } else {
            return ResponseEntity.ok("Hello Anonymous");
        }
    }

    @GetMapping("/test-token-user")
    public ResponseEntity<String> testTokenUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok("Hello " + authentication.getPrincipal());
        } else {
            return ResponseEntity.ok("Hello Anonymous");
        }
    }

    @GetMapping("/compliance-data")
    public ResponseEntity<String> getComplianceData() {
        return ResponseEntity.ok("""
                The General Data Protection Regulation (GDPR) of the European Union and Singapore's Personal Data Protection Act (PDPA) are both comprehensive frameworks designed to protect personal data. While they share common objectives, there are notable differences in their scope, definitions, legal bases for processing, organizational obligations, individual rights, and enforcement mechanisms.
                
                Scope and Applicability
                The GDPR applies to both private and public entities within the EU, as well as organizations outside the EU that process personal data of EU residents. In contrast, the PDPA governs private sector organizations in Singapore but excludes public agencies and organizations acting on their behalf. Both regulations have extraterritorial reach; the GDPR applies to organizations offering goods or services to, or monitoring the behavior of, individuals in the EU, while the PDPA applies to organizations collecting, using, or disclosing personal data within Singapore, regardless of their physical presence in the country.\s
                
                Definitions and Data Classification
                Both regulations define "personal data" as information relating to an identified or identifiable individual. However, the GDPR specifies "special categories" of personal data, such as racial or ethnic origin, political opinions, and health information, which require higher protection levels. The PDPA does not differentiate between categories of personal data, treating all personal data uniformly without additional safeguards for sensitive information.\s
                
                Legal Bases for Processing
                The GDPR outlines several legal grounds for processing personal data, including consent, performance of a contract, compliance with legal obligations, protection of vital interests, tasks carried out in the public interest, and legitimate interests pursued by the controller or a third party. The PDPA primarily relies on the individual's consent for data processing but also recognizes other bases, such as contractual necessity and legitimate interests, under specific circumstances.
                
                Organizational Obligations
                Both regulations require organizations to appoint a Data Protection Officer (DPO) responsible for ensuring compliance with data protection laws. Under the GDPR, conducting Data Protection Impact Assessments (DPIAs) is mandatory in certain situations where data processing is likely to result in high risks to individuals' rights and freedoms. The PDPA does not explicitly mandate DPIAs but encourages their use, especially when relying on deemed consent by notification or the legitimate interests exception. Additionally, both regulations impose obligations to implement appropriate technical and organizational measures to safeguard personal data
                
                Individual Rights
                The GDPR grants individuals several rights, including the right to access their data, rectify inaccuracies, erase data ("right to be forgotten"), restrict processing, data portability, and object to processing. The PDPA provides rights to access and correct personal data but does not include the right to erasure or data portability. Both regulations allow individuals to withdraw consent to data processing at any time.
                """);
    }
}
