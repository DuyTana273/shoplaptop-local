package com.example.shoplaptop.controller;

import com.example.shoplaptop.common.EncryptPasswordUtils;
import com.example.shoplaptop.model.Role;
import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.model.dto.UserDTO;
import com.example.shoplaptop.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/dashboard/users")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @GetMapping("/list")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "role", required = false) Role role,
                       Model model) {
        Pageable pageable = PageRequest.of(page, 2);
        Page<Users> users;

        if ((keyword != null && !keyword.trim().isEmpty()) || role != null) {
            users = iUserService.searchUsers(keyword, role, pageable);
            if (users.isEmpty()) {
                model.addAttribute("messageType", "error");
                model.addAttribute("message", "Không tìm thấy người dùng nào phù hợp!");
            }
        } else {
            users = iUserService.findAll(pageable);
            if (users.isEmpty()) {
                model.addAttribute("messageType", "error");
                model.addAttribute("message", "Hiện tại không có người dùng nào trong hệ thống!");
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("role", role);
        model.addAttribute("roles", Role.values());
        return "dashboard/users/list";
    }

    @GetMapping("/create")
    public ModelAndView createUser(Model model) {
        ModelAndView modelAndView = new ModelAndView("dashboard/users/create");
        modelAndView.addObject("users", new UserDTO());
        modelAndView.addObject("roles", Role.values());
        return modelAndView;
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("users") UserDTO userDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model,
                             Principal principal) {
        String loggedInUsername = principal.getName();
        Users loggedInUser = iUserService.findByUsername(loggedInUsername);

        if (iUserService.existsByUsername(userDTO.getUsername())) {
            model.addAttribute("users", userDTO);
            model.addAttribute("roles", Role.values());
            bindingResult.rejectValue("username", "","Tài khoản đã tồn tại!");
        }

        if (iUserService.existsByEmail(userDTO.getEmail())) {
            model.addAttribute("users", userDTO);
            model.addAttribute("roles", Role.values());
            bindingResult.rejectValue("email", "", "Email đã tồn tại!");
        }

        if (!"admin".equalsIgnoreCase(loggedInUsername) && loggedInUser.getRole() == Role.ADMIN) {
            if (userDTO.getRole() == Role.ADMIN) {
                model.addAttribute("users", userDTO);
                model.addAttribute("roles", Role.values());
                bindingResult.rejectValue("role", "", "Bạn không có quyền tạo tài khoản Admin!");
            }
        }

        new UserDTO().validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userDTO);
            model.addAttribute("roles", Role.values());
            return "dashboard/users/create";
        }

        Users user = new Users();
        BeanUtils.copyProperties(userDTO, user);
        iUserService.save(user);

        long totalUsers = iUserService.countUsers();
        int lastPage = (int) Math.ceil((double) totalUsers / 2) - 1;

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm người dùng mới thành công!");
        return "redirect:/dashboard/users/list?page=" + lastPage;
    }

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Long id,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           Model model,
                           RedirectAttributes redirectAttributes,
                           Principal principal) {
        String loggedInUsername = principal.getName();
        Users loggedInUser = iUserService.findByUsername(loggedInUsername);
        Users user = iUserService.getById(id);

        if (user == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy người dùng!");
            return "redirect:/dashboard/users/list?page=" + page;
        }

        if (loggedInUsername.equals(user.getUsername())) {
            model.addAttribute("user", user);
            model.addAttribute("currentPage", page);
            return "dashboard/users/view";
        }

        if (!"admin".equalsIgnoreCase(loggedInUsername) && loggedInUser.getRole() == Role.ADMIN) {
            if (user.getRole() == Role.ADMIN) {
                model.addAttribute("users", user);
                model.addAttribute("roles", Role.values());
                redirectAttributes.addFlashAttribute("messageType", "error");
                redirectAttributes.addFlashAttribute("message", "Bạn không thể xem thông tin Admin khác!");
                return "redirect:/dashboard/users/list?page=" + page;
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("currentPage", page);
        return "dashboard/users/view";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Principal principal) {
        String loggedInUsername = principal.getName();
        Users loggedInUser = iUserService.findByUsername(loggedInUsername);

        Users user = iUserService.getById(id);

        if (user == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Người dùng không tồn tại");
            return "redirect:/dashboard/users/list?page=" + page;
        }

        if (!"admin".equalsIgnoreCase(loggedInUsername) && loggedInUser.getRole() == Role.ADMIN && !loggedInUsername.equals(user.getUsername())) {
            if (user.getRole() == Role.ADMIN) {
                redirectAttributes.addFlashAttribute("messageType", "error");
                redirectAttributes.addFlashAttribute("message", "Bạn không thể cập nhật thông tin Admin khác!");
                return "redirect:/dashboard/users/list?page=" + page;
            }
        }

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);

        model.addAttribute("user", userDTO);
        model.addAttribute("roles", Role.values());
        return "dashboard/users/update";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") UserDTO userDTO,
                             BindingResult bindingResult,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Principal principal) {
        String loggedInUsername = principal.getName();
        Users loggedInUser = iUserService.findByUsername(loggedInUsername);
        Users userToUpdate = iUserService.getById(userDTO.getId());

        if (userToUpdate == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Người dùng không tồn tại!");
            return "redirect:/dashboard/users/list?page=" + page;
        }

        if (!userToUpdate.getEmail().equals(userDTO.getEmail()) && iUserService.existsByEmail(userDTO.getEmail())) {
            model.addAttribute("user", userDTO);
            model.addAttribute("roles", Role.values());
            bindingResult.rejectValue("email", "", "Email này đã tồn tại!");
            return "dashboard/users/update";
        }

        if (loggedInUsername.equals(userToUpdate.getUsername())) {
            userDTO.setRole(userToUpdate.getRole());
            userDTO.setStatus(userToUpdate.getStatus());
        } else {
            if (!"admin".equalsIgnoreCase(loggedInUsername) && loggedInUser.getRole() == Role.ADMIN) {
                if (userDTO.getRole() == Role.ADMIN) {
                    bindingResult.rejectValue("role", "", "Bạn không có quyền tạo tài khoản Admin!");
                }
            }
        }

        userDTO.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            model.addAttribute("roles", Role.values());
            return "dashboard/users/update";
        }

        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setPhone(userDTO.getPhone());
        userToUpdate.setFullName(userDTO.getFullName());
        userToUpdate.setGender(userDTO.getGender());
        userToUpdate.setAddress(userDTO.getAddress());
        userToUpdate.setRole(userDTO.getRole());
        userToUpdate.setStatus(userDTO.getStatus());
        userToUpdate.setAvatar(userDTO.getAvatar());

        iUserService.save(userToUpdate);

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật người dùng thành công");
        return "redirect:/dashboard/users/list?page=" + page;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             RedirectAttributes redirectAttributes,
                             Principal principal) {
        String loggedInUsername = principal.getName();
        Users loggedInUser = iUserService.findByUsername(loggedInUsername);
        Users userToDelete = iUserService.getById(id);

        if (userToDelete == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Người dùng không tồn tại!");
            return "redirect:/dashboard/users/list?page=" + page;
        }

        if (loggedInUsername.equals(userToDelete.getUsername())) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Bạn không thể tự xóa chính mình!");
            return "redirect:/dashboard/users/list?page=" + page;
        }

        if (userToDelete.getRole() == Role.ADMIN) {
            if (!"admin".equalsIgnoreCase(loggedInUsername)) {
                redirectAttributes.addFlashAttribute("messageType", "error");
                redirectAttributes.addFlashAttribute("message", "Bạn không có quyền xóa Admin!");
                return "redirect:/dashboard/users/list?page=" + page;
            }
        }

        try {
            iUserService.deleteById(id);
            long totalUsers = iUserService.countUsers();
            int lastPage = (int) Math.ceil((double) totalUsers / 2) - 1;

            if (page > lastPage) {
                page = lastPage;
            }

            redirectAttributes.addFlashAttribute("messageType", "success");
            redirectAttributes.addFlashAttribute("message", "Xoá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Có lỗi khi xoá người dùng này!");
        }
        return "redirect:/dashboard/users/list?page=" + page;
    }

}
