package by.mksn.epam.bidbuy.service.impl;

import by.mksn.epam.bidbuy.dao.UserDAO;
import by.mksn.epam.bidbuy.dao.exception.DAOException;
import by.mksn.epam.bidbuy.dao.factory.DAOFactory;
import by.mksn.epam.bidbuy.entity.User;
import by.mksn.epam.bidbuy.service.UserService;
import by.mksn.epam.bidbuy.service.exception.ServiceException;
import by.mksn.epam.bidbuy.service.exception.UserServiceException;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceImpl implements UserService {

    public static final int HASH_ROUNDS = 12;
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private static final String EMAIL_REGEX = "^[a-zA-Z_0-9]+@[a-zA-Z_0-9]+\\.[a-zA-Z_0-9]+$";
    private static final String USERNAME_REGEX = "^[a-zA-Z_0-9]{5,45}$";

    private static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    private static boolean isValidUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }

    @Override
    public User register(String username, String email, String password) throws ServiceException {
        UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.MY_SQL).getUserDAO();
        User user;
        try {
            if (!isValidEmail(email)) {
                logger.debug("Registration failed: invalid email format \"" + email + "\"");
                throw new UserServiceException("Invalid email format", UserServiceException.WRONG_INPUT);
            }
            if (!isValidUsername(username)) {
                logger.debug("Registration failed: invalid username format \"" + email + "\"");
                throw new UserServiceException("Invalid username format", UserServiceException.WRONG_INPUT);
            }

            user = userDAO.selectByUsername(username);
            if (user != null) {
                logger.debug("Registration failed: user with username \"" + username + "\" is already exists.");
                throw new UserServiceException("User with that username is already exists", UserServiceException.USER_EXISTS);
            }

            user = userDAO.selectByEmail(email);
            if (user != null) {
                logger.debug("Registration failed: user with email \"" + email + "\" is already exists.");
                throw new UserServiceException("User with that email is already exists", UserServiceException.EMAIL_EXISTS);
            }

            password = BCrypt.hashpw(password, BCrypt.gensalt(HASH_ROUNDS));

            user = userDAO.insert(username, email, password);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public User login(String username, String password) throws ServiceException {
        UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.MY_SQL).getUserDAO();
        User user;
        try {
            user = userDAO.selectByUsername(username);
            if (user != null && (user.getStatus() != User.STATUS_DELETED)) {
                if (!BCrypt.checkpw(password, user.getPassHash())) {
                    logger.debug("Authorization failed: user with username \"" + username + "\" .");
                    throw new UserServiceException("Incorrect password.", UserServiceException.INCORRECT_PASSWORD);
                }
            } else {
                logger.debug("Authorization failed: user with username \"" + username + "\" doesn't exists.");
                throw new UserServiceException("Incorrect password.", UserServiceException.USER_NOT_EXIST);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public void updateUser(User updatedUser) throws ServiceException {
        UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.MY_SQL).getUserDAO();
        try {
            userDAO.update(updatedUser);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
