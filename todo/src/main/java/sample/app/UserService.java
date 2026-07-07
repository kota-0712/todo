package sample.app;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.app.dao.entity.User;
import sample.app.dao.mapper.UserMapper;

/**
 * ユーザー管理サービス
 * ユーザーの登録・認証を提供する
 */
@Service
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /** Spring Securityの認証処理 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + username);
        }
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles("USER")
            .build();
    }

    /** ユーザー名でユーザーを取得 */
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    /** ユーザー登録（重複チェックあり） */
    @Transactional
    public void register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("このユーザー名はすでに使われています");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
    }

//UserService.java の中（クラスの閉じ括弧 `}` の前）にこれを追記
/** コントローラーからの呼び出し用 */
@Transactional
public void registerUser(String username, String password) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(password);
    
    // 既存の登録ロジックを呼び出す
    this.register(user);
}
}