package pt.ul.fc.css.example.demo.controllersWebApp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private HttpSession session;

    @GetMapping
    public String showDashboard(Model model) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        model.addAttribute("userId", session.getAttribute("loggedInUser"));
        model.addAttribute("tipoUser", session.getAttribute("tipoUser"));
        model.addAttribute("userName", session.getAttribute("userName"));

        return "dashboard";
    }

    @PostMapping("/listarVotacoes")
    public String performUseCase1() {
        // Logic to perform Use Case 1
        // Redirect to appropriate page
        return "redirect:/dashboard/listarVotacoes";
    }

    @PostMapping("/use-case-2")
    public String performUseCase2() {
        // Logic to perform Use Case 2
        // Redirect to appropriate page
        return "redirect:/dashboard";
    }


}
