package mechanics;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * Created by gantz on 19.04.16.
 */
@WebServlet(name = "WebSocketChatServlet", urlPatterns = {"/game"})
public class WebSocketGameServlet extends WebSocketServlet {
    private static final int LOGOUT_TIME = 10 * 60 * 1000;
    private final GameService gameService;

    public WebSocketGameServlet() {
        this.gameService = new GameService();
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator((req, resp) -> new GameWebSocket(gameService));
    }
}
