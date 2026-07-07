package sample.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sample.app.UserService;

/**
 * ユーザー管理コントローラー
 * TOP・ログイン・ユーザー登録を提供する
 */
@Controller
public class UserController {

    private final UserService userService;

    // コンストラクタ注入
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** TOP画面 */
    @GetMapping("/")
    public String top() {
        return "top";
    }

    /** ログイン画面 */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /** ユーザー登録フォーム表示 */
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }
    /** ユーザー登録処理 */
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
    	// 必須チェック
        if (username == null || username.isBlank()) {
            model.addAttribute("error", "ユーザー名を入力してください");
            return "register";
        }
        if (password == null || password.isBlank()) {
            model.addAttribute("error", "パスワードを入力してください");
            return "register";
        }

        // 桁数チェック
        if (username.length() > 50) {
            model.addAttribute("error", "ユーザー名は50文字以内で入力してください");
            return "register";
        }
        if (password.length() < 8) {
            model.addAttribute("error", "パスワードは8文字以上で入力してください");
            return "register";
        }

        // ユーザー登録処理
        try {
            userService.registerUser(username, password);
        } catch (IllegalArgumentException e) {
            // ユーザー名重複などのビジネスエラーを画面に表示
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        return "redirect:/login";
    }
    }
