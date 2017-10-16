package mechanics;

import frontend.GameWebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import services.AccountService;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "WebSocketChatServlet", urlPatterns = {"/game"})
public class WebSocketGameServlet extends WebSocketServlet {
    private static final int LOGOUT_TIME = 10 * 60 * 1000;

    private AccountService accountService;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

    public WebSocketGameServlet(AccountService authService,
                                GameMechanics gameMechanics,
                                WebSocketService webSocketService) {
        this.accountService = authService;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new GameWebSocketCreator(accountService, gameMechanics, webSocketService));
    }
}
