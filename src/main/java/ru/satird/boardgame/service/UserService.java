package ru.satird.boardgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.satird.boardgame.domain.AuthenticationProvider;
import ru.satird.boardgame.domain.Role;
import ru.satird.boardgame.domain.User;
import ru.satird.boardgame.repository.UserRepository;
import ru.satird.boardgame.security.oauth.CustomOAuth2User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${hostname}")
    private String hostname;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if (userRepository.findByUsername(username) != null) {
//            return userRepository.findByUsername(username);
//        } else {
//            return null;
//        }
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        } else {
            return userRepository.findByUsername(username);
        }
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        User userMail = userRepository.findByEmail(user.getEmail());

        if (userFromDb != null) {
            return false;
        }
        if (userMail != null) {
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setAuthProvider(AuthenticationProvider.LOCAL);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Привет, %s Для продолжения регистрации на сайте mediaContainer перейдите пожалуйста по ссылке: " +
                            "http://%s/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }

        userRepository.save(user);

        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActive(true);
        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }

    public void createNewUserAfterOAuthLoginSuccess(CustomOAuth2User customOAuth2User) {
        User user = new User();
        user.setRoles(Collections.singleton(Role.USER));
        user.setEmail(customOAuth2User.getEmail());
        user.setUsername(customOAuth2User.getName());
        user.setCreatedTime(new Date());
        user.setAuthProvider(customOAuth2User.getProvider());
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        if (customOAuth2User.getProvider() == AuthenticationProvider.GOOGLE) {
            user.setGoogleId(customOAuth2User.getGoogleId());
            user.setLocale(customOAuth2User.getLocale());
            user.setUserpic(customOAuth2User.getUserpic());
        }

        user.setActive(true);

        userRepository.save(user);
    }

    public void updateUserAfterOAuthLoginSuccess(User user, String name, AuthenticationProvider provider) {
        user.setUsername(name);
        user.setAuthProvider(provider);

        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public void updateProfile(User user, String password, MultipartFile file) throws IOException {

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if (file != null && !file.getOriginalFilename().isEmpty() ) {
            saveUserpic(user, file);
        }

        userRepository.save(user);
    }

    private void saveUserpic(User user,
                             MultipartFile file) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
           if (!uploadDir.mkdir()) {
               throw new IOException("Need to create directory " + uploadDir);
           }
        }
        if (user.getUserpic() != null) {
            File currentUserPic = new File(uploadPath + user.getUserpic().replace("/img/", ""));

            if (currentUserPic.exists()) {
                Path fileToDeletePath = Paths.get(uploadPath + user.getUserpic().replace("/img/", ""));
                Files.delete(fileToDeletePath);
            }
        }
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName = uuidFile + "." + file.getOriginalFilename();

        file.transferTo(new File(uploadPath + "/" + resultFileName));

        user.setUserpic("/img/" + resultFileName);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updatePassword(User user, String password) {
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepository.save(user);
    }
}
