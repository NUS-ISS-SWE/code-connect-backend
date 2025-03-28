package com.nus.iss.appuser.config.security;

import com.nus.iss.appuser.entity.AppUser;
import com.nus.iss.appuser.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isPresent() && user.get().getStatus().equalsIgnoreCase("ACTIVE")) {
            return User.builder()
                    .username(user.get().getUsername())
                    .password(user.get().getPassword())
                    .roles(getRoles(user.get()))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getRoles(AppUser appUser) {
        return (null == appUser.getRole())
                ? new String[]{"USER"}
                : Arrays.stream(appUser.getRole().split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
}
