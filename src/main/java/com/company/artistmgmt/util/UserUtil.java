package com.company.artistmgmt.util;

import com.company.artistmgmt.model.User;
import com.company.artistmgmt.model.general.Role;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserUtil {

    public static Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getPrincipal() instanceof User userDetails) {
            return userDetails.getId();
        }
        return null;
    }

    public static boolean isUserLoggedIn() {
        return getUserId() != null;
    }

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getPrincipal() instanceof User userDetails) {
            username = authentication.getName();
        }
        if (!Objects.equals(username, "")) {
            return username;
        }
        return null;
    }

    public static Collection<? extends GrantedAuthority> getRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    public static boolean isSuperAdmin() {
        Collection<? extends GrantedAuthority> roles = getRoles();
        return roles != null && roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .contains("ROLE_" + Role.SUPER_ADMIN);
    }

    public static boolean isArtistManager() {
        Collection<? extends GrantedAuthority> roles = getRoles();
        return roles != null && roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .contains("ROLE_" + Role.ARTIST_MANAGER);
    }

    public static boolean isArtist() {
        Collection<? extends GrantedAuthority> roles = getRoles();
        return roles != null && roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .contains("ROLE_" + Role.ARTIST);
    }

}
