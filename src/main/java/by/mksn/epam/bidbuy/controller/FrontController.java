package by.mksn.epam.bidbuy.controller;

import by.mksn.epam.bidbuy.command.Command;
import by.mksn.epam.bidbuy.command.exception.CommandException;
import by.mksn.epam.bidbuy.command.factory.CommandFactory;
import by.mksn.epam.bidbuy.command.resource.PathManager;
import by.mksn.epam.bidbuy.dao.pool.ConnectionPool;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.mksn.epam.bidbuy.command.resource.Constants.*;

/**
 * Main Controller of web app
 */
@WebServlet(name = "FrontController", urlPatterns = {"/controller"})
public class FrontController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(FrontController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().releasePool();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Command command = CommandFactory.defineCommand(request);
        try {
            command.execute(request, response);
        } catch (CommandException e) {
            logger.error("Cannot execute command.\n", e);
            request.setAttribute(ERROR_TITLE_ATTRIBUTE, ERROR_TITLE_COMMAND);
            request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, ERROR_MESSAGE_COMMAND);
            String pagePath = PathManager.getProperty(PathManager.ERROR);
            request.getRequestDispatcher(pagePath).forward(request, response);
        }
    }
}
